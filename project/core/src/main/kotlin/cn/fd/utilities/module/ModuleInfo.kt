package cn.fd.utilities.module

import java.io.File

class ModuleInfo(
    //ģ���ʶ��
    val identifier: String,
    //�Ƿ�����
    val isEnabled: Boolean = true,
    //ģ�����ڵ��ļ�(.jar��.class)
    val filePath: File? = null
)