package fi.softala.passi.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import fi.softala.passi.R;
import fi.softala.passi.activities.Tehtavakortti;
import fi.softala.passi.adapters.TehtavakorttiAdapter;
import fi.softala.passi.models.Ryhma;
import fi.softala.passi.models.Worksheet;
import fi.softala.passi.network.PassiClient;
import fi.softala.passi.network.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by villeaaltonen on 25/10/2016.
 */

public class Tehtavakortit extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private ConstraintLayout constraintLayout;
    RecyclerView recyclerView;
    ProgressBar mProgressBar;
    RecyclerView.Adapter adapter;
    Ryhma ryhma;

    private Tehtavakortit.OnFragmentInteractionListener mListener;

    public Tehtavakortit() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ryhma = getArguments().getParcelable("ryhma");
        }
        getRyhmat(ryhma.getGroupID());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View test =   inflater.inflate(R.layout.fragment_ryhmat, container, false);
        recyclerView = (RecyclerView) test.findViewById(R.id.my_recycler_view);
        mProgressBar = (ProgressBar) test.findViewById(R.id.include);

        return test;
    }

    /*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    */

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Tehtavakortit.OnFragmentInteractionListener) {
            mListener = (Tehtavakortit.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int ryhmaID, String korttiJSON);
    }

    public void getRyhmat(int groupID) {
        SharedPreferences mySharedPreferences = this.getActivity().getSharedPreferences("konfiguraatio", Context.MODE_PRIVATE);
        String token = mySharedPreferences.getString("token", null);
        PassiClient service = ServiceGenerator.createService(PassiClient.class, token);
        Call<List<Worksheet>> call = service.haeTehtavakortit(groupID);
        call.enqueue(new Callback<List<Worksheet>>() {
            @Override
            public void onResponse(Call<List<Worksheet>> call, Response<List<Worksheet>> response) {
                List<Worksheet> tehtavaKortit = response.body();
                asetaData(tehtavaKortit);
            }

            @Override
            public void onFailure(Call<List<Worksheet>> call, Throwable t) {
                Toast.makeText(getActivity(), "Korttien haku epäonnistui" + t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void asetaData(List<Worksheet> worksheets) {
        final Gson gson = new Gson();
        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(getActivity(), 2);

        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new TehtavakorttiAdapter(getActivity(), worksheets, R.layout.button_layout, new TehtavakorttiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Worksheet kortti) {
                Toast.makeText(getActivity(), "WorksheetID" + kortti.getWorksheetID(), Toast.LENGTH_SHORT).show();
                String korttiJSON = gson.toJson(kortti);
                int ryhmaID = ryhma.getGroupID();
                mListener.onFragmentInteraction(ryhmaID, korttiJSON);

            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }
}
