package xspleet.daggerapi.collections;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import xspleet.daggerapi.DaggerAPI;
import xspleet.daggerapi.attributes.Attribute;
import xspleet.daggerapi.attributes.operations.AttributeOperation;
import xspleet.daggerapi.collections.registration.Mapper;

public class Operations
{
    public static void registerOperations()
    {
        DaggerAPI.LOGGER.info("> Registering operations...");
    }
    public static AttributeOperation<Double> ADD = Mapper.registerOperation("add", EntityAttributeModifier.Operation.ADDITION);
    public static AttributeOperation<Double> MULTIPLY_BASE = Mapper.registerOperation("multiply_base", EntityAttributeModifier.Operation.MULTIPLY_BASE);
    public static AttributeOperation<Double> MULTIPLY_TOTAL = Mapper.registerOperation("multiply_total", EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
}
