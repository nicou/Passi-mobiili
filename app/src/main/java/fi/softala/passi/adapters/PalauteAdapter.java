package fi.softala.passi.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import fi.softala.passi.R;
import fi.softala.passi.models.Answersheet;

/**
 * Created by villeaaltonen on 01/11/2016.
 */

public class PalauteAdapter extends RecyclerView.Adapter<PalauteAdapter.PalauteHolder> {
    private int rowLayout;
    List<Answersheet> vastaukset;
    private OnClickListener mListener;

    public static class PalauteHolder extends RecyclerView.ViewHolder {
        TextView tehtavakorttiNimi;
        TextView opeKommentti;

        public PalauteHolder(View v) {
            super(v);
            tehtavakorttiNimi = (TextView) v.findViewById(R.id.tehtavakortti_nimi);
            opeKommentti = (TextView) v.findViewById(R.id.opettaja_kommentti);
        }

        public void bind(final Answersheet vastaus, final OnClickListener mListener) {

            tehtavakorttiNimi.setText(vastaus.getWorksheetName());
            String opeComment = vastaus.getInstructorComment();
            if (opeComment.length() > 0) {
                opeKommentti.setText(opeComment);
                opeKommentti.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onClick(vastaus);
                    }
                });
            } else {
                opeKommentti.setText("EI KOMMENTOITAVAA HJUVA");
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
