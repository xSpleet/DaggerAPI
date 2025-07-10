package xspleet.daggerapi.collections.registration;

import com.google.gson.JsonElement;

@FunctionalInterface
public interface Parser<T>
{
    public T parse(JsonElement element);
}
