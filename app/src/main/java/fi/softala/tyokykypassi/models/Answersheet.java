package fi.softala.tyokykypassi.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by villeaaltonen on 01/11/2016.
 */

public class Answersheet implements Parcelable{

    @SerializedName("answersheetID")
    private String answersheetId;
    @SerializedName("planning")
    private String planning;
    @SerializedName("instructorComment")
    private String instructorComment;
    @SerializedName("timestamp")
    private long timestamp;
    @SerializedName("worksheetID")
    private Integer worksheetId;
    @SerializedName("groupID")
    private Integer groupId;
    @SerializedName("userID")
    private Integer userId;
    @SerializedName("answerpoints")
    private List<Answerpoints> answerpointsList;
    private String worksheetName;

    public String getWorksheetName() {
        return worksheetName;
    }

    public void setWorksheetName(String worksheetName) {
        this.worksheetName = worksheetName;
    }

    public Answersheet() {
    }

    public List<Answerpoints> getAnswerpointsList() {
        return answerpointsList;
    }

    public void setAnswerpointsList(List<Answerpoints> answerpointsList) {
        this.answerpointsList = answerpointsList;
    }

    public String getAnswersheetId() {
        return answersheetId;
    }

    public void setAnswersheetId(String answersheetId) {
        this.answersheetId = answersheetId;
    }

    public String getPlanning() {
        return planning;
    }

    public void setPlanning(String planning) {
        this.planning = planning;
    }

    public String getInstructorComment() {
        return instructorComment;
    }

    public void setInstructorComment(String instructorComment) {
        this.instructorComment = instructorComment;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getWorksheetId() {
        return worksheetId;
    }

    public void setWorksheetId(Integer worksheetId) {
        this.worksheetId = worksheetId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Answersheet{" +
                "answersheetId='" + answersheetId + '\'' +
                ", planning='" + planning + '\'' +
                ", instructorComment='" + instructorComment + '\'' +
                ", timestamp=" + timestamp +
                ", worksheetId=" + worksheetId +
                ", groupId=" + groupId +
                ", userId=" + userId +
                ", answerpointsList=" + answerpointsList +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
