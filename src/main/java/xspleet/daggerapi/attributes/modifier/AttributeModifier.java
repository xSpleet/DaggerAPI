package xspleet.daggerapi.attributes.modifier;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.network.PacketByteBuf;
import xspleet.daggerapi.attributes.operations.AttributeOperation;
import xspleet.daggerapi.api.registration.Mapper;

import java.util.UUID;

public interface AttributeModifier<T>
{
    T getValue();
    AttributeOperation<T> getOperation();
    UUID getUUID();
    String getName();
    String getArtifactName();

    default EntityAttributeModifier toMinecraftModifier() {
        if(getValue() instanceof Double value) {
            return new EntityAttributeModifier(
                    getUUID(),
                    getName(),
                    value,
                    switch (getOperation().getName())
                    {
                        case "addition" -> EntityAttributeModifier.Operation.ADDITION;
                        case "multiply_base" -> EntityAttributeModifier.Operation.MULTIPLY_BASE;
                        case "multiply_total" -> EntityAttributeModifier.Operation.MULTIPLY_TOTAL;
                        default -> throw new IllegalArgumentException("Unknown operation: " + getOperation().getName());
                    }
            );
        }
        throw new IllegalCallerException("Cannot convert non-double attribute modifier to Minecraft EntityAttributeModifier. Modifier: " + getName() + ", Value: " + getValue());
    }

    default void write(PacketByteBuf buf) {
        buf.writeUuid(getUUID());
        buf.writeString(getName());
        buf.writeString(Mapper.getNameOf(getOperation()));
        buf.writeString(getArtifactName());
        if(getOperation().getType() == Double.class)
            buf.writeDouble((Double) getValue());
        else if(getOperation().getType() == Integer.class)
            buf.writeInt((Integer) getValue());
        else if(getOperation().getType() == Boolean.class)
            buf.writeBoolean((Boolean) getValue());
        else
            throw new IllegalArgumentException("Unsupported attribute modifier type: " + getOperation().getType().getName());
    }
}
