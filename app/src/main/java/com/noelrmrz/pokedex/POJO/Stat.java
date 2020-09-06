package com.noelrmrz.pokedex.POJO;

import com.google.gson.annotations.SerializedName;

public class Stat {

    @SerializedName("base_stat")
    private int baseStat;

    @SerializedName("effort")
    private int effort;

    @SerializedName("stat")
    private StatLink stat;

    public Stat(int baseStat, int effort, StatLink stat) {
        this.baseStat = baseStat;
        this.effort = effort;
        this.stat = stat;
    }

    public int getBaseStat() {
        return baseStat;
    }

    public void setBaseStat(int baseStat) {
        this.baseStat = baseStat;
    }

    public int getEffort() {
        return effort;
    }

    public void setEffort(int effort) {
        this.effort = effort;
    }

    public StatLink getStat() {
        return stat;
    }

    public void setStats(StatLink stat) {
        this.stat = stat;
    }
}
