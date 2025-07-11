package xspleet.daggerapi.collections.registration;

import com.google.gson.JsonElement;
import xspleet.daggerapi.data.ProviderData;
import xspleet.daggerapi.data.key.DaggerKey;
import xspleet.daggerapi.exceptions.BadArgumentException;
import xspleet.daggerapi.exceptions.BadArgumentsException;
import xspleet.daggerapi.models.On;
import xspleet.daggerapi.trigger.Trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Provider<T> {
    protected final String name;
    protected final Map<String, ProviderArgument<?>> arguments = new HashMap<>();
    protected final List<Trigger> associatedTriggers = new ArrayList<>();
    protected final Function<ProviderData, T> provider;
    protected boolean isModifier = false;

    public Provider(String name, Function<ProviderData, T> provider) {
        this.provider = provider;
        this.name = name;
    }

    public <U> Provider<T> addArgument(DaggerKey<U> argument, Parser<U> parser) {
        arguments.put(argument.key(), new ProviderArgument<>(argument, parser));
        return this;
    }

    public Provider<T> addAssociatedTrigger(Trigger trigger) {
        associatedTriggers.add(trigger);
        return this;
    }

    public String getName() {
        return name;
    }

    public T provide(On on, Map<String, JsonElement> args) throws BadArgumentsException {
        var data = getArgs(args).setOn(on);
        return provider.apply(data);
    }

    public T provide(ProviderData data) throws BadArgumentsException {
        checkArgs(data);
        return provider.apply(data);
    }

    public void checkArgs(ProviderData data) throws BadArgumentsException {
        List<BadArgumentException> errors = new ArrayList<>();
        for (var argument : arguments.entrySet()) {
            try {
                ProviderArgument<?> providerArgument = argument.getValue();
                var key = providerArgument.key();
                if (!data.hasData(key)) {
                    throw new BadArgumentException("Missing argument: " + key.key());
                }
            } catch (BadArgumentException e) {
                errors.add(e);
            }
        }
        if (!errors.isEmpty())
            throw new BadArgumentsException(
                    errors.stream()
                            .map(BadArgumentException::getMessage)
                            .toList()
            );
    }

    private ProviderData getArgs(Map<String, JsonElement> args) throws BadArgumentsException {
        ProviderData data = new ProviderData();
        List<BadArgumentException> errors = new ArrayList<>();
        for (var argument : arguments.entrySet()) {
            try {
                String name = argument.getKey();
                ProviderArgument<?> providerArgument = argument.getValue();

                if (!args.containsKey(name)) {
                    throw new BadArgumentException("Missing argument: " + name);
                }

                var element = args.get(name);
                providerArgument.addData(data, element);
            } catch (BadArgumentException e) {
                errors.add(e);
            }
        }
        if (!errors.isEmpty())
            throw new BadArgumentsException(
                    errors.stream()
                            .map(BadArgumentException::getMessage)
                            .toList()
            );
        return data;
    }

    public boolean canBeOnTrigger(
            Trigger trigger
    ) {
        return associatedTriggers.isEmpty() || associatedTriggers.stream()
                .anyMatch(t -> t.getName().equals(trigger.getName()));
    }

    public boolean isModifier() {
        return isModifier;
    }

    public Provider<T> modifier() {
        this.isModifier = true;
        return this;
    }
}