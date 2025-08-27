plugins {
    id("idea")
    kotlin("jvm") version "2.1.21"
    alias(libs.plugins.shadow)
    alias(libs.plugins.modDevGradle)
}

val modId = "ported"
val modName = "Ported"
val modLicense = "MIT License"
val modAuthors = "ConcerroX"
val modDescription = "Hello world!"
version = "1.0.0"
group = "concerrox.ported"
base.archivesName = modId

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

sourceSets.main.get().resources { srcDir("src/generated/resources") }

neoForge {
    version = libs.versions.neoForge.get()
    setAccessTransformers("src/main/resources/META-INF/accesstransformer.cfg")
    parchment {
        mappingsVersion = libs.versions.parchmentMappings.get()
        minecraftVersion = libs.versions.minecraft.get()
    }
    mods { create(modId) { sourceSet(sourceSets.main.get()) } }
    runs {
        create("client") { client() }
        create("server") { server(); programArgument("--nogui") }
        create("data") {
            data()
            programArguments.addAll(
                "--mod", modId,
                "--all", "--output", file("src/generated/resources/").absolutePath,
                "--existing", file("src/main/resources/").absolutePath,
            )
        }
        configureEach {
            systemProperty("forge.logging.markers", "REGISTRIES")
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
            logLevel = org.slf4j.event.Level.DEBUG
        }
    }
}
//
//configurations {
//    runtimeClasspath.extendsFrom(project.loc)
//}

repositories {
    maven("https://thedarkcolour.github.io/KotlinForForge/") // Kotlin for Forge
    maven("https://maven.terraformersmc.com/") // EMI
}

dependencies {
    implementation(libs.kotlinForForge)
    runtimeOnly(libs.emi)
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

tasks.withType<ProcessResources>().configureEach {
    val ver = libs.versions
    val replaceProperties = mapOf(
        "minecraft_version" to ver.minecraft,
        "minecraft_version_range" to ver.minecraftRange,
        "neo_version" to ver.neoForge,
        "neo_version_range" to ver.neoForgeRange,
        "loader_version_range" to ver.neoForgeLoaderRange,
        "mod_id" to modId,
        "mod_name" to modName,
        "mod_license" to modLicense,
        "mod_version" to version,
        "mod_authors" to modAuthors,
        "mod_description" to modDescription
    )
    inputs.properties(replaceProperties)
    filesMatching("META-INF/neoforge.mods.toml") { expand(replaceProperties) }
}
