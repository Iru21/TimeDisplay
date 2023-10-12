plugins {
    val kotlinVersion: String by System.getProperties()
    kotlin("jvm").version(kotlinVersion)
    application
    id("fabric-loom")
    id("com.modrinth.minotaur") version "2.+"
}

val archives_base_name: String by project
base {
    archivesName.set(archives_base_name)
}

val mod_version: String by project
version = mod_version
val maven_group: String by project
group = maven_group

val minecraft_version: String by project
val loader_version: String by project
val fabric_kotlin_version: String by project
val cloth_config_version: String by project

repositories {
    mavenCentral()
    maven(url = "https://maven.shedaniel.me")
    maven(url = "https://maven.terraformersmc.com/releases/")
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraft_version")

    val yarn_mappings: String by project
    mappings("net.fabricmc:yarn:$yarn_mappings:v2")

    val fabric_version: String by project
    modImplementation("net.fabricmc:fabric-loader:$loader_version")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabric_version")
    modImplementation("net.fabricmc:fabric-language-kotlin:$fabric_kotlin_version")

    modApi("me.shedaniel.cloth:cloth-config-fabric:$cloth_config_version")

    val mod_menu_version: String by project
    modApi("com.terraformersmc:modmenu:$mod_menu_version")
}

tasks {
    jar {
        from("LICENSE") {
            rename { "${it}_${base.archivesName}"
            }
        }
    }

    processResources {
        filesMatching("fabric.mod.json") {
            expand(
                mapOf(
                    "mod_version" to mod_version,
                    "minecraft_version" to minecraft_version,
                    "loader_version" to loader_version,
                    "fabric_kotlin_version" to fabric_kotlin_version,
                    "cloth_config_version" to cloth_config_version
                )
            )
        }
    }

    register<Exec>("createGitHubRelease") {
        dependsOn(remapJar)

        commandLine("git", "tag", "-a", "v${mod_version}", "-m", "Release v${mod_version}")
        commandLine("git", "push", "--tags")

        commandLine("gh", "release", "create", "v${mod_version}", "-F", "changelog.md", "-t", "v${mod_version}")
    }

    register("createReleases") {
        dependsOn("createGitHubRelease")
        dependsOn(modrinth)
        dependsOn(modrinthSyncBody)
    }
}

modrinth {
    val version = mod_version

    token.set(System.getenv("MODRINTH_TOKEN"))
    projectId.set("timedisplay")
    versionNumber.set(version)
    versionType.set("release")
    versionName.set("Time Display $mod_version")
    uploadFile.set(tasks.remapJar as Any)
    gameVersions.add(minecraft_version)
    loaders.addAll("fabric", "quilt")
    changelog.set(rootProject.file("changelog.md").readText())
    syncBodyFrom.set(rootProject.file("README.md").readText())
}