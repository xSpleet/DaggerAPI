package xspleet.daggerapi.base;

import com.fathzer.soft.javaluator.*;
import com.google.gson.JsonElement;
import xspleet.daggerapi.data.DaggerContext;
import xspleet.daggerapi.data.key.DaggerKey;
import xspleet.daggerapi.data.key.DaggerKeys;
import xspleet.daggerapi.exceptions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DoubleExpression
{
    private List<DaggerKey<Double>> keys = new ArrayList<>();
    private String expression;

    private static final DoubleEvaluator evaluator = new DoubleEvaluator();

    public void addKey(String key) throws ParseException {
        var daggerKey = DaggerKeys.get(key);
        if(daggerKey == null) {
            throw new ParseException("Key '" + key + "' does not exist");
        }
        if(daggerKey.type() != Double.class) {
            throw new ParseException("Key '" + key + "' is not of type Double");
        }
        keys.add((DaggerKey<Double>) daggerKey);
    }

    public static DoubleExpression create(JsonElement jsonElement) throws ExpressionParseException {
        var doubleExpression = new DoubleExpression();
        List<ParseException> badKeys = new ArrayList<>();

        if(jsonElement == null || !jsonElement.isJsonPrimitive() || !jsonElement.getAsJsonPrimitive().isString()) {
            throw new ExpressionParseException("Expected an expression string");
        }

        var expression = jsonElement.getAsString();

        Set<String> functions = evaluator.getFunctions().stream()
                .map(Function::getName)
                .collect(Collectors.toSet());

        Set<String> constants = evaluator.getConstants().stream()
                .map(Constant::getName)
                .collect(Collectors.toSet());

        Set<String> operators = evaluator.getOperators().stream()
                .map(Operator::getSymbol)
                .collect(Collectors.toSet());

        Pattern pattern = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*");
        Matcher matcher = pattern.matcher(expression);

        Set<String> variables = new HashSet<>();

        while (matcher.find()) {
            String token = matcher.group();
            if (!functions.contains(token) &&
                    !constants.contains(token) &&
                    !operators.contains(token)) {
                variables.add(token);
            }
        }

        for (String variable : variables) {
            try {
                doubleExpression.addKey(variable);
            } catch (ParseException e) {
                badKeys.add(e);
            }
        }

        if(!badKeys.isEmpty()) {
            throw new ExpressionParseException(
                    badKeys.stream().map(ParseException::getMessage)
                            .collect(Collectors.toList())
            );
        }

        try {
            var testSet = new StaticVariableSet<Double>();
            for (DaggerKey<Double> key : doubleExpression.keys)
                testSet.set(key.key(), 1000.0);
            evaluator.evaluate(expression, testSet);
        } catch (IllegalArgumentException e) {
            throw new ExpressionParseException("Invalid expression: " + e.getMessage());
        } catch (ArithmeticException ignored) {
        } catch (Exception e) {
            DaggerLogger.warn("Unexpected exception during expression validation", e);
        }

        doubleExpression.expression = expression;
        return doubleExpression;
    }

    public Double evaluate(DaggerContext data)
    {
        var set = new StaticVariableSet<Double>();
        for (DaggerKey<Double> key : keys) {
            Double value = data.getData(key);
            set.set(key.key(), value);
        }
        return evaluator.evaluate(expression, set);
    }
}
