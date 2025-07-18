package xspleet.daggerapi.collections.registration;

import xspleet.daggerapi.data.key.DaggerKey;

import java.util.List;
import java.util.Set;

public interface ComplexDataEntry {
    public Set<DaggerKey<?>> getRequiredData();
}
