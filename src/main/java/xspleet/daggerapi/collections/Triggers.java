package xspleet.daggerapi.collections;

import xspleet.daggerapi.DaggerAPI;
import xspleet.daggerapi.base.DaggerLogger;
import xspleet.daggerapi.trigger.Trigger;

import static xspleet.daggerapi.collections.registration.Mapper.*;

public class Triggers
{
    public static void registerTriggers()
    {
        DaggerLogger.info("> Registering triggers...");
    }

    public static final Trigger BEFORE_DAMAGE = registerTrigger("beforeDamage")
            .setHasTriggerer()
            .setWorldful();

    public static final Trigger ACTIVATE = registerTrigger("activate")
            .setHasTriggerer()
            .setWorldful();
}
