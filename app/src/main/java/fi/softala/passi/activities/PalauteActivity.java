package fi.softala.passi.activities;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fi.softala.passi.R;
import fi.softala.passi.fragments.Palaute;
import fi.softala.passi.fragments.Palautetut;
import fi.softala.passi.fragments.Valikko;
import fi.softala.passi.models.Answersheet;
import fi.softala.passi.models.Worksheet;
import fi.softala.passi.network.PassiClient;
import fi.softala.passi.network.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PalauteActivity extends ToolbarActivity implements Palaute.OnFragmentInteractionListener {

    private PassiClient passiClient;
    int groupId;
    int userId;
    private List<Worksheet> tekemattomatKortit;
    private List<Answersheet> tehdytKortit;

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


        Call<List<Worksheet>> call = passiClient.haeTehtavakortit(groupId);
        call.enqueue(new Callback<List<Worksheet>>() {
            @Override
            public void onResponse(Call<List<Worksheet>> call, Response<List<Worksheet>> response) {

                if (response.isSuccessful()) {
                    List<Worksheet> tehtavaKortit = response.body();

                    new haeVastaukset().execute(tehtavaKortit);

                } else {
                    Toast.makeText(PalauteActivity.this, "Korttien haku epäonnistui", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Worksheet>> call, Throwable t) {
                Log.e("Passi", "Tehtäväkorttien haku epäonnistui " + t.toString());
            }
        });


    }

    @Override
    public void onFragmentInteraction(int valinta) {
        // palautetut tehtavakortit
        if (valinta == 1) {
            Palautetut palautetut = new Palautetut();
            Bundle args = new Bundle();
            args.putParcelableArrayList("kortit", (ArrayList<? extends Parcelable>) tehdytKortit);
            palautetut.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container_palaute, palautetut);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (valinta == 2) {
            Toast.makeText(this, "Ei tehty viela", Toast.LENGTH_SHORT).show();
        }
    }


    //atm vastaus pitää poistaa enne uuden lisäämistä samaan vastaukseen
    private class haeVastaukset extends AsyncTask<List<Worksheet>, Object, List<Answersheet>> {


        @Override
        protected void onPreExecute() {

            Toast.makeText(getApplicationContext(), "Haetaan vastauksia", Toast.LENGTH_LONG).show();
        }

        @SafeVarargs
        @Override
        protected final List<Answersheet> doInBackground(List<Worksheet>... path) {
            List<Answersheet> vastaukset = new ArrayList<>();
            tekemattomatKortit = new ArrayList<>();
            for (Worksheet tehtavakortti :
                    path[0]) {
                int id = tehtavakortti.getWorksheetID();
                Call<Answersheet> vastaus = passiClient.haeOpettajanKommentit(
                        id,
                        groupId,
                        userId);

                Answersheet yksVastaus = null;
                try {
                    yksVastaus = vastaus.execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if ((yksVastaus != null ? yksVastaus.getAnswersheetId() : null) != null) {
                    yksVastaus.setWorksheetName(tehtavakortti.getWorksheetHeader());
                    vastaukset.add(yksVastaus);
                } else {
                    tekemattomatKortit.add(tehtavakortti);
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
            tehdytKortit = new ArrayList<>();
            tehdytKortit.addAll(result);
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.include);
            FrameLayout frameLayout = (FrameLayout) findViewById(R.id.fragment_container_palaute);
            progressBar.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);
        }
    }

    //@Override
    //public void onBackPressed() {
    //  int fragments = getFragmentManager().getBackStackEntryCount();
    //if (fragments == 1) {
    //  finish();
    //}
    //super.onBackPressed();
    //}

}
