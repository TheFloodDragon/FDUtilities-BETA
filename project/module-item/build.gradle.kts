dependencies {
    compileNMS()
    compileCore(12005, mapped = true)
    compileOnly("com.google.guava:guava:32.1.2-jre")
    compileTaboo("platform-bukkit")
    compileTaboo("module-nms")
    compileTaboo("module-nms-util-stable")
    compileTaboo("module-bukkit-xseries")
    compileTaboo("module-bukkit-util")
    compileModule("runtime-bukkit")
}