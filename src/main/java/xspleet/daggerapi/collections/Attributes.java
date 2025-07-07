package xspleet.daggerapi.collections;

import net.minecraft.entity.attribute.EntityAttributes;
import xspleet.daggerapi.DaggerAPI;
import xspleet.daggerapi.attributes.Attribute;
import xspleet.daggerapi.collections.registration.Mapper;

public class Attributes
{
    public static void registerAttributes()
    {
        DaggerAPI.LOGGER.info("> Registering entity attributes...");
    }
    public static Attribute<Double> GENERIC_MOVEMENT_SPEED = Mapper.registerEntityAttribute("movement_speed", EntityAttributes.GENERIC_MOVEMENT_SPEED);
}
