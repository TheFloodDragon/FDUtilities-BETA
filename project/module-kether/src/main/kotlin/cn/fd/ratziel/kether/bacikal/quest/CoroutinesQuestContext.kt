package cn.fd.ratziel.kether.bacikal.quest

import cn.fd.ratziel.core.coroutine.ProxyCoroutineScope
import kotlinx.coroutines.launch
import taboolib.common.platform.function.info
import taboolib.library.kether.AbstractQuestContext.*
import taboolib.library.kether.*
import taboolib.module.kether.ScriptContext
import java.util.*
import java.util.concurrent.CompletableFuture

/**
 * @author Lanscarlos
 * @since 2023-08-25 11:39
 */
class CoroutinesQuestContext(quest: BacikalQuest) : AbstractQuestContext(quest) {

    companion object : ProxyCoroutineScope()

    override fun createRootFrame(context: InnerContext): QuestContext.Frame {
        return InnerRootFrame(context)
    }

    inner class InnerRootFrame(context: ScriptContext) :
        AbstractFrame(null, LinkedList(), SimpleVarTable(null), context) {

        var block: Quest.Block? = null
        var current: Int = -1

        init {
            quest.source.getBlock(QuestContext.BASE_BLOCK).ifPresent(this::setNext)
        }

        override fun name(): String {
            return QuestContext.BASE_BLOCK
        }

        override fun currentAction(): Optional<ParsedAction<*>> {
            return block?.get(current) ?: Optional.empty()
        }

        override fun setNext(action: ParsedAction<*>) {
            current = block?.indexOf(action) ?: -1

            if (current == -1) {
                block = quest.source.blockOf(action).orElse(null)
                current = block?.indexOf(action) ?: -1
            }
        }

        override fun setNext(block: Quest.Block) {
            this.block = block
            this.current = 0
        }

        fun nextAction(): ParsedAction<*>? {
            return block?.get(current++)?.orElse(null)
        }

        @Suppress("UNCHECKED_CAST")
        override fun <T : Any?> run(): CompletableFuture<T> {
            val future = CompletableFuture<T>()
            this.future = future

            scope.launch {
                info("running Coroutines...")
                try {
                    var cache: Any? = null
                    while (!source.exitStatus.isPresent) {
                        cleanup()
                        frames.removeIf(QuestContext.Frame::isDone)

                        val action = nextAction() ?: break
                        cache = action.process(this@InnerRootFrame).join()
                        info("running cache: $cache")
                    }
                    info("final cache: $cache")
                    future.complete(cache as? T)
                } catch (e: Throwable) {
                    future.completeExceptionally(e)
                }
            }

            return future
        }

        fun cleanup() {
            while (closeables.isNotEmpty()) {
                try {
                    (closeables.pollFirst() as AutoCloseable).close()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }

}