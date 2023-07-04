package cn.fd.utilities.util

import java.io.File

/**
 * ����ļ���ȡ
 */
fun File.listFilesDeep(): Iterator<File> {
    return this.walk().filter { it.isFile }.iterator()
}