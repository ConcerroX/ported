import net.neoforged.moddevgradle.dsl.RunModel

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
version = "1.2.0"
group = "concerrox.ported"
base.archivesName = modId + "-neoforge-" + libs.versions.minecraft.get()

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

sourceSets.main.get().resources { srcDir("src/generated/resources") }

neoForge {
    version = libs.versions.neoForge.get()
    setAccessTransformers("src/main/resources/META-INF/accesstransformer.cfg")

    parchment {
        mappingsVersion = libs.versions.parchmentMappings.get()
        minecraftVersion = libs.versions.minecraft.get()
    }

    mods {
        create(modId) {
            sourceSet(sourceSets.main.get())
        }
    }

    runs {
        create("client", Action<RunModel> {
            client()
        })
        create("server", Action<RunModel> {
            server()
            programArgument("--nogui")
        })
        create("data", Action<RunModel> {
            data()
            programArguments.addAll(
                "--mod", modId,
                "--all", "--output", file("src/generated/resources/").absolutePath,
                "--existing", file("src/main/resources/").absolutePath,
            )
        })
        configureEach {
            systemProperty("forge.logging.markers", "REGISTRIES")
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
            logLevel = org.slf4j.event.Level.DEBUG
        }
    }
}

repositories {
    maven("https://thedarkcolour.github.io/KotlinForForge/") // Kotlin for Forge
    maven("https://maven.terraformersmc.com/") // EMI
    maven("https://maven.minecraftforge.net/") // TerraBlender
    maven("https://cursemaven.com/") // Cyanide
}

dependencies {
    implementation(libs.kotlinForForge)
    implementation(libs.emi)
    implementation(libs.cyanide)
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

tasks.withType<ProcessResources>().configureEach {
    val versions = libs.versions
    val replaceProperties = mapOf(
        "minecraft_version" to versions.minecraft,
        "minecraft_version_range" to versions.minecraftRange,
        "neo_version" to versions.neoForge,
        "neo_version_range" to versions.neoForgeRange,
        "loader_version_range" to versions.neoForgeLoaderRange,
        "terra_blender_version_range" to versions.terraBlenderRange,
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

tasks.shadowJar {
    isZip64 = true
}