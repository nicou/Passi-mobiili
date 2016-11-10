package fi.softala.tyokykypassi.models;

import java.util.List;

/**
 * Created by 1 on 10/11/16.
 */

public class Category {

    Integer categoryID;
    String categoryName;
    List <Worksheet> categoryWorksheets;

    public Category(Integer categoryID, String categoryName, List<Worksheet> categoryWorksheets) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.categoryWorksheets = categoryWorksheets;
    }

    public Category(){
    }

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<Worksheet> getCategoryWorksheets() {
        return categoryWorksheets;
    }

    public void setCategoryWorksheets(List<Worksheet> categoryWorksheets) {
        this.categoryWorksheets = categoryWorksheets;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryID=" + categoryID +
                ", categoryName='" + categoryName + '\'' +
                ", categoryWorksheets=" + categoryWorksheets +
                '}';
    }
}
