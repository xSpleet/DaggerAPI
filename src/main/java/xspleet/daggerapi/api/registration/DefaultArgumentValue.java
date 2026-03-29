package xspleet.daggerapi.api.registration;

import com.google.gson.JsonElement;
import xspleet.daggerapi.data.collection.ProviderData;
import xspleet.daggerapi.data.key.DaggerKey;
import xspleet.daggerapi.exceptions.BadArgumentException;

public record DefaultArgumentValue<T>(DaggerKey<T> key, T value) {
    public T addData(ProviderData data) {
        data.addData(key, value);
        return value;
    }
}
