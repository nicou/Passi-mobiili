package fi.softala.passi.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import fi.softala.passi.R;
import fi.softala.passi.adapters.TehtavakorttiAdapter;
import fi.softala.passi.models.Ryhma;
import fi.softala.passi.models.Worksheet;
import fi.softala.passi.network.PassiClient;
import fi.softala.passi.network.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TehtavakortitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tehtavakortit);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.tehtavakortit_recycleview);
        final ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        final Gson gson = new Gson();

        String ryhmaJSON = getIntent().getStringExtra("Group");
        Ryhma ryhma = gson.fromJson(ryhmaJSON, Ryhma.class);
        final int ryhmaID = ryhma.getGroupID();
        SharedPreferences mySharedPreferences = getSharedPreferences("konfiguraatio", Context.MODE_PRIVATE);

        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(gridLayoutManager);

        String base = mySharedPreferences.getString("token", "");

        PassiClient service =
                ServiceGenerator.createService(PassiClient.class, base);

        Call<List<Worksheet>> call = service.haeTehtavakortit(ryhmaID);
        call.enqueue(new Callback<List<Worksheet>>() {
            @Override
            public void onResponse(Call<List<Worksheet>> call, Response<List<Worksheet>> response) {
                List<Worksheet> tehtavaKortit = response.body();
                RecyclerView.Adapter adapter = new TehtavakorttiAdapter(getApplicationContext(), tehtavaKortit, R.layout.button_layout, new TehtavakorttiAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Worksheet kortti) {
                        Toast.makeText(getApplicationContext(), "WorksheetID" + kortti.getWorksheetID(), Toast.LENGTH_SHORT).show();
                        String korttiJSON = gson.toJson(kortti);
                        Intent intent = new Intent(getApplicationContext(), Tehtavakortti.class);
                        intent.putExtra("Tehtavakortti", korttiJSON);
                        intent.putExtra("ryhmaID", String.valueOf(ryhmaID));
                        startActivity(intent);

                    }
                });
                recyclerView.setAdapter(adapter);
                recyclerView.setHasFixedSize(true);
                recyclerView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Worksheet>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Korttien haku epäonnistui" + t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
