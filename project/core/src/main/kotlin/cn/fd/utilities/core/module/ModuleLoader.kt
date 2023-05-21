package cn.fd.utilities.core.module

import cn.fd.utilities.common.util.toSimple
import cn.fd.utilities.core.clsload.ClassUtil.getSubClasses
import taboolib.common.platform.function.console
import taboolib.common.platform.function.getDataFolder
import taboolib.module.lang.sendLang
import java.io.File

object ModuleLoader {


    private var moduleManager = ModuleManager(listOf(File(getDataFolder().path, "workspace")))

    /**
     * ���ļ�����Ѱ�������ļ�
     */
    fun findModulesInDirs(dirs: List<File>): List<Class<out ModuleExpansion>> {/*
          ��ȡģ���ļ�������JAR��׺���ļ�
          ���һ���ļ���û�У��ͷ���һ���յ��б�
         */
        val files: List<File> = arrayListOf<File>().also { list ->
            dirs.forEach { ws ->
                ws.listFiles { _, name: String ->
                    name.endsWith(".jar") || name.endsWith(".class")
                }?.forEach { list.add(it) }
            }
        }

        return if (files.isEmpty()) emptyList()
        else {
            //���������ļ���ģ����չ��ļ���
            files.map { file: File? ->
                findModuleClass(file!!)
            }.toSimple()
        }
    }


    //ע��ģ��
    fun ModuleExpansion.register(): Boolean {
        //��ȡģ���ʶ��
        val identifier = this.name

//        val removed: ModuleExpansion? = ModuleManager.getModule(identifier)
//        if (removed != null && !removed.unregister()) {
//            return false
//        }

        moduleManager.modules[ModuleInfo(identifier)] = this

        console().sendLang("Module-Loader-Success", identifier, this.version)
        return true
    }

    //ж��ģ��
    fun ModuleExpansion.unregister() {
        moduleManager.modules.remove(ModuleInfo(this.name))
        console().sendLang("Module-Loader-Unregistered", this.name, this.version)
    }

    /**
     * �ӵ����ļ���Ѱ��ģ����չ��
     * @param file Ҫ��Ѱ�ҵĵ����ļ�
     */
    fun findModuleClass(file: File): List<Class<out ModuleExpansion>> {
        try {
            //��ȡ�̳е����� (�ȼ���,���ȡ����)
            val subClasses = getSubClasses(file, ModuleExpansion::class.java)
            //�����JAR�ļ����Ҳ���ģ����չ��
            if (file.endsWith(".jar") && subClasses.isEmpty()) {
                console().sendLang("Module-Loader-NotClassError", file.name)
                return listOf()
            }
            //::Begin �ƺ�ûʲô��
            //��ȡģ����չ���������ķ���
//            val moduleMethods = Arrays.stream(mClass!!.declaredMethods).map { method: Method ->
//                MethodSignature(method.name, method.parameterTypes)
//            }.collect(Collectors.toSet())
//            //�����û�б��������ķ���
//            if (!moduleMethods.containsAll(
//                    Arrays.stream(ModuleExpansion::class.java.declaredMethods).filter { method: Method ->
//                        Modifier.isAbstract(method.modifiers)
//                    }.map { method: Method ->
//                        MethodSignature(method.name, method.parameterTypes)
//                    }.collect(Collectors.toSet())
//                )
//            ) {
//                console().sendLang("Module-Loader-NotRequiredMethodError", file.name)
//                return null
//            }
            //::END

            return subClasses

        } catch (ex: VerifyError) {
            console().sendLang("Module-Loader-VerifyError", file.name, ex.javaClass.simpleName, ex.message ?: "UNKNOWN")
            return listOf()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return listOf()
    }

    fun setModuleManager(mm: ModuleManager) {
        moduleManager = mm
    }

    fun getModuleManager(): ModuleManager {
        return moduleManager
    }

}