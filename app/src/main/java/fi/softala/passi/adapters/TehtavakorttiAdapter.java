package fi.softala.passi.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

import fi.softala.passi.R;
import fi.softala.passi.models.Worksheet;

/**
 * Created by villeaaltonen on 13/10/2016.
 */

public class TehtavakorttiAdapter extends RecyclerView.Adapter<TehtavakorttiAdapter.TehtavakorttiHolder> {
    private List<Worksheet> kortit;
    private int rowLayout;
    private Context context;
    private TehtavakorttiAdapter.OnItemClickListener mListener;

    public static class TehtavakorttiHolder extends RecyclerView.ViewHolder {
        LinearLayout groupLayout;
        Button groupButton;

        public TehtavakorttiHolder(View v) {
            super(v);
            groupLayout = (LinearLayout) v.findViewById(R.id.group_layout);
            groupButton = (Button) v.findViewById(R.id.group_button);
        }

        public void bind(final Worksheet kortti, final TehtavakorttiAdapter.OnItemClickListener mListener){

            groupButton.setText(kortti.getWorksheetHeader());
            groupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(kortti);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Worksheet kortti);
    }

    public TehtavakorttiAdapter(Context context, List<Worksheet> kortit, int rowLayout, TehtavakorttiAdapter.OnItemClickListener listener) {
        this.context = context;
        this.kortit = kortit;
        this.rowLayout = rowLayout;
        this.mListener = listener;
    }

    @Override
    public TehtavakorttiAdapter.TehtavakorttiHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new TehtavakorttiAdapter.TehtavakorttiHolder(view);
    }

    @Override
    public void onBindViewHolder(final TehtavakorttiAdapter.TehtavakorttiHolder holder, final int position) {
        Worksheet worksheet = kortit.get(position);
        holder.bind(worksheet, mListener);
    }

    @Override
    public int getItemCount() {
        return kortit.size();
    }
}
