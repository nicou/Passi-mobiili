package fi.softala.passi.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import fi.softala.passi.R;

public class ValikkoActivity extends AppCompatActivity implements View.OnClickListener {

    int backButtonCount = 0;

    //Hoitaa toolbarin iconien klikkauksen
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home :
                Intent intent = new Intent(getApplicationContext(), fi.softala.passi.activities.ValikkoActivity.class);
                startActivity(intent);
                break;

            case R.id.logout :
                kirjauduUlos();
                break;

            case R.id.feedback :
                intent = new Intent(getApplicationContext(), PalauteActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valikko);
        Button button = (Button) findViewById(R.id.btnTyokykypassi);
       // Context context = getApplicationContext();

        ImageButton imHome = (ImageButton)findViewById(R.id.home);
        ImageButton imFeedback = (ImageButton)findViewById(R.id.feedback);
        ImageButton imLogout = (ImageButton)findViewById(R.id.logout);

        imHome.setOnClickListener(this);
        imFeedback.setOnClickListener(this);
        imLogout.setOnClickListener(this);



        // Button kauppaButton = (Button) findViewById(R.id.btnKauppa);


        // VÄLIAIKAINEN LINKKAUS VAHVISTUSSIVULLE
        Button vahvistusButton = (Button) findViewById(R.id.btnKauppa);
        vahvistusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ValikkoActivity.this, fi.softala.passi.activities.VahvistusActivity.class);
                startActivity(intent);
            }
        });

        
        final Button ryhmaButton = (Button) findViewById(R.id.btnProfiiliNappi);

        //ImageButton imHome = (ImageButton)findViewById(R.id.home);
        //imHome.setOnClickListener(new View.OnClickListener() {

//            @Override
  //          public void onClick(View v) {
    //            Intent intent = new Intent(ValikkoActivity.this, fi.softala.passi.activities.ValikkoActivity.class);
      //          startActivity(intent);

//            }
  //      });

  //      ImageButton imFeedback = (ImageButton)findViewById(R.id.feedback);
//        imFeedback.setOnClickListener(new View.OnClickListener() {

        //    @Override
      //      public void onClick(View v) {
    //            Intent intent = new Intent(ValikkoActivity.this, PalauteActivity.class);
  //              startActivity(intent);

      //      }
//        });


        //Kirjaudu ulos toolbarista
       // ImageButton imLogout = (ImageButton)findViewById(R.id.logout);
        //imLogout.setOnClickListener(new View.OnClickListener() {

         //   @Override
           // public void onClick(View v) {
             //   kirjauduUlos();
            //}
        //});

        ryhmaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ValikkoActivity.this, RyhmatActivity.class);
                View sharedView = ryhmaButton;
                String transition = getString(R.string.profile);
                ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        ValikkoActivity.this, sharedView, transition
                );
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                Intent intent = new Intent(ValikkoActivity.this, RyhmatActivity.class);
                startActivity(intent);

            }
        });


    }

    public void kirjauduUlos() {
        new AlertDialog.Builder(ValikkoActivity.this).setMessage("Kirjaudu ulos?")
                .setPositiveButton("Kyllä", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences mySharedPreferences = getSharedPreferences("konfiguraatio", Context.MODE_PRIVATE);
                        mySharedPreferences.edit()
                                .remove("tunnus")
                                .remove("token")
                                .apply();
                        Intent sisaanKirjautuminen = new Intent(getApplicationContext(), KirjautumisActivity.class);
                        startActivity(sisaanKirjautuminen);
                    }
                })
                .setNegativeButton("Peruuta", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                }).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }



}
