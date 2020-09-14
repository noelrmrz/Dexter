package com.noelrmrz.pokedex.POJO;

import com.google.gson.annotations.SerializedName;

public class FlavorTextEntry {
    @SerializedName("flavor_text")
    private String flavorText;

    @SerializedName("language")
    private Language language;

    @SerializedName(value = "version_group", alternate = "version")
    private VersionGroup versionGroup;

    public FlavorTextEntry(String flavorText, Language language, VersionGroup versionGroup) {
        this.flavorText = flavorText;
        this.language = language;
        this.versionGroup = versionGroup;
    }

    public String getFlavorText() {
        return flavorText;
    }

    public void setFlavorText(String flavorText) {
        this.flavorText = flavorText;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public VersionGroup getVersionGroup() {
        return versionGroup;
    }

    public void setVersionGroup(VersionGroup versionGroup) {
        this.versionGroup = versionGroup;
    }
}
