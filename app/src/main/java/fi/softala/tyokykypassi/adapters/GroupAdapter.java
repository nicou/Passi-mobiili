package fi.softala.tyokykypassi.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.List;

import fi.softala.tyokykypassi.R;
import fi.softala.tyokykypassi.models.Ryhma;

/**
 * Created by villeaaltonen on 11/10/2016.
 */

public class GroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Ryhma> ryhmat;
    private final int rowLayout;
    private final OnItemClickListener mListener;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private final GroupAdapter.OnClickListener mClickListener;


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

    public GroupAdapter(List<Ryhma> ryhmat, int rowLayout, OnItemClickListener listener, GroupAdapter.OnClickListener liste) {
        this.ryhmat = ryhmat;
        this.rowLayout = rowLayout;
        this.mListener = listener;
        this.mClickListener = liste;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        if (viewType == TYPE_ITEM) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
            return new GroupAdapter.GroupViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
            return new GroupAdapter.FooterViewHolder(view);
        } else {
            return null;
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof GroupAdapter.FooterViewHolder){
            GroupAdapter.FooterViewHolder footerHolder = (GroupAdapter.FooterViewHolder) holder;
            footerHolder.bind(mClickListener);

        }else if(holder instanceof GroupAdapter.GroupViewHolder){
            GroupAdapter.GroupViewHolder holde = (GroupAdapter.GroupViewHolder) holder;
            Ryhma r = ryhmat.get(position);
            holde.bind(r, mListener);
        }

    }

    @Override
    public int getItemCount() {
        return ryhmat.size() + 1;
    }

    public int getItemViewType(int position) {
        if (isPositionFooter(position)) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    public interface OnClickListener {
        void onClick();
    }

    private boolean isPositionFooter(int position) {
        return position == ryhmat.size();
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {

        final Button lisaaRyhma;

        public FooterViewHolder(View itemView) {
            super(itemView);
            lisaaRyhma = (Button) itemView.findViewById(R.id.group_button);

        }

        public void bind(final GroupAdapter.OnClickListener mClickListener) {
            lisaaRyhma.setText("Lisää uusi ryhmä");
            lisaaRyhma.setBackgroundResource(R.drawable.nappi);
            lisaaRyhma.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onClick();
                }

            });


        }

    }

}
