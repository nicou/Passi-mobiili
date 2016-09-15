package fi.softala.passi;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;

/**
 * Created by villeaaltonen on 15/09/16.
 */
public class KirjautumisActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kirjautuminen);
        final TextInputLayout usernameWrapper = (TextInputLayout) findViewById(R.id.usernameWrapper);
        final TextInputLayout passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);
        usernameWrapper.setHint("Käyttäjänimi");
        passwordWrapper.setHint("Salasana");
    }
}
