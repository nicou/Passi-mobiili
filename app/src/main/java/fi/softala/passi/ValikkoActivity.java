package fi.softala.passi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ValikkoActivity extends AppCompatActivity {

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
    }

    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(ValikkoActivity.this, ValikkoActivity.class);
        startActivity(intent);
    }


}
