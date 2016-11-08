package fi.softala.passi.activities;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fi.softala.passi.R;
import fi.softala.passi.adapters.PalauteAdapter;
import fi.softala.passi.models.Answersheet;
import fi.softala.passi.models.Worksheet;
import fi.softala.passi.network.PassiClient;
import fi.softala.passi.network.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PalauteActivity extends ToolbarActivity {

    private PassiClient passiClient;
    int groupId;
    int userId;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

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

        recyclerView = (RecyclerView) findViewById(R.id.palaute_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

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

    public List<Answersheet> haeVastaukset(List<Worksheet> tehtavaKortit) throws IOException {
        List<Answersheet> vastaukset = new ArrayList<>();
        for (Worksheet tehtavakortti :
                tehtavaKortit) {
            int id = tehtavakortti.getWorksheetID();
            Call<Answersheet> vastaus = passiClient.haeOpettajanKommentit(
                    id,
                    groupId,
                    userId);

            Answersheet yksVastaus = vastaus.execute().body();
            vastaukset.add(yksVastaus);
        }
        return vastaukset;
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
                }


            }
            return vastaukset;
        }

        @Override
        protected void onPostExecute(List<Answersheet> result) {
            super.onPostExecute(result);
            adapter = new PalauteAdapter(result, R.layout.kuvaboksi2, new PalauteAdapter.OnClickListener() {
                @Override
                public void onClick(Answersheet vastaus) {
                    // 1. Instantiate an AlertDialog.Builder with its constructor
                    AlertDialog.Builder builder = new AlertDialog.Builder(PalauteActivity.this);

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
