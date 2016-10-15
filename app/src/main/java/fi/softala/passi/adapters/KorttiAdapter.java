package fi.softala.passi.adapters;

/**
 * Created by 1 on 13/10/16.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

import fi.softala.passi.R;
import fi.softala.passi.models.WorksheetWaypoints;

public class KorttiAdapter extends RecyclerView.Adapter<KorttiAdapter.ViewHolder> {

    List<WorksheetWaypoints> SubjectNames;

    private KorttiAdapter.OnItemClickListener mListener;
    private onRadioButtonCheckChange mChangeListener;
    private KorttiAdapter.OnTextChangeListener mTextListener;
    public KorttiAdapter(List<WorksheetWaypoints> SubjectNames, KorttiAdapter.OnItemClickListener listener, onRadioButtonCheckChange mlgListener, KorttiAdapter.OnTextChangeListener textChangeListener) {
        this.SubjectNames = SubjectNames;
        this.mListener = listener;
        this.mChangeListener = mlgListener;
        this.mTextListener = textChangeListener;
    }


    @Override
    public KorttiAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_etappi, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final KorttiAdapter.ViewHolder Viewholder, final int i) {
        final WorksheetWaypoints waypoints = SubjectNames.get(i);
        final int id = waypoints.getWaypointID();

        Viewholder.editText.setText(id + ". " + waypoints.getWaypointTask());

        Viewholder.selotus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTextListener.onTextChange(id, Viewholder.selotus.getText().toString());
                Log.v("Passi", "Text changed its= " + Viewholder.selotus.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Viewholder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == -1) {
                    Log.v("Passi", "Android bug");
                }
                else {
                    Log.v("Passi", "Painettu! waypointid = " + id + " radiobutton id " + checkedId);
                    mChangeListener.onCheck(id, checkedId);
                }
            }
        });

        Viewholder.bind(waypoints, mListener);
    }

    @Override
    public int getItemCount() {
        return SubjectNames.size();
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        EditText editText;
        ImageButton camera;
        Context context;
        RadioGroup radioGroup;
        RadioButton button1;
        RadioButton button2;
        RadioButton button3;
        EditText selotus;
        public ViewHolder(View view) {
            super(view);
            context = view.getContext();
            editText = (EditText) view.findViewById(R.id.editTextEtappi);
            camera = (ImageButton) view.findViewById(R.id.kameraButton1);
            radioGroup = (RadioGroup) view.findViewById(R.id.radio1);
            button1 = (RadioButton) view.findViewById(R.id.radioButton1);
            button2 = (RadioButton) view.findViewById(R.id.radioButton2);
            button3 = (RadioButton) view.findViewById(R.id.radioButton3);
            selotus = (EditText) view.findViewById(R.id.selostusKentta1);
        }

        public void bind(final WorksheetWaypoints waypoints, final KorttiAdapter.OnItemClickListener mListener) {
            button1.setId(waypoints.getWaypointOptions().get(0).getOptionID());
            button2.setId(waypoints.getWaypointOptions().get(1).getOptionID());
            button3.setId(waypoints.getWaypointOptions().get(2).getOptionID());

            camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(waypoints, context, camera);
                }
            });
        }

    }

}