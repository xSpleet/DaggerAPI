package xspleet.daggerapi.models;

import java.util.ArrayList;

public class CombatModifierModel extends JSONModel
{
    private ArrayList<String> attackTypeModifications = new ArrayList<>();
    private String attackDamageModificationType;
    private int attackDamageModificationValue;

    public ArrayList<String> getAttackTypeModifications() {
        return attackTypeModifications;
    }

    public void setAttackTypeModifications(ArrayList<String> attackTypeModifications) {
        this.attackTypeModifications = attackTypeModifications;
    }

    public String getAttackDamageModificationType() {
        return attackDamageModificationType;
    }

    public void setAttackDamageModificationType(String attackDamageModificationType) {
        this.attackDamageModificationType = attackDamageModificationType;
    }

    public int getAttackDamageModificationValue() {
        return attackDamageModificationValue;
    }

    public void setAttackDamageModificationValue(int attackDamageModificationValue) {
        this.attackDamageModificationValue = attackDamageModificationValue;
    }
}
