package fi.softala.passi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class ValikkoActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valikko);
        Button button = (Button) findViewById(R.id.btnTyokykypassi);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                Intent intent = new Intent(ValikkoActivity.this, TehtavakortinValintaActivity.class);
                startActivity(intent);

            }
        });

        // Palaa home-valikkoon toolbarista
        ImageButton imHome = (ImageButton)findViewById(R.id.home);
        imHome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ValikkoActivity.this, ValikkoActivity.class);
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

        //Kirjaudu ulos toolbar-kohta. Toiminnallisuus toistaiseksi puuttuu
        ImageButton imLogout = (ImageButton)findViewById(R.id.logout);
        imLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("logout","klikattu");

            }
        });
    }

    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(ValikkoActivity.this, ValikkoActivity.class);
        startActivity(intent);
    }


}
