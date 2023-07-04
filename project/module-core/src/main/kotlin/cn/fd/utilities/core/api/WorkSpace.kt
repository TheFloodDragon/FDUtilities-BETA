package cn.fd.utilities.core.api

import cn.fd.utilities.util.listFilesDeep
import java.io.File

class WorkSpace(
    /**
     * �����ռ�·��
     */
    val path: File
) {

    /**
     * ��ȡ�����ռ��е��ļ�
     */
    fun getFiles(): Iterator<File> {
        return path.listFilesDeep()
    }

}