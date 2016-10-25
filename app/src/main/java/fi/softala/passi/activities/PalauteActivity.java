package fi.softala.passi.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import fi.softala.passi.R;

public class PalauteActivity extends ValikkoActivity {

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
    }

}
