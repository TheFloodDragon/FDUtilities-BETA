@file:Suppress("IMPLICIT_CAST_TO_ANY")

package cn.fd.ratziel.module.itemengine.nbt

import cn.fd.ratziel.core.function.MirrorClass
import cn.fd.ratziel.core.function.getMethodUnsafe
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.library.reflex.Reflex.Companion.invokeConstructor
import taboolib.library.reflex.ReflexClass
import taboolib.module.nms.MinecraftVersion
import taboolib.module.nms.nmsClass
import java.util.function.Consumer

/**
 * NBT别名
 */
typealias NBTTag = NBTCompound

/**
 * NBTCompound - 复合NBT类型
 *
 * @author TheFloodDragon
 * @since 2023/11/24 21:53
 */
open class NBTCompound(rawData: Any) : NBTData(rawData, NBTDataType.COMPOUND) {

    constructor() : this(TiNBTTag())

    /**
     * 获取数据
     * @param node 节点
     */
    operator fun get(node: String): NBTData? =
        (if (node == APEX_NODE_SIGN) this // 如果为顶级节点则返回自身
        else if (isTiNBT()) (data as TiNBTTag)[node]
        else NMSMethods.getMethod.invoke(data, node))?.let { toNBTData(it) }

    /**
     * 深度获取
     * @param node 节点
     */
    fun getDeep(node: String): NBTData? =
        (if (isTiNBT()) (data as TiNBTTag).getDeep(node)
        else getDeepWith(node, false) { it[node.substringAfterLast(DEEP_SEPARATION)] })?.let { toNBTData(it) }

    /**
     * 写入数据
     * @param node 节点
     * @param value NBT数据
     */
    fun put(node: String, value: NBTData?) {
        if (value == null) return
        if (isTiNBT()) (data as TiNBTTag)[node] = value.getAsTiNBT()
        else NMSMethods.putMethod.invoke(data, node, value.getAsNmsNBT())
    }

    fun put(node: String, value: Any?) = put(node, toNBTData(value))

    /**
     * 深度写入
     */
    fun putDeep(node: String, value: NBTData?) {
        if (value == null) return
        if (isTiNBT()) (data as TiNBTTag).putDeep(node, value.getAsTiNBT())
        else getDeepWith(node, true) { it.put(node.substringAfterLast(DEEP_SEPARATION), value.getAsNmsNBT()) }
    }

    fun putDeep(node: String, value: Any?) = putDeep(node, toNBTData(value))

    /**
     * 删除数据
     * @param node 节点
     */
    fun remove(node: String) {
        if (isTiNBT()) (data as TiNBTTag).remove(node)
        else NMSMethods.removeMethod.invoke(data, node)
    }

    /**
     * 深度删除，以 "." 作为分层符
     */
    fun removeDeep(node: String) {
        if (isTiNBT()) (data as TiNBTTag).removeDeep(node)
        else getDeepWith(node, false) { it.remove(node.substringAfterLast(DEEP_SEPARATION)) }
    }

    /**
     * 针对"深度方法"的重复代码做出的优化
     */
    fun getDeepWith(node: String, create: Boolean, action: (NBTCompound) -> Any?): NBTData? {
        // 分割节点 (丢弃最后一层)
        val keys = node.split(DEEP_SEPARATION).dropLast(1)
        // 找到的标签
        var find: NBTCompound = this
        // 遍历各级节点
        for (element in keys) {
            var next = find[element] // 下一级节点
            if (next == null) {
                if (create) {
                    next = NBTCompound()
                    find[element] = next
                } else return null
            }
            // 如果下一级节点还是复合标签,则代表可以继续获取
            if (next is NBTCompound) find = next else return null
        }
        return action(find) as? NBTData
    }

    /**
     * 批量设置数据
     */
    fun putAll(vararg dataPair: Pair<String, NBTData?>, deep: Boolean = false) = this.apply {
        dataPair.forEach { if (deep) putDeep(it.first, it.second) else put(it.first, it.second) }
    }

    /**
     * 浅度修改 - 不允许深层节点的修改 (不会起效除非你套娃)
     * @param node 浅层节点
     * @param default 不存在时使用的默认消费品
     * @param function 对消费品进行操作
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : NBTData> editShallow(
        node: String,
        default: T,
        function: Consumer<T>,
    ): T = (this[node] as? T ?: default).also {
        function.accept(it) // 修改数据
        this.put(node, it) // 重设置
    }

    fun editShallow(node: String, function: Consumer<NBTTag>): NBTTag = editShallow(node, NBTTag(), function)

    /**
     * 合并复合标签
     * @param replace 是否替换原有的标签
     */
    fun merge(target: NBTCompound, replace: Boolean = true): NBTCompound = this.also { source ->
        (if (target.isTiNBT()) target.data as TiNBTTag else NMSMethods.getAsMap(target))
            ?.forEach { (key, value) ->
                val origin = source[key]
                // 如果存在该标签并且不允许替换,则直接跳出循环
                if (origin != null && !replace) return@forEach
                // 设置值
                source[key] = toNBTData(value).let {
                    // 复合标签和基本标签判断
                    if (it is NBTCompound) NBTCompound(new()).merge(it, replace) else it
                }
            }
    }

    /**
     * Kotlin操作符
     */
    operator fun set(node: String, value: NBTData?) = put(node, value)

    operator fun set(node: String, value: Any?) = put(node, value)

    companion object : MirrorClass<NBTCompound>() {

        @JvmStatic
        override val clazz: Class<out Any> by lazy { nmsClass("NBTTagCompound") }

        @JvmStatic
        override fun of(obj: Any) = NBTCompound(obj)

        /**
         * 深度操作分层符
         */
        const val DEEP_SEPARATION = "."

        /**
         * 顶级节点符号
         */
        const val APEX_NODE_SIGN = "!"

        /**
         * NBTTagCompound#constructor()
         */
        @JvmStatic
        fun new() = clazz.invokeConstructor()

        /**
         * NBTTagCompound#constructor(Map<String,NBTBase>)
         */
        @JvmStatic
        fun new(map: HashMap<String, Any>) = clazz.invokeConstructor(map)

    }

    internal object NMSMethods {

        val classStructure by lazy { ReflexClass.of(clazz).structure }

        val mapFieldName = if (MinecraftVersion.isUniversal) "x" else "map"

        fun getAsMap(target: NBTCompound) = target.data.getProperty<Map<String, Any>>(mapFieldName)

        val getMethod by lazy {
            classStructure.getMethodUnsafe(
                name = if (MinecraftVersion.isUniversal) "c" else "get",
                String::class.java
            )
        }

        val putMethod by lazy {
            classStructure.getMethodUnsafe(
                name = if (MinecraftVersion.isUniversal) "a" else "set",
                String::class.java, classNBTBase
            )
        }

        val removeMethod by lazy {
            classStructure.getMethodUnsafe(
                name = if (MinecraftVersion.isUniversal) "r" else "remove",
                String::class.java
            )
        }

    }

}