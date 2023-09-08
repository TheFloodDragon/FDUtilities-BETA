package cn.fd.ratziel.kether.bacikal.parser

import taboolib.library.kether.QuestAction
import taboolib.module.kether.ScriptFrame
import java.util.concurrent.CompletableFuture
import java.util.function.Function

/**
 * @author Lanscarlos
 * @since 2023-08-21 10:15
 */
class BacikalFruit<T>(private val func: Function<BacikalFrame, CompletableFuture<T>>) : QuestAction<T>() {
    override fun process(frame: ScriptFrame): CompletableFuture<T> {
        return func.apply(DefaultFrame(frame))
    }
}