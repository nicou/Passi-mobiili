package fi.softala.passi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageButton;

public class turvallisuuskavely_toteutus_2 extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turvallisuuskavely_toteutus_2);

        ImageButton cameraButton = (ImageButton) findViewById(R.id.button_kamera);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(turvallisuuskavely_toteutus_2.this, Camera2Activity.class);
                startActivity(cameraIntent);
            }
        });
    }
    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(turvallisuuskavely_toteutus_2.this, TehtavakortinValintaActivity.class);
        startActivity(intent);

    }

}
