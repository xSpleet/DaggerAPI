package xspleet.daggerapi.trigger.actions;

import org.jetbrains.annotations.NotNull;
import xspleet.daggerapi.data.collection.ActionData;

import java.util.function.Consumer;

public interface Action extends Consumer<ActionData>
{
    @Override
    @NotNull
    default Action andThen(@NotNull Consumer<? super ActionData> after) {
        return data -> {
            accept(data);
            after.accept(data);
        };
    }

    default Action repeat(int repeats) {
        return data -> {
            for (int i = 0; i < repeats; i++)
                accept(data);
        };
    }
}
