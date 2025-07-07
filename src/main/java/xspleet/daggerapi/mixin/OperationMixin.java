package xspleet.daggerapi.mixin;

import com.mojang.datafixers.util.Function3;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.transformer.meta.MixinInner;
import xspleet.daggerapi.attributes.mixin.MixinAttributeOperation;
import xspleet.daggerapi.attributes.operations.DoubleOperation;
import xspleet.daggerapi.base.Self;

@Mixin(EntityAttributeModifier.Operation.class)
public class OperationMixin implements Self<EntityAttributeModifier.Operation>, MixinAttributeOperation<Double> {

    @Unique
    private DoubleOperation asOperation() {
        return switch (self())
        {
            case ADDITION -> DoubleOperation.ADD;
            case MULTIPLY_TOTAL -> DoubleOperation.MULTIPLY_TOTAL;
            case MULTIPLY_BASE -> DoubleOperation.MULTIPLY_BASE;
        };
    }

    @Override
    public String DaggerAPI$getName() {
        return asOperation().getName();
    }

    @Override
    public int DaggerAPI$getPrecedence() {
        return asOperation().getPrecedence();
    }

    @Override
    public Function3<Double, Double, Double, Double> DaggerAPI$getOperation() {
        return asOperation().getOperation();
    }

    @Override
    public Double DaggerAPI$apply(Double prevResult, Double resultFromPrevType, Double value) {
        return asOperation().apply(prevResult, resultFromPrevType, value);
    }
}
