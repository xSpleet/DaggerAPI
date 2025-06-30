package xspleet.daggerapi.collections;

import xspleet.daggerapi.DaggerAPI;
import xspleet.daggerapi.collections.registration.Mapper;
import xspleet.daggerapi.trigger.Trigger;

public class Triggers
{
    public static void registerTriggers()
    {
        DaggerAPI.LOGGER.info("> Registering triggers...");
    }
    public static final Trigger AFTER_HIT = Mapper.registerTrigger("afterHit").setHasTriggerer().setWorldful();
    public static final Trigger BEFORE_HIT = Mapper.registerTrigger("beforeHit").setHasTriggerer().setWorldful();
}
