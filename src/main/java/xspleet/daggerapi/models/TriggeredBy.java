package xspleet.daggerapi.models;

import com.google.gson.annotations.SerializedName;

public enum TriggeredBy
{
    @SerializedName("self")
    ONLY_SELF,

    @SerializedName("other-player")
    OTHER_PLAYER,

    @SerializedName("other-living")
    OTHER_LIVING,

    @SerializedName("other-entity")
    OTHER_ENTITY,

    @SerializedName("any-player")
    ANY_PLAYER,

    @SerializedName("any-living")
    ANY_LIVING,

    @SerializedName("any-entity")
    ANY_ENTITY,

    @SerializedName("any")
    ANY
}
