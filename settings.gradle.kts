rootProject.name = "FDUtilities"

include("project:bukkit")
include("plugin")
//��������ģ��
File("${rootDir}\\module").listFiles()?.filter { it.isDirectory }?.forEach {
    include("module:${it.name}")
}