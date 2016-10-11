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

import fi.softala.passi.R;

public class VahvistusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vahvistus);
        Button button = (Button) findViewById(R.id.btnPalaaKortteihin);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                Intent intent = new Intent(VahvistusActivity.this, TehtavakortinValintaActivity.class);
                startActivity(intent);

            }
        });
        Button button2 = (Button) findViewById(R.id.btnTakaisinEtusivulle);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                Intent intent = new Intent(VahvistusActivity.this, ValikkoActivity.class);
                startActivity(intent);

            }
        });

        // Palaa home-valikkoon toolbarista
        ImageButton imHome = (ImageButton)findViewById(R.id.home);
        imHome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VahvistusActivity.this, ValikkoActivity.class);
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

    }

    private void kirjauduUlos() {
        new AlertDialog.Builder(VahvistusActivity.this).setMessage("Kirjaudu ulos?")
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



}
