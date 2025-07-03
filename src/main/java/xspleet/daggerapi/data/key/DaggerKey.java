package xspleet.daggerapi.data.key;

import java.lang.reflect.Type;
import java.util.NoSuchElementException;

public class DaggerKey<T>
{
    private final String key;
    private final Class<T> type;

    public DaggerKey(String key, Class<T> type)
    {
        this.key = key;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public Class<T> getType() {
        return type;
    }

    public T getItem(Object o)
    {
        if(!type.isInstance(o))
            throw new IllegalArgumentException();
        return type.cast(o);
    }

    @Override
    public String toString() {
        return "DaggerKey{" +
                "key='" + key + '\'' +
                ", type=" + type +
                '}';
    }
}
