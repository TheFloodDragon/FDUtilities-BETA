rootProject.name = "FDUtilities"

//��������Ŀ�ļ���(�����Լ�һ��һ����)
File("${rootDir}\\project").listFiles()?.filter { it.isDirectory }?.forEach {
    include("project:${it.name}")
}

//Bukkitʵ��
include("plugin:platform-bukkit")
//ȫ���ۺϰ�
include("plugin:all")


//��������ģ�� TODO Ӧ��������Ϊ�ű�ϵͳ(FScript-����)
//File("${rootDir}\\module").listFiles()?.filter { it.isDirectory }?.forEach {
//    include("module:${it.name}")
//}