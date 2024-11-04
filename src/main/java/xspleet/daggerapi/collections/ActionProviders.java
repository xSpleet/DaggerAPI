package xspleet.daggerapi.collections;

import xspleet.daggerapi.DaggerAPI;
import xspleet.daggerapi.base.Mapper;
import xspleet.daggerapi.exceptions.WrongArgumentException;
import xspleet.daggerapi.providers.ActionProvider;

public class ActionProviders
{
    public static void registerActionProviders()
    {
        DaggerAPI.LOGGER.info("> Registering action providers...");
    }

    public static ActionProvider DO_NOTHING = Mapper.registerActionProvider("doNothing", (map) -> {
        return data -> {};
    });

    public static ActionProvider HEAL = Mapper.registerActionProvider("heal", (map) -> {
        if(!map.get("amount").matches("\\d+"))
            throw new WrongArgumentException("amount", map.get("amount"));

        int amount = Integer.parseInt(map.get("amount"));

        return data -> {
            data.getPlayer().heal(amount);
        };
    }).addArgument("amount");
}
