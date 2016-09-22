package fi.softala.passi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Credentials;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import static fi.softala.passi.R.id.password;

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
        final Button btn = (Button) findViewById(R.id.btn);

        usernameWrapper.setHint("Käyttäjänimi");
        passwordWrapper.setHint("Salasana");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                String username = usernameWrapper.getEditText().getText().toString();
                String password = passwordWrapper.getEditText().getText().toString();
                if (!validateUsername(username)) {
                    if (validatePassword(password)) {
                        passwordWrapper.setErrorEnabled(false);
                    }
                    usernameWrapper.requestFocus();
                    usernameWrapper.setError("Käyttäjänimi liian lyhyt!");
                } else if (!validatePassword(password)) {
                    if (validateUsername(username)) {
                        usernameWrapper.setErrorEnabled(false);
                    }
                    passwordWrapper.requestFocus();
                    passwordWrapper.setError("Salasana liian lyhyt!");
                } else {
                    usernameWrapper.setErrorEnabled(false);
                    passwordWrapper.setErrorEnabled(false);
                    doLogin(username, password);
                }
            }
        });

    }

    public boolean validateUsername(String username) {
        return username.length() > 4;
    }

    public boolean validatePassword(String password) {
        return password.length() > 4;
    }

    public void doLogin(String username,String password) {
        final ProgressDialog progressDialog = new ProgressDialog(KirjautumisActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Kirjaudutaan...");
        progressDialog.show();
        Log.d("Passi", username + " username " + password + " password");
        // TODO: Implement your own authentication logic here.
        if (username.equals("admin") && password.equals("admin")) {
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {

                            // On complete call either onLoginSuccess or onLoginFailed
                            onLoginSuccess();
                            // onLoginFailed();
                            progressDialog.dismiss();
                        }
                    }, 3000);
        } else {
            progressDialog.dismiss();
            int duration = Toast.LENGTH_LONG;
            String text = "Salasana tai käyttäjänimi väärin";
            Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }


    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(KirjautumisActivity.this, MainActivity.class);
        startActivity(intent);

    }

    public void onLoginSuccess() {
        // btn.setEnabled(true);

        Intent valikko = new Intent(KirjautumisActivity.this, ValikkoActivity.class);
        startActivity(valikko);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        // btn.setEnabled(true);
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


}
