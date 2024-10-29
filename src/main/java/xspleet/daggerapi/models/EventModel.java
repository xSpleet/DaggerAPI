package xspleet.daggerapi.models;

import java.util.ArrayList;

public class EventModel
{
    public String trigger;
    public int weight;
    public ArrayList<ConditionModel> conditions;
    public ArrayList<ActionModel> actions;
}
