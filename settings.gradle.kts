rootProject.name = "FDUtilities"

applyAll("project")
applyAll("script")

//Bukkitʵ��
include("plugin:platform-bukkit")
//ȫ���ۺϰ�
include("plugin:all")

//��������Ŀ�ļ���(�����Լ�һ��һ����)
fun applyAll(name: String) {
    File("${rootDir}\\$name").listFiles()?.filter { it.isDirectory }?.forEach {
        include("$name:${it.name}")
    }
}