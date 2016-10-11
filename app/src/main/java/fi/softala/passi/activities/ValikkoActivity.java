package fi.softala.passi.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import fi.softala.passi.R;

public class ValikkoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valikko);
        Button button = (Button) findViewById(R.id.btnTyokykypassi);
        Button kauppaButton = (Button) findViewById(R.id.btnKauppa);


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
    }

    private void kirjauduUlos() {
        new AlertDialog.Builder(ValikkoActivity.this).setMessage("Kirjaudu ulos?")
                .setPositiveButton("Kyllä", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences mySharedPreferences = getSharedPreferences("konfiguraatio", Context.MODE_PRIVATE);
                        mySharedPreferences.edit().remove("token").commit();
                        mySharedPreferences.edit().remove("username").commit();
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

    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(ValikkoActivity.this, ValikkoActivity.class);
        startActivity(intent);
    }


}
