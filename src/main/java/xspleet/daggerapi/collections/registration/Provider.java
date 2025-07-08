package xspleet.daggerapi.collections.registration;

import xspleet.daggerapi.data.ProviderData;
import xspleet.daggerapi.trigger.Trigger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Provider<T>
{
    protected final String name;
    protected final List<String> arguments = new ArrayList<>();
    protected final List<String> associatedTriggers = new ArrayList<>();
    protected final Function<ProviderData, T> provider;

    protected boolean isPlayerDependant = false;
    protected boolean isWorldDependant  = false;

    public Provider(String name, Function<ProviderData, T> provider)
    {
        this.provider = provider;
        this.name = name;
    }

    public Provider<T> addArgument(String argument)
    {
        arguments.add(argument);
        return this;
    }

    public Provider<T> addAssociatedTrigger(Trigger trigger)
    {
        associatedTriggers.add(trigger.getName());
        return this;
    }

    public String getName()
    {
        return name;
    }

    public T provide(ProviderData data)
    {
        return provider.apply(data);
    }

    private boolean checkArgs(ProviderData data)
    {
        for(String argument: arguments)
        {
            if(data.getData(argument) == null)
            {
                return false;
            }
        }

        if(associatedTriggers.isEmpty())
            return true;

        return data.getData("trigger") != null && associatedTriggers.contains(data.getData("trigger"));
    }


}