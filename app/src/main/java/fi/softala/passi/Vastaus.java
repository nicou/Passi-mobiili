package fi.softala.passi;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by villeaaltonen on 27/09/16.
 */

public class Vastaus {
    String planningText;
    ArrayList<String> selectedOptionID;
    ArrayList<File> image;
    ArrayList<String> selostukset;

    public ArrayList<String> getSelostukset() {
        return selostukset;
    }

    public void setSelostukset(ArrayList<String> selostukset) {
        this.selostukset = selostukset;
    }

    public Vastaus() {
    }

    public String getPlanningText() {
        return planningText;
    }

    public void setPlanningText(String planningText) {
        this.planningText = planningText;
    }

    public ArrayList<String> getSelectedOptionID() {
        return selectedOptionID;
    }

    public void setSelectedOptionID(ArrayList<String> selectedOptionID) {
        this.selectedOptionID = selectedOptionID;
    }

    public ArrayList<File> getImage() {
        return image;
    }

    public void setImage(ArrayList<File> image) {
        this.image = image;
    }
}
