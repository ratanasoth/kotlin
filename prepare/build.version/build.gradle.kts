
import java.io.File

val buildVersionFilePath = "${rootProject.extra["distDir"]}/build.txt"

val buildVersion by configurations.creating
val buildNumber: String by rootProject.extra
val kotlinVersion: String by rootProject.extra

val writeBuildNumber by tasks.creating {
    val versionFile = File(buildVersionFilePath)
    inputs.property("version", buildNumber)
    outputs.file(buildVersionFilePath)
    doLast {
        versionFile.parentFile.mkdirs()
        versionFile.writeText(buildNumber)
    }
}

val writeStdlibVersion by tasks.creating {
    val versionFile = rootDir.resolve("libraries/stdlib/src/kotlin/util/KotlinVersion.kt")
    inputs.property("version", kotlinVersion)
    outputs.file(versionFile)
    doLast {
        val (major, minor, optPatch) = Regex("""^(\d+)\.(\d+)(\.\d+)?""").find(kotlinVersion)?.destructured ?: error("Cannot parse version")
        val newVersion = "$major, $minor, ${optPatch.trimStart('.').takeIf { it.isNotEmpty() } ?: "0" }"

        check(versionFile.isFile) { "Version file $versionFile is not found" }
        val text = versionFile.readText()
        val pattern = Regex("""val CURRENT: KotlinVersion = KotlinVersion\((\d+, \d+, \d+)\)""")
        val match = pattern.find(text) ?: error("Version pattern is missing")
        logger.lifecycle("Writing new standard library version components: $newVersion")
        versionFile.writeText(text.replaceRange(match.groups[1]!!.range, newVersion))
    }
}

val writeCompilerVersion by tasks.creating {
    val versionFile = rootDir.resolve("core/util.runtime/src/org/jetbrains/kotlin/config/KotlinCompilerVersion.java")
    inputs.property("version", kotlinVersion)
    outputs.file(versionFile)

    doLast {
        check(versionFile.isFile) { "Version file $versionFile is not found" }
        val text = versionFile.readText()
        val pattern = Regex("""public static final String VERSION = "([^"]+)"""")
        val match = pattern.find(text) ?: error("Version pattern is missing")
        logger.lifecycle("Writing new compiler version: $kotlinVersion")
        versionFile.writeText(text.replaceRange(match.groups[1]!!.range, kotlinVersion))
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
