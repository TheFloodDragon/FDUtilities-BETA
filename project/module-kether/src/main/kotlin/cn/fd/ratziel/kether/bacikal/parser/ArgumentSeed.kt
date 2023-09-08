package cn.fd.ratziel.kether.bacikal.parser

import java.util.concurrent.CompletableFuture

/**
 * @author Lanscarlos
 * @since 2023-08-21 17:59
 */
class ArgumentSeed<T>(val seed: BacikalSeed<T>, val prefix: Array<out String>, val def: T) : BacikalSeed<T> {

    override val isAccepted: Boolean
        get() = seed.isAccepted

    fun accept(prefix: String, reader: BacikalReader) {
        if (prefix in this.prefix) {
            seed.accept(reader)
        }
    }

    override fun accept(reader: BacikalReader) {
    }

    override fun accept(frame: BacikalFrame): CompletableFuture<T> {
        return if (seed.isAccepted) {
            seed.accept(frame)
        } else {
            CompletableFuture.completedFuture(def)
        }
    }
}