package xspleet.daggerapi.data.key;

import java.util.Objects;

public class DaggerKey<T> {
    protected final String key;
    protected final Class<T> type;

    public DaggerKey(String key, Class<T> type) {
        this.key = key;
        this.type = type;
    }

    public T getItem(Object o) {
        if (!type.isInstance(o))
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

    public String key() {
        return key;
    }

    public Class<T> type() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (DaggerKey<?>) obj;
        return Objects.equals(this.key, that.key) &&
                Objects.equals(this.type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, type);
    }

}
