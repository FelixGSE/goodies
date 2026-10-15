package goodies

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.declaration.KoFileDeclaration
import com.lemonappdev.konsist.api.declaration.KoFunctionDeclaration
import com.lemonappdev.konsist.api.declaration.KoImportDeclaration
import com.lemonappdev.konsist.api.ext.list.withAnnotationNamed
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

/**
 * Layering (ARCH-1, ARCH-2) and test conventions (T-NAME-2, T-BODY-1), checked on the source with Konsist.
 *
 * Files that break a rule today are listed below, one set per rule. Fix a file, delete its name.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ArchitectureTest {
    private val production = Konsist.scopeFromProduction().files
    private val tests = Konsist.scopeFromTest().files

    private val core = setOf("algebra", "geometry", "exceptions")
    private val draw = "draw"
    private val toolkit = production.map { it.topPackage() }.toSet() - core - draw

    // owner: algebra layering
    private val algebraImportingOthers = setOf("GMatrix", "LinearAlgebraUtils", "Matrix")

    // owner: shape layering
    private val geometryImportingUpwards =
        setOf("Line", "Point", "Polygon", "PolygonLikeShape", "Quadrilateral", "Sphere")

    // owner: shape layering
    private val coreImportingProcessing = setOf("Line", "Point", "Polygon", "PolygonPath", "Quadrilateral", "Sphere")

    @Test
    fun `algebra should not import other goodies packages`() {
        // Given
        val files = production.inPackages("algebra").without(algebraImportingOthers)

        // When
        val result = files.importing(core + toolkit + draw - "algebra")

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `geometry should import only algebra and exceptions`() {
        // Given
        val files = production.inPackages("geometry").without(geometryImportingUpwards)

        // When
        val result = files.importing(toolkit + draw)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `toolkit should not import draw`() {
        // Given
        val files = production.inPackages(*toolkit.toTypedArray())

        // When
        val result = files.importing(setOf(draw))

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `core should not import processing`() {
        // Given
        val files = production.inPackages(*core.toTypedArray()).without(coreImportingProcessing)

        // When
        val result = files.filter { file -> file.imports.any { it.name.startsWith("processing.") } }.names()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `test names should contain should`() {
        // Given
        val functions = tests.testFunctions()

        // When
        val result = functions.filterNot { it.name.contains(" should ") }.described()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `test bodies should have given when then in order`() {
        // Given
        val functions = tests.testFunctions()

        // When
        val result = functions.filterNot { it.hasGivenWhenThen() }.described()

        // Then
        assertThat(result).isEmpty()
    }

    private fun KoFileDeclaration.topPackage(): String =
        packagee
            ?.name
            .orEmpty()
            .split('.')
            .getOrElse(1) { "" }

    private fun List<KoFileDeclaration>.inPackages(vararg packages: String) = filter { it.topPackage() in packages }

    private fun List<KoFileDeclaration>.without(known: Set<String>) = filterNot { it.name in known }

    private fun List<KoFileDeclaration>.names() = map { it.name }

    private fun List<KoFileDeclaration>.importing(packages: Set<String>) =
        filter { file -> file.imports.any { import -> import.goodiesPackage() in packages } }.names()

    private fun KoImportDeclaration.goodiesPackage(): String? =
        name.split('.').takeIf { it.first() == "goodies" }?.getOrNull(1)

    private fun List<KoFunctionDeclaration>.described() = map { "${it.containingFile.name}: ${it.name}" }

    private fun List<KoFileDeclaration>.testFunctions(): List<KoFunctionDeclaration> =
        flatMap { it.functions(includeNested = true) }.withAnnotationNamed("Test", "ParameterizedTest")

    private fun KoFunctionDeclaration.hasGivenWhenThen(): Boolean {
        val given = text.indexOf("// Given")
        val whenAt = text.indexOf("// When")
        val then = text.indexOf("// Then")
        return given in 0 until whenAt && whenAt < then
    }
}
