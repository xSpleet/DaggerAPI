package xspleet.daggerapi.api.models;

import java.util.ArrayList;
import java.util.List;

public class TagModel
{
    private final boolean replace;
    private final List<String> values;

    public TagModel(boolean replace, List<String> values) {
        this.replace = replace;
        this.values = values;
    }

    public boolean isReplace() {
        return replace;
    }

    public List<String> getValues() {
        return values;
    }
}
