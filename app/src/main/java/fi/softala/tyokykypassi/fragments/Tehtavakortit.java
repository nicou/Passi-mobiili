package fi.softala.tyokykypassi.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import fi.softala.tyokykypassi.R;
import fi.softala.tyokykypassi.adapters.TehtavakorttiAdapter;
import fi.softala.tyokykypassi.models.Category;
import fi.softala.tyokykypassi.models.Ryhma;
import fi.softala.tyokykypassi.models.Worksheet;
import fi.softala.tyokykypassi.network.PassiClient;
import fi.softala.tyokykypassi.network.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by villeaaltonen on 25/10/2016.
 */

public class Tehtavakortit extends Fragment {

    RecyclerView recyclerView;
    ProgressBar mProgressBar;
    RecyclerView.Adapter adapter;
    Integer ryhma;
    Integer kategoria;

    private OnTehtavakortitFragmentInteractionListener mListener;

    public Tehtavakortit() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ryhma = getArguments().getInt("ryhmaID");
            kategoria = getArguments().getInt("kategoriaID");
        }
        getRyhmat(ryhma, kategoria);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ryhmat, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.ryhma_recycler_view);
        mProgressBar = (ProgressBar) v.findViewById(R.id.include);
        recyclerView.setAdapter(adapter);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTehtavakortitFragmentInteractionListener) {
            mListener = (OnTehtavakortitFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSaariFragmentInteractionListener");
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
    public interface OnTehtavakortitFragmentInteractionListener {
        // TODO: Update argument type and name
        void onTehtavakortitFragmentInteraction(int ryhmaID, String korttiJSON);
    }

    public void getRyhmat(int groupID, final int kategoriaID) {
        SharedPreferences mySharedPreferences = this.getActivity().getSharedPreferences("konfiguraatio", Context.MODE_PRIVATE);
        String token = mySharedPreferences.getString("token", null);
        PassiClient service = ServiceGenerator.createService(PassiClient.class, token);
        Call<List<Category>> call = service.haeTehtavakortit(groupID);
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {

                if (response.isSuccessful()) {
                    List<Category> kategoriat = response.body();
                    List<Worksheet> tehtavaKortit = new ArrayList<Worksheet>();
                    for (Category cat :
                            kategoriat) {
                        Integer jsonKategoria = cat.getCategoryID();
                        if(jsonKategoria == kategoriaID){
                            tehtavaKortit.addAll(cat.getCategoryWorksheets());

                        }

                    }
                    asetaData(tehtavaKortit);
                } else {
                    Toast.makeText(getActivity(), "Korttien haku epäonnistui", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("Passi", "Tehtäväkorttien haku epäonnistui " + t.toString());
            }
        });
    }

    public void asetaData(List<Worksheet> worksheets) {
        final Gson gson = new Gson();
        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(getActivity(), 2);

        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new TehtavakorttiAdapter(worksheets, R.layout.button_layout, new TehtavakorttiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Worksheet kortti) {
                String korttiJSON = gson.toJson(kortti);
                int ryhmaID = ryhma;
                mListener.onTehtavakortitFragmentInteraction(ryhmaID, korttiJSON);

            }
        });

        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(adapter);
        mProgressBar.setVisibility(View.GONE);


    }
}
