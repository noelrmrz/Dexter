package com.noelrmrz.pokedex.pojo;

import com.google.gson.annotations.SerializedName;

public class DamageRelations {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("no_damage_to")
    private Type[] noDamageTo;

    @SerializedName("half_damage_to")
    private Type[] halfDamageTo;

    @SerializedName("double_damage_to")
    private Type[] doubleDamageTo;

    @SerializedName("no_damage_from")
    private Type[] noDamageFrom;

    @SerializedName("half_damage_from")
    private Type[] halfDamageFrom;

    @SerializedName("double_damage_from")
    private Type[] doubleDamageFrom;

    public DamageRelations(int id, String name, Type[] noDamageTo, Type[] halfDamageTo,
                           Type[] doubleDamageTo, Type[] noDamageFrom, Type[] halfDamageFrom,
                           Type[] doubleDamageFrom) {
        this.id = id;
        this.name = name;
        this.noDamageTo = noDamageTo;
        this.halfDamageTo = halfDamageTo;
        this.doubleDamageTo = doubleDamageTo;
        this.noDamageFrom = noDamageFrom;
        this.halfDamageFrom = halfDamageFrom;
        this.doubleDamageFrom = doubleDamageFrom;
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

    public Type[] getNoDamageTo() {
        return noDamageTo;
    }

    public void setNoDamageTo(Type[] noDamageTo) {
        this.noDamageTo = noDamageTo;
    }

    public Type[] getHalfDamageTo() {
        return halfDamageTo;
    }

    public void setHalfDamageTo(Type[] halfDamageTo) {
        this.halfDamageTo = halfDamageTo;
    }

    public Type[] getDoubleDamageTo() {
        return doubleDamageTo;
    }

    public void setDoubleDamageTo(Type[] doubleDamageTo) {
        this.doubleDamageTo = doubleDamageTo;
    }

    public Type[] getNoDamageFrom() {
        return noDamageFrom;
    }

    public void setNoDamageFrom(Type[] noDamageFrom) {
        this.noDamageFrom = noDamageFrom;
    }

    public Type[] getHalfDamageFrom() {
        return halfDamageFrom;
    }

    public void setHalfDamageFrom(Type[] halfDamageFrom) {
        this.halfDamageFrom = halfDamageFrom;
    }

    public Type[] getDoubleDamageFrom() {
        return doubleDamageFrom;
    }

    public void setDoubleDamageFrom(Type[] doubleDamageFrom) {
        this.doubleDamageFrom = doubleDamageFrom;
    }
}
