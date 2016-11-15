package fi.softala.tyokykypassi.activities;

import android.os.Bundle;
import android.widget.ImageButton;

import fi.softala.tyokykypassi.R;

public class SaarinakymaActivity extends ToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_saarinakyma);

        //Luo iconeille listenerit ja lähettää valikkoActivityyn, jossa id:n perusteella toiminnot
        ImageButton imHome = (ImageButton) findViewById(R.id.home);
        ImageButton imFeedback = (ImageButton) findViewById(R.id.feedback);
        ImageButton imLogout = (ImageButton) findViewById(R.id.logout);


        imHome.setOnClickListener(this);
        imFeedback.setOnClickListener(this);
        imLogout.setOnClickListener(this);


    }

}
