package xspleet.daggerapi.attributes;

import java.util.List;

public interface ClientAttributeHolder
{
    public void addAttributeToUpdate(Attribute<?> attribute);
    public boolean updatesAttribute(Attribute<?> attribute);
    public void removeAttributeToUpdate(Attribute<?> attribute);
    public void removeAllAttributesToUpdate();
    public void updateAttribute(Attribute<?> attribute, long tick);
    public long getAttributeUpdateTime(Attribute<?> attribute);
    public List<Attribute<?>> getAttributesToUpdate();
    public void reset(Attribute<?> attribute);
}
