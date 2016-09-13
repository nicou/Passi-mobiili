package fi.softala.passi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

public class Turvallisuuskavely_toteutusActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turvallisuuskavely_toteutus);

        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(Turvallisuuskavely_toteutusActivity.this, Turvallisuuskavely_johdantoActivity.class);
                startActivity(intent);
            }
        });

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(Turvallisuuskavely_toteutusActivity.this, Turvallisuuskavely_suunnitelmaActivity.class);
                startActivity(intent);
            }
        });


    }

    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(Turvallisuuskavely_toteutusActivity.this, PassinValintaActivity.class);
        startActivity(intent);

    }

}
