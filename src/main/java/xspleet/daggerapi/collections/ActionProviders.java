package xspleet.daggerapi.collections;

public class ActionProviders
{
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
