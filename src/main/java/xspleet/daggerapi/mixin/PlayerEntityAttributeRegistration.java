package xspleet.daggerapi.mixin;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import xspleet.daggerapi.attributes.Attribute;
import xspleet.daggerapi.attributes.AttributeHolder;
import xspleet.daggerapi.attributes.container.DaggerAttributeContainer;
import xspleet.daggerapi.attributes.instance.AttributeInstance;
import xspleet.daggerapi.attributes.mixin.MixinAttribute;
import xspleet.daggerapi.attributes.mixin.MixinAttributeHolder;
import xspleet.daggerapi.base.Self;

@Mixin(PlayerEntity.class)
public class PlayerEntityAttributeRegistration implements Self<PlayerEntity>, MixinAttributeHolder {

    @Unique
    private DaggerAttributeContainer daggerAPI$attributeContainer;

    @Unique
    private DaggerAttributeContainer getAttributeContainer()
    {
        return daggerAPI$attributeContainer;
    }

    @Override
    public <T> AttributeInstance<T> DaggerAPI$getAttributeInstance(Attribute<T> attribute) {
        if(attribute instanceof MixinAttribute && attribute instanceof EntityAttribute vanillaAttribute) {
            return (AttributeInstance<T>) self().getAttributeInstance(vanillaAttribute);
        }
        return getAttributeContainer().getAttributeInstance(attribute);
    }
}
