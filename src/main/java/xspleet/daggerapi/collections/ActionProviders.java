package xspleet.daggerapi.collections;

import xspleet.daggerapi.DaggerAPI;
import xspleet.daggerapi.base.Action;
import xspleet.daggerapi.base.Mapper;
import xspleet.daggerapi.exceptions.WrongArgumentException;
import xspleet.daggerapi.base.Provider;

public class ActionProviders
{
    public static void registerActionProviders()
    {
        DaggerAPI.LOGGER.info("> Registering action providers...");
    }

    public static Provider<Action> DO_NOTHING = Mapper.registerActionProvider("doNothing", (map) -> {
        return data -> {};
    });

    public static Provider<Action> HEAL = Mapper.registerActionProvider("heal", (map) -> {
        if(!map.get("amount").matches("\\d+"))
            throw new WrongArgumentException("amount", map.get("amount"));

        int amount = Integer.parseInt(map.get("amount"));

        return data -> {
            data.getPlayer().heal(amount);
        };
    }).addArgument("amount");
}
