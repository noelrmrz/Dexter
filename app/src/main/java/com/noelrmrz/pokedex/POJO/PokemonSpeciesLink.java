package com.noelrmrz.pokedex.POJO;

import com.google.gson.annotations.SerializedName;

public class PokemonSpeciesLink {
    @SerializedName("name")
    private String name;

    @SerializedName("url")
    private String url;

    public PokemonSpeciesLink(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}