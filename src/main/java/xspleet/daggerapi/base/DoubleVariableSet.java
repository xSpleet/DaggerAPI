package xspleet.daggerapi.base;

import com.fathzer.soft.javaluator.*;
import com.google.gson.JsonArray;
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

public class DoubleVariableSet
{
    public List<DaggerKey<Double>> keys = new ArrayList<>();

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

    public List<DaggerKey<Double>> getKeys() {
        return keys;
    }

    public static DoubleVariableSet create(JsonElement jsonElement) throws BadArgumentsException, ParseException {
        if(!jsonElement.isJsonArray()) {
            throw new IllegalArgumentException("Expected a JSON array");
        }
        var jsonArray = jsonElement.getAsJsonArray();
        var doubleVariableSet = new DoubleVariableSet();
        List<ParseException> badKeys = new ArrayList<>();
        for(var element : jsonArray) {
            if(!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isString()) {
                throw new ParseException("Expected a JSON array of strings");
            }
            String key = element.getAsString();
            try {
                doubleVariableSet.addKey(key);
            }
            catch (ParseException e) {
                badKeys.add(e);
            }
        }
        if(!badKeys.isEmpty()) {
            throw new BadArgumentsException(
                    badKeys.stream().map(ParseException::getMessage)
                            .collect(Collectors.toList())
            );
        }
        return doubleVariableSet;
    }

    public StaticVariableSet<Double> asStaticVariableSet(DaggerContext data)
    {
        var set = new StaticVariableSet<Double>();
        for (DaggerKey<Double> key : keys) {
            Double value = data.getData(key);
            set.set(key.key(), value);
        }
        return set;
    }

    public void validate(String expression) {
        List<String> messages = new ArrayList<>();
        Set<String> variableNames = getVariableNames(expression);
        Set<String> keyNames = keys.stream()
                .map(DaggerKey::key)
                .collect(Collectors.toSet());

        if(!variableNames.containsAll(keyNames))
        {
            Set<String> missingKeys = new HashSet<>(keyNames);
            missingKeys.removeAll(variableNames);
            messages.add("Missing keys in expression: " + String.join(", ", missingKeys));
        }
        if(!keyNames.containsAll(variableNames))
        {
            Set<String> extraVariables = new HashSet<>(variableNames);
            extraVariables.removeAll(keyNames);
            messages.add("Extra variables in expression: " + String.join(", ", extraVariables));
        }
        if(!messages.isEmpty())
        {
            throw new BadExpressionException(messages);
        }
    }

    public static Set<String> getVariableNames(String expression) {
        DoubleEvaluator evaluator = new DoubleEvaluator();

        Set<String> functions = evaluator.getFunctions().stream()
                .map(Function::getName)
                .collect(Collectors.toSet());

        Set<String> constants = evaluator.getConstants().stream()
                .map(Constant::getName)
                .collect(Collectors.toSet());

        Set<String> operators = evaluator.getOperators().stream()
                .map(Operator::getSymbol)
                .collect(Collectors.toSet());

        // Match valid variable-like identifiers
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

        return variables;
    }
}
