package xspleet.daggerapi.api.collections;

import com.google.gson.JsonElement;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import xspleet.daggerapi.api.logging.DaggerLogger;
import xspleet.daggerapi.evaluation.DoubleExpression;
import xspleet.daggerapi.api.logging.LoggingContext;
import xspleet.daggerapi.api.registration.Mapper;
import xspleet.daggerapi.api.registration.Provider;
import xspleet.daggerapi.data.key.DaggerKeys;
import xspleet.daggerapi.trigger.actions.Action;

public class ActionProviders
{
    public static void registerActionProviders()
    {
        DaggerLogger.info(LoggingContext.STARTUP, "Registering action providers...");
    }

    public static Provider<Action> DO_NOTHING = Mapper.registerActionProvider("doNothing", (args) -> data -> {});

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

        return data -> data.getActEntity(args.getOn()).sendMessage(Text.literal(message));
    }).addArgument(DaggerKeys.Provider.MESSAGE, JsonElement::getAsString);

    public static Provider<Action> KILL = Mapper.registerActionProvider("kill", (args) -> data -> {
        LivingEntity living = (LivingEntity) data.getActEntity(args.getOn());
        living.kill();
    });

    public static Provider<Action> CHANGE_AMOUNT = Mapper.registerActionProvider("changeAmount", (args) -> {
        var amountExpression = args.getData(DaggerKeys.Provider.AMOUNT);

        return data -> {
            var result = amountExpression.evaluate(data);
            data.addData(DaggerKeys.AMOUNT, result);
        };
    }).addArgument(DaggerKeys.Provider.AMOUNT, DoubleExpression::create)
            .addRequiredData(DaggerKeys.AMOUNT)
            .addAssociatedTrigger(Triggers.BEFORE_DAMAGE)
            .modifier();
}
