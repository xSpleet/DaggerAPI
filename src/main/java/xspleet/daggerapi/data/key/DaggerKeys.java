package xspleet.daggerapi.data.key;

import net.minecraft.datafixer.fix.LevelDataGeneratorOptionsFix;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import xspleet.daggerapi.trigger.Trigger;

public class DaggerKeys
{
    public static DaggerKey<Integer> AMOUNT = new DaggerKey<>("amount", Integer.class);

    public static DaggerKey<Trigger> TRIGGER = new DaggerKey<>("trigger", Trigger.class);

    public static DaggerKey<World> WORLD = new DaggerKey<>("world", World.class);

    public static DaggerKey<PlayerEntity> TRIGGERED = new DaggerKey<>("triggered", PlayerEntity.class);
    public static DaggerKey<PlayerEntity> PLAYER = new DaggerKey<>("player", PlayerEntity.class);

    public static DaggerKey<Entity> TRIGGERER = new DaggerKey<>("triggerer", Entity.class);

    public static DaggerKey<Identifier> ARTIFACT_ID = new DaggerKey<>("artifact_id", Identifier.class);
    public static DaggerKey<Boolean> SUCCESSFUL = new DaggerKey<>("successful", Boolean.class);
}
