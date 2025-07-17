package xspleet.daggerapi.collections.registration;

import xspleet.daggerapi.data.key.DaggerKey;

import java.util.List;

public interface ComplexDataEntry {
    public List<DaggerKey<Double>> getRequiredData();
}
