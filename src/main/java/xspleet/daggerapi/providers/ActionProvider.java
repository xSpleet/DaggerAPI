package xspleet.daggerapi.providers;

import net.minecraft.entity.player.PlayerEntity;
import xspleet.daggerapi.base.DaggerData;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class ActionProvider extends Provider<Consumer<DaggerData>>
{
    public ActionProvider(String name, Function<Map<String, String>, Consumer<DaggerData>> provider) {
        super(name, provider);
    }

    public ActionProvider addArgument(String argument)
    {
        super.addArgument(argument);
        return this;
    }
}
