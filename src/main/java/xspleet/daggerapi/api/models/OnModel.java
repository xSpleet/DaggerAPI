package xspleet.daggerapi.api.models;

import com.google.gson.annotations.SerializedName;

public enum OnModel {
    @SerializedName("self")
    SELF,

    @SerializedName("source")
    SOURCE,

    @SerializedName("triggered")
    TRIGGERED,

    @SerializedName("world")
    WORLD
}
