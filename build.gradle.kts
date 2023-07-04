import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    `maven-publish`
    id("org.jetbrains.kotlin.jvm") version kotlinVersion apply false
    id("com.github.johnrengelman.shadow") version shadowJarVersion apply false
}

subprojects {
    applyPlugins()

    repositories {
        projectRepositories()
    }

    dependencies {
        compileOnly(kotlin("stdlib"))

        if (parent?.name == "project") {
            compileCore(11903)
            compileTabooLib()
            //MiniMessage: https://docs.adventure.kyori.net/minimessage/api.html
            adventure()
        }

        //Runtimeʵ�ֵ�ģ������
        if (name.contains("runtime"))
            parent!!.childProjects.forEach {
                if (it.value.name.contains("module"))
                    implementation(it.value)
            }
    }

    java {
        withSourcesJar()
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    group = rootGroup
    version = rootVersion

    if (parent?.name != "plugin" && parent?.name != "script") {
        buildDirClean()
    }

    tasks {
        withType<JavaCompile> { options.encoding = "UTF-8" }
        //һ������
        withType<ShadowJar> {
            // Options
            archiveAppendix.set("")
            archiveClassifier.set("")
            archiveVersion.set(rootVersion)
            //archiveBaseName.set("$rootName-Bukkit")
            // Exclude
            exclude("META-INF/**")
            exclude("com/**", "org/**")
            // Adventure (����Ҫ,��Ϊ�Ƕ�̬����)
            //relocate("net.kyori", "$rootGroup.common.adventure")
            // Taboolib
            relocate("taboolib", "$rootGroup.taboolib")
            relocate("tb", "$rootGroup.taboolib")
            relocate("org.tabooproject", "$rootGroup.taboolib.library")
            // Kotlin
            relocate("kotlin.", "kotlin1822.") { exclude("kotlin.Metadata") }
            relocate("kotlinx.serialization", "kotlinx150.serialization")
        }
    }

}

buildDirClean()

output()