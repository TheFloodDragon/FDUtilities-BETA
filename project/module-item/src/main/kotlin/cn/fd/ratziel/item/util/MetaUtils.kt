package cn.fd.ratziel.item.util

import cn.fd.ratziel.adventure.serializeByMiniMessage
import cn.fd.ratziel.adventure.toJsonFormat
import cn.fd.ratziel.item.api.ItemCharacteristic
import cn.fd.ratziel.item.meta.VItemCharacteristic
import cn.fd.ratziel.item.meta.VItemDisplay
import cn.fd.ratziel.item.meta.VItemDurability
import cn.fd.ratziel.item.meta.VItemMeta
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import net.kyori.adventure.text.Component
import taboolib.module.nms.MinecraftVersion

typealias ItemChar = ItemCharacteristic
typealias VItemChar = VItemCharacteristic

/**
 * NMS:
 *   1.13+ > Json Format
 *   1.13- > Original Format (§)
 */
fun nmsComponent(component: Component): String =
    if (MinecraftVersion.isLower(MinecraftVersion.V1_13)) {
        serializeByMiniMessage(component)
    } else component.toJsonFormat()

/**
 * 构建 VItemMeta
 */
fun buildVMeta(json: Json, element: JsonElement): VItemMeta =
    json.decodeFromJsonElement<VItemMeta>(element).apply {
        if (display == VItemDisplay())
            display = json.decodeFromJsonElement<VItemDisplay>(element)
        if (characteristic == VItemCharacteristic())
            characteristic = json.decodeFromJsonElement<VItemCharacteristic>(element)
        if (durability == VItemDurability())
            durability = json.decodeFromJsonElement<VItemDurability>(element)
    }