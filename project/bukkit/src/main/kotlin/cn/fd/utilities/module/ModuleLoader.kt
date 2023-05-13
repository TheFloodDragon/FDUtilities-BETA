package cn.fd.utilities.module

import cn.fd.utilities.util.ClassUtil
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang
import java.io.File
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.*
import java.util.stream.Collectors

object ModuleLoader {

    /**
     * ��ģ���ļ�����Ѱ�������ļ�
     */
    fun findModules(): List<Class<out ModuleExpansion?>?> {

        /*
          ��ȡģ���ļ�������JAR��׺���ļ�
          ���һ���ļ���û�У��ͷ���һ���յ��б�
         */
        val files: List<File> = arrayListOf<File>().also { list ->
            ModuleManager.getWorkspaces().forEach { ws ->
                ws.listFiles { _, name: String ->
                    name.endsWith(".jar")
                }?.forEach { list.add(it) }
            }
        }

        return if (files.isEmpty()) emptyList()
        else {
            //���������ļ���ģ����չ��ļ���
            files.map { file: File? ->
                findModuleInFile(file!!)
            }.toList()
        }
    }

    /**
     * �ӵ����ļ���Ѱ��ģ����չ��
     * @param file Ҫ��Ѱ�ҵĵ����ļ�
     */
    private fun findModuleInFile(file: File): Class<out ModuleExpansion>? {
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

}