package xspleet.daggerapi.models;

import java.util.ArrayList;

public class ArtifactCombatModifierModel extends JSONModel
{
    private String direction;
    private ArrayList<ConditionModel> conditions = new ArrayList<>();
    private ArrayList<CombatModifierModel> modifiers = new ArrayList<>();

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public ArrayList<ConditionModel> getConditions() {
        return conditions;
    }

    public void setConditions(ArrayList<ConditionModel> conditions) {
        this.conditions = conditions;
    }

    public ArrayList<CombatModifierModel> getModifiers() {
        return modifiers;
    }

    public void setModifiers(ArrayList<CombatModifierModel> modifiers) {
        this.modifiers = modifiers;
    }
}
