package xspleet.daggerapi.api.models;

import java.util.ArrayList;

public class ArtifactAttributeModifierModel extends JSONModel
{
    private ArrayList<ConditionModel> conditions = new ArrayList<>();
    private ArrayList<AttributeModifierModel> modifiers = new ArrayList<>();

    public ArrayList<ConditionModel> getConditions() {
        return conditions;
    }

    public ArrayList<AttributeModifierModel> getModifiers() {
        return modifiers;
    }

}
