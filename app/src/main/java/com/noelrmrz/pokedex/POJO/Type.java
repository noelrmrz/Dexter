package com.noelrmrz.pokedex.POJO;

import com.google.gson.annotations.SerializedName;

public class Type {
    @SerializedName("name")
    private String name;
    @SerializedName("url")
    private String url;

    public Type(String name, String url) {
        name = name;
        url = url;
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
