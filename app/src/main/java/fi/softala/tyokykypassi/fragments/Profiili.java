package fi.softala.tyokykypassi.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import fi.softala.tyokykypassi.R;
import fi.softala.tyokykypassi.activities.LogotActivity;
import fi.softala.tyokykypassi.models.Kayttaja;
import fi.softala.tyokykypassi.network.PassiClient;
import fi.softala.tyokykypassi.network.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profiili extends Fragment {

    private ProgressBar mProgressBar;
    private LinearLayout profiiliWrapper;

    private OnProfiiliFragmentInteractionListener mListener;

    public Profiili() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getKayttaja();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_profiili, container, false);

        profiiliWrapper = (LinearLayout) v.findViewById(R.id.profiilitietoWrapper);
        mProgressBar = (ProgressBar) v.findViewById(R.id.include);

        Button button3 = (Button) v.findViewById(R.id.logo_button);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(Profiili.this.getActivity(), LogotActivity.class);
                startActivity(intent);

            }
        });

        Button Rekisteriseloste = (Button) v.findViewById(R.id.rekisteri_button);
        Rekisteriseloste.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Uri uri = Uri.parse("http://tyokykypassi.net/passi/static/combo_rekisteriseloste.pdf");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnProfiiliFragmentInteractionListener) {
            mListener = (OnProfiiliFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnProfiiliFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void renderoiKayttajatiedot(Kayttaja kayttaja) {
        TextView profiiliKokonimi = (TextView) profiiliWrapper.findViewById(R.id.profiiliKokonimi);
        TextView profiiliKayttajanimi = (TextView) profiiliWrapper.findViewById(R.id.profiiliKayttajanimi);
        TextView profiiliSahkoposti = (TextView) profiiliWrapper.findViewById(R.id.profiiliSahkoposti);
        String kokonimi = kayttaja.getFirstname() + " " + kayttaja.getLastname();
        profiiliKokonimi.setText(kokonimi);
        profiiliKayttajanimi.setText(kayttaja.getUsername());
        profiiliSahkoposti.setText(kayttaja.getEmail());

        mProgressBar.setVisibility(View.GONE);
        profiiliWrapper.setVisibility(View.VISIBLE);
    }

    public void getKayttaja() {
        SharedPreferences mySharedPreferences = this.getActivity().getSharedPreferences("konfiguraatio", Context.MODE_PRIVATE);
        String base = mySharedPreferences.getString("token", null);
        String tunnus = mySharedPreferences.getString("tunnus", null);

        PassiClient service = ServiceGenerator.createService(PassiClient.class, base);

        Call<Kayttaja> call = service.haeKayttaja(tunnus);

        call.enqueue(new Callback<Kayttaja>() {
            @Override
            public void onResponse(Call<Kayttaja> call, Response<Kayttaja> response) {
                if (response.isSuccessful()) {
                    renderoiKayttajatiedot(response.body());
                } else {
                    Toast.makeText(getActivity(), "Profiilitietojen haku ei onnistunut", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Kayttaja> call, Throwable t) {
                Log.e("Passi", "Ryhmien haku epäonnistui " + t.toString());
                Toast.makeText(getActivity(), "Profiilitietojen haku ei onnistunut", Toast.LENGTH_SHORT).show();
            }
        });
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
    public interface OnProfiiliFragmentInteractionListener {
        // TODO: Update argument type and name
        void onProfiiliFragmentInteraction();
    }
}
