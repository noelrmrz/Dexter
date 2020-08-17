package com.noelrmrz.pokedex.POJO;

import com.google.gson.annotations.SerializedName;

public class Ability {
    @SerializedName("slot")
    private Integer mSlot;
    @SerializedName("is_hidden")
    private Boolean mIsHidden;
/*    @SerializedName("effect_entries")
    @Expose
    private List<EffectEntry> effectEntries;*/
}
