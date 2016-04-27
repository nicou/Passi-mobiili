package fi.softala.passi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


public class TehtavakorttiNostoActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tehtavakortti_nosto);
        final Button btKaksinKasin = (Button) findViewById(R.id.btKaksinKasin);
        btKaksinKasin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                showToast("VÄÄRIN!");

            }
        });
        Button btJaloilla = (Button) findViewById(R.id.btJaloilla);
        btJaloilla.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                HttpClient httpclient = new DefaultHttpClient();
                String url = "http://proto280.haaga-helia.fi/tkpassibackend/api2/addAnswer?id=1&answerNumber=1&answerText=Natiivista2&user_id=2";
                // Prepare a request object
                HttpGet httpget = new HttpGet(url);

                // Execute the request
                HttpResponse response;
                try {
                    response = httpclient.execute(httpget);
                    // Examine the response status
                    Log.i("Praeda", response.getStatusLine().toString());
                } catch (Exception e) {
                }
                showToast("OIKEIN!");
                //Move to next view
                Intent intent = new Intent(TehtavakorttiNostoActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });
        final Button btSeuraava = (Button) findViewById(R.id.btSeuraava);
        btSeuraava.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(TehtavakorttiNostoActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });
    }

    public void showToast(String text) {
        int duration = Toast.LENGTH_SHORT;
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tehtavakortti_nosto, menu);
        return true;
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
