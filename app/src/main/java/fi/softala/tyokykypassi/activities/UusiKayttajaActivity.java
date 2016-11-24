package fi.softala.tyokykypassi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import fi.softala.tyokykypassi.R;

public class UusiKayttajaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etusivu);

        Button loginButton = (Button) findViewById(R.id.btnLogin);
        Button registerButton = (Button) findViewById(R.id.btnRegister);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(UusiKayttajaActivity.this, KirjautumisActivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(UusiKayttajaActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
    public void onBackPressed() {
        moveTaskToBack(true);
    }


}
