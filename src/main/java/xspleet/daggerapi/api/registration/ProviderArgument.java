package xspleet.daggerapi.api.registration;

import com.google.gson.JsonElement;
import xspleet.daggerapi.data.collection.ProviderData;
import xspleet.daggerapi.data.key.DaggerKey;
import xspleet.daggerapi.exceptions.BadArgumentException;

public record ProviderArgument<T>(DaggerKey<T> key, Parser<T> parser)
{
    public T addData(ProviderData data, JsonElement value) throws BadArgumentException {
        try {
            data.addData(key, parser.parse(value));
            return data.getData(key);
        }
        catch (Exception e) {
            throw new BadArgumentException("Failed to get argument '" + key.key() + "' with value " + value.getAsString() + ": " + e.getMessage());
        }
    }
}
