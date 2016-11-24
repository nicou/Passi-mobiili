package fi.softala.tyokykypassi.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
                EditText etKayttajaTunnus = (EditText)findViewById(R.id.etKayttajaNimi);
                EditText etEtunimi = (EditText)findViewById(R.id.etEtuNimi);
                EditText etSukunimi = (EditText)findViewById(R.id.etSukunimi);
                EditText etSalasana = (EditText)findViewById(R.id.etSalasana);

                String stringKayttajatunnus = etKayttajaTunnus.getText().toString().trim();
                String stringEtunimi = etEtunimi.getText().toString().trim();
                String stringSukunimi = etSukunimi.getText().toString().trim();
                String stringSalasana = etSalasana.getText().toString().trim();
                String stringVahvistaSalasana = etSalasana.getText().toString().trim();

                UusiKayttaja kayttaja = new UusiKayttaja();
                kayttaja.setFirstname(stringEtunimi);
                kayttaja.setLastname(stringSukunimi);
                kayttaja.setUsername(stringKayttajatunnus);
                kayttaja.setPassword(stringSalasana);
                kayttaja.setConfirmPassword(stringVahvistaSalasana);

                // Hardcoded values for now
                kayttaja.setEmail("test@testerinx.fi");
                kayttaja.setPhone("0401231234");

                doRegister(kayttaja);
            }
        });

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
                    Log.v("RegisterActivity", "Rekisteroinnissa virhe: kayttajanimi on jo olemassa!");
                    onRegisterFailed("Käyttäjänimi on jo olemassa!");
                } else if (response.code() == RESULT_RUNTIME_EXCEPTION) {
                   Log.v("RegisterActivity", "Rekisteroinnissa virhe: runtime exception");
                   onRegisterFailed("Täytithän kaikki kentät?");
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
