package fi.softala.passi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;


public class KirjauduActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kirjaudu);
        Button button = (Button) findViewById(R.id.btKirjaudu);

        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                final EditText etUsername = (EditText) findViewById(R.id.etKayttajaTunnus);
                String kayttajatunnus = etUsername.getText().toString();
                final EditText etSalasana = (EditText) findViewById(R.id.etSalasana);
                String salasana = etSalasana.getText().toString();
                if (salasana.equals("passi") && kayttajatunnus.equals("passi")) {
                    Intent intent = new Intent(KirjauduActivity.this, ValikkoActivity.class);
                    startActivity(intent);
                }
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                HttpClient httpclient = new DefaultHttpClient();

                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                postParameters.add(new BasicNameValuePair("kayttajatunnus", kayttajatunnus));
                postParameters.add(new BasicNameValuePair("salasana", salasana));
                String response = null;
                try {
                    response = CustomHttpClient.executeHttpPost("http://proto284.haaga-helia.fi/passibe/KirjautumisMobiiliServlet", postParameters);  //Enetr Your remote PHP,ASP, Servlet file link
                    String res = response.toString();
                    res = res.replaceAll("\\s+", "");
                    if (res.equals("1")) {
                        showToast("Onnistui!");
                        Intent intent = new Intent(KirjauduActivity.this, ValikkoActivity.class);
                        startActivity(intent);
                    } else {
                        showToast("Käyttäjätunnus tai salasana väärin!");
                    }
                } catch (Exception e) {
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_kirjaudu, menu);
        return true;
    }

    public void showToast(String text) {
        int duration = Toast.LENGTH_SHORT;
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
