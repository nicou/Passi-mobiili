package fi.softala.tyokykypassi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.test.suitebuilder.TestMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import fi.softala.tyokykypassi.R;

public class VahvistusActivity extends ToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vahvistus);

        String tehtavakortinNimi = getIntent().getStringExtra("nimi");

        if (tehtavakortinNimi != null) {
            TextView otsikko = (TextView) findViewById(R.id.textView3);
            otsikko.setText(tehtavakortinNimi);
        }

        //Luo iconeille listenerit ja lähettää valikkoActivityyn, jossa id:n perusteella toiminnot
        ImageButton imHome = (ImageButton) findViewById(R.id.home);
        ImageButton imFeedback = (ImageButton) findViewById(R.id.feedback);
        ImageButton imLogout = (ImageButton) findViewById(R.id.logout);

        imHome.setOnClickListener(this);
        imFeedback.setOnClickListener(this);
        imLogout.setOnClickListener(this);


        Button button2 = (Button) findViewById(R.id.btnTakaisinEtusivulle);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(VahvistusActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

    }


}
