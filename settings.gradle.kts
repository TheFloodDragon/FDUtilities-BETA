rootProject.name = "FDUtilities"

include("plugin:platform-bukkit")

include("project:common")
include("project:core")
include("project:taboolib-generate-bukkit")

//��������ģ��
File("${rootDir}\\module").listFiles()?.filter { it.isDirectory }?.forEach {
    include("module:${it.name}")
}