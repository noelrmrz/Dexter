package com.noelrmrz.pokedex.pojo;

import com.google.gson.annotations.SerializedName;

public class EvolutionChainLink {
    @SerializedName("url")
    private String url;

    @SerializedName("chain")
    private EvolutionChain chain;

    @SerializedName("id")
    private int id;

    @SerializedName("baby_trigger_item")
    private String babyTriggerItem;

    public EvolutionChainLink(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public EvolutionChain getChain() {
        return chain;
    }

    public void setChain(EvolutionChain chain) {
        this.chain = chain;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBabyTriggerItem() {
        return babyTriggerItem;
    }

    public void setBabyTriggerItem(String babyTriggerItem) {
        this.babyTriggerItem = babyTriggerItem;
    }
}
