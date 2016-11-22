package fi.softala.tyokykypassi.models;

import java.util.List;

/**
 * Created by 1 on 06/10/16.
 */

public class WorksheetWaypoints {
    private Integer waypointID;
    private String waypointTask;
    private Boolean waypointPhotoEnabled;
    private List<WaypointOptions> waypointOptions;
    private String wanhaVastaus;
    private int wanhaRadioButtonValinta;

    public WorksheetWaypoints(Integer waypointID, String waypointTask, Boolean waypointPhotoEnabled, List<WaypointOptions> waypointOptions) {
        this.waypointID = waypointID;
        this.waypointTask = waypointTask;
        this.waypointPhotoEnabled = waypointPhotoEnabled;
        this.waypointOptions = waypointOptions;
    }

    public WorksheetWaypoints() {

    }

    public int getWanhaRadioButtonValinta() {
        return wanhaRadioButtonValinta;
    }

    public void setWanhaRadioButtonValinta(int wanhaRadioButtonValinta) {
        this.wanhaRadioButtonValinta = wanhaRadioButtonValinta;
    }

    public String getWanhaVastaus() {
        return wanhaVastaus;
    }

    public void setWanhaVastaus(String wanhaVastaus) {
        this.wanhaVastaus = wanhaVastaus;
    }

    public Integer getWaypointID() {
        return waypointID;
    }

    public void setWaypointID(Integer waypointID) {
        this.waypointID = waypointID;
    }

    public String getWaypointTask() {
        return waypointTask;
    }

    public void setWaypointTask(String waypointTask) {
        this.waypointTask = waypointTask;
    }

    public Boolean getWaypointPhotoEnabled() {
        return waypointPhotoEnabled;
    }

    public void setWaypointPhotoEnabled(Boolean waypointPhotoEnabled) {
        this.waypointPhotoEnabled = waypointPhotoEnabled;
    }

    public List<WaypointOptions> getWaypointOptions() {
        return waypointOptions;
    }

    public void setWaypointOptions(List<WaypointOptions> waypointOptions) {
        this.waypointOptions = waypointOptions;
    }

    @Override
    public String toString() {
        return "WorksheetWaypoints{" +
                "waypointID=" + waypointID +
                ", waypointTask='" + waypointTask + '\'' +
                ", waypointPhotoEnabled=" + waypointPhotoEnabled +
                ", waypointOptions=" + waypointOptions +
                '}';
    }
}
