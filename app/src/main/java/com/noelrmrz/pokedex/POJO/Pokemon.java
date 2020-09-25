package com.noelrmrz.pokedex.POJO;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName="favorite_pokemon")
public class Pokemon implements Comparable<Pokemon> {

    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name="id")
    @NonNull
    private int mId;

    @SerializedName("name")
    @ColumnInfo(name="name")
    private String mName;

    @SerializedName("height")
    @ColumnInfo(name="height")
    private int mHeight;

    @SerializedName("weight")
    @ColumnInfo(name="weight")
    private int mWeight;

    @SerializedName("stats")
    @Ignore
    private Stat[] mStatList;

    @SerializedName("types")
    @Ignore
    private TypeLink[] mTypeList;

    @SerializedName("abilities")
    @Ignore
    private AbilityLink[] mAbilityList;

    @SerializedName("moves")
    @Ignore
    private MoveLink[] mMoveList;

    @SerializedName("species")
    @Ignore
    private PokemonSpecies pokemonSpecies;

    @SerializedName("url")
    @ColumnInfo(name="url")
    private String mUrl;

    @ColumnInfo(name="favorite")
    private Boolean favorite = false;

    @ColumnInfo(name="json_string")
    private String jsonString;

    @Ignore
    private List<String> superEffective = new ArrayList<>(0);

    @Ignore
    private List<String> effective = new ArrayList<>(0);

    @Ignore
    private List<String> normal = new ArrayList<>(0);

    @Ignore
    private List<String> notEffective = new ArrayList<>(0);

    @Ignore
    private List<String> notVeryEffective = new ArrayList<>(0);

    @Ignore
    private List<String> immune = new ArrayList<>(0);

    public Pokemon(){

    }

    public Pokemon(int mId, String mName, int mHeight, int mWeight, MoveLink[] mMoveList,
                   Stat[] mStatList, TypeLink[] mTypeList, AbilityLink[] mAbilityLinkList, String mImageUrl,
                   String mUrl) {
        this.mId = mId;
        this.mName = mName;
        this.mHeight = mHeight;
        this.mWeight = mWeight;
        this.mMoveList = mMoveList;
        this.mStatList = mStatList;
        this.mTypeList = mTypeList;
        this.mAbilityList = mAbilityLinkList;
        this.mUrl = mUrl;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public int getHeight() {
        return mHeight;
    }

    public int getWeight() {
        return mWeight;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    public void setWeight(int weight) {
        mWeight = weight;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public TypeLink[] getTypeList() {
        return mTypeList;
    }

    public void setTypeList(TypeLink[] typeList) {
        mTypeList = typeList;
    }

    public AbilityLink[] getAbilityList() {
        return mAbilityList;
    }

    public void setAbilityList(AbilityLink[] abilityList) {
        mAbilityList = abilityList;
    }

    public MoveLink[] getMoveList() {
        return mMoveList;
    }

    public void setMoveList(MoveLink[] moveList) {
        mMoveList = moveList;
    }

    public PokemonSpecies getPokemonSpecies() {
        return pokemonSpecies;
    }

    public void setPokemonSpecies(PokemonSpecies pokemonSpecies) {
        this.pokemonSpecies = pokemonSpecies;
    }

    public Stat[] getStatList() {
        return mStatList;
    }

    public void setStatList(Stat[] statList) {
        mStatList = statList;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }

    public List<String> getSuperEffective() {
        return superEffective;
    }

    public void setSuperEffective(List<String> superEffective) {
        this.superEffective = superEffective;
    }

    public List<String> getEffective() {
        return effective;
    }

    public void setEffective(List<String> effective) {
        this.effective = effective;
    }

    public List<String> getNormal() {
        return normal;
    }

    public void setNormal(List<String> normal) {
        this.normal = normal;
    }

    public List<String> getNotEffective() {
        return notEffective;
    }

    public void setNotEffective(List<String> notEffective) {
        this.notEffective = notEffective;
    }

    public List<String> getNotVeryEffective() {
        return notVeryEffective;
    }

    public void setNotVeryEffective(List<String> notVeryEffective) {
        this.notVeryEffective = notVeryEffective;
    }

    public List<String> getImmune() {
        return immune;
    }

    public void setImmune(List<String> immune) {
        this.immune = immune;
    }

    @Override
    public int compareTo(Pokemon pokemon) {
        if(this.mId == pokemon.getId())
            return 0;
        else if(this.mId > pokemon.getId())
            return 1;
        else
            return -1;
    }
}

