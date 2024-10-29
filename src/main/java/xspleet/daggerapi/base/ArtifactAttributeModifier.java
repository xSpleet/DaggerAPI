package xspleet.daggerapi.base;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;

import java.util.function.Predicate;

public class ArtifactAttributeModifier
{
    private Predicate<PlayerEntity> condition;
    private EntityAttribute attribute;
    private EntityAttributeModifier modifier;

    public ArtifactAttributeModifier()
    {
        condition = ConditionProvider.alwaysTrue();
    }

    public ArtifactAttributeModifier setAttribute(EntityAttribute attribute)
    {
        this.attribute = attribute;
        return this;
    }

    public ArtifactAttributeModifier setModifier(EntityAttributeModifier modifier)
    {
        this.modifier = modifier;
        return this;
    }

    public ArtifactAttributeModifier setCondition(Predicate<PlayerEntity> condition)
    {
        this.condition = condition;
        return this;
    }

    public void updatePlayer(PlayerEntity player)
    {
        if(player.getAttributeInstance(attribute) == null)
           return;

        if(!condition.test(player))
            cleansePlayer(player);
        else
            modifyPlayer(player);
    }

    public void modifyPlayer(PlayerEntity player)
    {
        if(!player.getAttributeInstance(attribute).hasModifier(modifier))
            player.getAttributeInstance(attribute).addTemporaryModifier(modifier);
    }

    public void cleansePlayer(PlayerEntity player)
    {
        if(player.getAttributeInstance(attribute).hasModifier(modifier))
            player.getAttributeInstance(attribute).removeModifier(modifier);
    }
}
