package xspleet.daggerapi.models;

public class AttributeModifierModel extends JSONModel
{
    private String attribute;
    private String modificationType;
    private Double modificationValue;

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

    public Double getModificationValue() {
        return modificationValue;
    }

    public void setModificationValue(double modificationValue) {
        this.modificationValue = modificationValue;
    }
}
