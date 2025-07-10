package xspleet.daggerapi.collections.registration;

import com.google.gson.JsonElement;
import xspleet.daggerapi.data.ProviderData;
import xspleet.daggerapi.data.key.DaggerKey;

public record ProviderArgument<T>(DaggerKey<T> key, Parser<T> parser)
{
    public void addData(ProviderData data, JsonElement value)
    {
        data.addData(key, parser.parse(value));
    }

    public static <T> ProviderArgument<T> of(DaggerKey<T> key, Parser<T> parser)
    {
        return new ProviderArgument<>(key, parser);
    }
}
