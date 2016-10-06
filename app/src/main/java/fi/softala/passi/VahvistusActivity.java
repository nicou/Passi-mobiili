package fi.softala.passi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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
    }
}
