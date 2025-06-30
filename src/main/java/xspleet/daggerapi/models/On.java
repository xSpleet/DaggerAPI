package xspleet.daggerapi.models;

import com.google.gson.annotations.SerializedName;

public enum On {
    @SerializedName("self")
    SELF,

    @SerializedName("triggerer")
    TRIGGERER,

    @SerializedName("triggered")
    TRIGGERED,

    @SerializedName("world")
    WORLD
}
