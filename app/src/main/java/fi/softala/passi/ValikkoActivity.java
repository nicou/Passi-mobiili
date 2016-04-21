package fi.softala.passi;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ValikkoActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valikko);
        Button button = (Button) findViewById(R.id.btValikkoon);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(ValikkoActivity.this, TehtavakorttiNostoActivity.class);
                startActivity(intent);
            }
        });
    }
}
