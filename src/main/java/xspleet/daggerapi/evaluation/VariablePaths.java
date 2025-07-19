package xspleet.daggerapi.evaluation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import xspleet.daggerapi.api.logging.DaggerLogger;
import xspleet.daggerapi.api.logging.LoggingContext;
import xspleet.daggerapi.exceptions.NoSuchVariablePathException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class VariablePaths
{
    private static final Map<String, VariablePath<?, ?>> paths = new HashMap<>();

    public static void registerVariablePaths()
    {
        DaggerLogger.info(LoggingContext.STARTUP, "Registering variable paths...");
    }

    public static VariablePath<Entity, Double> HEALTH = register("health", entity -> {
        if (entity instanceof LivingEntity livingEntity) {
            return (double) livingEntity.getHealth();
        }
        return 0.0;
    }, Entity.class, Double.class);

    public static VariablePath<Entity, Double> MAX_HEALTH = register("max_health", entity -> {
        if (entity instanceof LivingEntity livingEntity) {
            return (double) livingEntity.getMaxHealth();
        }
        return 0.0;
    }, Entity.class, Double.class);

    public static <T, U> VariablePath<T, U> getPath(String path, Class<T> type, Class<U> returnType) throws NoSuchVariablePathException {
        VariablePath<?, ?> variablePath = paths.get(path);
        if(variablePath == null || !variablePath.getType().isAssignableFrom(type) || !variablePath.getReturnType().isAssignableFrom(returnType)) {
            throw new NoSuchVariablePathException(path);
        }
        return (VariablePath<T, U>) variablePath;
    }

    private static <T, U> VariablePath<T, U> register(String path, Function<T, U> function, Class<T> type, Class<U> returnType)
    {
        VariablePath<T, U> variablePath = new VariablePath<>(function, type, returnType);
        paths.put(path, variablePath);
        return variablePath;
    }
}
