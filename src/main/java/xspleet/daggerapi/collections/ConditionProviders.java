package xspleet.daggerapi.collections;

import net.minecraft.world.World;
import xspleet.daggerapi.DaggerAPI;
import xspleet.daggerapi.base.Condition;
import xspleet.daggerapi.base.Mapper;
import xspleet.daggerapi.exceptions.WrongArgumentException;
import xspleet.daggerapi.base.Provider;

import java.util.HashMap;

public class ConditionProviders
{
    public static void registerConditionProviders()
    {
        DaggerAPI.LOGGER.info("> Registering condition providers...");
    }

    public static Provider<Condition> ALWAYS = Mapper
            .registerConditionProvider("always", (map) -> {
                return data -> true;
                    });

    public static Condition alwaysTrue() {
        return ALWAYS.provide(new HashMap<>());
    }

    public static Provider<Condition> NEVER = Mapper
            .registerConditionProvider("never", (map) -> {
                return data -> false;
                    });

    public static Provider<Condition> IF_WEATHER = Mapper
            .registerConditionProvider("ifWeather", (map) ->
            {
                String weather = map.get("weather");
                boolean isRaining = weather.equalsIgnoreCase("rain");
                boolean isThundering = weather.equalsIgnoreCase("thunder");
                boolean isClear = weather.equalsIgnoreCase("clear");
                if(!isRaining && !isThundering && !isClear)
                    throw new WrongArgumentException("weather", map.get(weather));

                return data -> {
                    World world = data.getPlayer().getWorld();

                    return isRaining && world.isRaining()
                            || isThundering && world.isThundering()
                            || isClear && !world.isRaining();
                };
            })
            .addArgument("weather");

    public static Provider<Condition> IF_DIMENSION = Mapper
            .registerConditionProvider("ifDimension", (map) ->
            {
                String dimension = map.get("dimension");

                return data -> {
                    World world = data.getPlayer().getWorld();

                    return world.getRegistryKey().getValue().toString().equalsIgnoreCase(dimension);
                };
            })
            .addArgument("dimension");
}
