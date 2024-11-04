package xspleet.daggerapi.base;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import xspleet.daggerapi.DaggerAPI;

public class DaggerAttributeModifier
{
    private EntityAttribute attribute;
    private EntityAttributeModifier modifier;

    public DaggerAttributeModifier(EntityAttribute attribute, EntityAttributeModifier modifier)
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
