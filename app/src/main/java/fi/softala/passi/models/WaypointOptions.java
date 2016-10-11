package fi.softala.passi.models;

/**
 * Created by 1 on 06/10/16.
 */

public class WaypointOptions {
    Integer optionID;
    String optionText;

    public WaypointOptions(String optionText, Integer optionID) {
        this.optionText = optionText;
        this.optionID = optionID;
    }

    public WaypointOptions(){

    }

    public Integer getOptionID() {
        return optionID;
    }

    public void setOptionID(Integer optionID) {
        this.optionID = optionID;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    @Override
    public String toString() {
        return "WaypointOptions{" +
                "optionID=" + optionID +
                ", optionText='" + optionText + '\'' +
                '}';
    }
}
