package xspleet.daggerapi.base;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import xspleet.daggerapi.collections.ConditionProviders;
import xspleet.daggerapi.providers.ConditionProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ArtifactAttributeModifier
{
    private Condition condition;
    private List<DaggerAttributeModifier> modifiers;

    public ArtifactAttributeModifier()
    {
        condition = ConditionProviders.alwaysTrue();
        modifiers = new ArrayList<>();
    }

    public ArtifactAttributeModifier addAttributeModifier(DaggerAttributeModifier modifier)
    {
        modifiers.add(modifier);
        return this;
    }

    public ArtifactAttributeModifier addCondition(Condition condition)
    {
        this.condition = this.condition.and(condition);
        return this;
    }

    public void updatePlayer(DaggerData data)
    {
        if(!condition.test(data))
            cleansePlayer(data);
        else
            modifyPlayer(data);
    }

    public void modifyPlayer(DaggerData data)
    {
        PlayerEntity player = data.getPlayer();
        for(DaggerAttributeModifier attributeModifier: modifiers) {

            var attribute = attributeModifier.getAttribute();
            var modifier = attributeModifier.getModifier();

            if(player.getAttributeInstance(attribute) == null)
                return;

            if(!player.getAttributeInstance(attribute).hasModifier(modifier))
                player.getAttributeInstance(attribute).addTemporaryModifier(modifier);
        }
    }

    public void cleansePlayer(DaggerData data)
    {
        PlayerEntity player = data.getPlayer();
        for(DaggerAttributeModifier attributeModifier: modifiers) {

            var attribute = attributeModifier.getAttribute();
            var modifier = attributeModifier.getModifier();

            if(player.getAttributeInstance(attribute) == null)
                return;

            if (player.getAttributeInstance(attribute).hasModifier(modifier))
                player.getAttributeInstance(attribute).removeModifier(modifier);
        }
    }
}
