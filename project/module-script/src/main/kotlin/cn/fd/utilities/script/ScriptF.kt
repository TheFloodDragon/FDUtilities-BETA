package cn.fd.utilities.script

abstract class ScriptF {

    /**
     * �ű���ʶ��(����)
     */
    val name: String = this.javaClass.name

    /**
     * �ű�������ʱ����
     */
    abstract fun init()

    /**
     * �ű�����ʱ����
     */
    abstract fun enable()

    /**
     * �ű�����ʱ����
     */
    abstract fun disable()

}