package xspleet.daggerapi.collections;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.StaticVariableSet;
import com.google.gson.JsonElement;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import xspleet.daggerapi.base.DaggerLogger;
import xspleet.daggerapi.base.DoubleExpression;
import xspleet.daggerapi.collections.registration.Mapper;
import xspleet.daggerapi.collections.registration.Provider;
import xspleet.daggerapi.data.key.DaggerKeys;
import xspleet.daggerapi.trigger.actions.Action;

public class ActionProviders
{
    public static void registerActionProviders()
    {
        DaggerLogger.info("> Registering action providers...");
    }

    public static Provider<Action> DO_NOTHING = Mapper.registerActionProvider("doNothing", (args) -> {
        return data -> {};
    });

    public static Provider<Action> HEAL = Mapper.registerActionProvider("heal", (args) -> {
        var amountExpression = args.getData(DaggerKeys.Provider.AMOUNT);

        return data -> {
            LivingEntity living = (LivingEntity) data.getActEntity(args.getOn());
            var result = amountExpression.evaluate(data);
            living.heal(result.floatValue());
        };
    }).addArgument(DaggerKeys.Provider.AMOUNT, DoubleExpression::create);

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

        return data -> {
            var result = amountExpression.evaluate(data);
            data.addData(DaggerKeys.AMOUNT, result);
        };
    }).addArgument(DaggerKeys.Provider.AMOUNT, DoubleExpression::create)
            .addAssociatedTrigger(Triggers.BEFORE_DAMAGE);
}
