package xspleet.daggerapi.models;

import java.util.HashMap;
import java.util.Map;

public class ConditionModel extends JSONModel
{

    public String condition;
    public Map<String, String> arguments = new HashMap<>();
}
