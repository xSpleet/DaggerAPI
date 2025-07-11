package xspleet.daggerapi.exceptions;

import net.minecraft.world.event.GameEvent;

import java.util.List;
import java.util.stream.Collectors;

public class BadArgumentsException extends DaggerAPIException
{
    private final String message;

    public BadArgumentsException(List<String> messages) {
        this.message = String.join(", ", messages.stream().map(m -> m + "\n").collect(Collectors.toSet()));
    }

    @Override
    public String getMessage() {
        return "Bad arguments: \n" + message;
    }
}
