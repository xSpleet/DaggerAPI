package xspleet.daggerapi.api.collections;

import com.google.gson.JsonElement;
import io.netty.handler.logging.LogLevel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import xspleet.daggerapi.api.logging.DaggerLogger;
import xspleet.daggerapi.api.logging.LoggingContext;
import xspleet.daggerapi.api.registration.Mapper;
import xspleet.daggerapi.api.registration.Provider;
import xspleet.daggerapi.evaluation.DoubleExpression;
import xspleet.daggerapi.trigger.Condition;
import xspleet.daggerapi.exceptions.WrongArgumentException;

import java.util.Objects;

public class ConditionProviders
{
    public static void registerConditionProviders()
    {
        DaggerLogger.info(LoggingContext.STARTUP, "Registering condition providers...");
    }

    public static final Provider<Condition> ALWAYS = Mapper.registerConditionProvider("always", (args) -> {
                return data -> true;
                    });

    public static final Provider<Condition> NEVER = Mapper.registerConditionProvider("never", (args) -> {
                return data -> false;
                    });

    public static final Provider<Condition> IF_WEATHER = Mapper.registerConditionProvider("ifWeather", (args) -> {
                String weather = args.getData(DaggerKeys.Provider.WEATHER);
                boolean isRaining = weather.equalsIgnoreCase("rain");
                boolean isThundering = weather.equalsIgnoreCase("thunder");
                boolean isClear = weather.equalsIgnoreCase("clear");
                if(!isRaining && !isThundering && !isClear)
                    throw new WrongArgumentException(DaggerKeys.Provider.WEATHER.key(), weather);

                return data -> {
                    World world = data.getTestWorld(args.getOn());

                    return isRaining && world.isRaining()
                            || isThundering && world.isThundering()
                            || isClear && !world.isRaining();
                };
            })
            .addArgument(DaggerKeys.Provider.WEATHER, JsonElement::getAsString);

    public static final Provider<Condition> IF_DIMENSION = Mapper.registerConditionProvider("ifDimension", (args) -> {
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

    public static final Provider<Condition> IF_ARTIFACT = Mapper.registerConditionProvider("ifArtifact", args -> {
                Identifier artifact = args.getData(DaggerKeys.Provider.ARTIFACT);

                return data -> {
                    return data.getData(DaggerKeys.ARTIFACT).getIdentifier().equals(artifact);
                };
            })
            .addArgument(DaggerKeys.Provider.ARTIFACT, e -> new Identifier(e.getAsString()))
            .addRequiredData(DaggerKeys.ARTIFACT)
            .addAssociatedTrigger(Triggers.ACTIVATE);

    public static final Provider<Condition> IF_SUCCESSFUL = Mapper.registerConditionProvider("ifSuccessful", args -> {
                return data -> {
                    return data.getData(DaggerKeys.SUCCESSFUL);
                };
            })
            .addRequiredData(DaggerKeys.SUCCESSFUL)
            .addAssociatedTrigger(Triggers.ACTIVATE);

    public static final Provider<Condition> IF_DAMAGE_TYPE = Mapper.registerConditionProvider("ifDamageType", args -> {
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

    public static final Provider<Condition> HAS_STATUS_EFFECT = Mapper.registerConditionProvider("hasStatusEffect", args -> {
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

    public static final Provider<Condition> IF_RAINED_ON = Mapper.registerConditionProvider("ifRainedOn", args -> {
                return data -> {
                    World world = data.getTestWorld(args.getOn());
                    return world.hasRain(data.getTestEntity(args.getOn()).getBlockPos());
                };
            });

    public static final Provider<Condition> COMPARE = Mapper.registerConditionProvider("compare", args -> {
                DoubleExpression valueExpression = args.getData(DaggerKeys.Provider.VALUE);
                String operator = args.getData(DaggerKeys.Provider.OPERATOR);
                DoubleExpression compareValueExpression = args.getData(DaggerKeys.Provider.COMPARE_VALUE);

                //check if operator is a valid operator
                if (!operator.matches("[<>!=]=?|==")) {
                    throw new WrongArgumentException(DaggerKeys.Provider.OPERATOR.key(), operator);
                }

                return data -> {
                    var value = (double) valueExpression.evaluate(data);
                    var compareValue = (double) compareValueExpression.evaluate(data);

                    return switch (operator) {
                        case "==" -> value == compareValue;
                        case "!=" -> value != compareValue;
                        case "<" -> value < compareValue;
                        case "<=" -> value <= compareValue;
                        case ">" -> value > compareValue;
                        case ">=" -> value >= compareValue;
                        default -> throw new IllegalArgumentException("Invalid operator: " + operator);
                    };
                };
            })
            .addArgument(DaggerKeys.Provider.VALUE, DoubleExpression::create)
            .addArgument(DaggerKeys.Provider.OPERATOR, JsonElement::getAsString)
            .addArgument(DaggerKeys.Provider.COMPARE_VALUE, DoubleExpression::create);

    public static final Provider<Condition> CHANCE = Mapper.registerConditionProvider("chance", args -> {
                DoubleExpression chanceExpression = args.getData(DaggerKeys.Provider.CHANCE);

                return data -> {
                    double chance = chanceExpression.evaluate(data);
                    if (chance < 0 || chance > 1) {
                        DaggerLogger.report(LoggingContext.GENERIC, LogLevel.WARN, "Chance must be between 0 and 1, but got: " + chance);
                    }
                    return Math.random() < chance;
                };
            })
            .addArgument(DaggerKeys.Provider.CHANCE, DoubleExpression::create);
}
