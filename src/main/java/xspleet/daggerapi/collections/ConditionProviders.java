package xspleet.daggerapi.collections;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import xspleet.daggerapi.base.Mapper;
import xspleet.daggerapi.exceptions.WrongArgumentException;
import xspleet.daggerapi.providers.ConditionProvider;

import java.util.function.Predicate;

public class ConditionProviders
{
    public static Predicate<PlayerEntity> getConditionFromData()

    public static ConditionProvider ALWAYS = Mapper
            .registerConditionProvider("always", (map) -> {
                return data -> true;
                    });

    public static ConditionProvider NEVER = Mapper
            .registerConditionProvider("never", (map) -> {
                return data -> false;
                    });

    public static ConditionProvider IF_WEATHER = Mapper
            .registerConditionProvider("ifWeather", (map) -> {

                String weather = map.get("weather");
                boolean isRaining = weather.equalsIgnoreCase("rain");
                boolean isThundering = weather.equalsIgnoreCase("thunder");
                boolean isClear = weather.equalsIgnoreCase("clear");
                if(!isRaining && !isThundering && !isClear)
                    throw new WrongArgumentException("weather", map.get(weather));

                return data -> {
                    World world = data.getWorld();

                    if(world == null)
                        throw new NullPointerException("The world may not be null in the ");

                    return isRaining && world.isRaining()
                            || isThundering && world.isThundering()
                            || isClear && !world.isRaining();
                };
            })
            .addArgument("weather");
}
