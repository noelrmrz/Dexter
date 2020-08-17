package com.noelrmrz.pokedex.POJO;

import com.google.gson.annotations.SerializedName;

class FlavorTextEntry {
    @SerializedName("flavor_text")
    private String flavorText;

    @SerializedName("language")
    private Language language;
}
