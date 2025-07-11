package xspleet.daggerapi.exceptions;

import java.util.List;

public class BadExpressionException extends RuntimeException
{
    private final List<String> messages;

    public BadExpressionException(List<String> messages) {
        this.messages = messages;
    }

    @Override
    public String getMessage() {
        return "Bad expression: " + String.join(", ", messages);
    }
}
