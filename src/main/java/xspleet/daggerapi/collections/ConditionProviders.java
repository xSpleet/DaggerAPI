package xspleet.daggerapi.collections;

import com.google.gson.JsonElement;
import net.minecraft.world.World;
import xspleet.daggerapi.DaggerAPI;
import xspleet.daggerapi.base.DaggerLogger;
import xspleet.daggerapi.base.LoggingContext;
import xspleet.daggerapi.collections.registration.Mapper;
import xspleet.daggerapi.collections.registration.Provider;
import xspleet.daggerapi.base.Condition;
import xspleet.daggerapi.data.key.DaggerKeys;
import xspleet.daggerapi.exceptions.WrongArgumentException;

public class ConditionProviders
{
    public static void registerConditionProviders()
    {
        DaggerLogger.info(LoggingContext.STARTUP, "Registering condition providers...");
    }

    public static final Provider<Condition> ALWAYS = Mapper
            .registerConditionProvider("always", (args) -> {
                return data -> true;
                    });

    public static final Provider<Condition> NEVER = Mapper
            .registerConditionProvider("never", (args) -> {
                return data -> false;
                    });

    public static final Provider<Condition> IF_WEATHER = Mapper
            .registerConditionProvider("ifWeather", (args) ->
            {
                String weather = args.getData(DaggerKeys.Provider.WEATHER);
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
            .addArgument(DaggerKeys.Provider.WEATHER, JsonElement::getAsString);

    public static final Provider<Condition> IF_DIMENSION = Mapper
            .registerConditionProvider("ifDimension", (args) ->
            {
                String dimension = args.getData(DaggerKeys.Provider.DIMENSION);

                return data -> {
                    World world = data.getTestEntity(args.getOn()).getWorld();

                    return world.getRegistryKey()
                            .getValue()
                            .toString()
                            .equalsIgnoreCase(dimension);
                };
            })
            .addArgument(DaggerKeys.Provider.DIMENSION, JsonElement::getAsString);

    public static final Provider<Condition> IF_ARTIFACT = Mapper
            .registerConditionProvider("ifArtifact", args ->
            {
                String artifact = args.getData(DaggerKeys.Provider.ARTIFACT);

                return data -> {
                    return data.getData(DaggerKeys.ARTIFACT).getIdentifier().toString().equalsIgnoreCase(artifact);
                };
            })
            .addArgument(DaggerKeys.Provider.ARTIFACT, JsonElement::getAsString)
            .addAssociatedTrigger(Triggers.ACTIVATE);

    public static final Provider<Condition> IF_SUCCESSFUL = Mapper
            .registerConditionProvider("ifSuccessful", args ->
            {
                return data -> {
                    return data.getData(DaggerKeys.SUCCESSFUL);
                };
            })
            .addAssociatedTrigger(Triggers.ACTIVATE);

    public static final Provider<Condition> IF_DAMAGE_SOURCE = Mapper
            .registerConditionProvider("ifDamageSource", args ->
            {
                String damageSource = args.getData(DaggerKeys.Provider.DAMAGE_SOURCE);

                return data -> {
                    return data.getData(DaggerKeys.DAMAGE_SOURCE).getName().equalsIgnoreCase(damageSource);
                };
            })
            .addArgument(DaggerKeys.Provider.DAMAGE_SOURCE, JsonElement::getAsString)
            .addAssociatedTrigger(Triggers.BEFORE_DAMAGE);

}
