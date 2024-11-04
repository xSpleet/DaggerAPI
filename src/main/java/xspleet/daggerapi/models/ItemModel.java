package xspleet.daggerapi.models;

import java.util.ArrayList;

public class ItemModel
{
    public String name;
    public String rarity;
    public boolean active;
    public int cooldown;
    public EventModel onActivation;
    public ArrayList<ArtifactAttributeModifierModel> attributeModifiers = new ArrayList<>();
    public ArrayList<EventModel> events = new ArrayList<>();
    public boolean modifiesCombat;
    public ArrayList<ArtifactCombatModifierModel> combatModifiers = new ArrayList<>();
}
