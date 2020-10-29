package com.noelrmrz.pokedex.pojo;

import com.google.gson.annotations.SerializedName;

public class MoveDamageClass {
    @SerializedName("name")
    private String mName;

    @SerializedName("url")
    private String mUrl;

    public MoveDamageClass(String mName, String mUrl) {
        this.mName = mName;
        this.mUrl = mUrl;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }
}
