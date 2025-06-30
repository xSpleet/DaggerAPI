package xspleet.daggerapi.models;

public class AttributeModifierModel extends JSONModel
{
    private String attribute;
    private String modificationType;
    private double modificationValue;

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

    public double getModificationValue() {
        return modificationValue;
    }

    public void setModificationValue(double modificationValue) {
        this.modificationValue = modificationValue;
    }
}
