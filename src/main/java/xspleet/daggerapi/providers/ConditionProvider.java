package xspleet.daggerapi.providers;

import xspleet.daggerapi.base.Condition;
import xspleet.daggerapi.base.DaggerData;
import java.util.Date;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class ConditionProvider extends Provider<Condition>
{
    public ConditionProvider(String name, Function<Map<String, String>, Condition> provider) {
        super(name, provider);
    }
    public ConditionProvider addArgument(String argument)
    {
        super.addArgument(argument);
        return this;
    }

}
