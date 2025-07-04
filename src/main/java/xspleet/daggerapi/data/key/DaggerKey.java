package xspleet.daggerapi.data.key;

public record DaggerKey<T>(String key, Class<T> type) {

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
}
