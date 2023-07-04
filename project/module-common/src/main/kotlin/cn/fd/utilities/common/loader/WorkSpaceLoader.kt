package cn.fd.utilities.common.loader

import cn.fd.utilities.core.api.WorkSpace
import java.io.File

object WorkSpaceLoader {

    /**
     * ��ȡ�����ռ�
     * @param paths �����ռ�·�����ַ�������
     */
    fun getWorkSpaces(paths: Iterable<String>): List<WorkSpace> {
        return paths.map { WorkSpace(File(it)) }
    }

}