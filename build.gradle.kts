@file:Suppress("UnstableApiUsage")

import net.fabricmc.loom.task.RemapJarTask

plugins {
    java
    `java-library`
    id("fabric-loom").version(Fabric.Loom.version)

    `maven-publish`
}

base.archivesBaseName = ServerConfig.name
group = ServerConfig.group
version = ServerConfig.version

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    mavenLocal { name = "Maven Local" }
    maven(url = "https://maven.fabricmc.net/") { name = "Fabric" }
    maven(url = "https://cursemaven.com/") {
        name = "Curse Maven"
        content {
            includeGroup("curse.maven")
        }
    }
    maven(url = "https://jitpack.io/") {
        name = "JitPack"
    }
}

dependencies {
    minecraft(Minecraft.coordinate)
    mappings(Fabric.yarn.coordinate)

    deps.forEach {
        modImplementation(it.coordinate) {
            if (it.group != Fabric.api.group) {
                exclude(group = Fabric.api.group)
            }
            if (!it.transitive) {
                isTransitive = false
            }
        }
    }

    jijDeps.forEach { include(it.coordinate) }
    runtimeDeps.forEach { modRuntime(it.coordinate) }

    implementation(Gson.coordinate)
    compileOnly(Jsr305.coordinate)
}

object Meta {
    val changelogLink =
        "See https://github.com/alex5nader/${ServerConfig.name}/milestones for a list of changelogs."

    val minecraftVersions = listOf(
        "1.16.4"
    )

    val modVersionName = "Version ${ServerConfig.version}"
}

tasks.getByName<ProcessResources>("processResources") {
    inputs.properties("version" to ServerConfig.version)
    filesMatching("fabric.mod.json") {
        expand(
            "modId" to ServerConfig.modId,
            "modVersion" to ServerConfig.version,
            "modName" to ServerConfig.name,
            "modPrettyName" to ServerConfig.prettyName,

            "fapiVersion" to Fabric.api.version,
            "mcVersions" to Meta.minecraftVersions.joinToString("\",\"")
        )
    }
    filesMatching("mixins.${ServerConfig.modId}.json") {
        expand("modId" to ServerConfig.modId)
    }
}

val remapJar = tasks.getByName<RemapJarTask>("remapJar")

publishing {
    repositories {
        maven(url = "https://maven.pkg.github.com/alex5nader/${ServerConfig.name}") {
            name = "GitHub_Packages"
            credentials {
                username = "alex5nader"
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }

    publications.create<MavenPublication>("maven") {
        artifactId = ServerConfig.version

        artifact(remapJar) {
            classifier = null
            builtBy(remapJar)
        }
    }
}
