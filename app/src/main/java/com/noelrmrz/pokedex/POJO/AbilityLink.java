package com.noelrmrz.pokedex.POJO;

import com.google.gson.annotations.SerializedName;

public class AbilityLink {
    @SerializedName("slot")
    private int mSlot;
    @SerializedName("is_hidden")
    private Boolean mIsHidden;
    @SerializedName("ability")
    private Ability mAbility;

    public AbilityLink(int slot, Boolean isHidden, Ability ability) {
        mSlot = slot;
        mIsHidden = isHidden;
        mAbility = ability;
    }

    public int getSlot() {
        return mSlot;
    }

    public void setSlot(Integer slot) {
        mSlot = mSlot;
    }

    public Boolean getIsHidden() {
        return mIsHidden;
    }

    public void setIsHidden(Boolean isHidden) {
        this.mIsHidden = mIsHidden;
    }

    public Ability getAbility() {
        return mAbility;
    }

    public void setAbility(Ability ability) {
        mAbility = ability;
    }
}
