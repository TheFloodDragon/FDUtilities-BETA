package cn.fd.ratziel.bukkit.element.action

import cn.fd.ratziel.bukkit.element.action.api.Trait
import cn.fd.ratziel.core.memory.HashMapMemory

/**
 * ActionManager
 *
 * @author TheFloodDragon
 * @since 2023/8/25 14:47
 */
object ActionManager :
/**
 * 储存容器
 * K - 特征ID
 * V - 特征
 */
    HashMapMemory<String, Trait>()