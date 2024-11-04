package xspleet.daggerapi.models;

import java.util.ArrayList;

public class EventModel
{
    public String trigger;
    public Integer weight;
    public ArrayList<ConditionModel> conditions = new ArrayList<>();
    public ArrayList<ActionModel> actions = new ArrayList<>();
}
