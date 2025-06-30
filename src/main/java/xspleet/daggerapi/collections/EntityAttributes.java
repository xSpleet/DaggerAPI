package xspleet.daggerapi.collections;

import net.minecraft.entity.attribute.EntityAttribute;
import xspleet.daggerapi.DaggerAPI;
import xspleet.daggerapi.collections.registration.Mapper;

public class EntityAttributes
{
    public static void registerEntityAttributes()
    {
        DaggerAPI.LOGGER.info("> Registering entity attributes...");
    }
    public static EntityAttribute GENERIC_MOVEMENT_SPEED = Mapper.registerEntityAttribute("movement_speed", net.minecraft.entity.attribute.EntityAttributes.GENERIC_MOVEMENT_SPEED);
}
