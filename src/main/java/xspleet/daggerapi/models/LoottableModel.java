package xspleet.daggerapi.models;

import java.util.List;

public class LoottableModel
{
    private String loottable;
    private List<String> entries;

    public void setLoottable(String loottable) {
        this.loottable = loottable;
    }

    public String getLoottable() {
        return loottable;
    }

    public void setEntries(List<String> entries) {
        this.entries = entries;
    }

    public List<String> getEntries() {
        return entries;
    }
}
