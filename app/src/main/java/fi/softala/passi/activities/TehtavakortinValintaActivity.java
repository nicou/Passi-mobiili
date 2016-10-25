package fi.softala.passi.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import fi.softala.passi.R;

public class TehtavakortinValintaActivity extends ValikkoActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tehtavakortin_valinta);
      //  Context context = getApplicationContext();


        //Luo iconeille listenerit ja lähettää valikkoActivityyn, jossa id:n perusteella toiminnot
        ImageButton imHome = (ImageButton)findViewById(R.id.home);
        ImageButton imFeedback = (ImageButton)findViewById(R.id.feedback);
        ImageButton imLogout = (ImageButton)findViewById(R.id.logout);

        imHome.setOnClickListener(this);
        imFeedback.setOnClickListener(this);
        imLogout.setOnClickListener(this);

        Button button1 = (Button) findViewById(R.id.btnPaihteet1);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        Button button2 = (Button) findViewById(R.id.btnPaihteet2);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        Button button3 = (Button) findViewById(R.id.btnPaihteet);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        Button button4 = (Button) findViewById(R.id.btnRavitsemus);
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        Button button5 = (Button) findViewById(R.id.btnMitaOnTerveys);
        button5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        Button button6 = (Button) findViewById(R.id.btnMielenHyvinvointi);
        button6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


            }
        });

        Button button7 = (Button) findViewById(R.id.btnVaarakartta);
        button7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


            }
        });

        Button button8 = (Button) findViewById(R.id.btnKulttuuriPassi);
        button8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


            }
        });

        Button button9 = (Button) findViewById(R.id.btnLaadiTyoohjeet);
        button9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


            }
        });

        Button button10 = (Button) findViewById(R.id.btnOmaTapaLiikkua);
        button10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


            }
        });

//          Turvallisuuskävely
        Button button11 = (Button) findViewById(R.id.btnTurvallisuuskavely);
        button11.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(TehtavakortinValintaActivity.this, RyhmatActivity.class);
                startActivity(intent);
            }
        });

        Button button12 = (Button) findViewById(R.id.btnTyotaHakemassa);
        button12.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


            }
        });

    }
    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(TehtavakortinValintaActivity.this, ValikkoActivity.class);
        startActivity(intent);

    }

}
