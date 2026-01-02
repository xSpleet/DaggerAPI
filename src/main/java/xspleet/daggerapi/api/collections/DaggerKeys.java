package xspleet.daggerapi.api.collections;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import xspleet.daggerapi.artifact.ArtifactItem;
import xspleet.daggerapi.data.key.DaggerKey;
import xspleet.daggerapi.evaluation.DoubleExpression;
import xspleet.daggerapi.trigger.Trigger;

import java.util.HashMap;
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

    public static final DaggerKey<Double> DAMAGE_AMOUNT = register("damage_amount", Double.class);
    public static final DaggerKey<Integer> FOOD_AMOUNT = register("food_amount", Integer.class);
    public static final DaggerKey<Float> SATURATION_AMOUNT = register("saturation_amount", Float.class);
    public static final DaggerKey<Trigger> TRIGGER = register("trigger", Trigger.class);
    public static final DaggerKey<World> WORLD = register("world", World.class);
    public static final DaggerKey<PlayerEntity> TRIGGERED = register("triggered", PlayerEntity.class);
    public static final DaggerKey<PlayerEntity> PLAYER = register("player", PlayerEntity.class);
    public static final DaggerKey<Entity> TRIGGER_SOURCE = register("trigger_source", Entity.class);
    public static final DaggerKey<ArtifactItem> ARTIFACT = register("artifact", ArtifactItem.class);
    public static final DaggerKey<Boolean> SUCCESSFUL = register("successful", Boolean.class);
    public static final DaggerKey<DamageSource> DAMAGE_SOURCE = register("damage_source", DamageSource.class);
    public static final DaggerKey<Item> ITEM = register("item", Item.class);
    public static final DaggerKey<ItemStack> ITEM_STACK = register("item_stack", ItemStack.class);
    public static final DaggerKey<Entity> KILLER = register("killer", Entity.class);
    public static final DaggerKey<LivingEntity> VICTIM = register("victim", LivingEntity.class);
    public static final DaggerKey<Boolean> ALLOW_DEATH = register("allow_death", Boolean.class);

    public static class Provider
    {
        public static final DaggerKey<DoubleExpression> AMOUNT = new DaggerKey<>("amount", DoubleExpression.class);
        public static final DaggerKey<String> MESSAGE = new DaggerKey<>("message", String.class);
        public static final DaggerKey<String> WEATHER = new DaggerKey<>("weather", String.class);
        public static final DaggerKey<String> DIMENSION = new DaggerKey<>("dimension", String.class);
        public static final DaggerKey<Identifier> ARTIFACT = new DaggerKey<>("artifact", Identifier.class);
        public static final DaggerKey<Identifier> DAMAGE_TYPE = new DaggerKey<>("damage_type", Identifier.class);
        public static final DaggerKey<Identifier> STATUS_EFFECT = new DaggerKey<>("status_effect", Identifier.class);
        public static final DaggerKey<Integer> DURATION = new DaggerKey<>("duration", Integer.class);
        public static final DaggerKey<Integer> AMPLIFIER = new DaggerKey<>("amplifier", Integer.class);
        public static final DaggerKey<DoubleExpression> VALUE = new DaggerKey<>("value", DoubleExpression.class);
        public static final DaggerKey<DoubleExpression> COMPARE_VALUE = new DaggerKey<>("compare_value", DoubleExpression.class);
        public static final DaggerKey<String> OPERATOR = new DaggerKey<>("operator", String.class);
        public static final DaggerKey<DoubleExpression> CHANCE = new DaggerKey<>("chance", DoubleExpression.class);
        public static final DaggerKey<Identifier> LIQUID = new DaggerKey<>("liquid", Identifier.class);
        public static final DaggerKey<Integer> LIGHT_LEVEL = new DaggerKey<>("light_level", Integer.class);
        public static final DaggerKey<Identifier> ITEM = new DaggerKey<>("item", Identifier.class);
        public static final DaggerKey<Integer> COUNT = new DaggerKey<>("count", Integer.class);
    }
}
