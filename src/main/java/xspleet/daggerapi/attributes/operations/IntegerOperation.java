package xspleet.daggerapi.attributes.operations;

import com.mojang.datafixers.util.Function3;
import xspleet.daggerapi.attributes.base.AttributeOperation;

public enum IntegerOperation implements AttributeOperation<Integer> {
    ADD_NONNEGATIVE("add_nonnegative", 0, (prev, prevGroup, value) -> Math.max(0, prev + value)),
    ADD("add", 10, (prev, prevGroup, value) -> prev + value),
    MULTIPLY_BASE("multiply_base", 20, (prev, prevGroup, value) -> prev + prevGroup * value),
    MULTIPLY_TOTAL("multiply_total", 30, (prev, prevGroup, value) -> prev * value);

    private final String name;
    private final int precedence;
    private final Function3<Integer, Integer, Integer, Integer> operation;

    IntegerOperation(String name, int precedence, Function3<Integer, Integer, Integer, Integer> operation) {
        this.name = name;
        this.precedence = precedence;
        this.operation = operation;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPrecedence() {
        return precedence;
    }

    @Override
    public Function3<Integer, Integer, Integer, Integer> getOperation() {
        return operation;
    }

    @Override
    public Integer apply(Integer prevResult, Integer resultFromPrevType, Integer value) {
        return operation.apply(prevResult, resultFromPrevType, value);
    }
}
