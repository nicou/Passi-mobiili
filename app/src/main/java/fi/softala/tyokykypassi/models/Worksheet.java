package fi.softala.tyokykypassi.models;


import java.util.List;

/**
 * Created by 1 on 06/10/16.
 */

public class Worksheet {

    private Integer worksheetID;
    private String worksheetHeader;
    private String worksheetPreface;
    private String worksheetPlanning;
    private List<WorksheetWaypoints> worksheetWaypoints;

    public Worksheet(Integer worksheetID, String worksheetHeader, String worksheetPreface, String worksheetPlanning, List<WorksheetWaypoints> worksheetWaypoints) {
        this.worksheetID = worksheetID;
        this.worksheetHeader = worksheetHeader;
        this.worksheetPreface = worksheetPreface;
        this.worksheetPlanning = worksheetPlanning;
        this.worksheetWaypoints = worksheetWaypoints;
    }
    public Worksheet(){

    }

    public Integer getWorksheetID() {
        return worksheetID;
    }

    public void setWorksheetID(Integer worksheetID) {
        this.worksheetID = worksheetID;
    }

    public String getWorksheetHeader() {
        return worksheetHeader;
    }

    public void setWorksheetHeader(String worksheetHeader) {
        this.worksheetHeader = worksheetHeader;
    }

    public String getWorksheetPreface() {
        return worksheetPreface;
    }

    public void setWorksheetPreface(String worksheetPreface) {
        this.worksheetPreface = worksheetPreface;
    }

    public String getWorksheetPlanning() {
        return worksheetPlanning;
    }

    public void setWorksheetPlanning(String worksheetPlanning) {
        this.worksheetPlanning = worksheetPlanning;
    }

    public List<WorksheetWaypoints> getWorksheetWaypoints() {
        return worksheetWaypoints;
    }

    public void setWorksheetWaypoints(List<WorksheetWaypoints> worksheetWaypoints) {
        this.worksheetWaypoints = worksheetWaypoints;
    }

    @Override
    public String toString() {
        return "Worksheet{" +
                "worksheetID=" + worksheetID +
                ", worksheetHeader='" + worksheetHeader + '\'' +
                ", worksheetPreface='" + worksheetPreface + '\'' +
                ", worksheetPlanning='" + worksheetPlanning + '\'' +
                ", worksheetWaypoints=" + worksheetWaypoints +
                '}';
    }
}
