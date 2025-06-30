package xspleet.daggerapi.collections;

import net.minecraft.world.World;
import xspleet.daggerapi.DaggerAPI;
import xspleet.daggerapi.collections.registration.Mapper;
import xspleet.daggerapi.collections.registration.Provider;
import xspleet.daggerapi.data.ProviderData;
import xspleet.daggerapi.base.Condition;
import xspleet.daggerapi.exceptions.WrongArgumentException;

public class ConditionProviders
{
    public static void registerConditionProviders()
    {
        DaggerAPI.LOGGER.info("> Registering condition providers...");
    }

    public static Provider<Condition> ALWAYS = Mapper
            .registerConditionProvider("always", (args) -> {
                return data -> true;
                    });

    public static Condition alwaysTrue() {
        return ALWAYS.provide(new ProviderData());
    }

    public static Provider<Condition> NEVER = Mapper
            .registerConditionProvider("never", (args) -> {
                return data -> false;
                    });

    public static Provider<Condition> IF_WEATHER = Mapper
            .registerConditionProvider("ifWeather", (args) ->
            {
                String weather = args.getData("weather");
                boolean isRaining = weather.equalsIgnoreCase("rain");
                boolean isThundering = weather.equalsIgnoreCase("thunder");
                boolean isClear = weather.equalsIgnoreCase("clear");
                if(!isRaining && !isThundering && !isClear)
                    throw new WrongArgumentException("weather", weather);

                return data -> {
                    World world = data.getTestEntity(args.getOn()).getWorld();

                    return isRaining && world.isRaining()
                            || isThundering && world.isThundering()
                            || isClear && !world.isRaining();
                };
            })
            .addArgument("weather");

    public static Provider<Condition> IF_DIMENSION = Mapper
            .registerConditionProvider("ifDimension", (args) ->
            {
                String dimension = args.getData("dimension");

                return data -> {
                    World world = data.getTestEntity(args.getOn()).getWorld();

                    return world.getRegistryKey()
                            .getValue()
                            .toString()
                            .equalsIgnoreCase(dimension);
                };
            })
            .addArgument("dimension");


}
