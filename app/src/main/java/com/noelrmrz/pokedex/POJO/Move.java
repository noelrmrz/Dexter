package com.noelrmrz.pokedex.POJO;

import java.util.List;

import me.sargunvohra.lib.pokekotlin.model.Effect;
import me.sargunvohra.lib.pokekotlin.model.MoveDamageClass;
import me.sargunvohra.lib.pokekotlin.model.Type;

class Move {
    private int mId;
    private String mName;
    private int mEffectChance;
    private List<Effect> mEffectEntries;
    private MoveDamageClass mMoveDamageClass;
    private int mAccuracy;
    private int mPp;
    private int mPriority;
    private int mPower;
    private Type mType;

}
