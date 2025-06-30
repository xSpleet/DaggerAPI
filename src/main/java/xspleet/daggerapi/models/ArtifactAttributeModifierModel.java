package xspleet.daggerapi.models;

import java.util.ArrayList;

public class ArtifactAttributeModifierModel extends JSONModel
{
    private ArrayList<ConditionModel> conditions = new ArrayList<>();
    private ArrayList<AttributeModifierModel> modifiers = new ArrayList<>();

    public ArrayList<ConditionModel> getConditions() {
        return conditions;
    }

    public void setConditions(ArrayList<ConditionModel> conditions) {
        this.conditions = conditions;
    }

    public ArrayList<AttributeModifierModel> getModifiers() {
        return modifiers;
    }

    public void setModifiers(ArrayList<AttributeModifierModel> modifiers) {
        this.modifiers = modifiers;
    }
}
