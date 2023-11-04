package cn.fd.ratziel.module.item.api

import cn.fd.ratziel.module.item.api.meta.ItemMetadata

/**
 * RatzielItem - Ratziel 物品
 *
 * @author TheFloodDragon
 * @since 2023/10/27 22:21
 */
interface RatzielItem : ItemPart {
    /**
     * 物品信息
     */
    val info: ItemInfo

    /**
     * 物品元数据
     */
    val meta: ItemMetadata
}