rootProject.name = "FDUtilities"

//Bukkitʵ��
include("plugin:platform-bukkit")
include("plugin:all")

//��������Ŀ�ļ���(�����Լ�һ��һ����)
File("${rootDir}\\project").listFiles()?.filter { it.isDirectory }?.forEach {
    include("project:${it.name}")
}


//��������ģ�� TODO Ӧ��������Ϊ�ű�ϵͳ(FScript-����)
//File("${rootDir}\\module").listFiles()?.filter { it.isDirectory }?.forEach {
//    include("module:${it.name}")
//}