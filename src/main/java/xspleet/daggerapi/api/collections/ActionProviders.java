package xspleet.daggerapi.api.collections;

import com.google.gson.JsonElement;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import xspleet.daggerapi.api.logging.DaggerLogger;
import xspleet.daggerapi.artifact.ArtifactItem;
import xspleet.daggerapi.evaluation.DoubleExpression;
import xspleet.daggerapi.api.logging.LoggingContext;
import xspleet.daggerapi.api.registration.Mapper;
import xspleet.daggerapi.api.registration.Provider;
import xspleet.daggerapi.exceptions.WrongArgumentException;
import xspleet.daggerapi.trigger.actions.Action;

public class ActionProviders
{
    public static void registerActionProviders()
    {
        DaggerLogger.info(LoggingContext.STARTUP, "Registering action providers...");
    }

    public static Provider<Action> DO_NOTHING = Mapper.registerActionProvider("doNothing", (args) -> data -> {});

    public static Provider<Action> HEAL = Mapper.registerActionProvider("heal", (args) -> {
        var amountExpression = args.getData(DaggerKeys.Provider.AMOUNT);

        return data -> {
            if(!(data.getActEntity(args.getOn()) instanceof LivingEntity living)) {
                DaggerLogger.error(LoggingContext.GENERIC, "Living entity not found for action 'heal'");
                return;
            }
            var result = amountExpression.evaluate(data);
            living.heal(result.floatValue());
        };
    }).addArgument(DaggerKeys.Provider.AMOUNT, DoubleExpression::create);

    public static Provider<Action> SEND_MESSAGE = Mapper.registerActionProvider("sendMessage", (args) -> {
        String message = args.getData(DaggerKeys.Provider.MESSAGE);

        return data -> data.getActEntity(args.getOn()).sendMessage(Text.literal(message));
    }).addArgument(DaggerKeys.Provider.MESSAGE, JsonElement::getAsString);

    public static Provider<Action> KILL = Mapper.registerActionProvider("kill", (args) -> data -> {
        if(!(data.getActEntity(args.getOn()) instanceof LivingEntity living)) {
            DaggerLogger.error(LoggingContext.GENERIC, "Living entity not found for action 'kill'");
            return;
        }
        living.kill();
    });

    public static Provider<Action> CHANGE_DAMAGE_AMOUNT = Mapper.registerActionProvider("changeDamageAmount", (args) -> {
        var amountExpression = args.getData(DaggerKeys.Provider.AMOUNT);

        return data -> {
            var result = amountExpression.evaluate(data);
            data.addData(DaggerKeys.DAMAGE_AMOUNT, result);
        };
    }).addArgument(DaggerKeys.Provider.AMOUNT, DoubleExpression::create)
            .addRequiredData(DaggerKeys.DAMAGE_AMOUNT)
            .addAssociatedTrigger(Triggers.BEFORE_DAMAGE)
            .modifier();

    public static Provider<Action> ADD_STATUS_EFFECT = Mapper.registerActionProvider("addStatusEffect", (args) -> {
        Identifier statusEffectId = args.getData(DaggerKeys.Provider.STATUS_EFFECT);
        int duration = args.getData(DaggerKeys.Provider.DURATION);
        int amplifier = args.getData(DaggerKeys.Provider.AMPLIFIER);

        return data -> {
            if(!(data.getActEntity(args.getOn()) instanceof LivingEntity living)) {
                DaggerLogger.error(LoggingContext.GENERIC, "Living entity not found for action 'addStatusEffect'");
                return;
            }
            var statusEffect = Registries.STATUS_EFFECT.get(statusEffectId);
            if (statusEffect != null) {
                living.addStatusEffect(new StatusEffectInstance(statusEffect, duration, amplifier));
            } else {
                DaggerLogger.error(LoggingContext.GENERIC, "Status effect not found: " + statusEffectId);
            }
        };
    }).addArgument(DaggerKeys.Provider.STATUS_EFFECT, e -> new Identifier(e.getAsString()))
            .addArgument(DaggerKeys.Provider.DURATION, JsonElement::getAsInt)
            .addArgument(DaggerKeys.Provider.AMPLIFIER, JsonElement::getAsInt);

    public static Provider<Action> REMOVE_STATUS_EFFECT = Mapper.registerActionProvider("removeStatusEffect", (args) -> {
        Identifier statusEffectId = args.getData(DaggerKeys.Provider.STATUS_EFFECT);

        return data -> {
            if(!(data.getActEntity(args.getOn()) instanceof LivingEntity living)) {
                DaggerLogger.error(LoggingContext.GENERIC, "Living entity not found for action 'removeStatusEffect'");
                return;
            }

            var statusEffect = Registries.STATUS_EFFECT.get(statusEffectId);
            if (statusEffect != null) {
                living.removeStatusEffect(statusEffect);
            } else {
                DaggerLogger.error(LoggingContext.GENERIC, "Status effect not found: " + statusEffectId);
            }
        };
    }).addArgument(DaggerKeys.Provider.STATUS_EFFECT, e -> new Identifier(e.getAsString()));

    public static Provider<Action> REPLENISH_AIR = Mapper.registerActionProvider("replenishAir", (args) -> {
        return data -> {
            if(!(data.getActEntity(args.getOn()) instanceof LivingEntity living)) {
                DaggerLogger.error(LoggingContext.GENERIC, "Living entity not found for action 'replenishAir'");
                return;
            }

            if(living.getAir() != living.getMaxAir()) {
                living.setAir(living.getMaxAir());
            }
        };
    });

    public static Provider<Action> REPLENISH_FOOD = Mapper.registerActionProvider("replenishFood", (args) -> {
        return data -> {
            if(!(data.getActEntity(args.getOn()) instanceof PlayerEntity player)) {
                DaggerLogger.error(LoggingContext.GENERIC, "Player entity not found for action 'replenishFood'");
                return;
            }
            if(player.getHungerManager().getFoodLevel() < 20) {
                player.getHungerManager().setFoodLevel(20);
                player.getHungerManager().setSaturationLevel(5.0F);
            }
        };
    });

    public static Provider<Action> CHANGE_FOOD_AMOUNT = Mapper.registerActionProvider("changeFoodAmount", (args) -> {
        var amountExpression = args.getData(DaggerKeys.Provider.AMOUNT);

        return data -> {
            if(!(data.getActEntity(args.getOn()) instanceof PlayerEntity player)) {
                DaggerLogger.error(LoggingContext.GENERIC, "Player entity not found for action 'changeFoodAmount'");
                return;
            }
            var result = amountExpression.evaluate(data);
            data.addData(DaggerKeys.FOOD_AMOUNT, result);
        };
    }).addArgument(DaggerKeys.Provider.AMOUNT, DoubleExpression::create)
            .addRequiredData(DaggerKeys.FOOD_AMOUNT)
            .addAssociatedTrigger(Triggers.EAT)
            .modifier();

    public static Provider<Action> CHANGE_SATURATION_AMOUNT = Mapper.registerActionProvider("changeSaturationAmount", (args) -> {
        var amountExpression = args.getData(DaggerKeys.Provider.AMOUNT);

        return data -> {
            if(!(data.getActEntity(args.getOn()) instanceof PlayerEntity player)) {
                DaggerLogger.error(LoggingContext.GENERIC, "Player entity not found for action 'changeSaturationAmount'");
                return;
            }
            var result = amountExpression.evaluate(data);
            data.addData(DaggerKeys.SATURATION_AMOUNT, result);
        };
    }).addArgument(DaggerKeys.Provider.AMOUNT, DoubleExpression::create)
            .addRequiredData(DaggerKeys.SATURATION_AMOUNT)
            .addAssociatedTrigger(Triggers.EAT)
            .modifier();

    public static Provider<Action> STRIKE_LIGHTNING = Mapper.registerActionProvider("strikeLightning", (args) -> {
        return data -> {
            var entity = data.getActEntity(args.getOn());
            var lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, entity.getWorld());

            lightning.setPos(entity.getX(), entity.getY(), entity.getZ());
            entity.getWorld().spawnEntity(lightning);
        };
    });

    public static Provider<Action> RECHARGE_ACTIVE_ARTIFACTS = Mapper.registerActionProvider("rechargeActiveArtifacts", (args) -> {
        return data -> {
            var entity = data.getActEntity(args.getOn());
            if (!(entity instanceof PlayerEntity player)) {
                DaggerLogger.error(LoggingContext.GENERIC, "Player entity not found for action 'rechargeActiveArtifacts'");
                return;
            }
            TrinketsApi.getTrinketComponent(player).ifPresent(trinketComponent -> {
                trinketComponent.getAllEquipped().forEach(
                        slotRef -> {
                            var itemStack = slotRef.getRight();
                            if (itemStack.getItem() instanceof ArtifactItem artifactItem && artifactItem.isActive()) {
                                player.getItemCooldownManager().remove(artifactItem);
                            }
                        }
                );
            });
        };
    });

    public static Provider<Action> SET_WEATHER = Mapper.registerActionProvider("setWeather", (args) -> {
        String weather = args.getData(DaggerKeys.Provider.WEATHER);

        boolean isClear = "clear".equalsIgnoreCase(weather);
        boolean isRain = "rain".equalsIgnoreCase(weather);
        boolean isThunder = "thunder".equalsIgnoreCase(weather);

        if (!isClear && !isRain && !isThunder) {
            throw new WrongArgumentException(
                    "weather", args.getData(DaggerKeys.Provider.WEATHER)
            );
        }

        return data -> {
            var world = data.getActWorld(args.getOn());
            if (!world.isClient())
            {
                var serverWorld = (ServerWorld) world;
                if (isClear)
                    serverWorld.setWeather(0, 0, false, false);
                else
                    serverWorld.setWeather(0, 60000, true, !isRain);
            }

        };
    }).addArgument(DaggerKeys.Provider.WEATHER, JsonElement::getAsString);

    public static Provider<Action> DAMAGE = Mapper.registerActionProvider("damage", (args) -> {
        var amountExpression = args.getData(DaggerKeys.Provider.AMOUNT);
        var type = args.getData(DaggerKeys.Provider.DAMAGE_TYPE);

        return data -> {
            var world = data.getActWorld(args.getOn());
            var damageType = world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).get(type);

            if (damageType == null) {
                DaggerLogger.error(LoggingContext.GENERIC, "Damage type not found: " + type);
                return;
            }

            var damageTypeKey = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, type);

            var damageSource = world.getDamageSources().create(damageTypeKey);
            var result = amountExpression.evaluate(data);
            var entity = data.getActEntity(args.getOn());
            if (!(entity instanceof LivingEntity living)) {
                DaggerLogger.error(LoggingContext.GENERIC, "Living entity not found for action 'damage'");
                return;
            }

            living.damage(damageSource, result.floatValue());
        };
    }).addArgument(DaggerKeys.Provider.AMOUNT, DoubleExpression::create)
            .addArgument(DaggerKeys.Provider.DAMAGE_TYPE, e -> new Identifier(e.getAsString()))
            .addRequiredData(DaggerKeys.DAMAGE_AMOUNT);
}
