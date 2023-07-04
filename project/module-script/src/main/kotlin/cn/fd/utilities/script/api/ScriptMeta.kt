package cn.fd.utilities.script.api

import java.io.File

interface ScriptMeta {

    /**
     * �ű�����
     */
    fun name(): String

    /**
     * �ű�Դ�ļ�
     */
    fun source(file: File)

    /**
     * �ű������Ļ����ļ�
     */
    fun compiled(file: File)

}