package fi.softala.tyokykypassi.activities;

/**
 * Created by Lucy Liu on 29.11.2016.
 */

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import fi.softala.tyokykypassi.R;

public class RekisteriselosteActivity extends ToolbarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekisteriseloste);

        ImageButton imHome = (ImageButton) findViewById(R.id.home);
        ImageButton imFeedback = (ImageButton) findViewById(R.id.feedback);
        ImageButton imLogout = (ImageButton) findViewById(R.id.logout);

        imHome.setOnClickListener(this);
        imFeedback.setOnClickListener(this);
        imLogout.setOnClickListener(this);

        TextView selostetext1 = (TextView) findViewById(R.id.selosteteksti1);
        selostetext1.setText("PLACEHOLDER");
    }
}