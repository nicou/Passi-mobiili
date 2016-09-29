package fi.softala.passi;

/**
 * Created by joakimkajan on 29/09/16.
 */

public class Etappi {

    Integer answeWaypointID;
    Integer answerID;
    Integer waypointID;
    Integer selectedOptionID;

    String imageURL;
    String answerText;
    String instructorComment;

    public Etappi(Integer answeWaypointID, Integer answerID, Integer waypointID, String imageURL, Integer selectedOptionID, String answerText, String instructorComment) {
        this.answeWaypointID = answeWaypointID;
        this.waypointID = waypointID;
        this.imageURL = imageURL;
        this.selectedOptionID = selectedOptionID;
        this.answerText = answerText;
        this.instructorComment = instructorComment;
        this.answerID = answerID;
    }

    public Etappi() {

    }

    public Integer getAnswerID() {
        return answerID;
    }

    public void setAnswerID(Integer answerID) {
        this.answerID = answerID;
    }

    public Integer getAnsweWaypointID() {
        return answeWaypointID;
    }

    public void setAnsweWaypointID(Integer answeWaypointID) {
        this.answeWaypointID = answeWaypointID;
    }

    public Integer getWaypointID() {
        return waypointID;
    }

    public void setWaypointID(Integer waypointID) {
        this.waypointID = waypointID;
    }

    public Integer getSelectedOptionID() {
        return selectedOptionID;
    }

    public void setSelectedOptionID(Integer selectedOptionID) {
        this.selectedOptionID = selectedOptionID;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getInstructorComment() {
        return instructorComment;
    }

    public void setInstructorComment(String instructorComment) {
        this.instructorComment = instructorComment;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    @Override
    public String toString() {
        return "Etappi{" +
                "answeWaypointID=" + answeWaypointID +
                ", waypointID=" + waypointID +
                ", selectedOptionID=" + selectedOptionID +
                ", imageURL='" + imageURL + '\'' +
                ", answerText='" + answerText + '\'' +
                ", instructorComment='" + instructorComment + '\'' +
                '}';
    }
}
