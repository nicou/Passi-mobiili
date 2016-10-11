package fi.softala.passi.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import fi.softala.passi.R;

public class ValikkoActivity extends AppCompatActivity {

    int backButtonCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valikko);
        Button button = (Button) findViewById(R.id.btnTyokykypassi);
        Button kauppaButton = (Button) findViewById(R.id.btnKauppa);
        Button ryhmaButton = (Button) findViewById(R.id.btnProfiili);

        Button profiiliButton = (Button)findViewById(R.id.btnProfiili);


        ImageButton imHome = (ImageButton)findViewById(R.id.home);
        imHome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ValikkoActivity.this, fi.softala.passi.activities.ValikkoActivity.class);
                startActivity(intent);

            }
        });

        //Anna palautetta toolbar-kohta. Toiminnallisuus toistaiseksi puuttuu
        ImageButton imFeedback = (ImageButton)findViewById(R.id.feedback);
        imFeedback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("feedback","klikattu");

            }
        });

        //Kirjaudu ulos toolbarista
        ImageButton imLogout = (ImageButton)findViewById(R.id.logout);
        imLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                kirjauduUlos();
            }
        });

        ryhmaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ValikkoActivity.this, ryhmatActivity.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                Intent intent = new Intent(ValikkoActivity.this, TehtavakortinValintaActivity.class);
                startActivity(intent);

            }
        });
        //väliaikanen uloskirjautumisnappula
        kauppaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                kirjauduUlos();
            }
        });

        profiiliButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                Intent intent = new Intent(ValikkoActivity.this, PalauteActivity.class);
                startActivity(intent);

            }
        });

    }

    private void kirjauduUlos() {
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
