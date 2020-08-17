package com.noelrmrz.pokedex.POJO;

import com.google.gson.annotations.SerializedName;

public class PokemonLink {

    @SerializedName("name")
    private String name;

    @SerializedName("url")
    private String url;

    public PokemonLink(String name, String url) {
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
