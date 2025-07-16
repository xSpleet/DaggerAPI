package xspleet.daggerapi.models;

import xspleet.daggerapi.artifact.builder.ArtifactRarity;

import java.util.ArrayList;

public class ItemModel extends JSONModel
{
    private String name;
    private ArtifactRarity rarity;
    private boolean active = false;
    private int cooldown = -1;
    private EventModel onActivation;
    private ArrayList<ArtifactAttributeModifierModel> attributeModifiers = new ArrayList<>();
    private ArrayList<EventModel> events = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArtifactRarity getRarity() {
        return rarity;
    }

    public void setRarity(ArtifactRarity rarity) {
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
}
