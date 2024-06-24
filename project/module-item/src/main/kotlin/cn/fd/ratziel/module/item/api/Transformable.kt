package cn.fd.ratziel.module.item.api

/**
 * Transformable - 可转换的
 *
 * @author TheFloodDragon
 * @since 2024/4/20 09:11
 */
@Deprecated("")
interface Transformable<T> {

    /**
     * 正向转化 - 输出型转换
     */
    fun transform(): T

    /**
     * 反向转化 - 应用型转换 (禁止改写[target])
     */
    fun detransform(target: T)

}