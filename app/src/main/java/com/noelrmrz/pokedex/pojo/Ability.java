package com.noelrmrz.pokedex.pojo;

import com.google.gson.annotations.SerializedName;

public class Ability {
    @SerializedName("id")
    private int id;

    @SerializedName("effect_entries")
    private EffectEntry[] effectEntries;

    @SerializedName("name")
    private String name;

    @SerializedName("url")
    private String url;

    public Ability(String name, String url, int id, EffectEntry[] effectEntries) {
        this.name = name;
        this.url = url;
        this.id = id;
        this.effectEntries = effectEntries;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EffectEntry[] getEffectEntries() {
        return effectEntries;
    }

    public void setEffectEntries(EffectEntry[] effectEntries) {
        this.effectEntries = effectEntries;
    }
}
