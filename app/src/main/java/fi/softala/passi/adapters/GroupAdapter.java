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
import fi.softala.passi.models.Ryhma;

/**
 * Created by villeaaltonen on 11/10/2016.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder>  {

    private List<Ryhma> ryhmat;
    private int rowLayout;
    private Context context;
    private OnItemClickListener mListener;

    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        LinearLayout groupLayout;
        Button groupButton;

        public GroupViewHolder(View v) {
            super(v);

            groupLayout = (LinearLayout) v.findViewById(R.id.group_layout);
            groupButton = (Button) v.findViewById(R.id.group_button);
        }
        public void bind(final Ryhma ryhma, final OnItemClickListener mListener){

            groupButton.setText("id=" + ryhma.getGroupID() + " " + ryhma.getGroupName());
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

    public GroupAdapter(Context context, List<Ryhma> ryhmat, int rowLayout, OnItemClickListener listener) {
        this.context = context;
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
