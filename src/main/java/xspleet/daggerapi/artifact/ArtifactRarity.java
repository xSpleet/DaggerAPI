package xspleet.daggerapi.artifact;

import com.google.gson.annotations.SerializedName;

public enum ArtifactRarity {
    @SerializedName("common")
    COMMON(15),

    @SerializedName("rare")
    RARE(9),

    @SerializedName("epic")
    EPIC(6),

    @SerializedName("legendary")
    LEGENDARY(3);

    private final int weight;

    ArtifactRarity(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }
}
