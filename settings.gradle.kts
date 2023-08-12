rootProject.name = "FDUtilities"

apply("project")
apply("script")

//Bukkit
include("plugin:platform-bukkit")
//全部聚合版
include("plugin:platform-all")

//所有字项目的加载(懒得自己一个一个打)
fun apply(name: String) {
    File(rootDir, name).listFiles()?.filter { it.isDirectory }?.forEach {
        include("$name:${it.name}")
    }
}