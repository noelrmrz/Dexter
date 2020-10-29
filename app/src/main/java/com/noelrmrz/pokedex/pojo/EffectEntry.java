package com.noelrmrz.pokedex.pojo;

import com.google.gson.annotations.SerializedName;

public class EffectEntry {
    @SerializedName("effect")
    private String effect;

    @SerializedName("language")
    private Language language;

    @SerializedName("short_effect")
    private String shortEffect;

    public EffectEntry(String effect, Language language, String shortEffect) {
        this.effect = effect;
        this.language = language;
        this.shortEffect = shortEffect;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getShortEffect() {
        return shortEffect;
    }

    public void setShortEffect(String shortEffect) {
        this.shortEffect = shortEffect;
    }
}
