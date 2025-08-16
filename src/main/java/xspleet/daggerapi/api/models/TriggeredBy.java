package xspleet.daggerapi.api.models;

import com.google.gson.annotations.SerializedName;

public enum TriggeredBy
{
    @SerializedName("self")
    ONLY_SELF,

    @SerializedName("other_player")
    OTHER_PLAYER,

    @SerializedName("other_living")
    OTHER_LIVING,

    @SerializedName("other_entity")
    OTHER_ENTITY,

    @SerializedName("any_player")
    ANY_PLAYER,

    @SerializedName("any_living")
    ANY_LIVING,

    @SerializedName("any_entity")
    ANY_ENTITY,

    @SerializedName("any")
    ANY
}
