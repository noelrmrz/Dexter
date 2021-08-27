package com.noelrmrz.pokedex.pojo;

import com.google.gson.annotations.SerializedName;

public class EvolutionDetail {

    @SerializedName("item")
    private Item item;

    @SerializedName("trigger")
    private EvolutionTrigger trigger;

    @SerializedName("gender")
    private int gender;

    @SerializedName("known_move")
    private Move move;

    @SerializedName("held_item")
    private Item heldItem;

    @SerializedName("known_move_type")
    private Type knownMoveType;

    @SerializedName("location")
    private Location location;

    @SerializedName("min_level")
    private int minLevel;

    @SerializedName("min_happiness")
    private int minHappiness;

    @SerializedName("min_beauty")
    private int minBeauty;

    @SerializedName("min_affection")
    private int minAffection;

    @SerializedName("needs_overworld_rain")
    private boolean needsOverworldRain;

    @SerializedName("party_species")
    private PokemonSpecies partySpecies;

    @SerializedName("party_type")
    private Type partyType;

    @SerializedName("relative_physical_stats")
    private int relativePhysicalStats;

    @SerializedName("time_of_day")
    private String timeOfDay;

    @SerializedName("trade_species")
    private PokemonSpecies tradeSpecies;

    @SerializedName("turn_upside_down")
    private Boolean turnUpsideDown;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public EvolutionTrigger getTrigger() {
        return trigger;
    }

    public void setTrigger(EvolutionTrigger trigger) {
        this.trigger = trigger;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    public Item getHeldItem() {
        return heldItem;
    }

    public void setHeldItem(Item heldItem) {
        this.heldItem = heldItem;
    }

    public Type getKnownMoveType() {
        return knownMoveType;
    }

    public void setKnownMoveType(Type knownMoveType) {
        this.knownMoveType = knownMoveType;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
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
        return minBeauty;
    }

    public void setBeauty(int minBeauty) {
        this.minBeauty = minBeauty;
    }

    public int getMinBeauty() {
        return minBeauty;
    }

    public void setMinBeauty(int minBeauty) {
        this.minBeauty = minBeauty;
    }

    public int getMinAffection() {
        return minAffection;
    }

    public void setMinAffection(int minAffection) {
        this.minAffection = minAffection;
    }

    public boolean isNeedsOverworldRain() {
        return needsOverworldRain;
    }

    public void setNeedsOverworldRain(boolean needsOverworldRain) {
        this.needsOverworldRain = needsOverworldRain;
    }

    public int getRelativePhysicalStats() {
        return relativePhysicalStats;
    }

    public void setRelativePhysicalStats(int relativePhysicalStats) {
        this.relativePhysicalStats = relativePhysicalStats;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public Boolean getTurnUpsideDown() {
        return turnUpsideDown;
    }

    public void setTurnUpsideDown(Boolean turnUpsideDown) {
        this.turnUpsideDown = turnUpsideDown;
    }
}
