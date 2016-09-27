package fi.softala.passi;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by villeaaltonen on 15/09/16.
 */
public class KirjautumisActivity extends Activity {
    // Constants
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.example.android.datasync.provider";
    public static final String ACCOUNT_TYPE = "fi.softala.passi";
    // An account type, in the form of a domain name

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

    public void doLogin(String username, final String password) {
        final ProgressDialog progressDialog = new ProgressDialog(KirjautumisActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Kirjaudutaan...");
        progressDialog.show();
        Context context = getApplicationContext();

        // Get an instance of the Android account manager
        final AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        Log.d("Passi", username + " username " + password + " password");
        // TODO: Implement your own authentication logic here.

        ArrayList tunnukset = new ArrayList();

        tunnukset.add("Oppilas1");
        tunnukset.add("Oppilas2");
        tunnukset.add("Oppilas3");
        tunnukset.add("Oppilas4");
        tunnukset.add("Oppilas5");
        tunnukset.add("Oppilas6");
        tunnukset.add("Oppilas7");
        tunnukset.add("Oppilas8");
        tunnukset.add("Oppilas9");
        tunnukset.add("Oppilas10");
        tunnukset.add("Oppilas11");
        tunnukset.add("Oppilas12");
        tunnukset.add("Oppilas13");
        tunnukset.add("Oppilas14");
        tunnukset.add("Oppilas15");

        SharedPreferences mySharedPreferences = getSharedPreferences("konfiguraatio", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();


        String tunnus = "";
        Boolean oikein = false;
        String pohja = "";
        String base ="";
        for(int i = 0; tunnukset.size() > i; i++){

            tunnus = (String) tunnukset.get(i);
            if(username.equals(tunnus) && password.equals("salakala") || username.equals("admin") && password.equals("admin")){
                oikein = true;
      if(username != null && password != null){
            pohja = "jaapa" + ":" + "jaapa";
            base =Base64.encodeToString(pohja.getBytes(), Base64.NO_WRAP);
            System.out.println(base + " Väliaikainen token");

            }

        }
        if (oikein == true) {
            Account account = new Account(username, ACCOUNT_TYPE);
            accountManager.addAccountExplicitly(account, password, null);
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            onLoginSuccess();
                            // onLoginFailed();
                            progressDialog.dismiss();
                        }
                    }, 3000);

            editor.putString("tunnus", tunnus);
            editor.commit();
            editor.putString("token", base);
            editor.commit();
        } else {


            progressDialog.dismiss();
            int duration = Toast.LENGTH_LONG;
            String text = "Salasana tai käyttäjänimi väärin";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }


    }}

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(KirjautumisActivity.this, MainActivity.class);
        startActivity(intent);

    }


    public void onLoginSuccess() {
        // btn.setEnabled(true);
// Create the dummy account
        // mAccount = CreateSyncAccount(this);
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
