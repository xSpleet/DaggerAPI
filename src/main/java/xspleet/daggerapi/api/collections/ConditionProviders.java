package xspleet.daggerapi.api.collections;

import com.google.gson.JsonElement;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import xspleet.daggerapi.api.logging.DaggerLogger;
import xspleet.daggerapi.api.logging.LoggingContext;
import xspleet.daggerapi.api.registration.Mapper;
import xspleet.daggerapi.api.registration.Provider;
import xspleet.daggerapi.trigger.Condition;
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
                    World world = data.getTestWorld(args.getOn());

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
                    World world = data.getTestWorld(args.getOn());

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
                Identifier artifact = args.getData(DaggerKeys.Provider.ARTIFACT);

                return data -> {
                    return data.getData(DaggerKeys.ARTIFACT).getIdentifier().equals(artifact);
                };
            })
            .addArgument(DaggerKeys.Provider.ARTIFACT, e -> new Identifier(e.getAsString()))
            .addRequiredData(DaggerKeys.ARTIFACT)
            .addAssociatedTrigger(Triggers.ACTIVATE);

    public static final Provider<Condition> IF_SUCCESSFUL = Mapper
            .registerConditionProvider("ifSuccessful", args ->
            {
                return data -> {
                    return data.getData(DaggerKeys.SUCCESSFUL);
                };
            })
            .addRequiredData(DaggerKeys.SUCCESSFUL)
            .addAssociatedTrigger(Triggers.ACTIVATE);

    public static final Provider<Condition> IF_DAMAGE_TYPE = Mapper
            .registerConditionProvider("ifDamageType", args ->
            {
                var damageType = args.getData(DaggerKeys.Provider.DAMAGE_TYPE);

                return data -> {
                    var source = data.getData(DaggerKeys.DAMAGE_SOURCE);
                    var world = data.getTestWorld(args.getOn());

                    var key = source.getTypeRegistryEntry().getKey().orElseThrow().getValue();
                    return key.equals(damageType);
                };
            })
            .addRequiredData(DaggerKeys.DAMAGE_SOURCE)
            .addArgument(DaggerKeys.Provider.DAMAGE_TYPE, e -> new Identifier(e.getAsString()))
            .addAssociatedTrigger(Triggers.BEFORE_DAMAGE);

    public static final Provider<Condition> HAS_STATUS_EFFECT = Mapper
            .registerConditionProvider("hasStatusEffect", args ->
            {
                Identifier statusEffect = args.getData(DaggerKeys.Provider.STATUS_EFFECT);
                var effect = Registries.STATUS_EFFECT.get(statusEffect);
                return data -> {
                    if (effect == null) {
                        DaggerLogger.error(LoggingContext.GENERIC, "Status effect not found: " + statusEffect);
                        return false;
                    }
                    if(data.getTestEntity(args.getOn()) instanceof LivingEntity living) {
                        return living.hasStatusEffect(effect);
                    }
                    return false;
                };
            })
            .addArgument(DaggerKeys.Provider.STATUS_EFFECT, e -> new Identifier(e.getAsString()));

}
