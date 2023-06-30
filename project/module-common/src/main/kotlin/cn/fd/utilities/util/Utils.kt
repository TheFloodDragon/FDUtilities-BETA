package cn.fd.utilities.util

import taboolib.module.lang.Level

fun debug(info: Any?, auto: Boolean = true, level: Level = Level.INFO) {
    if (auto) {
        when (info) {
            is Iterable<*> -> info.forEach { println(it) }
            else -> println(info)
        }
    }
    println(info)
}

/**
 * ��һ�������б�(Ԫ��)���б��кϲ����б�(Ԫ��)
 * Ч��:
 *  listA[ listB[ 1, 2, 3], listB[ 4, 5, 6] ]
 *  To: ListB[ 1, 2, 3, 4, 5, 6]
 */
fun <T> List<List<T>>.mergeAll(): List<T> {
    return mutableListOf<T>().also { out ->
        this.forEach { out.addAll(it) }
    }
}