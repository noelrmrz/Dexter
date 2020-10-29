package com.noelrmrz.pokedex.pojo;

import com.google.gson.annotations.SerializedName;

public class Type {
    @SerializedName("name")
    private String name;
    @SerializedName("url")
    private String url;
    @SerializedName("damage_relations")
    private DamageRelations damageRelations;

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

    public DamageRelations getDamageRelations() {
        return damageRelations;
    }

    public void setDamageRelations(DamageRelations damageRelations) {
        this.damageRelations = damageRelations;
    }
}
