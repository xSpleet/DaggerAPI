package xspleet.daggerapi.data.collection;

import xspleet.daggerapi.data.key.DaggerKey;

public class TriggerData implements DaggerContext {
    private final DaggerContext data;

    public TriggerData() {
        this.data = new DaggerData();
    }

    @Override
    public <T> TriggerData addData(DaggerKey<T> key, T value) {
        data.addData(key, value);
        return this;
    }

    @Override
    public <T> T getData(DaggerKey<T> key) {
        return data.getData(key);
    }

    @Override
    public boolean hasData(DaggerKey<?> key) {
        return data.hasData(key);
    }
}
