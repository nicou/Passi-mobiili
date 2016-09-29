package fi.softala.passi;

import java.util.List;

/**
 * Created by joakimkajan on 29/09/16.
 */

public class Vastaus1 {

    Integer answerID;
    Integer worksheetID;

    String username;
    String planningText;
    List<Etappi> waypoints;

    public Vastaus1(Integer answerID, Integer worksheetID, String username, String planningText, List<Etappi> waypoints) {
        this.answerID = answerID;
        this.worksheetID = worksheetID;
        this.username = username;
        this.planningText = planningText;
        this.waypoints = waypoints;
    }

    public Vastaus1() {

    }

    public Integer getAnswerID() {
        return answerID;
    }

    public void setAnswerID(Integer answerID) {
        this.answerID = answerID;
    }

    public Integer getWorksheetID() {
        return worksheetID;
    }

    public void setWorksheetID(Integer worksheetID) {
        this.worksheetID = worksheetID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Etappi> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<Etappi> waypoints) {
        this.waypoints = waypoints;
    }

    public String getPlanningText() {
        return planningText;
    }

    public void setPlanningText(String planningText) {
        this.planningText = planningText;
    }

    @Override
    public String toString() {
        return "Vastaus1{" +
                "answerWorksheetID=" + answerID +
                ", worksheetID=" + worksheetID +
                ", username='" + username + '\'' +
                ", planningText='" + planningText + '\'' +
                ", waypoints=" + waypoints +
                '}';
    }
}
