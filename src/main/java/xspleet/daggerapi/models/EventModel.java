package xspleet.daggerapi.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EventModel extends JSONModel
{
    private String trigger;
    private Integer weight;

    @SerializedName("triggerer")
    private TriggeredBy triggeredBy = null;

    @SerializedName("in-world")
    private TriggeredIn triggeredIn = null;

    private ArrayList<ConditionModel> conditions = new ArrayList<>();
    private ArrayList<ActionModel> actions = new ArrayList<>();

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public TriggeredBy getTriggeredBy() {
        return triggeredBy;
    }

    public void setTriggeredBy(TriggeredBy triggeredBy) {
        this.triggeredBy = triggeredBy;
    }

    public TriggeredIn getTriggeredIn() {
        return triggeredIn;
    }

    public void setTriggeredIn(TriggeredIn triggeredIn) {
        this.triggeredIn = triggeredIn;
    }

    public ArrayList<ConditionModel> getConditions() {
        return conditions;
    }

    public void setConditions(ArrayList<ConditionModel> conditions) {
        this.conditions = conditions;
    }

    public ArrayList<ActionModel> getActions() {
        return actions;
    }

    public void setActions(ArrayList<ActionModel> actions) {
        this.actions = actions;
    }
}
