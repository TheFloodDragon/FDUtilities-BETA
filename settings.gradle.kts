rootProject.name = "FDUtilities"

include("plugin:bukkit")
//��������ģ��
File("${rootDir}\\module").listFiles()?.filter { it.isDirectory }?.forEach {
    include("module:${it.name}")
}