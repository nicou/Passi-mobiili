package fi.softala.tyokykypassi.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import fi.softala.tyokykypassi.R;

public class LiityRyhmaActivity extends ToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_liity_ryhma);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

}
