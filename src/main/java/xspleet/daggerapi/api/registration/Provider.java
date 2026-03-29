package xspleet.daggerapi.api.registration;

import com.google.gson.JsonElement;
import xspleet.daggerapi.api.logging.DaggerLogger;
import xspleet.daggerapi.api.logging.LoggingContext;
import xspleet.daggerapi.data.collection.ProviderData;
import xspleet.daggerapi.data.key.DaggerKey;
import xspleet.daggerapi.exceptions.BadArgumentException;
import xspleet.daggerapi.exceptions.BadArgumentsException;
import xspleet.daggerapi.trigger.Trigger;

import java.util.*;
import java.util.function.Function;

public class Provider<T> {
    protected final String name;
    protected final Map<String, ProviderArgument<?>> arguments = new HashMap<>();
    protected final List<Trigger> associatedTriggers = new ArrayList<>();
    protected final Function<ProviderData, T> provider;
    protected final Map<DaggerKey<?>, DefaultArgumentValue<?>> defaultValues = new HashMap<>();
    protected boolean isModifier = false;
    protected final Set<DaggerKey<?>> requiredData = new HashSet<>();

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

    public <U> Provider<T> addDefaultValue(DaggerKey<U> key, U value) {
        defaultValues.put(key, new DefaultArgumentValue<>(key, value));
        return this;
    }

    public String getName() {
        return name;
    }

    public T provide(ProviderData data) throws BadArgumentsException{
        checkArgs(data);
        try {
            return provider.apply(data);
        }
        catch (Exception e) {
            throw new BadArgumentsException(List.of("Failed to provide data: " + e.getMessage()));
        }
    }

    public void checkArgs(ProviderData data) throws BadArgumentsException {
        List<BadArgumentException> errors = new ArrayList<>();
        for (var argument : arguments.entrySet()) {
            try {
                ProviderArgument<?> providerArgument = argument.getValue();
                var key = providerArgument.key();
                if (!data.hasData(key) && !defaultValues.containsKey(key)) {
                    throw new BadArgumentException("Missing argument: " + key.key());
                }
                if (!data.hasData(key) && defaultValues.containsKey(key)) {
                    DaggerLogger.warn(LoggingContext.GENERIC, "Using default value for argument '" + key.key() + "': " + defaultValues.get(key));
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

    public ProviderData readArgs(Map<String, JsonElement> args) throws BadArgumentsException {
        ProviderData data = new ProviderData();
        List<BadArgumentException> errors = new ArrayList<>();
        for (var argument : arguments.entrySet()) {
            try {
                String name = argument.getKey();
                ProviderArgument<?> providerArgument = argument.getValue();
                var key = providerArgument.key();

                if (args.containsKey(name)) {
                    providerArgument.addData(data, args.get(name));
                }
                else if (defaultValues.containsKey(key)) {
                    DaggerLogger.warn(LoggingContext.GENERIC, "Using default value for argument '" + key.key() + "': " + defaultValues.get(key));
                    var defaultValue = defaultValues.get(key);
                    defaultValue.addData(data);
                }
                else {
                    throw new BadArgumentException("Missing argument: " + name);
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
        return data;
    }

    public boolean canBeOnTrigger(
            Trigger trigger
    ) {
        return associatedTriggers.isEmpty() || associatedTriggers.stream()
                .anyMatch(t -> t.getName().equals(trigger.getName()));
    }

    public Provider<T> addRequiredData(DaggerKey<?> key) {
        requiredData.add(key);
        return this;
    }

    public boolean isModifier() {
        return isModifier;
    }

    public Provider<T> modifier() {
        this.isModifier = true;
        return this;
    }

    public Set<DaggerKey<?>> getRequiredData() {
        return new HashSet<>(requiredData);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var other = (Provider<?>) o;
        return Objects.equals(name, other.name) && Objects.equals(provider, other.provider);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, provider);
    }
}