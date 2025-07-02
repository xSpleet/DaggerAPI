package xspleet.daggerapi.artifact.builder;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;

public class AttributeModifier
{
    private EntityAttribute attribute;
    private EntityAttributeModifier modifier;

    public AttributeModifier(EntityAttribute attribute, EntityAttributeModifier modifier)
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
