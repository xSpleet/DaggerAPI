package xspleet.daggerapi.collections;

import xspleet.daggerapi.DaggerAPI;
import xspleet.daggerapi.base.DaggerLogger;
import xspleet.daggerapi.base.LoggingContext;
import xspleet.daggerapi.data.key.DaggerKeys;
import xspleet.daggerapi.trigger.Trigger;

import static xspleet.daggerapi.collections.registration.Mapper.*;

public class Triggers
{
    public static void registerTriggers()
    {
        DaggerLogger.info(LoggingContext.STARTUP, "Registering triggers...");
    }

    public static final Trigger BEFORE_DAMAGE = registerTrigger("beforeDamage")
            .setHasTriggerer()
            .setWorldful()
            .addProvidedData(DaggerKeys.AMOUNT)
            .addProvidedData(DaggerKeys.DAMAGE_SOURCE);

    public static final Trigger ACTIVATE = registerTrigger("activate")
            .setHasTriggerer()
            .setWorldful()
            .addProvidedData(DaggerKeys.ARTIFACT)
            .addProvidedData(DaggerKeys.SUCCESSFUL);
}
