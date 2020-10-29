package com.noelrmrz.pokedex.pojo;

import com.google.gson.annotations.SerializedName;

public class MoveLink {
    @SerializedName("move")
    private Move move;

    @SerializedName("version_group_details")
    private VersionGroupDetails[] versionGroupDetails;

    public MoveLink(Move move, VersionGroupDetails[] versionGroupDetails) {
        this.move = move;
        this.versionGroupDetails = versionGroupDetails;
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    public VersionGroupDetails[] getVersionGroupDetails() {
        return versionGroupDetails;
    }

    public void setVersionGroupDetails(VersionGroupDetails[] versionGroupDetails) {
        this.versionGroupDetails = versionGroupDetails;
    }
}
