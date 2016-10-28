package fi.softala.passi.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import fi.softala.passi.R;

public class UusiKayttajaActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);



        Button button = (Button) findViewById(R.id.btnLogin);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(UusiKayttajaActivity.this, KirjautumisActivity.class);
                startActivity(intent);
            }
        });

    }


    public void onBackPressed() {
        moveTaskToBack(true);
    }


}
