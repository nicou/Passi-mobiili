package fi.softala.tyokykypassi.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import fi.softala.tyokykypassi.R;

public class LiityRyhmaActivity extends ToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_liity_ryhma);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Button liityButton = (Button)findViewById(R.id.liityButton);
        final EditText liityText   = (EditText)findViewById(R.id.liityText);

        liityButton.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        Log.v("Ryhmätunnus: ", liityText.getText().toString());
                    }
                });

    }

}
