package com.noelrmrz.pokedex.pojo;

import com.google.gson.annotations.SerializedName;

public class Move {
    @SerializedName("id")
    private int mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("effect_chance")
    private int mEffectChance;

    @SerializedName("effect_entries")
    private EffectEntry[] mEffectEntries;

    @SerializedName("flavor_text_entries")
    private FlavorTextEntry[] mFlavorTextEntries;

    @SerializedName("damage_class")
    private MoveDamageClass mMoveDamageClass;

    @SerializedName("accuracy")
    private int mAccuracy;

    @SerializedName("pp")
    private int mPp;

    @SerializedName("priority")
    private int mPriority;

    @SerializedName("power")
    private int mPower;

    @SerializedName("type")
    private Type mType;

    public Move(int mId, String mName, int mEffectChance, EffectEntry[] mEffectEntries,
                FlavorTextEntry[] mFlavorTextEntries, MoveDamageClass mMoveDamageClass,
                int mAccuracy, int mPp, int mPriority, int mPower, Type mType) {

        this.mId = mId;
        this.mName = mName;
        this.mEffectChance = mEffectChance;
        this.mEffectEntries = mEffectEntries;
        this.mFlavorTextEntries = mFlavorTextEntries;
        this.mMoveDamageClass = mMoveDamageClass;
        this.mAccuracy = mAccuracy;
        this.mPp = mPp;
        this.mPriority = mPriority;
        this.mPower = mPower;
        this.mType = mType;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getEffectChance() {
        return mEffectChance;
    }

    public void setEffectChance(int effectChance) {
        mEffectChance = effectChance;
    }

    public EffectEntry[] getEffectEntries() {
        return mEffectEntries;
    }

    public void setEffectEntries(EffectEntry[] effectEntries) {
        mEffectEntries = effectEntries;
    }

    public FlavorTextEntry[] getFlavorTextEntries() {
        return mFlavorTextEntries;
    }

    public void setFlavorTextEntries(FlavorTextEntry[] flavorTextEntries) {
        mFlavorTextEntries = flavorTextEntries;
    }

    public MoveDamageClass getMoveDamageClass() {
        return mMoveDamageClass;
    }

    public void setMoveDamageClass(MoveDamageClass moveDamageClass) {
        mMoveDamageClass = moveDamageClass;
    }

    public int getAccuracy() {
        return mAccuracy;
    }

    public void setAccuracy(int accuracy) {
        mAccuracy = accuracy;
    }

    public int getPp() {
        return mPp;
    }

    public void setPp(int pp) {
        mPp = pp;
    }

    public int getPriority() {
        return mPriority;
    }

    public void setPriority(int priority) {
        mPriority = priority;
    }

    public int getPower() {
        return mPower;
    }

    public void setPower(int power) {
        mPower = power;
    }

    public Type getType() {
        return mType;
    }

    public void setType(Type type) {
        mType = type;
    }
}
