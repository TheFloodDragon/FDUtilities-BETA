package cn.fd.ratziel.function.argument

import cn.fd.ratziel.function.argument.exception.ArgumentException
import cn.fd.ratziel.function.util.uncheck
import java.util.function.Function

fun <T : Any> ArgumentFactory.popOrNull(type: Class<T>): Argument<T>? =
    try {
        pop(type)
    } catch (ex: ArgumentException) {
        null
    }

fun <T : Any> ArgumentFactory.popOr(type: Class<T>, default: T): Argument<T> =
    try {
        pop(type)
    } catch (ex: ArgumentException) {
        SingleArgument(default)
    }

fun <T : Any> ArgumentSupplier.supplyOrNull(type: Class<T>): Argument<T>? =
    try {
        get(type)
    } catch (ex: ArgumentException) {
        null
    }

/**
 * 判断参数是否为目标类型
 * 相当于 V is T
 */
infix fun <T> Argument<*>.instanceof(type: Class<T>): Boolean = type.isAssignableFrom(this.type)

/**
 * 如果参数为目标类型, 则执行 [onTrue], 反之执行 [onFalse]
 * 相当 if.. else...
 */
fun <T : Any, R, V : Any> Argument<V>.ascertain(
    clazz: Class<T>,
    onTrue: Function<Argument<T>, R>,
    onFalse: Function<Argument<V>, R>
): R = if (this.instanceof(clazz)) onTrue.apply(uncheck(this)) else onFalse.apply(this)


/**
 * Inline 集群
 */

inline fun <reified T : Any> ArgumentSupplier.supplyOrNull(): Argument<T>? = supplyOrNull(T::class.java)

inline fun <reified T : Any> ArgumentFactory.pop() = pop(T::class.java)

inline fun <reified T : Any> ArgumentFactory.popAll() = popAll(T::class.java)

inline fun <reified T : Any> ArgumentFactory.popOr(default: T): Argument<T> = popOr(T::class.java, default)

inline fun <reified T : Any> ArgumentFactory.popOrNull(): Argument<T>? = popOrNull(T::class.java)

inline fun <reified T> Argument<*>.instanceof(): Boolean = this.instanceof(T::class.java)

inline fun <reified T : Any, R, V : Any> Argument<V>.ascertain(
    onTrue: Function<Argument<T>, R>,
    onFalse: Function<Argument<V>, R>
): R = this.ascertain(T::class.java, onTrue, onFalse)