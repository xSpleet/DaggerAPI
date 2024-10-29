package xspleet.daggerapi.providers;

import xspleet.daggerapi.base.DaggerData;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class ConditionProvider extends Provider<Predicate<DaggerData>>
{
    public ConditionProvider(String name, Function<Map<String, String>, Predicate<DaggerData>> provider) {
        super(name, provider);
    }
    public ConditionProvider addArgument(String argument)
    {
        super.addArgument(argument);
        return this;
    }

}
