package fi.softala.tyokykypassi.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by villeaaltonen on 17/11/2016.
 */

public class Kuvat implements Parcelable {
    List<File> otetutKuvat = new ArrayList<>();

    public Kuvat(List<File> otetutKuvat) {
        this.otetutKuvat = otetutKuvat;
    }


    protected Kuvat(Parcel in) {
    }

    public static final Creator<Kuvat> CREATOR = new Creator<Kuvat>() {
        @Override
        public Kuvat createFromParcel(Parcel in) {
            return new Kuvat(in);
        }

        @Override
        public Kuvat[] newArray(int size) {
            return new Kuvat[size];
        }
    };

    public List<File> getOtetutKuvat() {
        return otetutKuvat;
    }

    public void setOtetutKuvat(List<File> otetutKuvat) {
        this.otetutKuvat = otetutKuvat;
    }

    @Override
    public String toString() {
        return "Kuvat{" +
                "otetutKuvat=" + otetutKuvat +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
