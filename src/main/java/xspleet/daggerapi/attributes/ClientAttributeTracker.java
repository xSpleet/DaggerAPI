package xspleet.daggerapi.attributes;

import java.util.List;

public interface ClientAttributeTracker
{
    void addAttributeToUpdate(Attribute<?> attribute);
    boolean updatesAttribute(Attribute<?> attribute);
    void removeAttributeToUpdate(Attribute<?> attribute);
    void removeAllAttributesToUpdate();
    void updateAttribute(Attribute<?> attribute, long tick);
    long getAttributeUpdateTime(Attribute<?> attribute);
    List<Attribute<?>> getAttributesToUpdate();
    void reset(Attribute<?> attribute);
}
