package xspleet.daggerapi.data;

import xspleet.daggerapi.data.key.DaggerKey;
import xspleet.daggerapi.exceptions.MissingDataException;

import java.util.HashMap;

public class DaggerData implements DaggerContext
{
    protected final ThreadLocal<HashMap<DaggerKey<?>, Object>> data = ThreadLocal.withInitial(HashMap::new);

    @Override
    public <T> DaggerData addData(DaggerKey<T> key, T value) {
        data.get().put(key, value);
        return this;
    }

    @Override
    public <T> T getData(DaggerKey<T> key) {
        if(!data.get().containsKey(key)) {
            throw new MissingDataException(key);
        }
        return key.getItem(data.get().get(key));
    }

    @Override
    public boolean hasData(DaggerKey<?> key) {
        return data.get().containsKey(key);
    }
}
