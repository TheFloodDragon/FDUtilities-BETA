package cn.fd.utilities.common.util

fun <T> List<List<T>>.toSimple(): List<T> {
    val list: MutableList<T> = mutableListOf()
    //Ƕ��ѭ��
    this.forEach { f ->
        f.forEach { s -> list.add(s) }
    }
    return list
}