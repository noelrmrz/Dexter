package com.noelrmrz.pokedex.POJO;

import com.google.gson.annotations.SerializedName;

public class Genera {
    @SerializedName("genus")
    private String genus;

    @SerializedName("language")
    private Language language;

    public Genera(String genus, Language language) {
        this.genus = genus;
        this.language = language;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }
}
