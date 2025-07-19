package xspleet.daggerapi.api.models;

import xspleet.daggerapi.artifact.ArtifactRarity;

import java.util.ArrayList;

public class ItemModel extends JSONModel
{
    private String name;
    private ArtifactRarity rarity = ArtifactRarity.COMMON;
    private boolean active = false;
    private int cooldown = -1;
    private ArrayList<ArtifactAttributeModifierModel> attributeModifiers = new ArrayList<>();
    private ArrayList<EventModel> events = new ArrayList<>();

    public String getName() {
        return name;
    }

    public ArtifactRarity getRarity() {
        return rarity;
    }

    public boolean isActive() {
        return active;
    }

    public int getCooldown() {
        return cooldown;
    }

    public ArrayList<ArtifactAttributeModifierModel> getAttributeModifiers() {
        return attributeModifiers;
    }

    public ArrayList<EventModel> getEvents() {
        return events;
    }

}
