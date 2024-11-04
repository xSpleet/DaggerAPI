package xspleet.daggerapi.base;

import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public interface Condition extends Predicate<DaggerData> {
    @NotNull
    @Override
    default Condition and(@NotNull Predicate<? super DaggerData> other) {
        return data -> other.test(data) && test(data);
    }

    @NotNull
    @Override
    default Condition negate() {
        return data -> !test(data);
    }

    @NotNull
    @Override
    default Condition or(@NotNull Predicate<? super DaggerData> other) {
        return data -> other.test(data) || test(data);
    }
}
