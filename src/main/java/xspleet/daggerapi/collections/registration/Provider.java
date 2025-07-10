package xspleet.daggerapi.collections.registration;

import com.google.gson.JsonElement;
import xspleet.daggerapi.data.ProviderData;
import xspleet.daggerapi.data.key.DaggerKey;
import xspleet.daggerapi.exceptions.MissingArgumentException;
import xspleet.daggerapi.models.On;
import xspleet.daggerapi.trigger.Trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Provider<T>
{
    protected final String name;
    protected final Map<String, ProviderArgument<?>> arguments = new HashMap<>();
    protected final List<Trigger> associatedTriggers = new ArrayList<>();
    protected final Function<ProviderData, T> provider;

    public Provider(String name, Function<ProviderData, T> provider)
    {
        this.provider = provider;
        this.name = name;
    }

    public <U> Provider<T> addArgument(DaggerKey<U> argument, Parser<U> parser)
    {
        arguments.put(argument.key(), new ProviderArgument<>(argument, parser));
        return this;
    }

    public Provider<T> addAssociatedTrigger(Trigger trigger)
    {
        associatedTriggers.add(trigger);
        return this;
    }

    public String getName()
    {
        return name;
    }

    public T provide(On on, Map<String, JsonElement> args) throws MissingArgumentException {
        var data = getArgs(args).setOn(on);
        return provider.apply(data);
    }

    private ProviderData getArgs(Map<String, JsonElement> args) throws MissingArgumentException {
        ProviderData data = new ProviderData();
        List<String> missingArguments = new ArrayList<>();
        for(var argument: arguments.entrySet())
        {
            String name = argument.getKey();
            ProviderArgument<?> providerArgument = argument.getValue();

            if(!args.containsKey(name))
            {
                missingArguments.add(name);
                continue;
            }

            var element = args.get(name);
            providerArgument.addData(data, element);
        }
        if(!missingArguments.isEmpty())
            throw new MissingArgumentException(missingArguments);
        return data;
    }

    public boolean canBeOnTrigger(
        Trigger trigger
    ) {
        return associatedTriggers.isEmpty() || associatedTriggers.stream()
                .anyMatch(t -> t.getName().equals(trigger.getName()));
    }
}