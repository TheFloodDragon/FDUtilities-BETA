package cn.fd.utilities.module

import java.nio.file.Path

class ModuleInfo(
    //ģ���ʶ��
    val identifier: String,
    //�Ƿ�����
    val isEnabled: Boolean = true,
    //ģ�����ڵ��ļ�(.jar��.class)
    val filePath: Path? = null
)