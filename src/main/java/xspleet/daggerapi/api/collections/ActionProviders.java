package xspleet.daggerapi.api.collections;

import com.google.gson.JsonElement;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.apache.logging.log4j.core.appender.rewrite.MapRewritePolicy;
import xspleet.daggerapi.api.logging.DaggerLogger;
import xspleet.daggerapi.artifact.ArtifactItem;
import xspleet.daggerapi.data.key.DaggerKey;
import xspleet.daggerapi.evaluation.DoubleExpression;
import xspleet.daggerapi.api.logging.LoggingContext;
import xspleet.daggerapi.api.registration.Mapper;
import xspleet.daggerapi.api.registration.Provider;
import xspleet.daggerapi.exceptions.WrongArgumentException;
import xspleet.daggerapi.networking.ServerNetworking;
import xspleet.daggerapi.trigger.actions.Action;
import xspleet.daggerapi.util.DamageTypeUtil;

import java.util.Map;
import java.util.Optional;

public class ActionProviders
{
    public static void registerActionProviders()
    {
        DaggerLogger.info(LoggingContext.STARTUP, "Registering action providers...");
    }

    public static final Provider<Action> DO_NOTHING = Mapper.registerActionProvider("doNothing", (args) -> data -> {});

    public static final Provider<Action> HEAL = Mapper.registerActionProvider("heal", (args) -> {
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

    public static final Provider<Action> SEND_MESSAGE = Mapper.registerActionProvider("sendMessage", (args) -> {
        String message = args.getData(DaggerKeys.Provider.MESSAGE);

        return data -> data.getActEntity(args.getOn()).sendMessage(Text.literal(message));
    }).addArgument(DaggerKeys.Provider.MESSAGE, JsonElement::getAsString);

    public static final Provider<Action> KILL = Mapper.registerActionProvider("kill", (args) -> data -> {
        if(!(data.getActEntity(args.getOn()) instanceof LivingEntity living)) {
            DaggerLogger.error(LoggingContext.GENERIC, "Living entity not found for action 'kill'");
            return;
        }
        living.kill();
    });

    public static final Provider<Action> CHANGE_DAMAGE_AMOUNT = Mapper.registerActionProvider("changeDamageAmount", (args) -> {
        var amountExpression = args.getData(DaggerKeys.Provider.AMOUNT);

        return data -> {
            var result = amountExpression.evaluate(data);
            data.addData(DaggerKeys.DAMAGE_AMOUNT, result);
        };
    }).addArgument(DaggerKeys.Provider.AMOUNT, DoubleExpression::create)
            .addRequiredData(DaggerKeys.DAMAGE_AMOUNT)
            .addAssociatedTrigger(Triggers.BEFORE_DAMAGE)
            .modifier();

    public static final Provider<Action> ADD_STATUS_EFFECT = Mapper.registerActionProvider("addStatusEffect", (args) -> {
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

    public static final Provider<Action> REMOVE_STATUS_EFFECT = Mapper.registerActionProvider("removeStatusEffect", (args) -> {
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

    public static final Provider<Action> REPLENISH_AIR = Mapper.registerActionProvider("replenishAir", (args) -> {
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

    public static final Provider<Action> REPLENISH_FOOD = Mapper.registerActionProvider("replenishFood", (args) -> {
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

    public static final Provider<Action> CHANGE_FOOD_AMOUNT = Mapper.registerActionProvider("changeFoodAmount", (args) -> {
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

    public static final Provider<Action> CHANGE_SATURATION_AMOUNT = Mapper.registerActionProvider("changeSaturationAmount", (args) -> {
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

    public static final Provider<Action> STRIKE_LIGHTNING = Mapper.registerActionProvider("strikeLightning", (args) -> {
        return data -> {
            var entity = data.getActEntity(args.getOn());
            var lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, entity.getWorld());

            lightning.setPos(entity.getX(), entity.getY(), entity.getZ());
            entity.getWorld().spawnEntity(lightning);
        };
    });

    public static final Provider<Action> RECHARGE_ACTIVE_ARTIFACTS = Mapper.registerActionProvider("rechargeActiveArtifacts", (args) -> {
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

    public static final Provider<Action> SET_WEATHER = Mapper.registerActionProvider("setWeather", (args) -> {
        String weather = args.getData(DaggerKeys.Provider.WEATHER);

        boolean isClear = "clear".equalsIgnoreCase(weather);
        boolean isRain = "rain".equalsIgnoreCase(weather);
        boolean isThunder = "thunder".equalsIgnoreCase(weather);

        if (!isClear && !isRain && !isThunder) {
            throw new WrongArgumentException(
                    DaggerKeys.Provider.WEATHER.key(), args.getData(DaggerKeys.Provider.WEATHER)
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

    public static final Provider<Action> DAMAGE = Mapper.registerActionProvider("damage", (args) -> {
        var amountExpression = args.getData(DaggerKeys.Provider.AMOUNT);
        var type = args.getData(DaggerKeys.Provider.DAMAGE_TYPE);

        return data -> {
            var world = data.getActWorld(args.getOn());

            var damageType = DamageTypeUtil.getDamageType(world, type);
            if (damageType == null) {
                DaggerLogger.error(LoggingContext.GENERIC, "Damage type not found: " + type);
                return;
            }

            var damageTypeKey = DamageTypeUtil.getDamageTypeKey(type);
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
            .addArgument(DaggerKeys.Provider.DAMAGE_TYPE, e -> new Identifier(e.getAsString()));

    public static final Provider<Action> CHANGE_DAMAGE_TYPE = Mapper.registerActionProvider("changeDamageType", (args) -> {
        var type = args.getData(DaggerKeys.Provider.DAMAGE_TYPE);

        return data -> {
            var world = data.getActWorld(args.getOn());
            var damageType = DamageTypeUtil.getDamageType(world, type);
            if (damageType == null) {
                DaggerLogger.error(LoggingContext.GENERIC, "Damage type not found: " + type);
                return;
            }

            var damageTypeKey = DamageTypeUtil.getDamageTypeKey(type);
            var damageSource = data.getData(DaggerKeys.DAMAGE_SOURCE);

            var newDamageSource = world.getDamageSources().create(damageTypeKey, damageSource.getSource(), damageSource.getAttacker());
            data.addData(DaggerKeys.DAMAGE_SOURCE, newDamageSource);
        };
    }).addArgument(DaggerKeys.Provider.DAMAGE_TYPE, e -> new Identifier(e.getAsString()))
            .addRequiredData(DaggerKeys.DAMAGE_SOURCE)
            .addAssociatedTrigger(Triggers.BEFORE_DAMAGE)
            .addAssociatedTrigger(Triggers.BEFORE_DEATH)
            .modifier();

    public static final Provider<Action> GIVE_ITEM = Mapper.registerActionProvider("giveItem", (args) -> {
        Identifier itemId = args.getData(DaggerKeys.Provider.ITEM);
        int countFinal = args.getData(DaggerKeys.Provider.COUNT);

        return data -> {
            int count = countFinal;
            var entity = data.getActEntity(args.getOn());
            if (!(entity instanceof PlayerEntity player)) {
                DaggerLogger.error(LoggingContext.GENERIC, "Player entity not found for action 'giveItem'");
                return;
            }

            var item = Registries.ITEM.get(itemId);

            if (item == Items.AIR) {
                DaggerLogger.error(LoggingContext.GENERIC, "Item not found: " + itemId);
                return;
            }

            while(count > item.getMaxCount()) {
                var itemStack = new ItemStack(item, item.getMaxCount());
                count -= item.getMaxCount();
                if (!player.getInventory().insertStack(itemStack)) {
                    player.dropItem(itemStack, false);
                }
            }
            var itemStack = new ItemStack(item, count);
            if (!player.getInventory().insertStack(itemStack)) {
                player.dropItem(itemStack, false);
            }
        };
    })
            .addArgument(DaggerKeys.Provider.ITEM, e -> new Identifier(e.getAsString()))
            .addArgument(DaggerKeys.Provider.COUNT, JsonElement::getAsInt);

    public static final Provider<Action> REMOVE_ITEM = Mapper.registerActionProvider("removeItem", (args) -> {
        Identifier itemId = args.getData(DaggerKeys.Provider.ITEM);
        int countFinal = args.getData(DaggerKeys.Provider.COUNT);

        return data -> {
            int count = countFinal;
            var entity = data.getActEntity(args.getOn());
            if (!(entity instanceof PlayerEntity player)) {
                DaggerLogger.error(LoggingContext.GENERIC, "Player entity not found for action 'removeItem'");
                return;
            }

            var item = Registries.ITEM.get(itemId);

            if (item == Items.AIR) {
                DaggerLogger.error(LoggingContext.GENERIC, "Item not found: " + itemId);
                return;
            }

            for (int i = 0; i < player.getInventory().size(); i++) {
                var itemStack = player.getInventory().getStack(i);
                if (itemStack.getItem() == item) {
                    int stackCount = itemStack.getCount();
                    if (stackCount <= count) {
                        player.getInventory().setStack(i, ItemStack.EMPTY);
                        count -= stackCount;
                    } else {
                        itemStack.decrement(count);
                        player.getInventory().setStack(i, itemStack);
                        count = 0;
                    }
                    if (count == 0) break;
                }
            }
        };
    })
            .addArgument(DaggerKeys.Provider.ITEM, e -> new Identifier(e.getAsString()))
            .addArgument(DaggerKeys.Provider.COUNT, JsonElement::getAsInt);

    public static final Provider<Action> FORBID_DEATH = Mapper.registerActionProvider("forbidDeath", (args) -> {
                return data -> {
                    data.addData(DaggerKeys.ALLOW_DEATH, false);
                };
            })
            .addRequiredData(DaggerKeys.ALLOW_DEATH)
            .addAssociatedTrigger(Triggers.BEFORE_DEATH)
            .modifier();

    public static final Provider<Action> SET_FIRE = Mapper.registerActionProvider("setFire", (args) -> {
        var durationExpression = args.getData(DaggerKeys.Provider.FIRE_DURATION);

        return data -> {
            var count = durationExpression.evaluate(data).intValue();
            data.getActEntity(args.getOn()).setOnFireFor(count / 20);
        };
    })
            .addArgument(DaggerKeys.Provider.FIRE_DURATION, DoubleExpression::create);

    public static final Provider<Action> ADD_VELOCITY = Mapper.registerActionProvider("addVelocity", (args) -> {
        var xExpression = args.getData(DaggerKeys.Provider.X);
        var yExpression = args.getData(DaggerKeys.Provider.Y);
        var zExpression = args.getData(DaggerKeys.Provider.Z);

        return data -> {
            var x = xExpression.evaluate(data);
            var y = yExpression.evaluate(data);
            var z = zExpression.evaluate(data);

            Entity entity = data.getActEntity(args.getOn());
            entity.addVelocity(x, y, z);
            entity.velocityModified = true;
        };
    })
            .addArgument(DaggerKeys.Provider.X, DoubleExpression::create)
            .addArgument(DaggerKeys.Provider.Y, DoubleExpression::create)
            .addArgument(DaggerKeys.Provider.Z, DoubleExpression::create);

    public static final Provider<Action> SUMMON = Mapper.registerActionProvider("summon", (args) -> {
                var xExpression = args.getData(DaggerKeys.Provider.X);
                var yExpression = args.getData(DaggerKeys.Provider.Y);
                var zExpression = args.getData(DaggerKeys.Provider.Z);
                var entityId = args.getData(DaggerKeys.Provider.ENTITY);

                return data -> {
                    var x = xExpression.evaluate(data);
                    var y = yExpression.evaluate(data);
                    var z = zExpression.evaluate(data);

                    var entity = Registries.ENTITY_TYPE.get(entityId);
                    if (entity == EntityType.PIG && !entityId.equals(new Identifier("minecraft", "pig"))) {
                        DaggerLogger.error(LoggingContext.GENERIC, "Entity type not found: " + entityId);
                        return;
                    }
                    if (entity == EntityType.PLAYER) {
                        DaggerLogger.error(LoggingContext.GENERIC, "Cannot summon player entity: " + entityId);
                        return;
                    }

                    var world = data.getActWorld(args.getOn());
                    var summonedEntity = entity.create(world);
                    if (summonedEntity == null) {
                        DaggerLogger.error(LoggingContext.GENERIC, "Failed to create entity: " + entityId);
                        return;
                    }
                    summonedEntity.refreshPositionAndAngles(x, y, z, 0.0f, 0.0f);
                    world.spawnEntity(summonedEntity);
                };
            })
            .addArgument(DaggerKeys.Provider.X, DoubleExpression::create)
            .addArgument(DaggerKeys.Provider.Y, DoubleExpression::create)
            .addArgument(DaggerKeys.Provider.Z, DoubleExpression::create)
            .addArgument(DaggerKeys.Provider.ENTITY, e -> new Identifier(e.getAsString()));

    public static final Provider<Action> SUMMON_EXPLOSION = Mapper.registerActionProvider("summonExplosion", (args) -> {
                var xExpression = args.getData(DaggerKeys.Provider.X);
                var yExpression = args.getData(DaggerKeys.Provider.Y);
                var zExpression = args.getData(DaggerKeys.Provider.Z);
                var strengthExpression = args.getData(DaggerKeys.Provider.STRENGTH);
                var breakBlocks = args.getData(DaggerKeys.Provider.BREAK_BLOCKS);
                var fire = args.getData(DaggerKeys.Provider.FIRE);

                return data -> {
                    var x = xExpression.evaluate(data);
                    var y = yExpression.evaluate(data);
                    var z = zExpression.evaluate(data);

                    var strength = strengthExpression.evaluate(data);
                    var world = data.getActWorld(args.getOn());
                    Explosion.DestructionType destructionType =
                            breakBlocks
                                    ? Explosion.DestructionType.DESTROY
                                    : Explosion.DestructionType.KEEP;

                    Explosion explosion = new Explosion(
                            world,
                            null,
                            x, y, z,
                            strength.floatValue(),
                            fire,
                            destructionType
                    );

                    explosion.collectBlocksAndDamageEntities();
                    explosion.affectWorld(true);
                };
            })
            .addArgument(DaggerKeys.Provider.X, DoubleExpression::create)
            .addArgument(DaggerKeys.Provider.Y, DoubleExpression::create)
            .addArgument(DaggerKeys.Provider.Z, DoubleExpression::create)
            .addArgument(DaggerKeys.Provider.STRENGTH, DoubleExpression::create)
            .addArgument(DaggerKeys.Provider.BREAK_BLOCKS, JsonElement::getAsBoolean)
            .addArgument(DaggerKeys.Provider.FIRE, JsonElement::getAsBoolean);

    public static final Provider<Action> PLAY_SOUND = Mapper.registerActionProvider("playSound", (args) -> {
        var xExpression = args.getData(DaggerKeys.Provider.X);
        var yExpression = args.getData(DaggerKeys.Provider.Y);
        var zExpression = args.getData(DaggerKeys.Provider.Z);
        Identifier soundId = args.getData(DaggerKeys.Provider.SOUND);
        var volume = args.getData(DaggerKeys.Provider.VOLUME);
        var pitch = args.getData(DaggerKeys.Provider.PITCH);

        return data -> {
            var x = xExpression.evaluate(data);
            var y = yExpression.evaluate(data);
            var z = zExpression.evaluate(data);
            var volumeValue = volume.evaluate(data);
            var pitchValue = pitch.evaluate(data);

            var sound = Registries.SOUND_EVENT.get(soundId);
            if (sound == null) {
                DaggerLogger.error(LoggingContext.GENERIC, "Sound event not found: " + soundId);
                return;
            }

            data.getActWorld(args.getOn())
                    .playSound(
                            null,
                            x, y, z,
                            sound,
                            SoundCategory.MASTER,
                            volumeValue.floatValue(),
                            pitchValue.floatValue()
                    );
        };
    })
            .addArgument(DaggerKeys.Provider.X, DoubleExpression::create)
            .addArgument(DaggerKeys.Provider.Y, DoubleExpression::create)
            .addArgument(DaggerKeys.Provider.Z, DoubleExpression::create)
            .addArgument(DaggerKeys.Provider.SOUND, e -> new Identifier(e.getAsString()))
            .addArgument(DaggerKeys.Provider.VOLUME, DoubleExpression::create)
            .addArgument(DaggerKeys.Provider.PITCH, DoubleExpression::create);

    public static final Provider<Action> FORBID_TELEPORT = Mapper.registerActionProvider("forbidTeleport", (args) -> {
                return data -> {
                    data.addData(DaggerKeys.ALLOW_TELEPORT, false);
                };
            })
            .addRequiredData(DaggerKeys.ALLOW_TELEPORT)
            .addAssociatedTrigger(Triggers.BEFORE_ENDER_PEARL_TELEPORT)
            .modifier();

    public static final Provider<Action> FORBID_FALL_DAMAGE = Mapper.registerActionProvider("forbidFallDamage", (args) -> {
                return data -> {
                    data.addData(DaggerKeys.ALLOW_FALL_DAMAGE, false);
                };
            })
            .addRequiredData(DaggerKeys.ALLOW_FALL_DAMAGE)
            .addAssociatedTrigger(Triggers.BEFORE_ENDER_PEARL_TELEPORT)
            .modifier();

    public static final Provider<Action> SPAWN_PARTICLES = Mapper.registerActionProvider("spawnParticles", (args) -> {
        var xExpression = args.getData(DaggerKeys.Provider.X);
        var yExpression = args.getData(DaggerKeys.Provider.Y);
        var zExpression = args.getData(DaggerKeys.Provider.Z);
        var xVelocityExpression = args.getData(DaggerKeys.Provider.VELOCITY_X);
        var yVelocityExpression = args.getData(DaggerKeys.Provider.VELOCITY_Y);
        var zVelocityExpression = args.getData(DaggerKeys.Provider.VELOCITY_Z);
        Identifier particleId = args.getData(DaggerKeys.Provider.PARTICLE);

        return data -> {
            var x = xExpression.evaluate(data);
            var y = yExpression.evaluate(data);
            var z = zExpression.evaluate(data);
            var xVelocity = xVelocityExpression.evaluate(data);
            var yVelocity = yVelocityExpression.evaluate(data);
            var zVelocity = zVelocityExpression.evaluate(data);

            var particleType = Registries.PARTICLE_TYPE.get(particleId);
            if (particleType == null) {
                DaggerLogger.error(LoggingContext.GENERIC, "Particle type not found: " + particleId);
                return;
            }

            ServerNetworking.sendParticlePacket(data.getActWorld(args.getOn()), particleId, x, y, z, xVelocity, yVelocity, zVelocity);
        };
    })
            .addArgument(DaggerKeys.Provider.X, DoubleExpression::create)
            .addArgument(DaggerKeys.Provider.Y, DoubleExpression::create)
            .addArgument(DaggerKeys.Provider.Z, DoubleExpression::create)
            .addArgument(DaggerKeys.Provider.VELOCITY_X, DoubleExpression::create)
            .addArgument(DaggerKeys.Provider.VELOCITY_Y, DoubleExpression::create)
            .addArgument(DaggerKeys.Provider.VELOCITY_Z, DoubleExpression::create)
            .addArgument(DaggerKeys.Provider.PARTICLE, e -> new Identifier(e.getAsString()));
}
