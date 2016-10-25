package fi.softala.passi.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import fi.softala.passi.R;
import fi.softala.passi.adapters.GroupAdapter;
import fi.softala.passi.models.Kayttaja;
import fi.softala.passi.models.Ryhma;
import fi.softala.passi.network.PassiClient;
import fi.softala.passi.network.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Ryhmat extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar mProgressBar;
    private RecyclerView.Adapter adapter;

    private OnFragmentInteractionListener mListener;

    public Ryhmat() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        getRyhmat();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ryhmat, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mProgressBar = (ProgressBar) v.findViewById(R.id.include);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
        void onFragmentInteraction(Ryhma uri);
    }

    public void getRyhmat() {
        SharedPreferences mySharedPreferences = this.getActivity().getSharedPreferences("konfiguraatio", Context.MODE_PRIVATE);
        String base = mySharedPreferences.getString("token", null);
        String tunnus = mySharedPreferences.getString("tunnus", null);

        PassiClient service = ServiceGenerator.createService(PassiClient.class, base);

        Call<Kayttaja> call = service.haeKayttaja(tunnus);

        call.enqueue(new Callback<Kayttaja>() {
            @Override
            public void onResponse(Call<Kayttaja> call, Response<Kayttaja> response) {
                if (response.isSuccessful()) {
                    List<Ryhma> ryhmat = response.body().getRyhmat();
                    asetaData(ryhmat);
                } else {
                    Toast.makeText(getActivity(), "Haku epäonnistui koodi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Kayttaja> call, Throwable t) {
                Log.e("Passi", "Ryhmien haku epäonnistui " + t.toString());
            }
        });
    }

    public void asetaData(List<Ryhma> ryhmat) {

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new GroupAdapter(
                getActivity(),
                ryhmat,
                R.layout.button_layout,
                new GroupAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Ryhma ryhma) {
                        Toast.makeText(getActivity(), "Ryhmaid " + ryhma.getGroupID(), Toast.LENGTH_SHORT).show();
                        mListener.onFragmentInteraction(ryhma);
                    }
                }
        );
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);

    }
}
