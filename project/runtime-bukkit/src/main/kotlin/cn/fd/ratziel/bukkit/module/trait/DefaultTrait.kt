package cn.fd.ratziel.bukkit.module.trait

import cn.fd.ratziel.bukkit.module.trait.api.Trait

/**
 * DefaultTrait
 *
 * @author TheFloodDragon
 * @since 2023/8/25 14:10
 */
enum class DefaultTrait(trait: Trait) {
    //TODO 什么垃圾东西
    INTERACT(Trait(listOf("interact","click"))),
}