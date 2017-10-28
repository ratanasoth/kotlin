
import java.io.File

val buildVersionFilePath = "${rootProject.extra["distDir"]}/build.txt"

val buildVersion by configurations.creating
val buildNumber: String by rootProject.extra
val kotlinVersion: String by rootProject.extra

val writeBuildNumber by tasks.creating {
    inputs.property("version", buildNumber)
    val versionFile = File(buildVersionFilePath).also { outputs.file(it) }
    doLast {
        versionFile.parentFile.mkdirs()
        versionFile.writeText(buildNumber)
    }
}

fun replaceVersion(versionFile: File, versionPattern: String, replacement: (MatchResult) -> String) {
    check(versionFile.isFile) { "Version file $versionFile is not found" }
    val text = versionFile.readText()
    val pattern = Regex(versionPattern)
    val match = pattern.find(text) ?: error("Version pattern is missing")
    val newValue = replacement(match)
    versionFile.writeText(text.replaceRange(match.groups[1]!!.range, newValue))
}

val writeStdlibVersion by tasks.creating {
    inputs.property("version", kotlinVersion)
    val versionFile = rootDir.resolve("libraries/stdlib/src/kotlin/util/KotlinVersion.kt").also { outputs.file(it) }
    doLast {
        replaceVersion(versionFile, """val CURRENT: KotlinVersion = KotlinVersion\((\d+, \d+, \d+)\)""") {
            val (major, minor, optPatch) = Regex("""^(\d+)\.(\d+)(\.\d+)?""").find(kotlinVersion)?.destructured ?: error("Cannot parse version")
            val newVersion = "$major, $minor, ${optPatch.trimStart('.').takeIf { it.isNotEmpty() } ?: "0" }"
            newVersion.also { logger.lifecycle("Writing new standard library version components: $it") }
        }
    }
}

val writeCompilerVersion by tasks.creating {
    inputs.property("version", kotlinVersion)
    val versionFile = rootDir.resolve("core/util.runtime/src/org/jetbrains/kotlin/config/KotlinCompilerVersion.java").also { outputs.file(it) }
    doLast {
        replaceVersion(versionFile, """public static final String VERSION = "([^"]+)"""") {
            logger.lifecycle("Writing new compiler version: $kotlinVersion")
            kotlinVersion
        }
    }
}

val writeVersions by tasks.creating {
    dependsOn(writeBuildNumber, writeStdlibVersion, writeCompilerVersion)
}


artifacts.add(buildVersion.name, file(buildVersionFilePath)) {
    builtBy(writeBuildNumber)
}

val distKotlinHomeDir: String by rootProject.extra

val dist by task<Copy> {
    from(writeBuildNumber)
    into(File(distKotlinHomeDir))
}
