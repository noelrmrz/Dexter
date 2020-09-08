package com.noelrmrz.pokedex.POJO;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName="favorite_pokemon")
public class Pokemon {

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

    @SerializedName("image_url")
    @ColumnInfo(name="image_url")
    private String mImageUrl;

    @SerializedName("url")
    @ColumnInfo(name="url")
    private String mUrl;

    @Ignore
    private String profileUrl;

    @ColumnInfo(name="favorite")
    private Boolean favorite = false;

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
        this.mImageUrl = mImageUrl;
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

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
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

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getProfileUrl() {
        return profileUrl;
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
}

