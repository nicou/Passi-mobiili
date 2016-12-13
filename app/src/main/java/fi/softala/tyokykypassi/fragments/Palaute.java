package fi.softala.tyokykypassi.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fi.softala.tyokykypassi.R;
import fi.softala.tyokykypassi.adapters.PalauteAdapter;
import fi.softala.tyokykypassi.models.Answerpoints;
import fi.softala.tyokykypassi.models.Answersheet;
import fi.softala.tyokykypassi.models.Category;
import fi.softala.tyokykypassi.models.Worksheet;
import fi.softala.tyokykypassi.network.PassiClient;
import fi.softala.tyokykypassi.network.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by villeaaltonen on 08/11/2016.
 */

public class Palaute extends android.support.v4.app.Fragment {

    private Palaute.OnFragmentInteractionListener mListener;
    private final int PALAUTETUT_TEHTAVAKORTIT = 1;
    private final int PALAUTTAMATTOMAT_TEHTAVAKORTIT = 2;
    private PassiClient passiClient;
    int groupId;
    int userId;
    private List<Answersheet> tekemattomatKortit;
    private List<Answersheet> tehdytKortit;
    Call<Answersheet> vastaus;
    Call<List<Category>> call;
    boolean lopetettu;
    ProgressBar progressBar;
    ConstraintLayout palauteBoksi;
    ConstraintLayout palauttamattomatBoksi;

    public Palaute() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            groupId = getArguments().getInt("groupID");
        }
        haeVastauksetJotkaArvioitu();
    }

    private void haeVastauksetJotkaArvioitu() {
        SharedPreferences mySharedPreferences = getActivity().getSharedPreferences("konfiguraatio", Context.MODE_PRIVATE);

        String base = mySharedPreferences.getString("token", null);
        userId = Integer.parseInt(mySharedPreferences.getString("userID", null));

        passiClient = ServiceGenerator.createService(PassiClient.class, base);
        Call<List<Category>> hae = passiClient.haeTehtavakortit(groupId);

        hae.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {


                    List<Category> kategoriat = response.body();
                    List<Worksheet> tehtavaKortit = new ArrayList<Worksheet>();
                    for (Category cat :
                            kategoriat
                            ) {
                        tehtavaKortit.addAll(cat.getCategoryWorksheets());
                    }

                    new haeVastaukset().execute(tehtavaKortit);

                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });


    }

    private class haeVastaukset extends AsyncTask<List<Worksheet>, Object, List<Answersheet>> {


        @SafeVarargs
        @Override
        protected final List<Answersheet> doInBackground(List<Worksheet>... path) {
            List<Answersheet> vastaukset = new ArrayList<>();
            tekemattomatKortit = new ArrayList<>();

            for (Worksheet tehtavakortti :
                    path[0]) {
                if (lopetettu) {
                    break;
                }
                int id = tehtavakortti.getWorksheetID();

                vastaus = passiClient.haeOpettajanKommentit(
                        id,
                        groupId,
                        userId);

                Answersheet yksVastaus = null;

                try {
                    yksVastaus = vastaus.execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                boolean tehty = true;

                if (yksVastaus != null) {
                    yksVastaus.setWorksheetName(tehtavakortti.getWorksheetHeader());
                    for (Answerpoints aw : yksVastaus.getAnswerpointsList()) {
                        if (Integer.parseInt(aw.getInstructorRating()) == 0) {
                            tehty = false;
                        }
                    }
                    if (!tehty) {
                        yksVastaus.setTyyppi(PalauteAdapter.VIEW_PALAUTTAMATTA);
                        tekemattomatKortit.add(yksVastaus);
                    } else {
                        yksVastaus.setTyyppi(PalauteAdapter.VIEW_PALAUTETTU);
                        vastaukset.add(yksVastaus);
                    }
                }


            }
            return vastaukset;
        }


        @Override
        protected void onPostExecute(List<Answersheet> result) {
            super.onPostExecute(result);
            asetaData(result);
        }

        private void asetaData(List<Answersheet> result) {
            if (!lopetettu) {
                tehdytKortit = new ArrayList<>();
                tehdytKortit.addAll(result);

                TextView otsikko = (TextView) palauteBoksi.findViewById(R.id.boksi_maara);
                otsikko.setText("Arvioituja: " + tehdytKortit.size());
                TextView otsikkoPalauttamatta = (TextView) palauttamattomatBoksi.findViewById(R.id.palauttamatta_maara);
                otsikkoPalauttamatta.setText("Odottaa palautetta: " + tekemattomatKortit.size());
                progressBar.setVisibility(View.GONE);
                palauteBoksi.setVisibility(View.VISIBLE);
                palauttamattomatBoksi.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_palaute, container, false);
        palauteBoksi = (ConstraintLayout) v.findViewById(R.id.boksi_palautettu);
        palauttamattomatBoksi = (ConstraintLayout) v.findViewById(R.id.boksi_palauttamatta);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBarTest);
        ImageView palautetut = (ImageView) v.findViewById(R.id.button_palaute);
        ImageView palauttamattomat = (ImageView) v.findViewById(R.id.button_palauttamatta);
        palautetut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFragmentInteraction(PALAUTETUT_TEHTAVAKORTIT, tehdytKortit);
            }
        });
        palauttamattomat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFragmentInteraction(PALAUTTAMATTOMAT_TEHTAVAKORTIT, tekemattomatKortit);
            }
        });
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Palaute.OnFragmentInteractionListener) {
            mListener = (Palaute.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSaariFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if (call != null && call.isExecuted()) {
            call.cancel();
        }
        lopetettu = true;
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
        void onFragmentInteraction(int valinta, List<Answersheet> tekemattomatKortit);
    }

}
