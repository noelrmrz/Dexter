package com.noelrmrz.pokedex.POJO;

import com.google.gson.annotations.SerializedName;

public class VersionGroupDetails {
    @SerializedName("level_learned_at")
    private int levelLearnedAt;

    public VersionGroupDetails(int levelLearnedAt) {
        this.levelLearnedAt = levelLearnedAt;
    }

    public int getLevelLearnedAt() {
        return levelLearnedAt;
    }

    public void setLevelLearnedAt(int levelLearnedAt) {
        this.levelLearnedAt = levelLearnedAt;
    }
}
