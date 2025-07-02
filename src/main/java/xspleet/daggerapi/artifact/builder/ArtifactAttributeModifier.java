package xspleet.daggerapi.artifact.builder;

import net.minecraft.entity.player.PlayerEntity;
import xspleet.daggerapi.base.Condition;
import xspleet.daggerapi.data.ConditionData;
import xspleet.daggerapi.collections.ConditionProviders;

import java.util.ArrayList;
import java.util.List;

public class ArtifactAttributeModifier
{
    private Condition condition;
    private List<AttributeModifier> modifiers;

    public ArtifactAttributeModifier()
    {
        condition = ConditionProviders.alwaysTrue();
        modifiers = new ArrayList<>();
    }

    public ArtifactAttributeModifier addAttributeModifier(AttributeModifier modifier)
    {
        modifiers.add(modifier);
        return this;
    }

    public ArtifactAttributeModifier addCondition(Condition condition)
    {
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

    public void modifyPlayer(ConditionData data)
    {
        PlayerEntity player = data.getPlayer();
        for(AttributeModifier attributeModifier: modifiers) {

            var attribute = attributeModifier.getAttribute();
            var modifier = attributeModifier.getModifier();

            if(player.getAttributeInstance(attribute) == null)
                return;

            if(!player.getAttributeInstance(attribute).hasModifier(modifier))
                player.getAttributeInstance(attribute).addTemporaryModifier(modifier);
        }
    }

    public void cleansePlayer(ConditionData data)
    {
        PlayerEntity player = data.getPlayer();
        for(AttributeModifier attributeModifier: modifiers) {

            var attribute = attributeModifier.getAttribute();
            var modifier = attributeModifier.getModifier();

            if(player.getAttributeInstance(attribute) == null)
                return;

            if (player.getAttributeInstance(attribute).hasModifier(modifier))
                player.getAttributeInstance(attribute).removeModifier(modifier);
        }
    }
}
