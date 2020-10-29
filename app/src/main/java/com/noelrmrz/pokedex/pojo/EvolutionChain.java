package com.noelrmrz.pokedex.pojo;

import com.google.gson.annotations.SerializedName;

public class EvolutionChain {
    @SerializedName("is_baby")
    private boolean isBaby;

    @SerializedName("species")
    private PokemonSpeciesLink species;

    @SerializedName("evolves_to")
    private EvolutionChain[] nextEvolutions;

/*    @SerializedName("evolution_details")
    private String evolutionDetails;*/

    public EvolutionChain(boolean isBaby, PokemonSpeciesLink species, EvolutionChain[] nextEvolutions,
                          String evolutionDetails) {
        this.isBaby = isBaby;
        this.species = species;
        this.nextEvolutions = nextEvolutions;
        //this.evolutionDetails = evolutionDetails;
    }

    public boolean isBaby() {
        return isBaby;
    }

    public void setBaby(boolean baby) {
        isBaby = baby;
    }

    public PokemonSpeciesLink getSpecies() {
        return species;
    }

    public void setSpecies(PokemonSpeciesLink species) {
        this.species = species;
    }

    public EvolutionChain[] getNextEvolutions() {
        return nextEvolutions;
    }

    public void setNextEvolutions(EvolutionChain[] nextEvolutions) {
        this.nextEvolutions = nextEvolutions;
    }

/*    public String getEvolutionDetails() {
        return evolutionDetails;
    }

    public void setEvolutionDetails(String evolutionDetails) {
        this.evolutionDetails = evolutionDetails;
    }*/
}
