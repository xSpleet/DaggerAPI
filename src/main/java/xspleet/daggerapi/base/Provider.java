package xspleet.daggerapi.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Provider<T>
{
    protected String name;
    protected List<String> arguments = new ArrayList<>();
    protected List<String> associatedTriggers = new ArrayList<>();
    protected Function<Map<String, String>, T> provider;

    protected boolean isPlayerDependant = false;
    protected boolean isWorldDependant  = false;

    public Provider(String name, Function<Map<String, String>, T> provider)
    {
        this.provider = provider;
        this.name = name;
    }

    public Provider<T> addArgument(String argument)
    {
        arguments.add(argument);
        return this;
    }

    public Provider<T> addAssociatedTrigger(String trigger)
    {
        associatedTriggers.add(trigger);
        return this;
    }

    public String getName()
    {
        return name;
    }

    public T provide(Map<String, String> args)
    {
        return provider.apply(args);
    }

    private boolean checkArgs(Map<String, String> args)
    {
        for(String argument: arguments)
        {
            if(!args.containsKey(argument))
            {
                return false;
            }
        }

        if(associatedTriggers.isEmpty())
            return true;

        return args.containsKey("trigger") && associatedTriggers.contains(args.get("trigger"));
    }


}