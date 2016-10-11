package fi.softala.passi.models;

import java.util.List;

/**
 * Created by 1 on 06/10/16.
 */

public class WorksheetWaypoints {
    Integer waypointID;
    String waypointTask;
    Boolean waypointPhotoEnabled;
    List<WaypointOptions> waypointOptions;


    public WorksheetWaypoints(Integer waypointID, String waypointTask, Boolean waypointPhotoEnabled, List<WaypointOptions> waypointOptions) {
        this.waypointID = waypointID;
        this.waypointTask = waypointTask;
        this.waypointPhotoEnabled = waypointPhotoEnabled;
        this.waypointOptions = waypointOptions;
    }

    public WorksheetWaypoints(){

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
