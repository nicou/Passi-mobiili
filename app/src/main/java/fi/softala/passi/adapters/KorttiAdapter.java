package fi.softala.passi.adapters;

/**
 * Created by 1 on 13/10/16.
 */

import android.content.Context;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.List;

import fi.softala.passi.R;
import fi.softala.passi.models.WorksheetWaypoints;

public class KorttiAdapter extends RecyclerView.Adapter<KorttiAdapter.ViewHolder> {

    List<WorksheetWaypoints> SubjectNames;

    View view1;
    private KorttiAdapter.OnItemClickListener mListener;

    public KorttiAdapter(List<WorksheetWaypoints> SubjectNames, KorttiAdapter.OnItemClickListener listener) {
        this.SubjectNames = SubjectNames;
        this.mListener = listener;
    }

    @Override
    public KorttiAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        view1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_etappi, viewGroup, false);
        return new ViewHolder(view1);
    }

    @Override
    public void onBindViewHolder(KorttiAdapter.ViewHolder Viewholder, int i) {
        WorksheetWaypoints waypoints = SubjectNames.get(i);
        int id = SubjectNames.get(i).getWaypointID();
        Viewholder.editText.setText(id + ". " + SubjectNames.get(i).getWaypointTask());
        Viewholder.bind(waypoints, mListener);
    }

    @Override
    public int getItemCount() {
        return SubjectNames.size();
    }

    public interface OnItemClickListener {
        void onItemClick(WorksheetWaypoints points, Context context, ImageButton camera);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        EditText editText;
        ImageButton camera;
        Context context;

        public ViewHolder(View view) {
            super(view);
            context = view.getContext();
            editText = (EditText) view.findViewById(R.id.editTextEtappi);
            camera = (ImageButton) view.findViewById(R.id.kameraButton1);
        }

        public void bind(final WorksheetWaypoints waypoints, final KorttiAdapter.OnItemClickListener mListener) {

            camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mListener.onItemClick(waypoints, context, camera);
                }
            });
        }
    }

}