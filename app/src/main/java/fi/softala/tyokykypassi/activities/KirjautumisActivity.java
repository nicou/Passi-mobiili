package fi.softala.tyokykypassi.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import fi.softala.tyokykypassi.R;
import fi.softala.tyokykypassi.models.Kayttaja;
import fi.softala.tyokykypassi.network.PassiClient;
import fi.softala.tyokykypassi.network.ServiceGenerator;
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
        final TextView salasananPalautus = (TextView) findViewById(R.id.salasananPalautus);

        final Button btn = (Button) findViewById(R.id.btn);

        usernameWrapper.setHint("Käyttäjänimi");
        passwordWrapper.setHint("Salasana");
        salasananPalautus.setMovementMethod(LinkMovementMethod.getInstance());
        String salasananPalautusText = "<a href='http://tyokykypassi.net/passi/passrestore'>Unohtuneen salasanan palautus</a>";
        salasananPalautus.setText(Html.fromHtml(salasananPalautusText));

        btn.setOnClickListener(new View.OnClickListener() {
            String username;
            String password;
            @Override
            public void onClick(View v) {
                hideKeyboard();

                if (usernameWrapper.getEditText() != null && passwordWrapper.getEditText() != null) {
                    username = usernameWrapper.getEditText().getText().toString().toLowerCase();
                    password = passwordWrapper.getEditText().getText().toString();
                }
                    if (!validateUsername(username)) {
                        if (validatePassword(password)) {
                            passwordWrapper.setErrorEnabled(false);
                        }
                        usernameWrapper.requestFocus();
                        usernameWrapper.setError("Liian lyhyt käyttäjänimi!");
                    } else if (!validatePassword(password)) {
                        if (validateUsername(username)) {
                            usernameWrapper.setErrorEnabled(false);
                        }
                        passwordWrapper.requestFocus();
                        passwordWrapper.setError("Liian lyhyt salasana!");
                    } else {
                        usernameWrapper.setErrorEnabled(false);
                        passwordWrapper.setErrorEnabled(false);
                        doLogin(username, password);
                    }


            }
        });

    }

    public boolean validateUsername(String username) {
        return username.length() > 2;
    }

    public boolean validatePassword(String password) {
        return password.length() > 4;
    }

    public void doLogin(String username, String password) {

        final ProgressDialog progressDialog = new ProgressDialog(KirjautumisActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Kirjaudutaan...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        final int RESULT_NOT_FOUND = 401;

        final String authToken = username + ":" + password;
        final String base = Base64.encodeToString(authToken.getBytes(), Base64.NO_WRAP);

        PassiClient service =
                ServiceGenerator.createService(PassiClient.class, username, password);
        Call<Kayttaja> call = service.haeKayttaja(username);

        call.enqueue(new Callback<Kayttaja>() {
            @Override
            public void onResponse(Call<Kayttaja> call, Response<Kayttaja> response) {
                String text;
                progressDialog.dismiss();

                if (response.isSuccessful()) {
                    Kayttaja k = response.body();

                    SharedPreferences mySharedPreferences = getSharedPreferences("konfiguraatio", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = mySharedPreferences.edit();
                    editor.putString("tunnus", k.getUsername());
                    editor.apply();
                    editor.putString("token", base);
                    editor.apply();
                    editor.putString("userID", k.getUserID());
                    editor.apply();

                    onLoginSuccess();
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
                Log.e("Passi", "Virhe kirjautumisessa " + t.toString());
            }
        });
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


}
