package xspleet.daggerapi.models;

import java.util.ArrayList;

public class ArtifactCombatModifierModel
{
    public String direction;
    public ArrayList<ConditionModel> conditions = new ArrayList<>();
    public ArrayList<CombatModifierModel> modifiers = new ArrayList<>();
}
