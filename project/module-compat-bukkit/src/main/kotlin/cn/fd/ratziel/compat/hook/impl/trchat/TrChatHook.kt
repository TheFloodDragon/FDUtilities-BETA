package cn.fd.ratziel.compat.hook.impl.trchat

import cn.fd.ratziel.compat.hook.HookManager
import cn.fd.ratziel.compat.hook.LinkedPluginHook
import cn.fd.ratziel.compat.util.isoInstance
import org.bukkit.Bukkit
import taboolib.common.LifeCycle
import taboolib.common.io.runningClassMapInJar
import taboolib.common.platform.Awake

/**
 * TrChatHook
 *
 * @author TheFloodDragon
 * @since 2024/2/17 11:28
 */
object TrChatHook : LinkedPluginHook {

    override val pluginName = "TrChat"

    override fun isHooked() = plugin != null

    val plugin get() = Bukkit.getPluginManager().getPlugin(pluginName)

    const val ISO_PATH = "me.arasple.mc.trchat.taboolib.common.classloader.IsolatedClassLoader"

    const val BASE_PATH = "cn.fd.ratziel.compat.hook.impl.trchat."

    val condition = java.util.function.Function<String, Boolean> {
        it.startsWith(this::class.java.`package`.name) && !it.equals(this::class.qualifiedName)
    }

    override val hookedClasses = runningClassMapInJar.mapNotNull { e -> e.value.takeIf { condition.apply(e.key) } }.toTypedArray()

    override val hookMap by lazy {
        HookManager.hookMapOf(
            0 to BASE_PATH + "TrChatListener",
            precedence = isoInstance(ISO_PATH, plugin!!::class.java.classLoader),
        )
    }

    @Awake(LifeCycle.ENABLE)
    private fun register() = HookManager.register(this)

}