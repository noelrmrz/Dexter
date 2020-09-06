package com.noelrmrz.pokedex.POJO;

import com.google.gson.annotations.SerializedName;

public class FlavorTextEntry {
    @SerializedName("flavor_text")
    private String flavorText;

    @SerializedName("language")
    private Language language;

    public FlavorTextEntry(String flavorText, Language language) {
        this.flavorText = flavorText;
        this.language = language;
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
}
