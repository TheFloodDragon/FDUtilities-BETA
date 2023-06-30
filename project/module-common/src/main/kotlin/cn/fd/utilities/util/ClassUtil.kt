package cn.fd.utilities.util

import taboolib.common.io.runningClassMap
import taboolib.common.util.unsafeLazy

/**
 * ��ǰ����������� (�ų�Taboolib�����)
 */
val runningClassMapWithoutTaboolib by unsafeLazy { runningClassMap.filter { !it.key.startsWith("tb") } }

/**
 * ��ǰ�����������ļ��� (�ų�Taboolib�����)
 */
val runningClassesWithoutTaboolib by unsafeLazy { runningClassMapWithoutTaboolib.values }