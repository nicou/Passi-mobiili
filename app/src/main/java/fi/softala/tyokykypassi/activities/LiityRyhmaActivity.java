package fi.softala.tyokykypassi.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import fi.softala.tyokykypassi.R;
import fi.softala.tyokykypassi.models.Kayttaja;
import fi.softala.tyokykypassi.network.KuvaUploadaus;
import fi.softala.tyokykypassi.network.PassiClient;
import fi.softala.tyokykypassi.network.ServiceGenerator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiityRyhmaActivity extends ToolbarActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_liity_ryhma);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Button liityButton = (Button)findViewById(R.id.liityButton);
        final EditText liityText   = (EditText)findViewById(R.id.liityText);

        liityButton.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        String liittymisTunnus = liityText.getText().toString();
                        tarkistaLiittymien(liittymisTunnus);


                    }
                });

    }

    public void tarkistaLiittymien(String tunnus){

        SharedPreferences mySharedPreferences = getSharedPreferences("konfiguraatio", Context.MODE_PRIVATE);


        String base = mySharedPreferences.getString("token", "");

        Integer userId = Integer.parseInt(mySharedPreferences.getString("userID", null));


        PassiClient passiClient = ServiceGenerator.createService(PassiClient.class, base);
        Call<ResponseBody> call = passiClient.LiityRyhmaan(tunnus, userId);
        call.enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    //Liitytty
                    Toast.makeText(getApplicationContext(), "Olet liittynyt ryhmään!", Toast.LENGTH_LONG).show();
                    Intent ryhmaSivu =  new Intent(LiityRyhmaActivity.this, MainActivity.class);
                    startActivity(ryhmaSivu);

                } else if(response.code() == 409){
                    // On jo ryhmässä
                    Toast.makeText(getApplicationContext(), "Olet jo liittynyt tähän ryhmään!", Toast.LENGTH_LONG).show();

                }else{
                    //Ei onnistunut
                    Toast.makeText(getApplicationContext(), "Ryhmää ei löytynyt avaimella!", Toast.LENGTH_LONG).show();


                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }

        });

}

}
