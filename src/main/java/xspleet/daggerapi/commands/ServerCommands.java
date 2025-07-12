package xspleet.daggerapi.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.text.Text;
import xspleet.daggerapi.DaggerAPI;
import xspleet.daggerapi.attributes.Attribute;
import xspleet.daggerapi.attributes.AttributeHolder;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ServerCommands
{
    public static final String COMMAND_PREFIX = "daggerapi";
    public static final String COMMAND_ATTRIBUTE = "attribute";
    public static final String COMMAND_PLAYER = "player";

    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, env) -> {
                    dispatcher.register(
                            literal(COMMAND_PREFIX).requires(source -> source.hasPermissionLevel(2) || DaggerAPI.DEBUG_MODE)
                                    .then(argument(COMMAND_PLAYER, EntityArgumentType.player())
                                            .then(literal(COMMAND_ATTRIBUTE)
                                                    .then(argument(COMMAND_ATTRIBUTE, new AttributeArgumentType())
                                                            .executes(ctx -> {
                                                                var player = EntityArgumentType.getPlayer(ctx, "player");
                                                                Attribute<?> attribute = ctx.getArgument("attribute", Attribute.class);

                                                                if(player instanceof AttributeHolder holder)
                                                                {
                                                                    var instance = holder.getAttributeInstance(attribute);

                                                                    if(instance == null) {
                                                                        ctx.getSource().sendFeedback(
                                                                                () -> Text.literal("Attribute instance not found for " + attribute.getName()),
                                                                                false
                                                                        );
                                                                        return 0;
                                                                    }

                                                                    var modifiers = instance.getModifiers();
                                                                    if(modifiers.isEmpty()) {
                                                                        ctx.getSource().sendFeedback(
                                                                                () -> Text.literal("No modifiers found for attribute " + attribute.getName()),
                                                                                false
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
                                                                            () -> Text.literal(feedback.toString()),
                                                                            false
                                                                    );
                                                                }

                                                                return 2;
                                                            }))))
                    );
                }
        );
    }
}
