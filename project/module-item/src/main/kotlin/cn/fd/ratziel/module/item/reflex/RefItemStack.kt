package cn.fd.ratziel.module.item.reflex

import cn.fd.ratziel.core.exception.UnsupportedTypeException
import cn.fd.ratziel.module.item.impl.ItemMaterialImpl
import cn.fd.ratziel.module.item.nbt.NBTCompound
import cn.fd.ratziel.module.item.reflex.NMSItem.Companion.nmsClass
import cn.fd.ratziel.module.item.reflex.RefItemStack.Companion.obcClass
import taboolib.library.reflex.Reflex.Companion.invokeConstructor
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import taboolib.library.reflex.ReflexClass
import taboolib.module.nms.MinecraftVersion
import taboolib.module.nms.nmsClass
import taboolib.module.nms.obcClass
import org.bukkit.Material as BukkitMaterial
import org.bukkit.inventory.ItemStack as BukkitItemStack

/**
 * RefItemStack
 *
 * @author TheFloodDragon
 * @since 2024/4/4 11:20
 */
class RefItemStack(raw: Any) {

    /**
     * ItemStack处理对象 (CraftItemStack)
     * 确保 CraftItemStack.handle 不为空
     */
    private var handle: Any = when {
        isObcClass(raw::class.java) -> raw // CraftItemStack
        isNmsClass(raw::class.java) -> newObc(raw) // net.minecraft.world.item.ItemStack
        /*
        烦人玩意 - an impl of interface BukkitItemStack
        用构造函数和asCraftCopy时handle都可能会null(它不会转换信息), 只有用asNMSCopy将其转换成NMS的ItemStack才能保留其信息
         */
        BukkitItemStack::class.java.isAssignableFrom(raw::class.java) -> newObc(asNMSCopy(raw))
        else -> throw UnsupportedTypeException(raw) // Unsupported Type
    }

    /**
     * 获取物品NBT数据
     */
    fun getData(): NBTCompound? = NMSItem.INSTANCE.getItemNBT(getAsNms())?.let { NBTCompound(it) }

    /**
     * 设置物品NBT数据
     */
    fun setData(data: NBTCompound) = NMSItem.INSTANCE.setItemNBT(getAsNms(), data.getData())

    /**
     * 获取物品材料
     */
    fun getMaterial(): BukkitMaterial =
        if (MinecraftVersion.isHigherOrEqual(MinecraftVersion.V1_13)) {
            obcGetMaterialMethod.invoke(handle)!! as BukkitMaterial
        } else {
            ItemMaterialImpl.getBukkitMaterial(obcGetMaterialMethodLegacy.invoke(handle)!! as Int)!!
        }

    /**
     * 设置物品材料
     */
    fun setMaterial(material: BukkitMaterial) =
        if (MinecraftVersion.isHigherOrEqual(MinecraftVersion.V1_13)) {
            obcSetMaterialMethod.invoke(handle, material)
        } else {
            obcSetMaterialMethodLegacy.invoke(handle, ItemMaterialImpl.getIdUnsafe(material))
        }

    /**
     * 获取物品数量
     */
    fun getAmount(): Int = obcGetAmountMethod.invoke(handle) as Int

    /**
     * 设置物品数量
     */
    fun setAmount(amount: Int) = obcSetAmountMethod.invoke(handle, amount)

    /**
     * 获取物品最大堆叠数量
     */
    fun getMaxStackSize(): Int = obcGetMaxStackSizeMethod.invoke(handle) as Int

    /**
     * 获取物品损伤值
     * @return [Short]
     */
    fun getDamage(): Short = obcGetDamageMethod.invoke(handle) as Short

    /**
     * 设置物品损伤值
     */
    fun setDamage(damage: Short) = obcSetDamageMethod.invoke(handle, damage)

    /**
     * 克隆数据
     */
    fun clone() = this.apply { this.handle = obcCloneMethod.invoke(handle)!! }

    /**
     * 获取NMS形式实例 (net.minecraft.world.ItemStack)
     */
    fun getAsNms(): Any = obcHandleField.get(handle)!!

    /**
     * 获取CraftBukkit形式实例 (CraftItemStack)
     */
    fun getAsObc(): Any = handle

    /**
     * 获取Bukkit形式实例 ([BukkitItemStack])
     */
    fun getAsBukkit(): BukkitItemStack = getAsObc() as BukkitItemStack

    companion object {

        /**
         * obc.ItemStack
         *   org.bukkit.craftbukkit.$VERSION.inventory.CraftItemStack
         */
        val obcClass by lazy { obcClass("inventory.CraftItemStack") }

        fun newObc() = obcClass.invokeConstructor()

        /**
         * private CraftItemStack(net.minecraft.world.item.ItemStack item)
         * private CraftItemStack(ItemStack item)
         */
        fun newObc(nmsItem: Any) = obcClass.invokeConstructor(nmsItem)

        fun newNms() = nmsClass.invokeConstructor()

        // private nms.ItemStack(NBTTagCompound nbt)
        fun newNms(nbt: Any) = nmsClass.invokeConstructor(nbt)

        /**
         * public static net.minecraft.world.item.ItemStack asNMSCopy(ItemStack original)
         */
        fun asNMSCopy(original: Any): Any = obcClass.invokeMethod("asNMSCopy", original, isStatic = true)!!

        /**
         * public static ItemStack asBukkitCopy(net.minecraft.world.item.ItemStack original)
         */
        fun asBukkitCopy(original: Any): BukkitItemStack = obcClass.invokeMethod("asBukkitCopy", original, isStatic = true)!!

        /**
         * public static CraftItemStack asCraftCopy(ItemStack original)
         */
        fun asCraftCopy(original: Any): Any = obcClass.invokeMethod("asCraftCopy", original, isStatic = true)!!

        /**
         * 检查类是否为[obcClass]
         */
        fun isObcClass(clazz: Class<*>) = obcClass.isAssignableFrom(clazz)

        /**
         * 检查类是否为[nmsClass]
         */
        fun isNmsClass(clazz: Class<*>) = nmsClass.isAssignableFrom(clazz)

        // net.minecraft.world.item.ItemStack handle;
        internal val obcHandleField by lazy {
            ReflexClass.of(obcClass).getField("handle")
        }

        // public CraftItemStack clone()
        internal val obcCloneMethod by lazy {
            ReflexClass.of(obcClass).getMethod("clone")
        }

        // public Material getType()
        // public int getTypeId()
        internal val obcGetMaterialMethod by lazy {
            ReflexClass.of(obcClass).getMethod("getType")
        }

        internal val obcGetMaterialMethodLegacy by lazy {
            ReflexClass.of(obcClass).getMethod("getTypeId")
        }

        // public void setType(Material type)
        // public void setTypeId(int var1)
        internal val obcSetMaterialMethod by lazy {
            ReflexClass.of(obcClass).structure.getMethodByType("setType", BukkitMaterial::class.java)
        }

        internal val obcSetMaterialMethodLegacy by lazy {
            ReflexClass.of(obcClass).structure.getMethodByType("setTypeId", Int::class.java)
        }

        // public int getAmount()
        internal val obcGetAmountMethod by lazy {
            ReflexClass.of(obcClass).getMethod("getAmount")
        }

        // public void setAmount(int var1)
        internal val obcSetAmountMethod by lazy {
            ReflexClass.of(obcClass).structure.getMethodByType("setAmount", Int::class.java)
        }

        // public int getMaxStackSize()
        internal val obcGetMaxStackSizeMethod by lazy {
            ReflexClass.of(obcClass).getMethod("getMaxStackSize")
        }

        // public short getDurability()
        internal val obcGetDamageMethod by lazy {
            ReflexClass.of(obcClass).getMethod("getDurability")
        }

        // public void setDurability(short var1)
        internal val obcSetDamageMethod by lazy {
            ReflexClass.of(obcClass).structure.getMethodByType("setDurability", Short::class.java)
        }

    }

}