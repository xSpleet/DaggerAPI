package xspleet.daggerapi.data.key;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import xspleet.daggerapi.artifact.ArtifactItem;
import xspleet.daggerapi.trigger.Trigger;

public class DaggerKeys
{
    public static final DaggerKey<Float> AMOUNT = new DaggerKey<>("amount", Float.class);
    public static final DaggerKey<Trigger> TRIGGER = new DaggerKey<>("trigger", Trigger.class);
    public static final DaggerKey<World> WORLD = new DaggerKey<>("world", World.class);
    public static final DaggerKey<PlayerEntity> TRIGGERED = new DaggerKey<>("triggered", PlayerEntity.class);
    public static final DaggerKey<PlayerEntity> PLAYER = new DaggerKey<>("player", PlayerEntity.class);
    public static final DaggerKey<Entity> TRIGGERER = new DaggerKey<>("triggerer", Entity.class);
    public static final DaggerKey<ArtifactItem> ARTIFACT = new DaggerKey<>("artifact", ArtifactItem.class);
    public static final DaggerKey<Boolean> SUCCESSFUL = new DaggerKey<>("successful", Boolean.class);
    public static final DaggerKey<DamageSource> DAMAGE_SOURCE = new DaggerKey<>("damage_source", DamageSource.class);
}
