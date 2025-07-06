package xspleet.daggerapi.attributes.operations;

import xspleet.daggerapi.attributes.operations.DaggerOperation;

import java.util.function.BiFunction;

public enum BooleanOperation implements DaggerOperation<Boolean> {
    OR,
    AND,
    XOR,
    FALSE,
    TRUE;
}
