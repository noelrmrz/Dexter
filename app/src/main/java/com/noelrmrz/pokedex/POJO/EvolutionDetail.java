package com.noelrmrz.pokedex.POJO;

import com.google.gson.annotations.SerializedName;

class EvolutionDetail {
    @SerializedName("item")
    private String item;

    @SerializedName("trigger")
    private String trigger;

    @SerializedName("gender")
    private String gender;

    @SerializedName("known_move")
    private String move;

    @SerializedName("held_item")
    private String heldItem;

    @SerializedName("known_move_type")
    private String knownMoveType;

    @SerializedName("location")
    private String location;

    @SerializedName("min_level")
    private int minLevel;

    @SerializedName("min_happiness")
    private int minHappiness;

    @SerializedName("min_beauty")
    private int beauty;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }

    public String getHeldItem() {
        return heldItem;
    }

    public void setHeldItem(String heldItem) {
        this.heldItem = heldItem;
    }

    public String getKnownMoveType() {
        return knownMoveType;
    }

    public void setKnownMoveType(String knownMoveType) {
        this.knownMoveType = knownMoveType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public void setMinLevel(int minLevel) {
        this.minLevel = minLevel;
    }

    public int getMinHappiness() {
        return minHappiness;
    }

    public void setMinHappiness(int minHappiness) {
        this.minHappiness = minHappiness;
    }

    public int getBeauty() {
        return beauty;
    }

    public void setBeauty(int beauty) {
        this.beauty = beauty;
    }
}
