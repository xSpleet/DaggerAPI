package xspleet.daggerapi.models;

import java.util.ArrayList;

public class ItemModel extends JSONModel
{
    private String name;
    private String rarity;
    private boolean active;
    private int cooldown;
    private EventModel onActivation;
    private ArrayList<ArtifactAttributeModifierModel> attributeModifiers = new ArrayList<>();
    private ArrayList<EventModel> events = new ArrayList<>();
    private boolean modifiesCombat;
    private ArrayList<ArtifactCombatModifierModel> combatModifiers = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public EventModel getOnActivation() {
        return onActivation;
    }

    public void setOnActivation(EventModel onActivation) {
        this.onActivation = onActivation;
    }

    public ArrayList<ArtifactAttributeModifierModel> getAttributeModifiers() {
        return attributeModifiers;
    }

    public void setAttributeModifiers(ArrayList<ArtifactAttributeModifierModel> attributeModifiers) {
        this.attributeModifiers = attributeModifiers;
    }

    public ArrayList<EventModel> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<EventModel> events) {
        this.events = events;
    }

    public boolean isModifiesCombat() {
        return modifiesCombat;
    }

    public void setModifiesCombat(boolean modifiesCombat) {
        this.modifiesCombat = modifiesCombat;
    }

    public ArrayList<ArtifactCombatModifierModel> getCombatModifiers() {
        return combatModifiers;
    }

    public void setCombatModifiers(ArrayList<ArtifactCombatModifierModel> combatModifiers) {
        this.combatModifiers = combatModifiers;
    }
}
