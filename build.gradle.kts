plugins {
    kotlin("jvm") version "1.9.25"
    `java-library`
    jacoco
    id("com.gradleup.shadow") version "8.3.5"
}

group = "org.felixgse"
version = "1.0"

/** Folder name the library gets inside the Processing sketchbook, and the jar name inside it. */
val libraryName = "extensions"

repositories {
    mavenCentral()
}

// ---------------------------------------------------------------------------
// Processing core
//
// Processing 4 is not published to Maven Central, so core.jar comes from a local
// Processing install. Resolution order:
//   1. -PprocessingCoreJar=/path/to/core.jar
//   2. PROCESSING_CORE_JAR environment variable (set for you in the dev container)
//   3. a Processing.app install in the usual macOS locations
// Run `./gradlew processingCore` to see what got picked.
// ---------------------------------------------------------------------------
val processingSearchPaths = listOf(
    "/Applications/Processing.app/Contents/app/resources/core/library",
    "${System.getProperty("user.home")}/Applications/Processing.app/Contents/app/resources/core/library",
)

fun locateProcessingCore(): File {
    val explicit = (findProperty("processingCoreJar") as String?) ?: System.getenv("PROCESSING_CORE_JAR")
    if (!explicit.isNullOrBlank()) {
        val jar = File(explicit)
        if (!jar.isFile) {
            throw GradleException("Processing core jar not found at '$explicit'.")
        }
        return jar
    }

    // Any core-*.jar in the install, so a Processing update does not break the build.
    val found = processingSearchPaths
        .map(::File)
        .firstNotNullOfOrNull { dir ->
            dir.listFiles { f: File -> f.name.startsWith("core") && f.name.endsWith(".jar") }
                ?.sortedBy { it.name }
                ?.lastOrNull()
        }

    return found ?: throw GradleException(
        """
        Could not find Processing's core.jar.

        Looked in:
        ${processingSearchPaths.joinToString("\n        ") { "- $it" }}

        Install Processing 4, or point the build at the jar explicitly:
          ./gradlew build -PprocessingCoreJar=/path/to/core.jar
          PROCESSING_CORE_JAR=/path/to/core.jar ./gradlew build

        The dev container ships one and sets PROCESSING_CORE_JAR for you.
        """.trimIndent()
    )
}

/** Lazy, so tasks that do not compile anything still work without Processing installed. */
val processingCore: FileCollection = files(provider { locateProcessingCore() })

dependencies {
    // Supplied by Processing at runtime, so never bundled into the distributed jar.
    compileOnly(processingCore)
    testImplementation(processingCore)

    api("org.apache.commons:commons-math3:3.6.1")
    api("com.github.haifengl:smile-plot:2.6.0")
    implementation("com.google.guava:guava:31.1-jre")
    implementation("org.jetbrains:annotations:24.1.0")

    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")

    testImplementation(kotlin("test"))
    testImplementation("org.assertj:assertj-core:3.23.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.1")
}

kotlin {
    jvmToolchain(17)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
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
    toolVersion = "0.8.12"
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
    mergeServiceFiles()
}

val libraryProperties by tasks.registering {
    description = "Generates library.properties for the Processing Contribution Manager."
    val output = layout.buildDirectory.file("processing/library.properties")
    outputs.file(output)
    val libVersion = project.version.toString()
    // Processing wants a monotonically increasing integer here; bump on each release.
    val libRevision = (findProperty("libraryRevision") as String?) ?: "1"
    doLast {
        output.get().asFile.writeText(
            """
            name=${libraryName}
            category=Other
            authors=[FelixGSE](https://github.com/FelixGSE)
            url=https://github.com/FelixGSE/goodies
            sentence=Utilities and extensions for creative coding with Processing.
            paragraph=Mathematical and geometric helpers for generative art: flow fields, curves, brush rendering, watercolor effects, agent simulations and more.
            version=${libRevision}
            prettyVersion=${libVersion}
            minRevision=1293
            maxRevision=0

            """.trimIndent()
        )
    }
}

val processingLibrary by tasks.registering(Sync::class) {
    group = "processing"
    description = "Assembles the installable Processing library folder in build/processing/$libraryName."

    into(layout.buildDirectory.dir("processing/$libraryName"))
    from(tasks.shadowJar) { into("library") }
    from(libraryProperties)
    from("examples") { into("examples") }
    from("data") { into("data") }
}

val processingLibraryZip by tasks.registering(Zip::class) {
    group = "processing"
    description = "Zips the Processing library folder for distribution."

    from(processingLibrary) { into(libraryName) }
    archiveFileName.set("$libraryName-${project.version}.zip")
    destinationDirectory.set(layout.buildDirectory.dir("distributions"))
}

val sketchbookDir: String = (findProperty("sketchbookDir") as String?)
    ?: System.getenv("PROCESSING_SKETCHBOOK")
    ?: "${System.getProperty("user.home")}/Documents/Processing"

val installLibrary by tasks.registering(Sync::class) {
    group = "processing"
    description = "Installs the library into the Processing sketchbook at $sketchbookDir/libraries/$libraryName."

    from(processingLibrary)
    into("$sketchbookDir/libraries/$libraryName")
    doLast { logger.lifecycle("Installed $libraryName to $sketchbookDir/libraries/$libraryName") }
}

tasks.register("processingCore") {
    group = "processing"
    description = "Prints the Processing core jar this build resolves against."
    doLast { println(locateProcessingCore().absolutePath) }
}
