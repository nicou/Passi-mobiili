package fi.softala.tyokykypassi.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by villeaaltonen on 01/11/2016.
 */

public class Answersheet implements Parcelable{
    private int tyyppi;
    @SerializedName("answersheet_id")
    private String answersheetId;
    @SerializedName("planning")
    private String planning;
    @SerializedName("instructor_comment")
    private String instructorComment;
    @SerializedName("timestamp")
    private long timestamp;
    @SerializedName("worksheet_id")
    private Integer worksheetId;
    @SerializedName("group_id")
    private Integer groupId;
    @SerializedName("user_id")
    private Integer userId;
    @SerializedName("answerpoints")
    private List<Answerpoints> answerpointsList;
    private String worksheetName;

    protected Answersheet(Parcel in) {
        tyyppi = in.readInt();
        answersheetId = in.readString();
        planning = in.readString();
        instructorComment = in.readString();
        timestamp = in.readLong();
        worksheetName = in.readString();
    }

    public static final Creator<Answersheet> CREATOR = new Creator<Answersheet>() {
        @Override
        public Answersheet createFromParcel(Parcel in) {
            return new Answersheet(in);
        }

        @Override
        public Answersheet[] newArray(int size) {
            return new Answersheet[size];
        }
    };

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

    public int getTyyppi() {
        return tyyppi;
    }

    public void setTyyppi(int tyyppi) {
        this.tyyppi = tyyppi;
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
        parcel.writeInt(tyyppi);
        parcel.writeString(answersheetId);
        parcel.writeString(planning);
        parcel.writeString(instructorComment);
        parcel.writeLong(timestamp);
        parcel.writeString(worksheetName);
    }
}
