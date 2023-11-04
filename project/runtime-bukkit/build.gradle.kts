plugins {
    id("io.izzel.taboolib") version taboolibPluginVersion
}

dependencies {
    // 其它
    compileCore(12002)
    compileOnly("com.google.code.gson:gson:2.8.9")
    compileOnly("net.md-5:bungeecord-chat:1.17")
    compileOnly("me.clip:placeholderapi:2.11.4")
    // Folia
    compileOnly("dev.folia:folia-api:1.20.1-R0.1-SNAPSHOT")
    taboo("com.tcoded:FoliaLib:0.3.1")
    // Module - Kether
    installModule("module-kether")
}

tasks {
    build { dependsOn(tabooRelocateJar) }
}

taboolib {
    version = taboolibVersion
    // Taboolib 模块
    taboolibModules.forEach { install(it) }
    install("platform-bukkit")
    install("module-nms")
    install("module-nms-util")
    install("expansion-player-fake-op")

    description {
        name = rootName

        contributors {
            name("TheFloodDragon")
        }

        dependencies {
            name("PlaceholderAPI").optional(true)
        }

        bukkitNodes = HashMap<String, Any>().apply {
            // API Version
            put("api-version", 1.13)
            // Folia Support
            put("folia-supported", true)
            // Build Info
            put("built-date", currentISODate)
            put("built-by", systemUserName)
            put("built-os", systemOS)
            put("built-ip", systemIP)
        }

    }

    relocate("com.tcoded.folialib.", "$rootGroup.library.folia.folialib_0_3_1.")

    classifier = null
    options("skip-minimize", "keep-kotlin-module", "skip-taboolib-relocate")
}