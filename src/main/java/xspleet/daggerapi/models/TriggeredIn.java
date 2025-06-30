package xspleet.daggerapi.models;

import com.google.gson.annotations.SerializedName;

public enum TriggeredIn
{
    @SerializedName("same-world")
    SAME_WORLD,

    @SerializedName("other-world")
    OTHER_WORLD,

    @SerializedName("any")
    ANY
}
