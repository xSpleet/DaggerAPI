package xspleet.daggerapi.attributes.operations;

import xspleet.daggerapi.attributes.operations.DaggerOperation;

import java.util.function.BiFunction;

public enum DoubleOperation implements DaggerOperation<Double>
{
    ADD,
    MULTIPLY_BASE,
    MULTIPLY_TOTAL;
}
