package fi.softala.passi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

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

        //Kirjaudu ulos toolbar-kohta. Toiminnallisuus toistaiseksi puuttuu
        ImageButton imLogout = (ImageButton)findViewById(R.id.logout);
        imLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("logout","klikattu");

            }
        });

    }



}
