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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;

import fi.softala.passi.R;
import fi.softala.passi.models.WorksheetWaypoints;

public class KorttiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final List<WorksheetWaypoints> SubjectNames;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private final KorttiAdapter.OnItemClickListener mListener;
    private final onRadioButtonCheckChange mChangeListener;
    private final KorttiAdapter.OnTextChangeListener mTextListener;
    private final KorttiAdapter.OnClickListener mClickListener;

    public KorttiAdapter(List<WorksheetWaypoints> SubjectNames,
                         KorttiAdapter.OnItemClickListener listener,
                         KorttiAdapter.onRadioButtonCheckChange mlgListener,
                         KorttiAdapter.OnTextChangeListener textChangeListener,
                        KorttiAdapter.OnClickListener clickListener) {
        this.SubjectNames = SubjectNames;
        this.mListener = listener;
        this.mChangeListener = mlgListener;
        this.mTextListener = textChangeListener;
        this.mClickListener = clickListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_etappi, viewGroup, false);
            return new KorttiAdapter.ViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_lahetys, viewGroup, false);
            return new KorttiAdapter.FooterViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder,final int position) {
        if (holder instanceof KorttiAdapter.FooterViewHolder) {
            KorttiAdapter.FooterViewHolder footerHolder = (KorttiAdapter.FooterViewHolder) holder;
            footerHolder.bind(mClickListener);
        } else if (holder instanceof KorttiAdapter.ViewHolder) {
            KorttiAdapter.ViewHolder viewHolder = (KorttiAdapter.ViewHolder) holder;
            WorksheetWaypoints waypoints = SubjectNames.get(position);
            viewHolder.bind(position, waypoints, mListener, mChangeListener, mTextListener);
        }
    }


    @Override
    public int getItemCount() {
        return SubjectNames.size() + 1;
    }

    public interface OnItemClickListener {
        void onItemClick(WorksheetWaypoints points, Context context, ImageButton camera);
    }

    public interface onRadioButtonCheckChange {
        void onCheck(int waypointId, int radioID);
    }

    public interface OnTextChangeListener {
        void onTextChange(int waypointId, String text);
    }

    public interface OnClickListener {
        void onClick();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final EditText editText;
        final ImageButton camera;
        final Context context;
        final RadioGroup radioGroup;
        final RadioButton button1;
        final RadioButton button2;
        final RadioButton button3;
        final EditText selostus;

        public ViewHolder(View view) {
            super(view);
            context = view.getContext();
            editText = (EditText) view.findViewById(R.id.editTextEtappi);
            camera = (ImageButton) view.findViewById(R.id.kameraButton1);
            radioGroup = (RadioGroup) view.findViewById(R.id.etappi_radiogroup);
            button1 = (RadioButton) view.findViewById(R.id.etappi_radiobutton_green);
            button2 = (RadioButton) view.findViewById(R.id.etappi_radiobutton_yellow);
            button3 = (RadioButton) view.findViewById(R.id.etappi_radiobutton_red);
            selostus = (EditText) view.findViewById(R.id.selostusKentta1);
        }

        /*
            Bindaa tiettyihin nappuloihin tietyt tekstit, id:t ja kuuntelijat
            id:t ja tekstit löytyvät etapin tiedoista hakemalla
         */
        public void bind(int position, final WorksheetWaypoints waypoints,
                         final OnItemClickListener mListener,
                         final onRadioButtonCheckChange mChangeListener,
                         final OnTextChangeListener mTextListener) {
            final int id = waypoints.getWaypointID();

            final boolean cameraValinta = waypoints.getWaypointPhotoEnabled();
            if (cameraValinta == false){
                camera.setVisibility(View.GONE);
            }

            button1.setId(waypoints.getWaypointOptions().get(0).getOptionID());
            button2.setId(waypoints.getWaypointOptions().get(1).getOptionID());
            button3.setId(waypoints.getWaypointOptions().get(2).getOptionID());

            button1.setText(waypoints.getWaypointOptions().get(0).getOptionText());
            button2.setText(waypoints.getWaypointOptions().get(1).getOptionText());
            button3.setText(waypoints.getWaypointOptions().get(2).getOptionText());

            position = position+1;
            editText.setText(position + ". " + waypoints.getWaypointTask());

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

     public static class FooterViewHolder extends RecyclerView.ViewHolder {

        final Button lahetaNappula;

        public FooterViewHolder(View itemView) {
            super(itemView);
            lahetaNappula = (Button) itemView.findViewById(R.id.lahetaNappula);
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