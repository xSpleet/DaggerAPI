package xspleet.daggerapi.api.collections;

import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import xspleet.daggerapi.api.logging.DaggerLogger;
import xspleet.daggerapi.api.logging.LoggingContext;
import xspleet.daggerapi.artifact.ArtifactItem;
import xspleet.daggerapi.evaluation.VariablePath;
import xspleet.daggerapi.exceptions.NoSuchVariablePathException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class VariablePaths
{
    private static final Map<String, VariablePath<?, ?>> paths = new HashMap<>();

    public static void registerVariablePaths()
    {
        DaggerLogger.info(LoggingContext.STARTUP, "Registering variable paths...");
    }

    public static VariablePath<Entity, Double> HEALTH = register("health", entity -> {
        if (entity instanceof LivingEntity livingEntity) {
            return (double) livingEntity.getHealth();
        }
        return 0.0;
    }, Entity.class, Double.class);

    public static VariablePath<Entity, Double> MAX_HEALTH = register("max_health", entity -> {
        if (entity instanceof LivingEntity livingEntity) {
            return (double) livingEntity.getMaxHealth();
        }
        return 0.0;
    }, Entity.class, Double.class);

    public static VariablePath<Entity, Double> AIR = register("air", entity -> {
        if (entity instanceof LivingEntity livingEntity) {
            return (double) livingEntity.getAir();
        }
        return 0.0;
    }, Entity.class, Double.class);

    public static VariablePath<Entity, Double> ABSORPTION_AMOUNT = register("absorptionAmount", entity -> {
        if (entity instanceof LivingEntity livingEntity) {
            return (double) livingEntity.getAbsorptionAmount();
        }
        return 0.0;
    }, Entity.class, Double.class);

    public static VariablePath<Entity, Double> HUNGER = register("hunger", entity -> {
        if (entity instanceof PlayerEntity playerEntity) {
            return (double) playerEntity.getHungerManager().getFoodLevel();
        }
        return 0.0;
    }, Entity.class, Double.class);

    public static VariablePath<Entity, Double> SATURATION = register("saturation", entity -> {
        if (entity instanceof PlayerEntity playerEntity) {
            return (double) playerEntity.getHungerManager().getSaturationLevel();
        }
        return 0.0;
    }, Entity.class, Double.class);

    public static VariablePath<Entity, Double> EXHAUSTION = register("exhaustion", entity -> {
        if (entity instanceof PlayerEntity playerEntity) {
            return (double) playerEntity.getHungerManager().getExhaustion();
        }
        return 0.0;
    }, Entity.class, Double.class);

    public static VariablePath<Entity, Double> X = register("x", Entity::getX, Entity.class, Double.class);
    public static VariablePath<Entity, Double> Y = register("y", Entity::getY, Entity.class, Double.class);
    public static VariablePath<Entity, Double> Z = register("z", Entity::getZ, Entity.class, Double.class);
    public static VariablePath<Entity, Double> YAW = register("yaw", (e) -> (double) e.getYaw(), Entity.class, Double.class);
    public static VariablePath<Entity, Double> PITCH = register("pitch", (e) -> (double) e.getPitch(), Entity.class, Double.class);
    public static VariablePath<Entity, Double> HEAD_YAW = register("head_yaw", (e) -> (double) e.getHeadYaw(), Entity.class, Double.class);
    public static VariablePath<Entity, Double> Y_VELOCITY = register("y_velocity", (e) -> e.getVelocity().y, Entity.class, Double.class);
    public static VariablePath<Entity, Double> X_VELOCITY = register("x_velocity", (e) -> e.getVelocity().x, Entity.class, Double.class);
    public static VariablePath<Entity, Double> Z_VELOCITY = register("z_velocity", (e) -> e.getVelocity().z, Entity.class, Double.class);
    public static VariablePath<Entity, Double> SPEED = register("speed", (e) -> e.getVelocity().length(), Entity.class, Double.class);
    public static VariablePath<Entity, Double> DISTANCE_TO_GROUND = register("distance_to_ground", (e) -> {
        double y = e.getY();
        int blockY = (int) Math.floor(y) - 1;
        World world = e.getWorld();
        while (blockY >= 0) {
            if (!world.isAir(e.getBlockPos().withY(blockY))) {
                break;
            }
            blockY--;
        }
        return y - (blockY + 1);
    }, Entity.class, Double.class);
    public static VariablePath<Entity, Double> DISTANCE_TO_WORLD_SPAWN = register("distance_to_world_spawn", (e) -> {
        double dx = e.getX() - e.getWorld().getSpawnPos().getX();
        double dy = e.getY() - e.getWorld().getSpawnPos().getY();
        double dz = e.getZ() - e.getWorld().getSpawnPos().getZ();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }, Entity.class, Double.class);
    public static VariablePath<Entity, Double> EXPERIENCE_LEVEL = register("experience_level", (e) -> e instanceof PlayerEntity playerEntity? playerEntity.experienceLevel * 1.0 : 0.0, Entity.class, Double.class);
    public static VariablePath<Entity, Double> ACTIVE_ARTIFACT_CHARGE = register("active_artifact_charge", (e) -> {
        if (e instanceof PlayerEntity player) {
            if(TrinketsApi.getTrinketComponent(player).isPresent()) {
                var artifactItem = TrinketsApi.getTrinketComponent(player).get().getEquipped(stack -> (stack.getItem() instanceof ArtifactItem artifact && artifact.isActive())).stream().findFirst();
                if(artifactItem.isPresent()) {
                    ItemStack stack = artifactItem.get().getRight();
                    if(stack.getItem() instanceof ArtifactItem artifact && artifact.isActive()) {
                        var cooldownProgress = 1.0 - player.getItemCooldownManager().getCooldownProgress(artifact, 0.0f);
                        return MathHelper.clamp(artifact.getCooldown() * cooldownProgress, 0.0, artifact.getCooldown() * 1.0);
                    }
                }
            }
        }
        return -1.0;
    }, Entity.class, Double.class);
    public static VariablePath<Entity, Double> BIOME_TEMPERATURE = register("biome_temperature", (e) -> {
        World world = e.getWorld();
        return (double) world.getBiome(e.getBlockPos()).value().getTemperature();
    }, Entity.class, Double.class);
    public static VariablePath<World, Double> TIME_OF_DAY = register("time_of_day", (w) -> w.getTimeOfDay() * 1.0, World.class, Double.class);
    public static VariablePath<World, Double> TIME = register("time", (w) -> w.getTime() * 1.0, World.class, Double.class);
    public static VariablePath<World, Double> MOON_PHASE = register("moon_phase", (w) -> w.getMoonPhase() * 1.0, World.class, Double.class);

    public static <T, U> VariablePath<T, U> getPath(String path, Class<T> type, Class<U> returnType) throws NoSuchVariablePathException {
        VariablePath<?, ?> variablePath = paths.get(path);
        if(variablePath == null || !variablePath.getType().isAssignableFrom(type) || !variablePath.getReturnType().isAssignableFrom(returnType)) {
            throw new NoSuchVariablePathException(path);
        }
        return (VariablePath<T, U>) variablePath;
    }

    private static <T, U> VariablePath<T, U> register(String path, Function<T, U> function, Class<T> type, Class<U> returnType)
    {
        VariablePath<T, U> variablePath = new VariablePath<>(function, type, returnType);
        paths.put(path, variablePath);
        return variablePath;
    }
}
