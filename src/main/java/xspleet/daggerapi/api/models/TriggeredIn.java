package xspleet.daggerapi.api.models;

import com.google.gson.annotations.SerializedName;

public enum TriggeredIn
{
    @SerializedName("same_world")
    SAME_WORLD,

    @SerializedName("other_world")
    OTHER_WORLD,

    @SerializedName("any")
    ANY
}
