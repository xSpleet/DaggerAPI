package xspleet.daggerapi.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.text.Text;
import xspleet.daggerapi.DaggerAPIClient;
import xspleet.daggerapi.attributes.Attribute;
import xspleet.daggerapi.attributes.AttributeHolder;
import xspleet.daggerapi.attributes.ClientAttributeHolder;

public class ClientCommands
{
    public static final String COMMAND_PREFIX = "daggerclient";
    public static final String COMMAND_ATTRIBUTE = "attribute";
    public static final String COMMAND_TRIGGER = "trigger";
    public static final String COMMAND_SNOOP = "snoop";
    public static final String COMMAND_SET = "set";
    public static final String COMMAND_PLAYER = "player";


    public static void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, commandRegistryAccess) -> {
                    dispatcher.register(ClientCommandManager.literal(COMMAND_PREFIX)
                            .requires(x -> x.hasPermissionLevel(2) && DaggerAPIClient.CLIENT_DEV_MODE)
                            .then(ClientCommandManager.literal(COMMAND_SNOOP)
                                    .then(ClientCommandManager.literal(COMMAND_ATTRIBUTE)
                                            .then(ClientCommandManager.argument(COMMAND_ATTRIBUTE, new AttributeArgumentType())
                                                    .then(ClientCommandManager.argument(COMMAND_SET, BoolArgumentType.bool())
                                                            .executes(
                                                                ctx -> {
                                                                    var player = ctx.getSource().getPlayer();
                                                                    boolean set = BoolArgumentType.getBool(ctx, COMMAND_SET);
                                                                    Attribute<?> attribute = ctx.getArgument(COMMAND_ATTRIBUTE, Attribute.class);

                                                                    if(player == null) {
                                                                        ctx.getSource().sendError(Text.literal("This command can only be used by players."));
                                                                        return 0;
                                                                    }

                                                                    if(!(player instanceof ClientAttributeHolder clientHolder)) {
                                                                        ctx.getSource().sendError(Text.literal("This command can only be used by players that are a AttributeHolder."));
                                                                        return 0;
                                                                    }

                                                                    if(set) {
                                                                        clientHolder.addAttributeToUpdate(attribute);
                                                                    }
                                                                    else {
                                                                        clientHolder.removeAttributeToUpdate(attribute);
                                                                    }

                                                                    return 1;
                                                                }
                                                            )
                                                    )
                                            )
                                    )
                            ).then(ClientCommandManager.literal(COMMAND_PLAYER)
                                    .then(ClientCommandManager.literal(COMMAND_ATTRIBUTE)
                                            .then(ClientCommandManager.argument(COMMAND_ATTRIBUTE, new AttributeArgumentType())
                                                    .executes(ctx -> {
                                                        var player = ctx.getSource().getPlayer();
                                                        Attribute<?> attribute = ctx.getArgument("attribute", Attribute.class);

                                                        if(player instanceof AttributeHolder holder)
                                                        {
                                                            var instance = holder.getAttributeInstance(attribute);

                                                            if(instance == null) {
                                                                ctx.getSource().sendFeedback(
                                                                        Text.literal("Attribute instance not found for " + attribute.getName())
                                                                );
                                                                return 0;
                                                            }

                                                            var modifiers = instance.getModifiers();
                                                            if(modifiers.isEmpty()) {
                                                                ctx.getSource().sendFeedback(
                                                                        Text.literal("No modifiers found for attribute " + attribute.getName())
                                                                );
                                                                return 1;
                                                            }

                                                            StringBuilder feedback = new StringBuilder("Modifiers for attribute " + attribute.getName() + ":\n");
                                                            for (var modifier : modifiers) {
                                                                feedback.append("From ")
                                                                        .append(modifier.getArtifactName())
                                                                        .append(": ")
                                                                        .append(modifier.getName())
                                                                        .append(" (")
                                                                        .append(modifier.getUUID())
                                                                        .append("): ")
                                                                        .append(modifier.getValue())
                                                                        .append("\n");
                                                            }

                                                            ctx.getSource().sendFeedback(
                                                                    Text.literal(feedback.toString())
                                                            );
                                                        }
                                                        else {
                                                            ctx.getSource().sendError(Text.literal("This command can only be used by players that are a AttributeHolder."));
                                                        }

                                                        return 2;
                                                    })
                                            )
                                    )
                            )
                    );
                }
        );
    }
}
