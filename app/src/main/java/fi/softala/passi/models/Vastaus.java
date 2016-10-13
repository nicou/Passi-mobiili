package fi.softala.passi.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import fi.softala.passi.models.Etappi;

/**
 * Created by joakimkajan on 29/09/16.
 */


public class Vastaus {

    @SerializedName("worksheetID")
    Integer worksheetID;
    @SerializedName("groupID")
    Integer groupID;
    @SerializedName("userID")
    Integer userID;
    @SerializedName("planning")
    String planningText;
    @SerializedName("answerpoints")
    List<Etappi> answerpoints;

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
