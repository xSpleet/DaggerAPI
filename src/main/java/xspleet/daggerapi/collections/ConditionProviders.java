package xspleet.daggerapi.collections;

import net.minecraft.world.World;
import xspleet.daggerapi.DaggerAPI;
import xspleet.daggerapi.collections.registration.Mapper;
import xspleet.daggerapi.collections.registration.Provider;
import xspleet.daggerapi.data.ProviderData;
import xspleet.daggerapi.base.Condition;
import xspleet.daggerapi.data.key.DaggerKeys;
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

    public static Provider<Condition> IF_ARTIFACT = Mapper
            .registerConditionProvider("ifArtifact", args ->
            {
                String artifact = args.getData("artifact");
                String[] artifactArguments = artifact.split(" ");

                boolean choose;
                String artifactName;

                if(artifactArguments.length > 1)
                {
                    if(artifactArguments[0].equalsIgnoreCase("choose"))
                    {
                        choose = true;
                        artifactName = artifactArguments[1];
                    }
                    else if(artifactArguments[0].equalsIgnoreCase("not"))
                    {
                        choose = false;
                        artifactName = artifactArguments[1];
                    }
                    else
                        throw new WrongArgumentException("artifact", artifact);
                }
                else
                    throw new WrongArgumentException("artifact", artifact);

                return data -> {
                    return choose == data.getData(DaggerKeys.ARTIFACT).getIdentifier().toString().equalsIgnoreCase(artifactName);
                };
            })
            .addArgument("artifact")
            .addAssociatedTrigger(Triggers.ACTIVATE);

    public static Provider<Condition> IF_SUCCESSFUL = Mapper
            .registerConditionProvider("ifSuccessful", args ->
            {
                String successful = args.getData("successful");

                return data -> {
                    return data.getData(DaggerKeys.SUCCESSFUL) == Boolean.parseBoolean(successful);
                };
            })
            .addArgument("successful")
            .addAssociatedTrigger(Triggers.ACTIVATE);

    public static Provider<Condition> IF_DAMAGE_SOURCE = Mapper
            .registerConditionProvider("ifDamageSource", args ->
            {
                String damageSource = args.getData("damageSource");

                return data -> {
                    return data.getData(DaggerKeys.DAMAGE_SOURCE).getName().equalsIgnoreCase(damageSource);
                };
            })
            .addArgument("damageSource")
            .addAssociatedTrigger(Triggers.BEFORE_DAMAGE);

}
