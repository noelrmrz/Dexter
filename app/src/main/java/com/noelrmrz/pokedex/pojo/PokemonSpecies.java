package com.noelrmrz.pokedex.pojo;

import com.google.gson.annotations.SerializedName;

public class PokemonSpecies {
    @SerializedName("gender_rate")
    private int genderRate;

    @SerializedName("capture_rate")
    private int captureRate;

    @SerializedName("base_happiness")
    private int baseHappiness;

    @SerializedName("evolves_from_species")
    private EvolvesFromSpeciesLink evolvesFromSpeciesLink;

    @SerializedName("evolution_chain")
    private EvolutionChainLink evolutionChainLink;

    @SerializedName("flavor_text_entries")
    private FlavorTextEntry[] flavorTextEntries;

    @SerializedName("genera")
    private Genera[] genera;

    @SerializedName("habitat")
    private Habitat habitat;

    public PokemonSpecies(int genderRate, int captureRate, int baseHappiness,
                          EvolvesFromSpeciesLink evolvesFromSpeciesLink,
                          EvolutionChainLink evolutionChainLink,
                          FlavorTextEntry[] flavorTextEntries, Genera[] genera, Habitat habitat) {
        this.genderRate = genderRate;
        this.captureRate = captureRate;
        this.baseHappiness = baseHappiness;
        this.evolvesFromSpeciesLink = evolvesFromSpeciesLink;
        this.evolutionChainLink = evolutionChainLink;
        this.flavorTextEntries = flavorTextEntries;
        this.genera = genera;
        this.habitat = habitat;
    }

    public int getGenderRate() {
        return genderRate;
    }

    public void setGenderRate(int genderRate) {
        this.genderRate = genderRate;
    }

    public int getCaptureRate() {
        return captureRate;
    }

    public void setCaptureRate(int captureRate) {
        this.captureRate = captureRate;
    }

    public int getBaseHappiness() {
        return baseHappiness;
    }

    public void setBaseHappiness(int baseHappiness) {
        this.baseHappiness = baseHappiness;
    }

    public EvolvesFromSpeciesLink getEvolvesFromSpeciesLink() {
        return evolvesFromSpeciesLink;
    }

    public void setEvolvesFromSpeciesLink(EvolvesFromSpeciesLink evolvesFromSpeciesLink) {
        this.evolvesFromSpeciesLink = evolvesFromSpeciesLink;
    }

    public EvolutionChainLink getEvolutionChainLink() {
        return evolutionChainLink;
    }

    public void setEvolutionChainLink(EvolutionChainLink evolutionChainLink) {
        this.evolutionChainLink = evolutionChainLink;
    }

    public FlavorTextEntry[] getFlavorTextEntries() {
        return flavorTextEntries;
    }

    public void setFlavorTextEntries(FlavorTextEntry[] flavorTextEntries) {
        this.flavorTextEntries = flavorTextEntries;
    }

    public Genera[] getGenera() {
        return genera;
    }

    public void setGenera(Genera[] genera) {
        this.genera = genera;
    }

    public Habitat getHabitat() {
        return habitat;
    }

    public void setHabitat(Habitat habitat) {
        this.habitat = habitat;
    }
}
