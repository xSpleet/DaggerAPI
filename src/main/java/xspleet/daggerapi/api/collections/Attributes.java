package xspleet.daggerapi.api.collections;

import dev.emi.trinkets.api.SlotAttributes;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import xspleet.daggerapi.attributes.Attribute;
import xspleet.daggerapi.attributes.ClampedAttribute;
import xspleet.daggerapi.attributes.StaticEnchantmentAttribute;
import xspleet.daggerapi.attributes.UnclampedAttribute;
import xspleet.daggerapi.api.logging.DaggerLogger;
import xspleet.daggerapi.api.logging.LoggingContext;
import xspleet.daggerapi.api.registration.Mapper;

public class Attributes
{
    public static void registerAttributes()
    {
        DaggerLogger.info(LoggingContext.STARTUP, "Registering entity attributes...");
    }

    public static final Attribute<Double> GENERIC_MOVEMENT_SPEED = Mapper.registerAttribute("movement_speed", EntityAttributes.GENERIC_MOVEMENT_SPEED);
    public static final Attribute<Double> GENERIC_ATTACK_DAMAGE = Mapper.registerAttribute("attack_damage", EntityAttributes.GENERIC_ATTACK_DAMAGE);
    public static final Attribute<Double> GENERIC_ATTACK_KNOCKBACK = Mapper.registerAttribute("attack_knockback", EntityAttributes.GENERIC_ATTACK_KNOCKBACK);
    public static final Attribute<Double> GENERIC_ATTACK_SPEED = Mapper.registerAttribute("attack_speed", EntityAttributes.GENERIC_ATTACK_SPEED);
    public static final Attribute<Double> GENERIC_ARMOR = Mapper.registerAttribute("armor", EntityAttributes.GENERIC_ARMOR);
    public static final Attribute<Double> GENERIC_ARMOR_TOUGHNESS = Mapper.registerAttribute("armor_toughness", EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
    public static final Attribute<Double> GENERIC_KNOCKBACK_RESISTANCE = Mapper.registerAttribute("knockback_resistance", EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE);
    public static final Attribute<Double> GENERIC_LUCK = Mapper.registerAttribute("luck", EntityAttributes.GENERIC_LUCK);
    public static final Attribute<Double> GENERIC_MAX_HEALTH = Mapper.registerAttribute("max_health", EntityAttributes.GENERIC_MAX_HEALTH);
    public static final Attribute<Double> JUMP_HEIGHT = Mapper.registerAttribute("jump_height", new ClampedAttribute<>("daggerapi:jump_height", Double.class, 0.42, 0.01, 2.0));
    public static final Attribute<Double> VILLAGER_DISCOUNT = Mapper.registerAttribute("villager_discount", new ClampedAttribute<>("daggerapi:villager_discount", Double.class, 0.0, -10.0, 1.0));

    public static final Attribute<Boolean> CAN_WALK_ON_WATER = Mapper.registerAttribute("can_walk_on_water", new UnclampedAttribute<>("daggerapi:can_walk_on_water", Boolean.class, false));

    public static final Attribute<Integer> STATIC_SWIFT_SNEAK = Mapper.registerAttribute("static_swift_sneak", new StaticEnchantmentAttribute("daggerapi:static_swift_sneak"));
    public static final Attribute<Integer> STATIC_KNOCKBACK = Mapper.registerAttribute("static_knockback", new StaticEnchantmentAttribute("daggerapi:static_knockback"));
    public static final Attribute<Integer> STATIC_FIRE_ASPECT = Mapper.registerAttribute("static_fire_aspect", new StaticEnchantmentAttribute("daggerapi:static_fire_aspect"));
    public static final Attribute<Integer> STATIC_RESPIRATION = Mapper.registerAttribute("static_respiration", new StaticEnchantmentAttribute("daggerapi:static_respiration"));
    public static final Attribute<Integer> STATIC_DEPTH_STRIDER = Mapper.registerAttribute("static_depth_strider", new StaticEnchantmentAttribute("daggerapi:static_depth_strider"));
    public static final Attribute<Integer> STATIC_EFFICIENCY = Mapper.registerAttribute("static_efficiency", new StaticEnchantmentAttribute("daggerapi:static_efficiency"));
    public static final Attribute<Integer> STATIC_LOOTING = Mapper.registerAttribute("static_looting", new StaticEnchantmentAttribute("daggerapi:static_looting"));
    public static final Attribute<Integer> STATIC_AQUA_AFFINITY = Mapper.registerAttribute("static_aqua_affinity", new StaticEnchantmentAttribute("daggerapi:static_aqua_affinity"));
    public static final Attribute<Integer> EXTRA_JUMPS = Mapper.registerAttribute("extra_jumps", new ClampedAttribute<>("daggerapi:extra_jumps", Integer.class, 0, 0, Integer.MAX_VALUE));
}
