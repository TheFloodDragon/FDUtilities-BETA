package cn.fd.utilities.module

import cn.fd.utilities.util.ClassUtil
import taboolib.common.platform.function.console
import taboolib.common.platform.function.getDataFolder
import taboolib.module.lang.sendLang
import java.io.File
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.*
import java.util.stream.Collectors

object ModuleLoader {


    private var moduleManager = ModuleManager(listOf(File(getDataFolder().path, "workspace")))

    /**
     * ���ļ�����Ѱ�������ļ�
     */
    fun findModulesInDirs(dirs: List<File>): List<Class<out ModuleExpansion?>?> {
        /*
          ��ȡģ���ļ�������JAR��׺���ļ�
          ���һ���ļ���û�У��ͷ���һ���յ��б�
         */
        val files: List<File> = arrayListOf<File>().also { list ->
            dirs.forEach { ws ->
                ws.listFiles { _, name: String ->
                    name.endsWith(".jar")
                }?.forEach { list.add(it) }
            }
        }

        return if (files.isEmpty()) emptyList()
        else {
            //���������ļ���ģ����չ��ļ���
            files.map { file: File? ->
                findModuleClass(file!!)
            }.toList()
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

        moduleManager.getModules()[ModuleInfo(identifier)] = this

        console().sendLang("Module-Loader-Success", identifier, this.version)
        return true
    }

    //ж��ģ��
    fun ModuleExpansion.unregister() {
        moduleManager.getModules().remove(ModuleInfo(this.name))
        console().sendLang("Module-Loader-Unregistered", this.name, this.version)
    }

    /**
     * �ӵ����ļ���Ѱ��ģ����չ��
     * @param file Ҫ��Ѱ�ҵĵ����ļ�
     */
    fun findModuleClass(file: File): Class<out ModuleExpansion>? {
        try {
            //��ȡ�̳е�����
            val mClass = ClassUtil.findClass(file, ModuleExpansion::class.java)
            //�����JAR�ļ����Ҳ���ģ����չ��
            if (file.endsWith(".jar") && mClass == null) {
                console().sendLang("Module-Loader-NotClassError", file.name)
                return null
            }
            //::Begin �ƺ�ûʲô��
            //��ȡģ����չ���������ķ���
            val moduleMethods = Arrays.stream(mClass!!.declaredMethods).map { method: Method ->
                MethodSignature(method.name, method.parameterTypes)
            }.collect(Collectors.toSet())
            //�����û�б��������ķ���
            if (!moduleMethods.containsAll(
                    Arrays.stream(ModuleExpansion::class.java.declaredMethods).filter { method: Method ->
                        Modifier.isAbstract(method.modifiers)
                    }.map { method: Method ->
                        MethodSignature(method.name, method.parameterTypes)
                    }.collect(Collectors.toSet())
                )
            ) {
                console().sendLang("Module-Loader-NotRequiredMethodError", file.name)
                return null
            }
            //::END

            return mClass

        } catch (ex: VerifyError) {
            console().sendLang("Module-Loader-VerifyError", file.name, ex.javaClass.simpleName, ex.message ?: "UNKNOWN")
            return null
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

//    fun getModuleInstance(clazz: Class<out ModuleExpansion?>): Optional<ModuleExpansion> {
//        try {
//            //����ʵ��
//            val module: ModuleExpansion = clazz.createInstance() ?: return Optional.empty()
//            //��Ҫģ��ı�ʶ�����ǿ�,����ͱ���
//            Objects.requireNonNull(module.name)
//            //���ģ��ûע��
//            return if (!module.register()) {
//                Optional.empty()
//            } else Optional.of(module)
//        } catch (ex: LinkageError) {
//            console().sendLang("Module-Loader-NotDependencyError", clazz.simpleName)
//        } catch (ex: NullPointerException) {
//            console().sendLang("Module-Loader-NullIdentifierError", clazz.simpleName)
//        }
//        return Optional.empty()
//    }
//
//    @Throws(LinkageError::class)
//    fun <T> Class<out T?>.createInstance(): T? {
//        return try {
//            this.getDeclaredConstructor().newInstance()
//        } catch (ex: java.lang.Exception) {
//            if (ex.cause is LinkageError) {
//                throw (ex.cause as LinkageError?)!!
//            }
//            console().sendLang("Module-Loader-UnknownError")
//            null
//        }
//    }

    fun setModuleManager(mm: ModuleManager) {
        moduleManager = mm
    }

    fun getModuleManager(): ModuleManager {
        return moduleManager
    }

}