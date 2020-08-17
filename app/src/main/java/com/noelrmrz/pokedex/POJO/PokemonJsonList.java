package com.noelrmrz.pokedex.POJO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PokemonJsonList {

    @SerializedName("count")
    private int count;

    @SerializedName("next")
    private String next;

    @SerializedName("previous")
    private String previous;

    @SerializedName("results")
    private List<PokemonLink> results;

    public PokemonJsonList(String next, String previous, List<PokemonLink> results) {
        this.count = count;
        this.next = next;
        this.previous = previous;
        this.results = results;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<PokemonLink> getResults() {
        return results;
    }

    public void setResults(List<PokemonLink> results) {
        this.results = results;
    }
}
