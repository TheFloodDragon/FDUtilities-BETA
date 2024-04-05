package cn.fd.ratziel.module.item.command

import cn.fd.ratziel.common.command.executeAsync
import cn.fd.ratziel.module.item.nbt.*
import cn.fd.ratziel.module.item.nbt.NBTCompound.DeepVisitor
import cn.fd.ratziel.module.item.reflex.RefItemStack
import org.bukkit.entity.Player
import org.bukkit.inventory.PlayerInventory
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.expansion.createHelper
import taboolib.module.chat.ComponentText
import taboolib.module.chat.Components
import taboolib.module.lang.*

/**
 * NBTCommand
 *
 * @author TheFloodDragon
 * @since 2023/12/15 21:39
 */
@CommandHeader(
    name = "r-nbt",
    aliases = ["nbt"],
    permission = "ratziel.command.nbt",
    description = "物品NBT管理命令"
)
object NBTCommand {

    @CommandBody
    val main = mainCommand { createHelper() }

    /**
     * 命令 - 查看 NBT
     * 用法: /nbt view <slot>
     */
    @CommandBody
    val view = subCommand {
        slot {
            executeAsync<ProxyPlayer> { player, _, arg ->
                player.cast<Player>().inventory.getDataBySlot(arg)?.also {
                    // 构建消息组件并发送
                    nbtAsComponent(player, it, 0, arg).sendTo(player)
                } ?: player.sendLang("NBTAction-EmptyTag")
            }
        }
    }

    /**
     * 命令 - 编辑 NBT
     * 用法: /nbt edit <slot> <node> <value>
     */
    @CommandBody
    val edit = subCommand {
        slot {
            dynamic {
                dynamic {
                    executeAsync<ProxyPlayer> { player, ctx, _ ->
                        // 获取基本信息
                        val rawNode = ctx.args()[2]
                        val rawValue = ctx.args()[3]
                        player.cast<Player>().inventory.getDataBySlot(ctx.args()[1])?.also {
                            val value = NBTSerializer.Mapper.deserializeFromString(rawValue)
                            it.putDeep(rawNode, value)
                            player.sendLang(
                                "NBTAction-Set",
                                rawNode, asString(value),
                                translateType(player, value).toLegacyText()
                            )
                        } ?: player.sendLang("NBTAction-EmptyTag")
                    }
                }
            }
        }
    }

    /**
     * 命令 - 删除 NBT
     * 用法: /nbt remove <slot> <node>
     */
    @CommandBody
    val remove = subCommand {
        slot {
            dynamic {
                executeAsync<ProxyPlayer> { player, ctx, _ ->
                    // 获取基本信息
                    val rawNode = ctx.args()[2]
                    player.cast<Player>().inventory.getDataBySlot(ctx.args()[1])?.also {
                        it.removeDeep(rawNode)
                        player.sendLang("NBTAction-Remove", rawNode)
                    } ?: player.sendLang("NBTAction-EmptyTag")
                }
            }
        }
    }

    fun nbtAsComponent(
        sender: ProxyCommandSender,
        nbt: NBTData,
        level: Int,
        slot: String,
        nodeDeep: String? = null,
        isFirst: Boolean = true,
    ): ComponentText = Components.empty().apply {
        val retractComponent = sender.asLangText("NBTFormat-Retract")
        /*
        列表类型特殊处理:
        - 键1:值1 (类型)
        或
        - 值2 (类型)
         */
        when (nbt) {
            is NBTList -> {
                nbt.content.let { list ->
                    if (list.isEmpty()) append(sender.asLangText("NBTFormat-List-Empty"))
                    else list.forEachIndexed { index, it ->
                        val deep = nodeDeep + DeepVisitor.LIST_INDEX_START + index + DeepVisitor.LIST_INDEX_END
                        newLine(); repeat(level) { append(retractComponent) } // 缩进
                        append(sender.asLangText("NBTFormat-Retract-List")) // 列表前缀
                        append(nbtAsComponent(sender, it, level + 1, slot, deep))
                    }
                }
            }
            /*
            复合类型特殊处理:
            键1:值1 (类型)
            键2:值2 (类型)
             */
            is NBTCompound -> {
                var first = isFirst
                nbt.toMapShallow().forEach { (shallow, value) ->
                    val deep = if (nodeDeep == null) shallow else nodeDeep + DeepVisitor.DEEP_SEPARATION + shallow // 深层节点的合成
                    if (!first) {
                        newLine(); repeat(level) { append(retractComponent) } // 缩进
                    }
                    append(componentKey(sender, deep, slot)) // 添加键
                    append(nbtAsComponent(sender, NBTConverter.convert(value), level + 1, slot, deep, isFirst = false))
                    first = false
                }
            }
            /*
            基本类型处理:
            值 (类型)
             */
            else -> append(componentValue(sender, nbt)).append(translateType(sender, nbt))
        }
    }

    /**
     * 获取数据
     */
    private fun PlayerInventory.getDataBySlot(slot: String): NBTCompound? = getItemBySlot(slot)?.let { RefItemStack(it).getData() }

    /**
     * 转换成 Map 形式 (全部以浅层即一层节点表示)
     */
    private fun NBTCompound.toMapShallow(source: Map<String, Any>? = this.sourceMap): Map<String, NBTData> = buildMap {
        source?.forEach { shallow -> this[shallow.key] = NBTConverter.convert(shallow.value) }
    }

    /**
     * 快捷创建键或值组件
     */
    private fun componentKey(
        sender: ProxyCommandSender,
        nodeDeep: String?,
        slot: String,
        nodeShallow: String? = nodeDeep?.substringAfterLast(DeepVisitor.DEEP_SEPARATION),
    ) = getTypeJson(sender, "NBTFormat-Entry-Key")
        .buildMessage(sender, nodeShallow.toString(), slot, nodeDeep.toString())

    private fun componentValue(sender: ProxyCommandSender, nbt: NBTData) =
        getTypeJson(sender, "NBTFormat-Entry-Value")
            .buildMessage(sender, asString(nbt), NBTSerializer.Mapper.serializeToString(nbt))

    /**
     * 获取语言文件Json内容
     */
    private fun getTypeJson(sender: ProxyCommandSender, node: String): TypeJson =
        sender.getLocaleFile()?.nodes?.get(node)?.let { if (it is TypeList) it.list.first() else it } as? TypeJson
            ?: TypeJson().apply { text = listOf("{$node}") }

    /**
     * 快捷匹配类型组件
     */
    private fun translateType(sender: ProxyCommandSender, nbt: NBTData): ComponentText = when (nbt.type) {
        NBTType.STRING -> sender.asLangText("NBTFormat-Type-String")
        NBTType.BYTE -> sender.asLangText("NBTFormat-Type-Byte")
        NBTType.SHORT -> sender.asLangText("NBTFormat-Type-Short")
        NBTType.INT -> sender.asLangText("NBTFormat-Type-Int")
        NBTType.LONG -> sender.asLangText("NBTFormat-Type-Long")
        NBTType.FLOAT -> sender.asLangText("NBTFormat-Type-Float")
        NBTType.DOUBLE -> sender.asLangText("NBTFormat-Type-Double")
        NBTType.BYTE_ARRAY -> sender.asLangText("NBTFormat-Type-ByteArray")
        NBTType.INT_ARRAY -> sender.asLangText("NBTFormat-Type-IntArray")
        NBTType.LONG_ARRAY -> sender.asLangText("NBTFormat-Type-LongArray")
        else -> null
    }?.let { Components.parseSimple(it).build { colored() } } ?: Components.empty()

    /**
     * 获取 [NBTData] 的字符串形式
     */
    private fun asString(nbt: NBTData): String = when (nbt) {
        is NBTString -> nbt.content
        is NBTByte -> nbt.contentString
        is NBTInt -> nbt.content.toString()
        is NBTFloat -> nbt.content.toString()
        is NBTDouble -> nbt.content.toString()
        is NBTLong -> nbt.content.toString()
        is NBTShort -> nbt.content.toString()
        is NBTByteArray -> nbt.content.toString()
        is NBTIntArray -> nbt.content.toString()
        is NBTLongArray -> nbt.content.toString()
        else -> "{UNSURE}"
    }

}