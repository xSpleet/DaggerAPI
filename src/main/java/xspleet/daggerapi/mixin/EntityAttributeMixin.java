package xspleet.daggerapi.mixin;

import net.minecraft.entity.attribute.EntityAttribute;
import org.spongepowered.asm.mixin.Mixin;
import xspleet.daggerapi.attributes.mixin.MixinAttribute;
import xspleet.daggerapi.util.Self;

@Mixin(EntityAttribute.class)
public class EntityAttributeMixin implements Self<EntityAttribute>, MixinAttribute<Double> {

    @Override
    public String DaggerAPI$getName() {
        return self().getTranslationKey();
    }

    @Override
    public Double DaggerAPI$getDefaultValue() {
        return self().getDefaultValue();
    }

    @Override
    public Class<Double> DaggerAPI$getType() {
        return Double.TYPE;
    }

    @Override
    public Double DaggerAPI$clamp(Double value) {
        return self().clamp(value);
    }
}
