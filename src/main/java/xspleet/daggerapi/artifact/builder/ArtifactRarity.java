package xspleet.daggerapi.artifact.builder;

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


    public static ArtifactRarity getRarity(String rarity)
    {
        return switch(rarity) {
            case "rare" -> ArtifactRarity.RARE;
            case "epic" -> ArtifactRarity.EPIC;
            case "legendary" -> ArtifactRarity.LEGENDARY;
            default -> ArtifactRarity.COMMON;
        };
    }

    public int getWeight() {
        return weight;
    }
}
