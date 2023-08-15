package cn.fd.utilities.bukkit.command

import cn.fd.utilities.bukkit.kether.KetherHandler
import cn.fd.utilities.common.command.CommandElement
import cn.fd.utilities.common.debug
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.expansion.createHelper
import taboolib.platform.util.onlinePlayers

/**
 * @author Arasple, TheFloodDragon
 * @since 2023/8/12 11:47
 */
@CommandHeader(
    name = "f-dev",
    aliases = ["fdev"],
    permission = "fdutilities.command.dev",
    description = "开发命令"
)
object CommandDev {

    @CommandBody
    val main = mainCommand { createHelper() }

    /**
     * 运行Kether
     */
    @CommandBody
    val runKether = subCommand {
        execute<CommandSender> { sender, _, argument ->
            val player = if (sender is Player) sender else onlinePlayers.random()
            debug(argument)
            val script = argument.removePrefix("runKether ")

            KetherHandler.invoke(script, player, mapOf()).thenApply {
                sender.sendMessage("§7Result: $it")
            }
        }
    }

    @CommandBody
    val element = CommandElement

}