package xspleet.daggerapi.collections;

import xspleet.daggerapi.DaggerAPI;
import xspleet.daggerapi.collections.registration.Mapper;
import xspleet.daggerapi.trigger.Trigger;

import static xspleet.daggerapi.collections.registration.Mapper.*;

public class Triggers
{
    public static void registerTriggers()
    {
        DaggerAPI.LOGGER.info("> Registering triggers...");
    }

    public static final Trigger AFTER_HIT = registerTrigger("afterHit")
            .setHasTriggerer()
            .setWorldful();

    public static final Trigger BEFORE_HIT = registerTrigger("beforeHit")
            .setHasTriggerer()
            .setWorldful();

    public static final Trigger ACTIVATE = registerTrigger("activate")
            .setHasTriggerer()
            .setWorldful();
}
