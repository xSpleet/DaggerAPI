package xspleet.daggerapi.api.models;

import com.google.gson.JsonElement;

public class AttributeModifierModel extends JSONModel
{
    private String attribute;
    private String modificationType;
    private JsonElement modificationValue;

    public String getAttribute() {
        return attribute;
    }

    public String getModificationType() {
        return modificationType;
    }

    public JsonElement getModificationValue() {
        return modificationValue;
    }

}
