package fi.softala.passi.adapters;

/**
 * Created by 1 on 13/10/16.
 */

        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;
        import java.util.ArrayList;
        import java.util.List;

        import fi.softala.passi.R;
        import fi.softala.passi.models.WorksheetWaypoints;

public class KorttiAdapter extends RecyclerView.Adapter<KorttiAdapter.ViewHolder>  {

    List<WorksheetWaypoints> SubjectNames;

    View view1;

    public KorttiAdapter(List<WorksheetWaypoints> SubjectNames) {

        this.SubjectNames = SubjectNames;
    }

    @Override
    public KorttiAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        view1  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_etappi, viewGroup, false);

        return new ViewHolder(view1);
    }

    @Override
    public void onBindViewHolder(KorttiAdapter.ViewHolder Viewholder, int i) {

    }

    @Override
    public int getItemCount() {

        return SubjectNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView SubjectTextView;
        public ViewHolder(View view) {

            super(view);

        }
    }

}