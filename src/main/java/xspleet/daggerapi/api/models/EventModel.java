package xspleet.daggerapi.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EventModel extends JSONModel
{
    private String trigger;
    private Integer weight;

    @SerializedName("source")
    private TriggeredBy triggeredBy = null;

    @SerializedName("inWorld")
    private TriggeredIn triggeredIn = null;

    private ArrayList<ConditionModel> conditions = new ArrayList<>();
    private ArrayList<ActionModel> actions = new ArrayList<>();

    public String getTrigger() {
        return trigger;
    }

    public Integer getWeight() {
        return weight;
    }

    public TriggeredBy getTriggeredBy() {
        return triggeredBy;
    }

    public TriggeredIn getTriggeredIn() {
        return triggeredIn;
    }

    public ArrayList<ConditionModel> getConditions() {
        return conditions;
    }

    public ArrayList<ActionModel> getActions() {
        return actions;
    }

}
