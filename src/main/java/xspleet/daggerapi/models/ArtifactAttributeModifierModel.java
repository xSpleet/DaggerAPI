package xspleet.daggerapi.models;

import java.util.ArrayList;

public class ArtifactAttributeModifierModel extends JSONModel
{
    public ArrayList<ConditionModel> conditions = new ArrayList<>();
    public ArrayList<AttributeModifierModel> modifiers = new ArrayList<>();
}
