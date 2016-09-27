package fi.softala.passi;

/**
 * Created by villeaaltonen on 27/09/16.
 */

public class Vastaus {
    String suunnitelma;
    String selostus;
    String kuvaURI;
    String valinta;

    public Vastaus() {
    }

    public String getSuunnitelma() {
        return suunnitelma;
    }

    public void setSuunnitelma(String suunnitelma) {
        this.suunnitelma = suunnitelma;
    }

    public String getSelostus() {
        return selostus;
    }

    public void setSelostus(String selostus) {
        this.selostus = selostus;
    }

    public String getKuvaURI() {
        return kuvaURI;
    }

    public void setKuvaURI(String kuvaURI) {
        this.kuvaURI = kuvaURI;
    }

    public String getValinta() {
        return valinta;
    }

    public void setValinta(String valinta) {
        this.valinta = valinta;
    }
}
