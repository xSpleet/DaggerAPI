package xspleet.daggerapi.attributes.operations;

import com.mojang.datafixers.util.Function3;

public enum BooleanOperation implements AttributeOperation<Boolean> {
    TRUE("true", 0, (prev, prevGroup, value) -> true),
    FALSE("false", 10, (prev, prevGroup, value) -> false);

    private final String name;
    private final int precedence;
    private final Function3<Boolean, Boolean, Boolean, Boolean> operation;

    BooleanOperation(String name, int precedence, Function3<Boolean, Boolean, Boolean, Boolean> operation) {
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
    public Function3<Boolean, Boolean, Boolean, Boolean> getOperation() {
        return operation;
    }

    @Override
    public Boolean apply(Boolean prevResult, Boolean resultFromPrevType, Boolean value) {
        return operation.apply(prevResult, resultFromPrevType, value);
    }
}
