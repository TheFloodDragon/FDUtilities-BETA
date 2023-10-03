package cn.fd.ratziel.item.meta.serializers

import cn.fd.ratziel.item.meta.matchAttributeOperation
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.attribute.AttributeModifier

/**
 * AttributeOperationSerializer
 *
 * @author TheFloodDragon
 * @since 2023/10/3 12:36
 */
object AttributeOperationSerializer : KSerializer<AttributeModifier.Operation> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("AttributeOperation", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: AttributeModifier.Operation) {
        encoder.encodeString(value.name)
    }

    override fun deserialize(decoder: Decoder) = matchAttributeOperation(decoder.decodeString())
}