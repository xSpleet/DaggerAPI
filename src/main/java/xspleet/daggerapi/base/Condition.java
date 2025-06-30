package xspleet.daggerapi.base;

import org.jetbrains.annotations.NotNull;
import xspleet.daggerapi.data.ConditionData;

import java.util.function.Predicate;

public interface Condition extends Predicate<ConditionData> {
    @NotNull
    @Override
    default Condition and(@NotNull Predicate<? super ConditionData> other) {
        return data -> other.test(data) && test(data);
    }

    @NotNull
    @Override
    default Condition negate() {
        return data -> !test(data);
    }

    @NotNull
    @Override
    default Condition or(@NotNull Predicate<? super ConditionData> other) {
        return data -> other.test(data) || test(data);
    }
}
