package cn.fd.ratziel.script.lang

import cn.fd.ratziel.script.api.CompilableScript
import cn.fd.ratziel.script.api.ScriptContent
import cn.fd.ratziel.script.api.ScriptEnvironment
import cn.fd.ratziel.script.api.ScriptExecutor
import cn.fd.ratziel.script.internal.CompilableScriptContent
import taboolib.common.platform.function.warning
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import javax.script.*

/**
 * JavaScriptExecutor
 *
 * @author TheFloodDragon
 * @since 2024/10/4 20:05
 */
object JavaScriptExecutor : ScriptExecutor {

    /**
     * 全局绑定键列表
     */
    val globalBindings by lazy { SimpleBindings(ConcurrentHashMap()) }

    /**
     * 构建脚本
     * @param compile 是否启用编译
     * @param async 若编译启用, 是否异步编译
     */
    fun build(script: String, compile: Boolean = true, async: Boolean = true): CompilableScriptContent {
        val sc = CompilableScriptContent(script, this)
        if (compile && sc.future == null) {
            sc.future = if (async) {
                CompletableFuture.supplyAsync { compile(script) }
            } else CompletableFuture.completedFuture(compile(script))
        }
        return sc
    }

    override fun build(script: String) = build(script, compile = true, async = true)

    override fun evaluate(script: ScriptContent, environment: ScriptEnvironment): Any? {
        if (script is CompilableScript) {
            val compiled = script.compiled
            if (compiled != null) return compiled.eval(environment.bindings)
        }
        return newEngine().eval(script.content, environment.bindings)
    }

    /**
     * 编译脚本
     */
    fun compile(script: String): CompiledScript? {
        try {
            return (newEngine() as Compilable).compile(script)
        } catch (e: Exception) {
            warning("Cannot compile script by '$this' ! Script content: $script")
            e.printStackTrace()
        }
        return null
    }

    fun newEngine(): ScriptEngine =
        ScriptEngineManager(this::class.java.classLoader).getEngineByName("js")
            .apply { setBindings(globalBindings, ScriptContext.GLOBAL_SCOPE) } // 设置全局绑定键
            ?: throw NullPointerException("Cannot find ScriptEngine for JavaScript Language")

}