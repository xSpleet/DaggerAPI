package xspleet.daggerapi.artifact.builder;

import net.minecraft.entity.player.PlayerEntity;
import xspleet.daggerapi.attributes.AttributeHolder;
import xspleet.daggerapi.base.Condition;
import xspleet.daggerapi.data.ConditionData;
import xspleet.daggerapi.data.key.DaggerKeys;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArtifactAttributeModifier
{
    private Condition condition;
    private List<String> conditions = new ArrayList<>();
    private final List<WrappedModifier<?>> modifiers;

    public ArtifactAttributeModifier()
    {
        condition = (conditionData -> true);
        modifiers = new ArrayList<>();
    }

    public ArtifactAttributeModifier addAttributeModifier(WrappedModifier<?> modifier)
    {
        modifiers.add(modifier);
        return this;
    }

    public ArtifactAttributeModifier addCondition(Condition condition, String name)
    {
        conditions.add(name);
        this.condition = this.condition.and(condition);
        return this;
    }

    public void updatePlayer(ConditionData data)
    {
        if(!condition.test(data))
            cleansePlayer(data);
        else
            modifyPlayer(data);
    }

    private <T> void safeAddTemporaryModifier(PlayerEntity player, WrappedModifier<T> attributeModifier)
    {
        var attribute = attributeModifier.attribute();
        var modifier = attributeModifier.modifier();
        var holder = AttributeHolder.asHolder(player);

        if(holder.getAttributeInstance(attribute) == null)
            return;

        if(!holder.getAttributeInstance(attribute).hasModifier(modifier))
            holder.getAttributeInstance(attribute).addTemporaryModifier(modifier);
    }

    private <T> void safeRemoveModifier(PlayerEntity player, WrappedModifier<T> attributeModifier)
    {
        var attribute = attributeModifier.attribute();
        var modifier = attributeModifier.modifier();
        var holder = AttributeHolder.asHolder(player);

        if(holder.getAttributeInstance(attribute) == null)
            return;

        if(holder.getAttributeInstance(attribute).hasModifier(modifier))
            holder.getAttributeInstance(attribute).removeModifier(modifier);
    }

    public void modifyPlayer(ConditionData data)
    {
        PlayerEntity player = data.getData(DaggerKeys.PLAYER);
        for(WrappedModifier<?> attributeModifier: modifiers) {
            safeAddTemporaryModifier(player, attributeModifier);
        }
    }

    public void cleansePlayer(ConditionData data)
    {
        PlayerEntity player = data.getData(DaggerKeys.PLAYER);
        for(WrappedModifier<?> attributeModifier: modifiers) {
            safeRemoveModifier(player, attributeModifier);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtifactAttributeModifier that = (ArtifactAttributeModifier) o;
        return Objects.equals(condition, that.condition) && Objects.equals(modifiers, that.modifiers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition, modifiers);
    }
}
