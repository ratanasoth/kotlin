
import java.io.File

val buildVersionFilePath = "${rootProject.extra["distDir"]}/build.txt"

val buildVersion by configurations.creating
val buildNumber: String by rootProject.extra
val kotlinVersion: String by rootProject.extra

open class WriteVersionTask : DefaultTask() {
    @Input
    lateinit var version: String
    @OutputFile
    lateinit var versionFile: File
}

class PatternReplaceVersionTask : WriteVersionTask() {
    @Input
    lateinit var versionPattern: String

    var replacement: (MatchResult) -> String = { version }
    fun replacement(f: (MatchResult) -> String) { replacement = f }

    @TaskAction
    fun invoke() {
        check(versionFile.isFile) { "Version file $versionFile is not found" }
        val text = versionFile.readText()
        val pattern = Regex(versionPattern)
        val match = pattern.find(text) ?: error("Version pattern is missing")
        val newValue = replacement(match)
        versionFile.writeText(text.replaceRange(match.groups[1]!!.range, newValue))
    }
}

val writeBuildNumber by tasks.creating(WriteVersionTask::class) {
    versionFile = File(buildVersionFilePath)
    version = buildNumber
    doLast {
        versionFile.parentFile.mkdirs()
        versionFile.writeText(buildNumber)
    }
}

val writeStdlibVersion by tasks.creating(PatternReplaceVersionTask::class) {
    version = kotlinVersion
    versionFile = rootDir.resolve("libraries/stdlib/src/kotlin/util/KotlinVersion.kt")
    versionPattern = """val CURRENT: KotlinVersion = KotlinVersion\((\d+, \d+, \d+)\)"""
    replacement {
        val (major, minor, optPatch) = Regex("""^(\d+)\.(\d+)(\.\d+)?""").find(kotlinVersion)?.destructured ?: error("Cannot parse version")
        val newVersion = "$major, $minor, ${optPatch.trimStart('.').takeIf { it.isNotEmpty() } ?: "0" }"
        logger.lifecycle("Writing new standard library version components: $newVersion")
        newVersion
    }
}

val writeCompilerVersion by tasks.creating(PatternReplaceVersionTask::class) {
    version = kotlinVersion
    versionFile = rootDir.resolve("core/util.runtime/src/org/jetbrains/kotlin/config/KotlinCompilerVersion.java")
    versionPattern = """public static final String VERSION = "([^"]+)""""
    replacement {
        logger.lifecycle("Writing new compiler version: $kotlinVersion")
        kotlinVersion
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
