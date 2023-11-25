package cn.fd.ratziel.common.command

import cn.fd.ratziel.common.WorkspaceLoader
import cn.fd.ratziel.common.util.executeAsync
import cn.fd.ratziel.core.element.service.ElementRegistry
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.expansion.createHelper
import taboolib.module.lang.sendLang

/**
 * CommandElement
 *
 * @author TheFloodDragon
 * @since 2023/8/15 11:20
 */
@CommandHeader(
    name = "element",
    aliases = ["rz-element", "rem", "em", "fem"],
    permission = "ratziel.command.element",
)
object CommandElement {

    @CommandBody
    val main = mainCommand { createHelper() }

    /**
     * 列出已加载的所有元素
     */
    @CommandBody
    val list = subCommand {
        executeAsync<ProxyCommandSender> { sender, _, _ ->
            sender.sendLang("Element-Header")
            WorkspaceLoader.elements.forEach {
                sender.sendLang("Element-Identifier-Format", it.id) // 标识符
                sender.sendLang(
                    "Element-Info-Format",
                    it.address.file?.name ?: "无", // 文件
                    it.type.toString(), // 类型
                    it.property.toString() // 属性
                )
            }
        }
    }


    /**
     * 列出所有元素类型
     */
    @CommandBody
    val listTypes = subCommand {
        executeAsync<ProxyCommandSender> { sender, _, _ ->
            sender.sendLang("Element-Type-Header")
            ElementRegistry.registry.forEach { etype, group ->
                // 命名空间消息
                sender.sendLang("Element-Type-Namespace-Format", etype.space)
                // 具体消息
                sender.sendLang(
                    "Element-Type-Info-Format",
                    etype.name, // 名称
                    format(etype.alias.toList()), // 别名
                    format(group.handlerMap.map { it::class.java.name }) //处理器
                )
            }
        }
    }

    private fun format(set: Set<*>?): String {
        return format(set?.toList())
    }

    private fun format(list: List<*>?): String {
        return list?.joinToString(separator = ", ") ?: "无"
    }

}