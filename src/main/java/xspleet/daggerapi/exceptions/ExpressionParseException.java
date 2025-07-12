package xspleet.daggerapi.exceptions;

import java.util.List;

public class ExpressionParseException extends DaggerAPIException
{
    private final String message;

    public ExpressionParseException(String message) {
        this.message = message;
    }

    public ExpressionParseException(List<String> messages) {
        this.message = String.join(",\n", messages);
    }

    @Override
    public String getMessage() {
        return "Expression parsing failed:\n" + message;
    }
}
