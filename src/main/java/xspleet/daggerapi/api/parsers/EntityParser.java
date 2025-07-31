package xspleet.daggerapi.api.parsers;

import com.google.gson.JsonElement;
import com.mojang.brigadier.StringReader;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.EntitySelectorReader;
import net.minecraft.entity.Entity;
import xspleet.daggerapi.api.registration.Parser;
import xspleet.daggerapi.exceptions.BadArgumentsException;
import xspleet.daggerapi.exceptions.ExpressionParseException;
import xspleet.daggerapi.exceptions.ParseException;

public class EntityParser implements Parser<Entity> {
    @Override
    public Entity parse(JsonElement element) throws BadArgumentsException, ParseException, ExpressionParseException {
        var selector = element.getAsString();

        return null;
    }
}
