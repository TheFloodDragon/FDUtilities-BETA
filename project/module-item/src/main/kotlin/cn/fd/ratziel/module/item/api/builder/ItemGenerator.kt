package cn.fd.ratziel.module.item.api.builder

import cn.fd.ratziel.core.element.Element

/**
 * ItemGenerator
 *
 * @author TheFloodDragon
 * @since 2023/10/28 12:20
 */
interface ItemGenerator {

    /**
     * 原始物品配置(元素)
     */
    val origin: Element

    /**
     * 物品序列化器
     */
    val serializers: Array<ItemSerializer<*, *>>

}