package xspleet.daggerapi.data.key;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import xspleet.daggerapi.artifact.ArtifactItem;
import xspleet.daggerapi.base.DoubleVariableSet;
import xspleet.daggerapi.trigger.Trigger;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DaggerKeys
{
    private static final Map<String, DaggerKey<?>> KEYS = new HashMap<>();

    public static <T> DaggerKey<T> register(String name, Class<T> type) {
        DaggerKey<T> key = new DaggerKey<>(name, type);
        KEYS.put(name, key);
        return key;
    }

    public static DaggerKey<?> get(String name) {
        return KEYS.get(name);
    }

    public static final DaggerKey<Double> AMOUNT = register("amount", Double.class);
    public static final DaggerKey<Trigger> TRIGGER = register("trigger", Trigger.class);
    public static final DaggerKey<World> WORLD = register("world", World.class);
    public static final DaggerKey<PlayerEntity> TRIGGERED = register("triggered", PlayerEntity.class);
    public static final DaggerKey<PlayerEntity> PLAYER = register("player", PlayerEntity.class);
    public static final DaggerKey<Entity> TRIGGERER = register("triggerer", Entity.class);
    public static final DaggerKey<ArtifactItem> ARTIFACT = register("artifact", ArtifactItem.class);
    public static final DaggerKey<Boolean> SUCCESSFUL = register("successful", Boolean.class);
    public static final DaggerKey<DamageSource> DAMAGE_SOURCE = register("damage_source", DamageSource.class);

    public static class Provider
    {
        public static final DaggerKey<String> AMOUNT = new DaggerKey<>("amount", String.class);
        public static final DaggerKey<String> MESSAGE = new DaggerKey<>("message", String.class);
        public static final DaggerKey<String> WEATHER = new DaggerKey<>("weather", String.class);
        public static final DaggerKey<String> DIMENSION = new DaggerKey<>("dimension", String.class);
        public static final DaggerKey<String> ARTIFACT = new DaggerKey<>("artifact", String.class);
        public static final DaggerKey<String> DAMAGE_SOURCE = new DaggerKey<>("damageSource", String.class);
        public static final DaggerKey<DoubleVariableSet> VARIABLES = new DaggerKey<>("variables", DoubleVariableSet.class);
    }
}
