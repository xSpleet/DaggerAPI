package xspleet.daggerapi.collections.registration;

import com.google.gson.JsonElement;
import xspleet.daggerapi.data.ProviderData;
import xspleet.daggerapi.data.key.DaggerKey;
import xspleet.daggerapi.exceptions.BadArgumentException;
import xspleet.daggerapi.exceptions.ParseException;

public record ProviderArgument<T>(DaggerKey<T> key, Parser<T> parser)
{
    public void addData(ProviderData data, JsonElement value) throws BadArgumentException {
        try {
            data.addData(key, parser.parse(value));
        }
        catch (Exception e) {
            throw new BadArgumentException("Failed to get argument '" + key.key() + "' with value " + value.getAsString() + ": " + e.getMessage());
        }
    }

    public static <T> ProviderArgument<T> of(DaggerKey<T> key, Parser<T> parser)
    {
        return new ProviderArgument<>(key, parser);
    }
}
