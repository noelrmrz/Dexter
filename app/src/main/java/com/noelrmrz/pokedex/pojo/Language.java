package com.noelrmrz.pokedex.pojo;

import com.google.gson.annotations.SerializedName;

public class Language {
    @SerializedName("name")
    private String language;

    @SerializedName("url")
    private String url;

    public Language(String language, String url) {
        this.language = language;
        this.url = url;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
