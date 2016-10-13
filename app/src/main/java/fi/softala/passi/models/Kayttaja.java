package fi.softala.passi.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vllle on 1.10.2016.
 */

public class Kayttaja {

    @SerializedName("userID")
    private String userID;
    @SerializedName("username")
    private String username;
    @SerializedName("firstname")
    private String firstname;
    @SerializedName("lastname")
    private String lastname;
    @SerializedName("email")
    private String email;
    @SerializedName("phone")
    private String phone;
    @SerializedName("groups")
    private List<Ryhma> ryhmat;

    public Kayttaja(String userID, String username, String firstname, String lastname, String email, String phone, List<Ryhma> ryhmat) {
        this.userID = userID;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.ryhmat = ryhmat;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Ryhma> getRyhmat() {
        return ryhmat;
    }

    public void setRyhmat(List<Ryhma> ryhmat) {
        this.ryhmat = ryhmat;
    }
}
