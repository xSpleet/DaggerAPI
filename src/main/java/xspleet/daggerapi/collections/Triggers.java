package xspleet.daggerapi.collections;

import xspleet.daggerapi.DaggerAPI;
import xspleet.daggerapi.base.Mapper;
import xspleet.daggerapi.base.Trigger;

public class Triggers
{
    public static void registerTriggers()
    {
        DaggerAPI.LOGGER.info("> Registering triggers...");
    }
    public static final Trigger AFTER_HIT = Mapper.registerTrigger("afterHit");
    public static final Trigger BEFORE_HIT = Mapper.registerTrigger("beforeHit");
}
