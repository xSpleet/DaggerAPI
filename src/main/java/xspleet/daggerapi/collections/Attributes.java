package xspleet.daggerapi.collections;

import net.minecraft.entity.attribute.EntityAttributes;
import org.w3c.dom.Attr;
import xspleet.daggerapi.DaggerAPI;
import xspleet.daggerapi.attributes.Attribute;
import xspleet.daggerapi.attributes.ClampedAttribute;
import xspleet.daggerapi.attributes.UnclampedAttribute;
import xspleet.daggerapi.collections.registration.Mapper;

public class Attributes
{
    public static void registerAttributes()
    {
        DaggerAPI.LOGGER.info("> Registering entity attributes...");
    }
    public static Attribute<Double> GENERIC_MOVEMENT_SPEED = Mapper.registerEntityAttribute("movement_speed", EntityAttributes.GENERIC_MOVEMENT_SPEED);
    public static Attribute<Double> JUMP_HEIGHT = Mapper.registerEntityAttribute("jump_height", new ClampedAttribute<>("daggerapi:jump_height", 0.42, 0.01, 2.0));
    public static Attribute<Boolean> CAN_FLY = Mapper.registerEntityAttribute("can_fly", new UnclampedAttribute<>("daggerapi:can_fly", false));
}
