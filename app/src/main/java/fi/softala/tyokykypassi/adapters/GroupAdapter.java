package fi.softala.tyokykypassi.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

import fi.softala.tyokykypassi.R;
import fi.softala.tyokykypassi.models.Ryhma;

/**
 * Created by villeaaltonen on 11/10/2016.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    private final List<Ryhma> ryhmat;
    private final int rowLayout;
    private final OnItemClickListener mListener;

    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        final LinearLayout groupLayout;
        final Button groupButton;

        public GroupViewHolder(View v) {
            super(v);

            groupLayout = (LinearLayout) v.findViewById(R.id.group_layout);
            groupButton = (Button) v.findViewById(R.id.group_button);
        }

        public void bind(final Ryhma ryhma, final OnItemClickListener mListener) {

            groupButton.setText(ryhma.getGroupName());
            groupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(ryhma);
                }
            });
        }


    }

    public interface OnItemClickListener {
        void onItemClick(Ryhma ryhma);
    }

    public GroupAdapter(List<Ryhma> ryhmat, int rowLayout, OnItemClickListener listener) {
        this.ryhmat = ryhmat;
        this.rowLayout = rowLayout;
        this.mListener = listener;
    }

    @Override
    public GroupAdapter.GroupViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new GroupAdapter.GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GroupViewHolder holder, final int position) {
        Ryhma r = ryhmat.get(position);
        holder.bind(r, mListener);
    }

    @Override
    public int getItemCount() {
        return ryhmat.size();
    }

}
