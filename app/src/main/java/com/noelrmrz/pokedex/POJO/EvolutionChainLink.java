package com.noelrmrz.pokedex.POJO;

import com.google.gson.annotations.SerializedName;

public class EvolutionChainLink {
    @SerializedName("url")
    private String url;

    public EvolutionChainLink(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
