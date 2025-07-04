package xspleet.daggerapi.data.key;

import net.minecraft.datafixer.fix.LevelDataGeneratorOptionsFix;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import xspleet.daggerapi.artifact.ArtifactItem;
import xspleet.daggerapi.trigger.Trigger;

public class DaggerKeys
{
    public static DaggerKey<Float> AMOUNT = new DaggerKey<>("amount", Float.class);
    public static DaggerKey<Trigger> TRIGGER = new DaggerKey<>("trigger", Trigger.class);
    public static DaggerKey<World> WORLD = new DaggerKey<>("world", World.class);
    public static DaggerKey<PlayerEntity> TRIGGERED = new DaggerKey<>("triggered", PlayerEntity.class);
    public static DaggerKey<PlayerEntity> PLAYER = new DaggerKey<>("player", PlayerEntity.class);
    public static DaggerKey<Entity> TRIGGERER = new DaggerKey<>("triggerer", Entity.class);
    public static DaggerKey<ArtifactItem> ARTIFACT = new DaggerKey<>("artifact", ArtifactItem.class);
    public static DaggerKey<Boolean> SUCCESSFUL = new DaggerKey<>("successful", Boolean.class);
    public static DaggerKey<DamageSource> DAMAGE_SOURCE = new DaggerKey<>("damage_source", DamageSource.class);
}
