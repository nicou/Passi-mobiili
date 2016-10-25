package fi.softala.passi.adapters;

/**
 * Created by 1 on 13/10/16.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;

import fi.softala.passi.R;
import fi.softala.passi.models.WorksheetWaypoints;

public class KorttiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<WorksheetWaypoints> SubjectNames;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    private KorttiAdapter.OnItemClickListener mListener;
    private onRadioButtonCheckChange mChangeListener;
    private KorttiAdapter.OnTextChangeListener mTextListener;
    private KorttiAdapter.OnClickListener mClickListener;

    public KorttiAdapter(List<WorksheetWaypoints> SubjectNames,
                         KorttiAdapter.OnItemClickListener listener,
                         onRadioButtonCheckChange mlgListener,
                         KorttiAdapter.OnTextChangeListener textChangeListener,
                        KorttiAdapter.OnClickListener clickListener) {
        this.SubjectNames = SubjectNames;
        this.mListener = listener;
        this.mChangeListener = mlgListener;
        this.mTextListener = textChangeListener;
        this.mClickListener = clickListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == TYPE_ITEM) {
            final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_etappi, viewGroup, false);
            return new KorttiAdapter.ViewHolder(view);
        } else if (i == TYPE_FOOTER) {
            final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_lahetys, viewGroup, false);
            return new KorttiAdapter.FooterViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder Viewholder, int i) {
        if (Viewholder instanceof KorttiAdapter.FooterViewHolder) {
            KorttiAdapter.FooterViewHolder footerHolder = (KorttiAdapter.FooterViewHolder) Viewholder;
            footerHolder.bind(mClickListener);
        } else if (Viewholder instanceof KorttiAdapter.ViewHolder) {
            KorttiAdapter.ViewHolder viewHolder = (KorttiAdapter.ViewHolder) Viewholder;
            WorksheetWaypoints waypoints = SubjectNames.get(i);
            viewHolder.bind(waypoints, mListener, mChangeListener, mTextListener);
        }
    }

    private WorksheetWaypoints getWaypoint(int position) {
        return SubjectNames.get(position);
    }

    @Override
    public int getItemCount() {
        return SubjectNames.size() + 1;
    }

    public interface OnItemClickListener {
        void onItemClick(WorksheetWaypoints points, Context context, ImageButton camera);
    }

    public interface onRadioButtonCheckChange {
        void onCheck(int points, int radioID);
    }

    public interface OnTextChangeListener {
        void onTextChange(int points, String radioID);
    }

    public interface OnClickListener {
        void onClick();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        EditText editText;
        ImageButton camera;
        Context context;
        RadioGroup radioGroup;
        RadioButton button1;
        RadioButton button2;
        RadioButton button3;
        EditText selostus;

        public ViewHolder(View view) {
            super(view);
            context = view.getContext();
            editText = (EditText) view.findViewById(R.id.editTextEtappi);
            camera = (ImageButton) view.findViewById(R.id.kameraButton1);
            radioGroup = (RadioGroup) view.findViewById(R.id.radio1);
            button1 = (RadioButton) view.findViewById(R.id.radioButton1);
            button2 = (RadioButton) view.findViewById(R.id.radioButton2);
            button3 = (RadioButton) view.findViewById(R.id.radioButton3);
            selostus = (EditText) view.findViewById(R.id.selostusKentta1);
        }

        /*
            Bindaa tiettyihin nappuloihin tietyt tekstit, id:t ja kuuntelijat
            id:t ja tekstit löytyvät etapin tiedoista hakemalla
         */
        public void bind(final WorksheetWaypoints waypoints,
                         final KorttiAdapter.OnItemClickListener mListener,
                         final KorttiAdapter.onRadioButtonCheckChange mChangeListener,
                         final KorttiAdapter.OnTextChangeListener mTextListener) {
            final int id = waypoints.getWaypointID();
            button1.setId(waypoints.getWaypointOptions().get(0).getOptionID());
            button2.setId(waypoints.getWaypointOptions().get(1).getOptionID());
            button3.setId(waypoints.getWaypointOptions().get(2).getOptionID());
            editText.setText(id + ". " + waypoints.getWaypointTask());

            selostus.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mTextListener.onTextChange(id, selostus.getText().toString());
                    Log.v("Passi", "Text changed its= " + selostus.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == -1) {
                        Log.v("Passi", "Android bug");
                    } else {
                        Log.v("Passi", "Painettu! waypointid = " + id + " radiobutton id " + checkedId);
                        mChangeListener.onCheck(id, checkedId);
                    }
                }
            });

            camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(waypoints, context, camera);
                }
            });
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionFooter(position)) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionFooter(int position) {
        return position == SubjectNames.size();
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

        ImageButton lahetaNappula;

        public FooterViewHolder(View itemView) {
            super(itemView);
            lahetaNappula = (ImageButton) itemView.findViewById(R.id.lahetaNappula);
        }

        public void bind(final KorttiAdapter.OnClickListener mClickListener) {
            lahetaNappula.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onClick();
                }
            });
        }

    }

}