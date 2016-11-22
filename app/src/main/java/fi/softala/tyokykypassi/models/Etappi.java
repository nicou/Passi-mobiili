package fi.softala.tyokykypassi.models;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by joakimkajan on 29/09/16.
 */

public class Etappi implements Serializable{

    @SerializedName("waypointID")
    private int waypointID;
    @SerializedName("optionID")
    private int selectedOptionID;
    @SerializedName("imageURL")
    private String imageURL;
    @SerializedName("answerText")
    private String answerText;
    private Boolean photoEnabled;

    public Boolean getPhotoEnabled() {
        return photoEnabled;
    }

    public void setPhotoEnabled(Boolean photoEnabled) {
        this.photoEnabled = photoEnabled;
    }

    public Etappi(Integer waypointID, String imageURL, Integer selectedOptionID, String answerText) {
        this.waypointID = waypointID;
        this.imageURL = imageURL;
        this.selectedOptionID = selectedOptionID;
        this.answerText = answerText;

    }

    public Etappi() {

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

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    @Override
    public String toString() {
        return "Etappi{" +
                ", waypointID=" + waypointID +
                ", selectedOptionID=" + selectedOptionID +
                ", imageURL='" + imageURL + '\'' +
                ", answerText='" + answerText + '\'' +
                '}';
    }
}
