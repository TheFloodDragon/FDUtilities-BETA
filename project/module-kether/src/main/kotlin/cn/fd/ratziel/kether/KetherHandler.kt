package cn.fd.ratziel.kether

import cn.fd.ratziel.common.loader.alert
import org.bukkit.entity.Player
import taboolib.common.platform.function.adaptPlayer
import taboolib.common.platform.function.console
import taboolib.module.kether.KetherFunction
import taboolib.module.kether.KetherShell
import taboolib.module.kether.KetherTransfer.namespace
import taboolib.module.kether.runKether
import java.util.concurrent.CompletableFuture


/**
 * cn.fd.utilities.kether.KetherHandler
 *
 * @author Arasple, TheFloodDragon
 * @since 2023/8/9 20:57
 */
object KetherHandler {

    fun invoke(source: String, player: Player?, vars: Map<String, Any?>): CompletableFuture<Any?> = alert {
        runKether {
            KetherShell.eval(
                source,
                sender = if (player != null) adaptPlayer(player) else console(),
                namespace = namespace,
                vars = KetherShell.VariableMap(vars)
            )
        }
    } ?: CompletableFuture.completedFuture(null)

    fun parseInline(source: String, player: Player?, vars: Map<String, Any?>) = alert {
        KetherFunction.parse(
            source,
            sender = if (player != null) adaptPlayer(player) else console(),
            namespace = namespace,
            vars = KetherShell.VariableMap(vars)
        )
    } ?: "<E: $source>"

}