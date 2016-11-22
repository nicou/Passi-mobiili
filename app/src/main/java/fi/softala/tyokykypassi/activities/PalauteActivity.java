package fi.softala.tyokykypassi.activities;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fi.softala.tyokykypassi.R;
import fi.softala.tyokykypassi.adapters.PalauteAdapter;
import fi.softala.tyokykypassi.fragments.Palaute;
import fi.softala.tyokykypassi.fragments.Palautetut;
import fi.softala.tyokykypassi.models.Answerpoints;
import fi.softala.tyokykypassi.models.Answersheet;
import fi.softala.tyokykypassi.models.Category;
import fi.softala.tyokykypassi.models.Worksheet;
import fi.softala.tyokykypassi.network.PassiClient;
import fi.softala.tyokykypassi.network.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PalauteActivity extends ToolbarActivity implements Palaute.OnFragmentInteractionListener {

    private PassiClient passiClient;
    int groupId;
    int userId;
    private List<Answersheet> tekemattomatKortit;
    private List<Answersheet> tehdytKortit;
    Call<Answersheet> vastaus;
    Call<List<Category>> call;
    boolean lopetettu;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (call.isExecuted()) {
            call.cancel();
        }
        if (vastaus.isExecuted()) {
            vastaus.cancel();
        }
        lopetettu = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palaute);

        //Luo iconeille listenerit ja lähettää valikkoActivityyn, jossa id:n perusteella toiminnot
        ImageButton imHome = (ImageButton) findViewById(R.id.home);
        ImageButton imFeedback = (ImageButton) findViewById(R.id.feedback);
        ImageButton imLogout = (ImageButton) findViewById(R.id.logout);


        imHome.setOnClickListener(this);
        imFeedback.setOnClickListener(this);
        imLogout.setOnClickListener(this);

        if (findViewById(R.id.fragment_container_palaute) != null) {
            if (savedInstanceState != null) {
                return;
            }
            Palaute palaute = new Palaute();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container_palaute, palaute)
                    .commit();
        }

        SharedPreferences mySharedPreferences = this.getSharedPreferences("konfiguraatio", Context.MODE_PRIVATE);

        String base = mySharedPreferences.getString("token", null);
        groupId = 1;
        userId = Integer.parseInt(mySharedPreferences.getString("userID", null));

        passiClient = ServiceGenerator.createService(PassiClient.class, base);


        call = passiClient.haeTehtavakortit(groupId);
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {

                if (response.isSuccessful()) {
                    List<Category> kategoriat = response.body();
                    List<Worksheet> tehtavaKortit = new ArrayList<Worksheet>();
                    for (Category cat :
                            kategoriat
                            ) {
                        tehtavaKortit.addAll(cat.getCategoryWorksheets());
                    }
                    new haeVastaukset().execute(tehtavaKortit);

                } else {
                    Toast.makeText(PalauteActivity.this, "Korttien haku epäonnistui", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("Passi", "Tehtäväkorttien haku epäonnistui " + t.toString());
            }
        });


    }

    @Override
    public void onFragmentInteraction(int valinta) {
        // palautetut tehtavakortit
        Palautetut palautetut = new Palautetut();
        Bundle args = new Bundle();
        if (valinta == 1) {
            args.putParcelableArrayList("kortit", (ArrayList<? extends Parcelable>) tehdytKortit);

        } else if (valinta == 2) {
            args.putParcelableArrayList("kortit", (ArrayList<? extends Parcelable>) tekemattomatKortit);

        }
        palautetut.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container_palaute, palautetut);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    //atm vastaus pitää poistaa enne uuden lisäämistä samaan vastaukseen
    private class haeVastaukset extends AsyncTask<List<Worksheet>, Object, List<Answersheet>> {
        
        @SafeVarargs
        @Override
        protected final List<Answersheet> doInBackground(List<Worksheet>... path) {
            List<Answersheet> vastaukset = new ArrayList<>();
            tekemattomatKortit = new ArrayList<>();
            for (Worksheet tehtavakortti :
                    path[0]) {
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
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.include);
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.fragment_container_palaute);
                ConstraintLayout palauteBoksi = (ConstraintLayout) frameLayout.findViewById(R.id.boksi_palautettu);
                TextView otsikko = (TextView) palauteBoksi.findViewById(R.id.boksi_maara);
                otsikko.setText("Arvioituja: " + tehdytKortit.size());
                ConstraintLayout palauttamattomatBoksi = (ConstraintLayout) frameLayout.findViewById(R.id.boksi_palauttamatta);
                TextView otsikkoPalauttamatta = (TextView) palauttamattomatBoksi.findViewById(R.id.palauttamatta_maara);
                otsikkoPalauttamatta.setText("Odottaa palautetta: " + tekemattomatKortit.size());

                progressBar.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
            }

        }
    }


}
