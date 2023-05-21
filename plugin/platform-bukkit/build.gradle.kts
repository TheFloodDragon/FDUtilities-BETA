import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

dependencies {

    //Bukkit
    installTaboo("platform-bukkit")

    compileOnly("com.google.guava:guava:31.1-jre")

    rootProject
        .childProjects["project"]!!.childProjects
        .values.filter { !it.name.contains("bungee") }
        .forEach { implementation(it) }
}

tasks {
    withType<ShadowJar> {
        // Options
        archiveAppendix.set("")
        archiveClassifier.set("")
        archiveVersion.set(rootVersion)
        //archiveBaseName.set("$rootName-Bukkit")
        archiveBaseName.set(rootName)
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
        relocate("kotlin.", "kotlin1810.") { exclude("kotlin.Metadata") }
        relocate("kotlinx.serialization", "kotlinx150.serialization")
    }
    build {
        dependsOn(shadowJar)
    }
}