package xspleet.daggerapi.trigger.actions;

public class WeightedConditionalAction extends ConditionalAction
{
    private final int weight;

    public WeightedConditionalAction(int weight)
    {
        this.weight = weight;
    }

    public int getWeight()
    {
        return weight;
    }
}
