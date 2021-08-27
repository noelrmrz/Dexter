package com.noelrmrz.pokedex.pojo;

import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    public Location(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
