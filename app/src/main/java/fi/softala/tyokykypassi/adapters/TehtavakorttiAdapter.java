package fi.softala.tyokykypassi.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import fi.softala.tyokykypassi.R;
import fi.softala.tyokykypassi.models.Worksheet;

/**
 * Created by villeaaltonen on 13/10/2016.
 */

public class TehtavakorttiAdapter extends RecyclerView.Adapter<TehtavakorttiAdapter.TehtavakorttiHolder> {
    private final List<Worksheet> kortit;
    private final int rowLayout;
    private final TehtavakorttiAdapter.OnItemClickListener mListener;
    private static Context context;

    public static class TehtavakorttiHolder extends RecyclerView.ViewHolder {
        final LinearLayout groupLayout;
        final Button groupButton;

        public TehtavakorttiHolder(View v) {
            super(v);
            groupLayout = (LinearLayout) v.findViewById(R.id.group_layout);
            groupButton = (Button) v.findViewById(R.id.group_button);
        }

        public void bind(final Worksheet kortti, final TehtavakorttiAdapter.OnItemClickListener mListener) {

            groupButton.setText(kortti.getWorksheetHeader());
            if (kortti.isWorksheetCompleted()) {
                Drawable backgroundCompleted = context.getResources().getDrawable(R.drawable.ammatin_tyokykyvalmiudet_small_complete);
                groupButton.setBackground(backgroundCompleted);

                groupButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(), "Tehtäväkortti on jo suoritettu", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                groupButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onItemClick(kortti);
                    }
                });
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Worksheet kortti);
    }

    public TehtavakorttiAdapter(List<Worksheet> kortit, int rowLayout, TehtavakorttiAdapter.OnItemClickListener listener, Context context) {
        this.kortit = kortit;
        this.rowLayout = rowLayout;
        this.mListener = listener;
        this.context = context;
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
