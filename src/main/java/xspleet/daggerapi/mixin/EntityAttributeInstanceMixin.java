package xspleet.daggerapi.mixin;

import net.minecraft.entity.attribute.EntityAttributeInstance;
import org.spongepowered.asm.mixin.Mixin;
import xspleet.daggerapi.attributes.mixin.MixinAttributeInstance;
import xspleet.daggerapi.attributes.modifier.AttributeModifier;
import xspleet.daggerapi.base.Self;

import java.util.UUID;

@Mixin(EntityAttributeInstance.class)
public class EntityAttributeInstanceMixin implements Self<EntityAttributeInstance>, MixinAttributeInstance<Double> {

    @Override
    public String DaggerAPI$getAttributeName() {
        return self().getAttribute().getTranslationKey();
    }

    @Override
    public boolean DaggerAPI$hasModifier(AttributeModifier<Double> modifier) {
        return self().hasModifier(modifier.toMinecraftModifier());
    }

    @Override
    public boolean DaggerAPI$hasModifier(UUID modifierId) {
        return self().getModifier(modifierId) != null;
    }

    @Override
    public void DaggerAPI$addTemporaryModifier(AttributeModifier<Double> modifier) {
        self().addTemporaryModifier(modifier.toMinecraftModifier());
    }

    @Override
    public void DaggerAPI$removeModifier(AttributeModifier<Double> modifier) {
        self().removeModifier(modifier.getUUID());
    }

    @Override
    public Double DaggerAPI$getValue() {
        return self().getValue();
    }

    @Override
    public Double DaggerAPI$getBaseValue() {
        return self().getBaseValue();
    }

    @Override
    public void DaggerAPI$clean() {}

    @Override
    public boolean DaggerAPI$isDirty() {
        return false;
    }
}
