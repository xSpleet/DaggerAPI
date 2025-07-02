package xspleet.daggerapi.data.key;

import java.lang.reflect.Type;
import java.util.NoSuchElementException;

public class DaggerKey<T>
{
    private String key;
    private Class<T> type;

    public DaggerKey(String key, Class<T> type)
    {
        this.key = key;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Class<T> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }

    public T getItem(Object o)
    {
        if(!type.isInstance(o))
            throw new IllegalArgumentException();
        return type.cast(o);
    }
}
