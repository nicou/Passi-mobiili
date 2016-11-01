package fi.softala.passi.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import fi.softala.passi.R;
import fi.softala.passi.models.Kayttaja;
import fi.softala.passi.network.PassiClient;
import fi.softala.passi.network.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by villeaaltonen on 15/09/16.
 */
public class KirjautumisActivity extends AppCompatActivity {

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

    public void doLogin(String username, String password) {

        new tarkistaKirjautuminen().execute(username, password);
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(KirjautumisActivity.this, UusiKayttajaActivity.class);
        startActivity(intent);

    }


    public void onLoginSuccess() {
        Intent valikko = new Intent(KirjautumisActivity.this, MainActivity.class);
        valikko.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(valikko);
    }

    public void onLoginFailed(String virheViesti) {
        Context context = getApplicationContext();
        Toast toast;
        int duration = Toast.LENGTH_LONG;
        toast = Toast.makeText(context, virheViesti, duration);
        toast.show();
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private class tarkistaKirjautuminen extends AsyncTask<String, Void, Integer> {

        Context context = getApplicationContext();
        final ProgressDialog progressDialog = new ProgressDialog(KirjautumisActivity.this,
                R.style.AppTheme_Dark_Dialog);

        Integer paluukoodi = 0;
        String username;
        String password;
        String base;
        final int RESULT_OK = 200;
        final int RESULT_NOT_FOUND = 401;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Kirjaudutaan...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(String... params) {

            username = params[0];
            password = params[1];

            String authToken = username + ":" + password;
            base = Base64.encodeToString(authToken.getBytes(), Base64.NO_WRAP);

            PassiClient service =
                    ServiceGenerator.createService(PassiClient.class, username, password);
            Call<Kayttaja> call = service.haeKayttaja(username);
            call.enqueue(new Callback<Kayttaja>() {
                @Override
                public void onResponse(Call<Kayttaja> call, Response<Kayttaja> response) {
                    progressDialog.dismiss();
                    Kayttaja k = response.body();
                    String text;
                    SharedPreferences mySharedPreferences = getSharedPreferences("konfiguraatio", Context.MODE_PRIVATE);

                    // Haku onnistui
                    if (response.code() == RESULT_OK) {
                        SharedPreferences.Editor editor = mySharedPreferences.edit();
                        editor.putString("tunnus", k.getUsername());
                        editor.apply();
                        editor.putString("token", base);
                        editor.apply();
                        editor.putString("userID", k.getUserID());
                        editor.apply();

                        onLoginSuccess();

                        // Väärät tunnukset
                    } else if (response.code() == RESULT_NOT_FOUND) {
                        text = "Salasana tai käyttäjänimi väärin";
                        onLoginFailed(text);

                        // Jokin muu virhe
                    } else {
                        text = "Virhe tietojen haussa";
                        onLoginFailed(text);
                    }
                }

                @Override
                public void onFailure(Call<Kayttaja> call, Throwable t) {

                }
            });
            return 2;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

        }
            /*
            try {
                Response response = call.execute();

                if (response.isSuccessful()) {
                    paluukoodi = 200;
                } else if (response.code() == 401) {
                    paluukoodi = 401;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


            return paluukoodi;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            String text;
            SharedPreferences mySharedPreferences = getSharedPreferences("konfiguraatio", Context.MODE_PRIVATE);

            // Haku onnistui
            if (result == RESULT_OK) {
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                editor.putString("tunnus", username);
                editor.apply();
                editor.putString("token", base);
                editor.apply();

                onLoginSuccess();

                // Väärät tunnukset
            } else if (result == RESULT_NOT_FOUND) {
                text = "Salasana tai käyttäjänimi väärin";
                onLoginFailed(text);

                // Jokin muu virhe
            } else {
                text = "Virhe tietojen haussa";
                onLoginFailed(text);
            }
        } */
    }


}
