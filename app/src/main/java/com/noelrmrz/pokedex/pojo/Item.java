package com.noelrmrz.pokedex.pojo;

import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("cost")
    private int cost;

    @SerializedName("fling_power")
    private int flingPower;

    public Item(int id, String name, int cost, int flingPower) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.flingPower = flingPower;
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

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getFlingPower() {
        return flingPower;
    }

    public void setFlingPower(int flingPower) {
        this.flingPower = flingPower;
    }
}
