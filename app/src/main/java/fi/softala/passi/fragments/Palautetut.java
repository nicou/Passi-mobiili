package fi.softala.passi.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import fi.softala.passi.R;
import fi.softala.passi.adapters.PalauteAdapter;
import fi.softala.passi.models.Answersheet;

/**
 * Created by villeaaltonen on 08/11/2016.
 */

public class Palautetut extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<Answersheet> kortit;

    public Palautetut() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            kortit = getArguments().getParcelableArrayList("kortit");
        }
        asetaData(kortit);
    }

    private void asetaData(List<Answersheet> kortit) {
        Log.d("Passi", "Korttien maara" + kortit.size());
        adapter = new PalauteAdapter(kortit, R.layout.kuvaboksi2, new PalauteAdapter.OnClickListener() {
            @Override
            public void onClick(Answersheet vastaus) {
                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage(vastaus.getInstructorComment())
                        .setTitle(vastaus.getWorksheetName());

                // 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_palautetut, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.palautetut_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        return v;
    }


}
