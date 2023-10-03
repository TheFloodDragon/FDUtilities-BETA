package cn.fd.ratziel.item.meta.serializers

import cn.fd.ratziel.item.meta.matchAttribute
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.attribute.Attribute

/**
 * AttributeSerializer
 *
 * @author TheFloodDragon
 * @since 2023/10/3 12:34
 */
object AttributeSerializer : KSerializer<Attribute> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Attribute", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Attribute) {
        encoder.encodeString(value.key.key)
    }

    override fun deserialize(decoder: Decoder) = matchAttribute(decoder.decodeString())
}