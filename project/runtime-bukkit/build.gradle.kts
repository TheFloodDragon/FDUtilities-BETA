plugins {
    id("io.izzel.taboolib") version taboolibPluginVersion
}

dependencies {
    compileOnly("com.google.guava:guava:31.1-jre")
    // Folia
    compileOnly("dev.folia:folia-api:1.20.1-R0.1-SNAPSHOT")
    // Ratziel Module - Kether
    installModule("module-kether")
}

taboolib {
    version = taboolibVersion
    taboolibModules.forEach { install(it) }
    install("platform-bukkit")
    install("module-kether")

    description {
        name = rootName

        contributors {
            name("TheFloodDragon")
        }

        dependencies {
//            name("PlaceholderAPI").optional(true)
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

    // 排除原有BukkitPlugin防止重复
    exclude("taboolib/platform/BukkitPlugin")

    classifier = null
    options("skip-minimize", "keep-kotlin-module", "skip-taboolib-relocate")
}