package fi.softala.passi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/**
 * Created by joakimkajan on 29/09/16.
 */


@JsonPropertyOrder({"planning", "worksheetID", "groupID", "userID", "answerpoints"})
public class Vastaus {


    Integer worksheetID;
    Integer groupID;
    Integer userID;
    String planningText;
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

    @JsonProperty("planning")
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
