package xspleet.daggerapi.collections;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.StaticVariableSet;
import com.google.gson.JsonElement;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import xspleet.daggerapi.DaggerAPI;
import xspleet.daggerapi.collections.registration.Mapper;
import xspleet.daggerapi.collections.registration.Provider;
import xspleet.daggerapi.data.key.DaggerKeys;
import xspleet.daggerapi.trigger.actions.Action;

public class ActionProviders
{
    public static void registerActionProviders()
    {
        DaggerAPI.LOGGER.info("> Registering action providers...");
    }

    public static Provider<Action> DO_NOTHING = Mapper.registerActionProvider("doNothing", (args) -> {
        return data -> {};
    });

    public static Provider<Action> HEAL = Mapper.registerActionProvider("heal", (args) -> {
        var amountExpression = args.getData(DaggerKeys.Provider.AMOUNT);
        DoubleEvaluator evaluator = new DoubleEvaluator();
        StaticVariableSet<Double> variables = new StaticVariableSet<>();

        return data -> {
            LivingEntity living = (LivingEntity) data.getActEntity(args.getOn());
            variables.set("amount", data.getData(DaggerKeys.AMOUNT).doubleValue());
            var result = evaluator.evaluate(amountExpression, variables);
            living.heal(result.floatValue());
        };
    }).addArgument(DaggerKeys.Provider.AMOUNT, JsonElement::getAsString);

    public static Provider<Action> SEND_MESSAGE = Mapper.registerActionProvider("sendMessage", (args) -> {
        String message = args.getData(DaggerKeys.Provider.MESSAGE);

        return data -> {
            data.getActEntity(args.getOn()).sendMessage(Text.literal(message));
        };
    }).addArgument(DaggerKeys.Provider.MESSAGE, JsonElement::getAsString);

    public static Provider<Action> KILL = Mapper.registerActionProvider("kill", (args) -> {
        return data -> {
            LivingEntity living = (LivingEntity) data.getActEntity(args.getOn());
            living.kill();
        };
    });

    public static Provider<Action> CHANGE_AMOUNT = Mapper.registerActionProvider("changeAmount", (args) -> {
        var amountExpression = args.getData(DaggerKeys.Provider.AMOUNT);
        DoubleEvaluator evaluator = new DoubleEvaluator();
        StaticVariableSet<Double> variables = new StaticVariableSet<>();

        return data -> {
            variables.set("amount", data.getData(DaggerKeys.AMOUNT).doubleValue());
            var result = evaluator.evaluate(amountExpression, variables);
            data.addData(DaggerKeys.AMOUNT, result.floatValue());
        };
    }).addArgument(DaggerKeys.Provider.AMOUNT, JsonElement::getAsString)
            .addAssociatedTrigger(Triggers.BEFORE_DAMAGE);
}
