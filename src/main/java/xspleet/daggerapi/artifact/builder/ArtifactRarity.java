package xspleet.daggerapi.artifact.builder;

public enum ArtifactRarity {
    COMMON,
    RARE,
    EPIC,
    LEGENDARY;

    public static ArtifactRarity getRarity(String rarity)
    {
        return switch(rarity) {
            case "rare" -> ArtifactRarity.RARE;
            case "epic" -> ArtifactRarity.EPIC;
            case "legendary" -> ArtifactRarity.LEGENDARY;
            default -> ArtifactRarity.COMMON;
        };
    }
}
