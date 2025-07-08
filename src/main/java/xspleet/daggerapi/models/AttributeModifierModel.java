package xspleet.daggerapi.models;

import com.google.gson.JsonElement;

public class AttributeModifierModel extends JSONModel
{
    private String attribute;
    private String modificationType;
    private JsonElement modificationValue;

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getModificationType() {
        return modificationType;
    }

    public void setModificationType(String modificationType) {
        this.modificationType = modificationType;
    }

    public JsonElement getModificationValue() {
        return modificationValue;
    }

    public void setModificationValue(JsonElement modificationValue) {
        this.modificationValue = modificationValue;
    }
}
