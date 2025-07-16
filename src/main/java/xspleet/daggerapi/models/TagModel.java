package xspleet.daggerapi.models;

import java.util.ArrayList;
import java.util.List;

public class TagModel
{
    private boolean replace = false;
    private List<String> values = new ArrayList<>();

    public TagModel(boolean replace, List<String> values) {
        this.replace = replace;
        this.values = values;
    }

    public boolean isReplace() {
        return replace;
    }

    public void setReplace(boolean replace) {
        this.replace = replace;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
