package xspleet.daggerapi.base;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import xspleet.daggerapi.data.key.DaggerKey;
import xspleet.daggerapi.data.key.DaggerKeys;

import java.util.HashMap;
import java.util.Map;

public class VariablePaths
{
    public static Map<String, VariablePath<?, Double>> paths = new HashMap<>();

    public static DoubleVariablePath<Entity> triggererHealth = register(
            "triggerer#health",
            DaggerKeys.TRIGGERER,
            new DoubleVariablePath<>(
                    "triggerer#health",
                    DaggerKeys.TRIGGERER,
                    entity -> entity instanceof LivingEntity
                            ? ((LivingEntity) entity).getHealth()
                            : 0.0
            )
    );

    public static DoubleVariablePath<Entity> triggererMaxHealth = new DoubleVariablePath<>(
            "triggerer#max_health",
            DaggerKeys.TRIGGERER,
            entity -> entity instanceof LivingEntity
                    ? ((LivingEntity) entity).getMaxHealth()
                    : 0.0
    );

    public static DoubleVariablePath<Entity> triggererAbsorption = new DoubleVariablePath<>(
            "triggerer#absorption",
            DaggerKeys.TRIGGERER,
            entity -> entity instanceof LivingEntity
                    ? ((LivingEntity) entity).getAbsorptionAmount()
                    : 0.0
    );

    public static DoubleVariablePath<Entity> triggererArmor = new DoubleVariablePath<>(
            "triggerer#armor",
            DaggerKeys.TRIGGERER,
            entity -> entity instanceof LivingEntity
                    ? ((LivingEntity) entity).getArmor()
                    : 0.0
    );

    public static DoubleVariablePath<PlayerEntity> triggeredHealth = new DoubleVariablePath<>(
            "triggered#health",
            DaggerKeys.TRIGGERED,
            player -> (double) player.getHealth()
    );

    public static DoubleVariablePath<PlayerEntity> triggeredMaxHealth = new DoubleVariablePath<>(
            "triggered#max_health",
            DaggerKeys.TRIGGERED,
            player -> (double) player.getMaxHealth()
    );

    public static DoubleVariablePath<PlayerEntity> triggeredAbsorption = new DoubleVariablePath<>(
            "triggered#absorption",
            DaggerKeys.TRIGGERED,
            player -> (double) player.getAbsorptionAmount()
    );

    public static DoubleVariablePath<PlayerEntity> triggeredArmor = new DoubleVariablePath<>(
            "triggered#armor",
            DaggerKeys.TRIGGERED,
            player -> (double) player.getArmor()
    );
}
