package xspleet.daggerapi.attributes.modifier;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import xspleet.daggerapi.attributes.operations.AttributeOperation;

import java.util.UUID;

public interface AttributeModifier<T>
{
    public T getValue();
    public AttributeOperation<T> getOperation();
    public UUID getUUID();
    public String getName();

    public default EntityAttributeModifier toMinecraftModifier() {
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
}
