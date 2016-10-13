package fi.softala.passi.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by villeaaltonen on 11/10/2016.
 */

public class Ryhma {
    @SerializedName("groupID")
    private int groupID;
    @SerializedName("groupName")
    private String groupName;

    public Ryhma() {
    }

    @Override
    public String toString() {
        return "Ryhma{" +
                "groupID=" + groupID +
                ", groupName='" + groupName + '\'' +
                '}';
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
