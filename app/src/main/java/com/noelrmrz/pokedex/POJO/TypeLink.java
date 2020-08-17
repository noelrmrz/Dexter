package com.noelrmrz.pokedex.POJO;

import com.google.gson.annotations.SerializedName;

public class TypeLink {
    @SerializedName("slot")
    private int slot;

    @SerializedName("type")
    private Type type;

    public TypeLink(int slot, Type type) {
        this.slot = slot;
        this.type = type;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
