package cn.fd.utilities.script.api

/**
 * ���п������
 *
 * @author ����
 * @since 2022/5/16 01:01
 */
interface RuntimeClassLoader {

    /**
     * ��ȡ�����Ѽ��ص���
     */
    fun runningClasses(): Map<String, Class<*>>

    /**
     * ��ȡ��
     */
    fun findClass(name: String): Class<*>
}