package cn.fd.ratziel.module.itemengine.nbt

import cn.fd.ratziel.core.function.MirrorClass
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.library.reflex.Reflex.Companion.invokeConstructor
import taboolib.module.nms.MinecraftVersion
import taboolib.module.nms.nmsClass

/**
 * NBTList - NBT列表
 *
 * @author TheFloodDragon
 * @since 2023/11/24 22:18
 */
open class NBTList(rawData: Any) : NBTData(
    if (rawData is List<*>) {
        // 查找第一个并判断类型
        rawData.firstOrNull()?.let {
            when {
                checkIsNmsNBT(it) -> new(rawData) // Nms 下的 NBTTagList
                it is TiNBTData -> TiNBTData.translateList(TiNBTList(), rawData) // Taboolib 下的 ItemTagList
                else -> null
            }
        } ?: new()
    } else rawData,
    NBTDataType.LIST
) {

    val content: List<NBTData>
        get() = if (isTiNBT()) (data as TiNBTList).map { toNBTData(it) } else getField(this)!!.map { toNBTData(it) }

    constructor() : this(TiNBTList())

    companion object : MirrorClass<NBTList>() {

        @JvmStatic
        override val clazz: Class<out Any> by lazy { nmsClass("NBTTagList") }

        @JvmStatic
        override fun of(obj: Any) = NBTList(obj)

        /**
         * NBTTagList#constructor()
         */
        @JvmStatic
        fun new() = clazz.invokeConstructor()

        /**
         * NBTTagList#constructor(List<NBTBase>,byte)
         */
        @JvmStatic
        fun new(list: ArrayList<Any?>) = clazz.invokeConstructor(list, 0)

        @JvmStatic
        fun new(collection: Collection<Any?>) = new(ArrayList(collection))

        internal val listFieldName = if (MinecraftVersion.isUniversal) "c" else "list"

        internal fun getField(target: NBTList) = target.data.getProperty<List<Any>>(listFieldName)

    }

}