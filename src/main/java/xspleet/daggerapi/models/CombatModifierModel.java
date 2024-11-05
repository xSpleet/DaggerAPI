package xspleet.daggerapi.models;

import java.util.ArrayList;

public class CombatModifierModel extends JSONModel
{
    public ArrayList<String> attackTypeModifications = new ArrayList<>();
    public String attackDamageModificationType;
    public int attackDamageModificationValue;
}
