@file:JvmName("Docgen")

package coffee.cypher.hexbound.docgen

import at.petrak.hexcasting.api.spell.Action
import at.petrak.hexcasting.api.spell.math.HexPattern
import coffee.cypher.hexbound.init.HexboundPatterns
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import java.io.File
import java.io.FileOutputStream
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmName

@OptIn(ExperimentalSerializationApi::class)
internal fun main(args: Array<String>) {
    val (targetPath) = args

    val patterns = DocgenPatterns()
    patterns.register()

    val outputFile = File(targetPath).also { it.parentFile.mkdirs() }

    val json = Json { prettyPrint = true }
    json.encodeToStream(patterns.patternData, FileOutputStream(outputFile))
}

private class DocgenPatterns : HexboundPatterns() {
    val patternData = mutableListOf<PatternData>()

    override fun registerPattern(pattern: HexPattern, id: String, action: Action, perWorld: Boolean) {
        val sourceFile = action::class.declaredMemberFunctions.first().let {
            val args = Array<Any?>(it.parameters.size - 1) { null }

            val stackTrace = try {
                it.isAccessible = true
                it.call(action, *args)
                null //should never happen
            } catch (e: Throwable) {
                var rootException = e
                while (rootException.cause != null) {
                    rootException = rootException.cause!!
                }

                rootException.stackTrace
            }

            stackTrace!!

            stackTrace[0].fileName!!
        }

        val srcPath = action::class.java.protectionDomain.codeSource.location.file
            .let {
                if (it.endsWith(".jar")) {
                    "external:${it.substringAfterLast('/')}:"
                } else {
                    it
                }
            }

        val sourcePackage = action::class.java.packageName.replace(".", "/")

        patternData += PatternData(
            id = "hexbound:$id",
            defaultStartDir = pattern.startDir.name,
            angleSignature = pattern.anglesSignature(),
            className = action::class.jvmName,
            pathToSource = "$srcPath$sourcePackage/$sourceFile",
            isPerWorld = perWorld
        )
    }
}

@Serializable
private data class PatternData(
    val id: String,
    val defaultStartDir: String,
    val angleSignature: String,
    val className: String,
    val pathToSource: String,
    val isPerWorld: Boolean
)
