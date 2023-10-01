package cn.fd.ratziel.adventure

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.AMPERSAND_CHAR
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.SECTION_CHAR
import taboolib.module.nms.MinecraftVersion

/**
 * MiniMessage
 */
fun deserializeByMiniMessage(target: String, vararg tagResolver: TagResolver = emptyArray())
        : Component = MiniMessage.miniMessage().deserialize(target, *tagResolver)

fun serializeByMiniMessage(target: Component)
        : String = MiniMessage.miniMessage().serialize(target)

/**
 * 将 '&' 转换成 '§'
 */
fun translateAmpersandColor(target: String) = target.replace(AMPERSAND_CHAR, SECTION_CHAR)

/**
 * 将 '§' 转换成 '&'
 */
fun translateLegacyColor(target: String) = target.replace(SECTION_CHAR, AMPERSAND_CHAR)

/**
 * 旧版消息格式序列化器
 */
private val legacyComponentSerializer by lazy {
    LegacyComponentSerializer.builder().apply {
        character(SECTION_CHAR)
        // 1.16+
        if (MinecraftVersion.majorLegacy >= 11600) {
            hexColors()
            useUnusualXRepeatedCharacterHexFormat()
        }
    }.build()
}

fun serializeLegacy(target: Component): String = legacyComponentSerializer.serialize(target)

fun deserializeLegacy(target: String): TextComponent = legacyComponentSerializer.deserialize(target)