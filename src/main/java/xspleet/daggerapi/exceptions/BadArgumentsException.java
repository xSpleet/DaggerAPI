package xspleet.daggerapi.exceptions;

import net.minecraft.world.event.GameEvent;

import java.util.List;

public class BadArgumentsException extends DaggerAPIException
{
    private final String message;

    public BadArgumentsException(List<String> messages) {
        this.message = String.join(", ", messages);
    }

    @Override
    public String getMessage() {
        return "Bad arguments: " + message;
    }
}
