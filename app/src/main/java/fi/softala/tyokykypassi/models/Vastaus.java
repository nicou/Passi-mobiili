package fi.softala.tyokykypassi.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by joakimkajan on 29/09/16.
 */


public class Vastaus {

    @SerializedName("worksheetID")
    private Integer worksheetID;
    @SerializedName("groupID")
    private Integer groupID;
    @SerializedName("userID")
    private Integer userID;
    @SerializedName("planning")
    private String planningText;
    @SerializedName("answerpoints")
    private List<Etappi> answerpoints;

    public Vastaus(Integer worksheetID, Integer groupId, Integer userId, String planningText, List<Etappi> answerpoints) {
        this.worksheetID = worksheetID;
        this.groupID = groupId;
        this.userID = userId;
        this.planningText = planningText;
        this.answerpoints = answerpoints;
    }

    public Vastaus() {

    }


    public Integer getGroupID() {
        return groupID;
    }

    public void setGroupID(Integer groupID) {
        this.groupID = groupID;
    }

    public Integer getWorksheetID() {
        return worksheetID;
    }

    public void setWorksheetID(Integer worksheetID) {
        this.worksheetID = worksheetID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public List<Etappi> getAnswerpoints() {
        return answerpoints;
    }

    public void setAnswerpoints(List<Etappi> answerpoints) {
        this.answerpoints = answerpoints;
    }

    public String getPlanningText() {
        return planningText;
    }

    public void setPlanningText(String planningText) {
        this.planningText = planningText;
    }

    @Override
    public String toString() {
        return "Vastaus{" +
                ", worksheetID=" + worksheetID +
                ", userID='" + userID + '\'' +
                ", planningText='" + planningText + '\'' +
                ", answerpoints=" + answerpoints +
                '}';
    }
}
