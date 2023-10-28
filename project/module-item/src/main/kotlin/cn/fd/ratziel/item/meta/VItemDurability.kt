@file:OptIn(ExperimentalSerializationApi::class)

package cn.fd.ratziel.item.meta

import cn.fd.ratziel.item.api.meta.ItemDurability
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

/**
 * VItemDurability
 *
 * @author TheFloodDragon
 * @since 2023/10/15 8:52
 */
@Serializable
data class VItemDurability(
    @JsonNames("max-durability", "durability-max", "durability")
    override var maxDurability: Int? = null,
    @JsonNames("current-durability", "durability-current")
    override var currentDurability: Int? = maxDurability,
    @JsonNames("repair-cost")
    override var repairCost: Int? = null,
) : ItemDurability {

    /**
     * 物品损伤值
     *   = 最大耐久-当前耐久
     */
    val damage: Int?
        get() = currentDurability?.let { maxDurability?.minus(it) }

}