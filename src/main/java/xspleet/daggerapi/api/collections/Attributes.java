package xspleet.daggerapi.api.collections;

import net.minecraft.entity.attribute.EntityAttributes;
import xspleet.daggerapi.attributes.Attribute;
import xspleet.daggerapi.attributes.ClampedAttribute;
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
    public static final Attribute<Double> GENERIC_MOVEMENT_SPEED = Mapper.registerAttribute("generic_movement_speed", EntityAttributes.GENERIC_MOVEMENT_SPEED);
    public static final Attribute<Double> JUMP_HEIGHT = Mapper.registerAttribute("jump_height", new ClampedAttribute<>("daggerapi:jump_height", Double.class, 0.42, 0.01, 2.0));
    public static final Attribute<Boolean> CAN_WALK_ON_WATER = Mapper.registerAttribute("can_walk_on_water", new UnclampedAttribute<>("daggerapi:can_walk_on_water", Boolean.class, false));
}
