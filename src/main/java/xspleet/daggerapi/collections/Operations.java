package xspleet.daggerapi.collections;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import xspleet.jdagapi.base.Mapper;

import java.util.Map;

public class Operations
{
    public static EntityAttributeModifier.Operation ADD = Mapper.registerOperation("add", EntityAttributeModifier.Operation.ADDITION);
    public static EntityAttributeModifier.Operation MULTIPLY_BASE = Mapper.registerOperation("multiply_base", EntityAttributeModifier.Operation.MULTIPLY_BASE);
    public static EntityAttributeModifier.Operation MULTIPLY_TOTAL = Mapper.registerOperation("multiply_total", EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
}
