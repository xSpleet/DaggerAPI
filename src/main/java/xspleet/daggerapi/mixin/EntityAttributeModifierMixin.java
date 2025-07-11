package xspleet.daggerapi.mixin;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import xspleet.daggerapi.attributes.mixin.MixinAttributeModifier;
import xspleet.daggerapi.attributes.operations.AttributeOperation;
import xspleet.daggerapi.attributes.operations.DoubleOperation;
import xspleet.daggerapi.base.Self;

import java.util.UUID;

@Mixin(EntityAttributeModifier.class)
public class EntityAttributeModifierMixin implements Self<EntityAttributeModifier>, MixinAttributeModifier<Double> {

    @Unique
    private String artifactName = null;

    @Override
    public Double DaggerAPI$getValue() {
        return self().getValue();
    }

    @Override
    public AttributeOperation<Double> DaggerAPI$getOperation() {
        return switch (self().getOperation())
        {
            case ADDITION -> DoubleOperation.ADD;
            case MULTIPLY_BASE -> DoubleOperation.MULTIPLY_BASE;
            case MULTIPLY_TOTAL -> DoubleOperation.MULTIPLY_TOTAL;
            default -> throw new IllegalArgumentException("Unknown operation: " + self().getOperation());
        };
    }

    @Override
    public UUID DaggerAPI$getUUID() {
        return self().getId();
    }

    @Override
    public String DaggerAPI$getName() {
        return self().getName();
    }

    @Override
    public String DaggerAPI$getArtifactName() {
        return artifactName;
    }

    @Override
    public void DaggerAPI$setArtifactName(String artifactName) {
        this.artifactName = artifactName;
    }
}
