package cn.fd.ratziel.function.argument

import cn.fd.ratziel.function.argument.exception.ArgumentNotFoundException
import cn.fd.ratziel.function.util.uncheck
import java.util.concurrent.CopyOnWriteArrayList

/**
 * ArgumentQueue
 *
 * @author TheFloodDragon
 * @since 2024/5/1 13:55
 */
open class DefaultArgumentFactory(protected open val list: CopyOnWriteArrayList<Argument<*>>) : ArgumentFactory, MutableList<Argument<*>> by list {

    constructor(collection: Collection<Argument<*>>) : this(CopyOnWriteArrayList(collection))

    constructor(vararg arguments: Argument<*>) : this(CopyOnWriteArrayList(arguments))

    constructor() : this(CopyOnWriteArrayList())

    override fun <T : Any> pop(type: Class<T>): Argument<T> =
        list.find { type.isAssignableFrom(it.type) }?.let { uncheck(it) } ?: throw ArgumentNotFoundException(type)

    override fun <T : Any> popAll(type: Class<T>): List<Argument<T>> =
        list.mapNotNull { arg -> arg.takeIf { it.type.isAssignableFrom(type) }?.let { uncheck(it) } }

}