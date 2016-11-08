package fi.softala.passi.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import fi.softala.passi.R;
import fi.softala.passi.models.Answerpoints;
import fi.softala.passi.models.Answersheet;

/**
 * Created by villeaaltonen on 01/11/2016.
 */

public class PalauteAdapter extends RecyclerView.Adapter<PalauteAdapter.PalauteHolder> {
    private final int rowLayout;
    final List<Answersheet> vastaukset;
    private final OnClickListener mListener;

    public static class PalauteHolder extends RecyclerView.ViewHolder {
        final TextView tehtavakorttiNimi;
        final TextView opeKommentti;

        public PalauteHolder(View v) {
            super(v);
            tehtavakorttiNimi = (TextView) v.findViewById(R.id.tehtavakortti_nimi);
            opeKommentti = (TextView) v.findViewById(R.id.opettaja_kommentti);
        }

        public void bind(final Answersheet vastaus, final OnClickListener mListener) {

            tehtavakorttiNimi.setText(vastaus.getWorksheetName());

            List<Answerpoints> answ = vastaus.getAnswerpointsList();

            final int maxPisteet = answ.size() * 3;

            Boolean exist = false;
            String comment = "";
            int numero = 1;
            int pisteet = 0;

            for(Answerpoints ans : answ){
                String kommentti = null;
                kommentti = ans.getInstructorComment();

                String pisteString = ans.getInstructorRating();

                if(kommentti != null && kommentti.length()>0){
                    comment = comment + "Etappi " + numero + ": "+kommentti + "\nPisteet: " +pisteString + "\n \n";
                    pisteet = pisteet + Integer.valueOf(pisteString);
                    exist = true;
                }else{
                    comment = comment + "Etappi " + numero + ": "+"Ei vielä arvioitu" + "\n \n";

                }
                numero++;

            }

            comment = comment + "Kokonaispisteet: " + pisteet +"/" +maxPisteet;

            //Asetetaan ns. pääkommenttiin kaikkien etappien kommentti. Tämä säästää hieman refaktorointia.
            vastaus.setInstructorComment(comment);
            if (exist) {
                opeKommentti.setText(comment);
                opeKommentti.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onClick(vastaus);
                    }
                });
            } else {
                opeKommentti.setText("EI KOMMENTTEJA");
            }


        }

    }

    public PalauteAdapter(List<Answersheet> vastaukset, int rowLayout, OnClickListener listener) {
        this.vastaukset = vastaukset;
        this.rowLayout = rowLayout;
        this.mListener = listener;
    }

    public interface OnClickListener {
        void onClick(Answersheet vastaus);
    }

    @Override
    public PalauteAdapter.PalauteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new PalauteAdapter.PalauteHolder(view);
    }

    @Override
    public void onBindViewHolder(PalauteAdapter.PalauteHolder holder, int position) {
        Answersheet vastaus = vastaukset.get(position);
        holder.bind(vastaus, mListener);
    }

    @Override
    public int getItemCount() {
        return vastaukset.size();
    }
}
