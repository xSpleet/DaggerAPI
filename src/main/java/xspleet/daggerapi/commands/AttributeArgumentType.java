package xspleet.daggerapi.commands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import xspleet.daggerapi.attributes.Attribute;
import xspleet.daggerapi.api.registration.Mapper;
import xspleet.daggerapi.exceptions.NoSuchAttributeException;

import java.util.concurrent.CompletableFuture;

public class AttributeArgumentType implements ArgumentType<Attribute<?>> {
    @Override
    public Attribute<?> parse(StringReader stringReader) throws CommandSyntaxException {
        try{
            return Mapper.getAttribute(stringReader.readString());
        }
        catch (NoSuchAttributeException e) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().create("No such attribute: " + stringReader.getString());
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for(String attribute : Mapper.getAllAttributeNames().stream().sorted().toList()) {
            builder.suggest(attribute);
        }

        return builder.buildFuture();
    }
}
