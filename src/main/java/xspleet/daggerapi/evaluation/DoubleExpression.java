package xspleet.daggerapi.evaluation;

import com.fathzer.soft.javaluator.*;
import com.google.gson.JsonElement;
import xspleet.daggerapi.api.collections.VariablePathTemplates;
import xspleet.daggerapi.api.logging.DaggerLogger;
import xspleet.daggerapi.api.logging.LoggingContext;
import xspleet.daggerapi.data.ComplexDataEntry;
import xspleet.daggerapi.data.collection.DaggerContext;
import xspleet.daggerapi.data.key.DaggerKey;
import xspleet.daggerapi.api.collections.DaggerKeys;
import xspleet.daggerapi.exceptions.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DoubleExpression implements ComplexDataEntry
{
    private final Set<DaggerKey<Double>> expressionVariables = new HashSet<>();
    private final Set<DaggerKey<?>> neededKeys = new HashSet<>();
    private final Map<String, VariablePath<Object, Double>> variablePaths = new HashMap<>();
    private String expression;

    private static final DoubleEvaluator evaluator = new DoubleEvaluator();

    public void addVariableKey(String key) throws ParseException {
        var daggerKey = DaggerKeys.get(key);
        if(daggerKey == null) {
            throw new ParseException("Key '" + key + "' does not exist");
        }
        if(daggerKey.type() != Double.class) {
            throw new ParseException("Key '" + key + "' is not of type Double");
        }
        expressionVariables.add((DaggerKey<Double>) daggerKey);
        neededKeys.add(daggerKey);
    }

    public void addVariablePath(String fullPath) throws ParseException {
        if(!fullPath.contains("#")) {
            throw new ParseException("Variable path must contain a '#' to separate key and path");
        }
        if(variablePaths.containsKey(fullPath)) {
            return;
        }
        String[] parts = fullPath.split("#", 2);
        var path = parts[1];
        var key = parts[0];
        VariablePath<Object, Double> variablePath;
        var daggerKey = DaggerKeys.get(key);
        if(daggerKey == null) {
            throw new ParseException("Key '" + key + "' does not exist");
        }
        try {
            variablePath = (VariablePath<Object, Double>) VariablePathTemplates.getPathTemplate(path, daggerKey.type(), Double.class).getPath();
        }
        catch (NoSuchVariablePathException e) {
            throw new ParseException("Variable path '" + path + "' of type double does not exist");
        }
        if(variablePath.getReturnType() != Double.class) {
            throw new ParseException("Variable path return type must be Double");
        }
        if(!variablePath.getType().isAssignableFrom(daggerKey.type())) {
            throw new ParseException("Variable path applied to illegal type: " + daggerKey.type().getSimpleName());
        }
        variablePath.setKey((DaggerKey<Object>) daggerKey);
        variablePaths.put(fullPath, variablePath);
        neededKeys.add(daggerKey);
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

        Pattern pattern = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_#]*");
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
            if(variable.contains("#"))
            {
                try {
                    doubleExpression.addVariablePath(variable);
                } catch (ParseException e) {
                    badKeys.add(e);
                }
            } else {
                try {
                    doubleExpression.addVariableKey(variable);
                } catch (ParseException e) {
                    badKeys.add(e);
                }
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
            for (String variable : variables)
                testSet.set(variable, 1000.0);
            evaluator.evaluate(expression, testSet);
        } catch (IllegalArgumentException e) {
            throw new ExpressionParseException("Invalid expression: " + e.getMessage());
        } catch (ArithmeticException ignored) {
        } catch (Exception e) {
            DaggerLogger.warn(LoggingContext.PARSING, "Unexpected exception during expression validation: {}", e.getMessage());
        }

        doubleExpression.expression = expression;
        return doubleExpression;
    }

    public Double evaluate(DaggerContext data)
    {
        var set = new StaticVariableSet<Double>();
        for (DaggerKey<Double> key : expressionVariables) {
            Double value = data.getData(key);
            set.set(key.key(), value);
        }
        for(var variablePath : variablePaths.entrySet()) {
            set.set(variablePath.getKey(), variablePath.getValue().get(data));
        }
        return evaluator.evaluate(expression, set);
    }

    @Override
    public Set<DaggerKey<?>> getRequiredData() {
        return neededKeys;
    }
}
