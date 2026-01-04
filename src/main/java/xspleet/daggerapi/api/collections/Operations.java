package xspleet.daggerapi.api.collections;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import xspleet.daggerapi.attributes.operations.AttributeOperation;
import xspleet.daggerapi.attributes.operations.BooleanOperation;
import xspleet.daggerapi.api.logging.DaggerLogger;
import xspleet.daggerapi.api.logging.LoggingContext;
import xspleet.daggerapi.api.registration.Mapper;
import xspleet.daggerapi.attributes.operations.IntegerOperation;

public class Operations
{
    public static void registerOperations()
    {
        DaggerLogger.info(LoggingContext.STARTUP, "Registering operations...");
    }
    public static AttributeOperation<Double> ADD = Mapper.registerOperation("add", EntityAttributeModifier.Operation.ADDITION);
    public static AttributeOperation<Double> MULTIPLY_BASE = Mapper.registerOperation("multiply_base", EntityAttributeModifier.Operation.MULTIPLY_BASE);
    public static AttributeOperation<Double> MULTIPLY_TOTAL = Mapper.registerOperation("multiply_total", EntityAttributeModifier.Operation.MULTIPLY_TOTAL);

    public static AttributeOperation<Integer> ADD_INTEGER = Mapper.registerOperation("iadd", IntegerOperation.ADD);
    public static AttributeOperation<Integer> MULTIPLY_BASE_INTEGER = Mapper.registerOperation("imultiply_base", IntegerOperation.MULTIPLY_BASE);
    public static AttributeOperation<Integer> MULTIPLY_TOTAL_INTEGER = Mapper.registerOperation("imultiply_total", IntegerOperation.MULTIPLY_TOTAL);

    public static final AttributeOperation<Boolean> SET_TRUE = Mapper.registerOperation("set_true", BooleanOperation.TRUE);
    public static final AttributeOperation<Boolean> SET_FALSE = Mapper.registerOperation("set_false", BooleanOperation.FALSE);
}
