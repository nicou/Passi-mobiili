package fi.softala.passi.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import static java.lang.System.out;

/**
 * Created by villeaaltonen on 11/10/2016.
 */

public class Ryhma implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
