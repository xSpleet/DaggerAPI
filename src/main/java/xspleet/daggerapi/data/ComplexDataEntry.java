package xspleet.daggerapi.data;

import xspleet.daggerapi.data.key.DaggerKey;

import java.util.Set;

public interface ComplexDataEntry {
    Set<DaggerKey<?>> getRequiredData();
}
