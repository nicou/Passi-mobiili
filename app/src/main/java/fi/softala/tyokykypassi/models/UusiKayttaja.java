package fi.softala.tyokykypassi.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 1 on 24/11/16.
 */

public class UusiKayttaja extends Kayttaja {

    @SerializedName("password")
    String password;
    @SerializedName("passwordConfirm")
    String confirmPassword;

    public UusiKayttaja() {
        super();
    }

    public UusiKayttaja(String userID, String username, String firstname, String lastname, String email, String phone, List<Ryhma> ryhmat, String password, String confirmPassword) {
        super(userID, username, firstname, lastname, email, phone, ryhmat);
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
