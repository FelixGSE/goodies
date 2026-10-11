plugins {
    kotlin("jvm") version "2.4.21"
    `java-library`
    jacoco
    id("com.gradleup.shadow") version "9.6.1"
    id("org.jlleitschuh.gradle.ktlint") version "14.2.0"
    id("dev.detekt") version "2.0.0-alpha.6"
}

group = "org.felixgse"
version = "1.0"

/** Folder name the library gets inside the Processing sketchbook, and the jar name inside it. */
val libraryName = "extensions"

repositories {
    mavenCentral()
}

dependencyLocking {
    lockAllConfigurations()
}

tasks.register("resolveAndLockAll") {
    description = "Resolves every resolvable configuration; with --write-locks it rewrites gradle.lockfile."
    notCompatibleWithConfigurationCache("resolves configurations at execution time")
    doLast {
        configurations.filter { it.isCanBeResolved }.forEach { it.resolve() }
    }
}

val processingCore = "org.processing:core:4.5.7"

dependencies {
    compileOnly(processingCore)
    testImplementation(processingCore)

    api("org.apache.commons:commons-math3:3.6.1")
    api("com.github.haifengl:smile-plot:2.6.0")
    implementation("com.google.guava:guava:33.7.2-jre")
    implementation("org.jetbrains:annotations:26.1.0")

    compileOnly("org.projectlombok:lombok:1.18.48")
    annotationProcessor("org.projectlombok:lombok:1.18.48")

    testImplementation(platform("org.junit:junit-bom:6.1.3"))
    testImplementation(kotlin("test"))
    testImplementation("org.assertj:assertj-core:3.27.7")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    detektPlugins("dev.detekt:detekt-rules-libraries:2.0.0-alpha.6")
}

kotlin {
    jvmToolchain(17)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

ktlint {
    version.set("1.8.0")
}

detekt {
    buildUponDefaultConfig = true
    allRules = true
    config.setFrom("config/detekt/detekt.yml")
    // the plugin derives baseline-main.xml and baseline-test.xml from this name
    baseline = file("config/detekt/baseline.xml")
}

tasks.check {
    // the plugin wires the untyped detekt into check; only the typed detektMain/detektTest run
    setDependsOn(dependsOn.filterNot { it is TaskProvider<*> && it.name == "detekt" })
    dependsOn("detektMain", "detektTest")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("failed")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.SHORT
    }
}

// ---------------------------------------------------------------------------
// Coverage
//
// `jacocoTestReport` writes HTML + XML to build/reports/jacoco/. The check fails
// when line coverage drops below -PcoverageMinimum (a ratio, 0.0-1.0).
// ---------------------------------------------------------------------------
jacoco {
    toolVersion = "0.8.15"
}

val coverageMinimum: String = (findProperty("coverageMinimum") as String?) ?: "0.0"

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.test)
    violationRules {
        rule {
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = coverageMinimum.toBigDecimal()
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Packaging for Processing
//
// A Processing library is a folder in the sketchbook's `libraries/` directory:
//   extensions/
//     library/extensions.jar   <- fat jar, everything but Processing core
//     library.properties       <- metadata the Contribution Manager reads
//     examples/ data/
// ---------------------------------------------------------------------------
tasks.shadowJar {
    archiveBaseName.set(libraryName)
    archiveClassifier.set("")
    archiveVersion.set("")
    // shadow 9 drops duplicates before its transformers run: service files stay
    // INCLUDE so mergeServiceFiles sees every copy, the rest keeps the first one
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    mergeServiceFiles()
    filesNotMatching("META-INF/services/**") {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}

val libraryProperties =
    tasks.register("libraryProperties") {
        description = "Generates library.properties for the Processing Contribution Manager."
        val output = layout.buildDirectory.file("processing/library.properties")
        outputs.file(output)
        val libVersion = project.version.toString()
        // Processing wants a monotonically increasing integer here; bump on each release.
        val libRevision = (findProperty("libraryRevision") as String?) ?: "1"
        doLast {
            output.get().asFile.writeText(
                """
                name=$libraryName
                category=Other
                authors=[FelixGSE](https://github.com/FelixGSE)
                url=https://github.com/FelixGSE/goodies
                sentence=Utilities and extensions for creative coding with Processing.
                paragraph=Mathematical and geometric helpers for generative art: flow fields, curves, brush rendering, watercolor effects, agent simulations and more.
                version=$libRevision
                prettyVersion=$libVersion
                minRevision=1293
                maxRevision=0

                """.trimIndent(),
            )
        }
    }

val processingLibrary =
    tasks.register<Sync>("processingLibrary") {
        group = "processing"
        description = "Assembles the installable Processing library folder in build/processing/$libraryName."

        into(layout.buildDirectory.dir("processing/$libraryName"))
        from(tasks.shadowJar) { into("library") }
        from(libraryProperties)
        from("examples") { into("examples") }
        from("data") { into("data") }
    }

tasks.register<Zip>("processingLibraryZip") {
    group = "processing"
    description = "Zips the Processing library folder for distribution."

    from(processingLibrary) { into(libraryName) }
    archiveFileName.set("$libraryName-${project.version}.zip")
    destinationDirectory.set(layout.buildDirectory.dir("distributions"))
}

val sketchbookDir: String =
    (findProperty("sketchbookDir") as String?)
        ?: System.getenv("PROCESSING_SKETCHBOOK")
        ?: "${System.getProperty("user.home")}/Documents/Processing"

tasks.register<Sync>("installLibrary") {
    group = "processing"
    description = "Installs the library into the Processing sketchbook at $sketchbookDir/libraries/$libraryName."

    from(processingLibrary)
    into("$sketchbookDir/libraries/$libraryName")
    doLast { logger.lifecycle("Installed $libraryName to $sketchbookDir/libraries/$libraryName") }
}
