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

public class VahvistusActivity extends ValikkoActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vahvistus);

        //Luo iconeille listenerit ja lähettää valikkoActivityyn, jossa id:n perusteella toiminnot
        ImageButton imHome = (ImageButton)findViewById(R.id.home);
        ImageButton imFeedback = (ImageButton)findViewById(R.id.feedback);
        ImageButton imLogout = (ImageButton)findViewById(R.id.logout);

        imHome.setOnClickListener(this);
        imFeedback.setOnClickListener(this);
        imLogout.setOnClickListener(this);

        Button button = (Button) findViewById(R.id.btnPalaaKortteihin);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(VahvistusActivity.this, TehtavakortinValintaActivity.class);
                startActivity(intent);

            }
        });

        Button button2 = (Button) findViewById(R.id.btnTakaisinEtusivulle);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(VahvistusActivity.this, ValikkoActivity.class);
                startActivity(intent);

            }
        });

    }


}
