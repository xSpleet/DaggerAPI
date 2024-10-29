package xspleet.daggerapi.collections;

import xspleet.jdagapi.base.Mapper;
import xspleet.jdagapi.exceptions.WrongArgumentException;
import xspleet.jdagapi.providers.ActionProvider;

public class ActionProviders
{
    public static ActionProvider DO_NOTHING = Mapper.registerActionProvider("doNothing", (map) -> {
        return playerEntity -> {};
    });

    public static ActionProvider HEAL = Mapper.registerActionProvider("heal", (map) -> {
        if(!map.get("amount").matches("\\d+"))
            throw new WrongArgumentException("amount", map.get("amount"));
        int amount = Integer.parseInt(map.get("amount"));

        return player -> {
            player.heal(amount);
        };
    }).addArgument("amount");
}
