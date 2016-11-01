package fi.softala.passi.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import fi.softala.passi.R;
import fi.softala.passi.models.Answersheet;
import fi.softala.passi.models.Worksheet;

/**
 * Created by villeaaltonen on 01/11/2016.
 */

public class PalauteAdapter extends RecyclerView.Adapter<PalauteAdapter.PalauteHolder> {
    private int rowLayout;
    List<Answersheet> vastaukset;

    public static class PalauteHolder extends RecyclerView.ViewHolder {
        TextView tehtavakorttiNimi;
        TextView opeKommentti;

        public PalauteHolder(View v) {
            super(v);
            tehtavakorttiNimi = (TextView) v.findViewById(R.id.tehtavakortti_nimi);
            opeKommentti = (TextView) v.findViewById(R.id.opettaja_kommentti);
        }

        public void bind(Answersheet vastaus) {
            Log.d("Passi", "vastaus" + vastaus.toString());
            tehtavakorttiNimi.setText(vastaus.getWorksheetName());
            String opeComment = vastaus.getInstructorComment();
            if (opeComment.length() > 0 ) {
                opeKommentti.setText(opeComment);
            } else {
                opeKommentti.setText("EI KOMMENTOITAVAA");
            }


        }

    }

    public PalauteAdapter(List<Answersheet> vastaukset, int rowLayout) {
        this.vastaukset = vastaukset;
        this.rowLayout = rowLayout;
    }

    @Override
    public PalauteAdapter.PalauteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new PalauteAdapter.PalauteHolder(view);
    }

    @Override
    public void onBindViewHolder(PalauteAdapter.PalauteHolder holder, int position) {
        Answersheet vastaus = vastaukset.get(position);
        holder.bind(vastaus);
    }

    @Override
    public int getItemCount() {
        return vastaukset.size();
    }
}
