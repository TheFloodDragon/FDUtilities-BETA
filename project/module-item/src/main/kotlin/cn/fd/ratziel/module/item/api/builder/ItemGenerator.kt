package cn.fd.ratziel.module.item.api.builder

import cn.fd.ratziel.core.Priority
import cn.fd.ratziel.core.element.Element
import java.util.concurrent.ConcurrentLinkedDeque

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
     * 物品解析器
     */
    val resolvers: ConcurrentLinkedDeque<Priority<ItemResolver>>

    /**
     * 物品序列化器
     */
    val serializers: ConcurrentLinkedDeque<Priority<ItemSerializer<*, *>>>

}