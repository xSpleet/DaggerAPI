package xspleet.daggerapi.attributes.operations;

import com.mojang.datafixers.util.Function3;

public enum DoubleOperation implements AttributeOperation<Double>
{
    ADD("addition", 0, (prev, prevGroup, value) -> prev + value),
    MULTIPLY_BASE("multiply_base", 30, (prev, prevGroup, value) -> prev + prevGroup * value),
    MULTIPLY_TOTAL("multiply_total", 40, (prev, prevGroup, value) -> prev * value),;

    private final String name;
    private final int precedence;
    private final Function3<Double, Double, Double, Double> operation;

    DoubleOperation(String name, int precedence, Function3<Double, Double, Double, Double> operation) {
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
    public Function3<Double, Double, Double, Double> getOperation() {
        return operation;
    }

    @Override
    public Double apply(Double prevResult, Double resultFromPrevType, Double value) {
        return operation.apply(prevResult, resultFromPrevType, value);
    }

    @Override
    public Class<Double> getType() {
        return Double.class;
    }
}
