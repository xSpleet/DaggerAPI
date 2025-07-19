package xspleet.daggerapi.artifact;

import com.google.gson.annotations.SerializedName;

public enum ArtifactRarity {
    @SerializedName("common")
    COMMON(5),

    @SerializedName("rare")
    RARE(80),

    @SerializedName("epic")
    EPIC(2),

    @SerializedName("legendary")
    LEGENDARY(1);

    private final int weight;

    ArtifactRarity(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }
}
