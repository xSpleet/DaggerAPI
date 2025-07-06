package xspleet.daggerapi.artifact.builder;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;

public class WrappedModifier
{
    private final EntityAttribute attribute;
    private final EntityAttributeModifier modifier;

    public WrappedModifier(EntityAttribute attribute, EntityAttributeModifier modifier)
    {
        this.attribute = attribute;
        this.modifier = modifier;
    }

    public EntityAttributeModifier getModifier() {
        return modifier;
    }

    public EntityAttribute getAttribute() {
        return attribute;
    }
}
