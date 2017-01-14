package fi.softala.tyokykypassi.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import fi.softala.tyokykypassi.R;
import fi.softala.tyokykypassi.models.UusiKayttaja;
import fi.softala.tyokykypassi.network.PassiClient;
import fi.softala.tyokykypassi.network.ServiceGenerator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button registerButton = (Button) findViewById(R.id.btnSubmitRegister);

        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            registerOnClick(v);
            }
        });

    }

    public String capitalizeFirstLetter(String s) {
        if (s.length() < 2) { return s; }
        return (s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase());
    }

    private String validateUser(UusiKayttaja kayttaja) {
        if (kayttaja.getUsername().length() < 3) {
            return "Käyttäjänimessä on oltava vähintään 3 merkkiä";
        } else if (kayttaja.getFirstname().length() < 2) {
            return "Etunimessä on oltava vähintään 2 merkkiä";
        } else if (kayttaja.getLastname().length() < 2) {
            return "Sukunimessä on oltava vähintään 2 merkkiä";
        } else if (kayttaja.getEmail().length() < 4 || !kayttaja.getEmail().contains("@")) {
            // Terrible validation for proper email, but the correct regex just seems just as ridiculous:
            // https://stackoverflow.com/questions/8204680/java-regex-email/13013056#13013056
            return "Sähköpostiosoitteessa on oltava vähintään 5 merkkiä";
        } else if (!kayttaja.getPassword().equals(kayttaja.getConfirmPassword())) {
            return "Salasanat eivät täsmää";
        } else if (kayttaja.getPassword().length() < 5 || kayttaja.getConfirmPassword().length() < 5) {
            return "Salasanassa on oltava vähintään 5 merkkiä";
        }
        return null;
    }

    public void registerOnClick(View v) {
        TextInputLayout etKayttajaTunnus = (TextInputLayout) findViewById(R.id.etKayttajaNimi);
        TextInputLayout etEtunimi = (TextInputLayout) findViewById(R.id.etEtuNimi);
        TextInputLayout etSukunimi = (TextInputLayout) findViewById(R.id.etSukunimi);
        TextInputLayout etSahkoposti = (TextInputLayout) findViewById(R.id.etSahkoposti);
        TextInputLayout etSalasana = (TextInputLayout) findViewById(R.id.etSalasana);
        TextInputLayout etSalasanaVahvistus = (TextInputLayout) findViewById(R.id.etSalasanaVahvistus) ;

        String stringKayttajatunnus = etKayttajaTunnus.getEditText().getText().toString().trim().toLowerCase();
        String stringEtunimi = etEtunimi.getEditText().getText().toString().trim();
        String stringSukunimi = etSukunimi.getEditText().getText().toString().trim();
        String stringSahkoposti = etSahkoposti.getEditText().getText().toString().trim();
        String stringSalasana = etSalasana.getEditText().getText().toString().trim();
        String stringVahvistaSalasana = etSalasanaVahvistus.getEditText().getText().toString().trim();

        UusiKayttaja kayttaja = new UusiKayttaja();
        kayttaja.setFirstname(capitalizeFirstLetter(stringEtunimi));
        kayttaja.setLastname(capitalizeFirstLetter(stringSukunimi));
        kayttaja.setUsername(stringKayttajatunnus);
        kayttaja.setPassword(stringSalasana);
        kayttaja.setConfirmPassword(stringVahvistaSalasana);
        kayttaja.setEmail(stringSahkoposti);

        String validation = validateUser(kayttaja);

        if (validation == null) {
            doRegister(kayttaja);
        } else {
            onRegisterFailed(validation);
        }
    }

    public void doRegister(UusiKayttaja kayttaja) {

        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Rekisteröidytään...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        final int RESULT_USERNAME_EXISTS = 409;
        final int RESULT_RUNTIME_EXCEPTION = 417;

        PassiClient service = ServiceGenerator.createService(PassiClient.class);

        Call<ResponseBody> call = service.rekisteroiKayttaja(kayttaja);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                if (response.isSuccessful()) {
                    Log.v("RegisterActivity", "Rekisterointi onnistui!");
                    onRegisterSuccess();
                } else if (response.code() == RESULT_USERNAME_EXISTS) {
                    Log.v("RegisterActivity", "Rekisteroinnissa virhe: kayttajanimi tai sähköposti on jo käytössä!");
                    onRegisterFailed("Käyttäjänimi tai sähköposti on jo käytössä!");
                } else if (response.code() == RESULT_RUNTIME_EXCEPTION) {
                   Log.v("RegisterActivity", "Rekisteroinnissa virhe: runtime exception");
                   onRegisterFailed("Käyttäjän lisäys tietokantaan ei onnistunut!");
                } else {
                    Log.v("RegisterActivity", "Rekisteroinnissa virhe: status " + response.code());
                    onRegisterFailed("Rekisteröitymisessä tapahtui virhe!");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("RegisterActivity", "Rekisteroinnissa virhe: " + t.toString());
                onRegisterFailed("Rekisteröitymisessä tapahtui virhe!");
            }
        });
    }

    public void onRegisterSuccess() {
        Toast toast;
        toast = Toast.makeText(getApplicationContext(), "Rekisteröinti onnistui!", Toast.LENGTH_LONG);
        toast.show();
        Intent intent = new Intent(RegisterActivity.this, UusiKayttajaActivity.class);
        startActivity(intent);
    }

    public void onRegisterFailed(String virheViesti) {
        Context context = getApplicationContext();
        Toast toast;
        int duration = Toast.LENGTH_LONG;
        toast = Toast.makeText(context, virheViesti, duration);
        toast.show();
    }

}
