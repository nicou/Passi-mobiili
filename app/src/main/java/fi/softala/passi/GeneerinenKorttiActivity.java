package fi.softala.passi;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GeneerinenKorttiActivity extends ActionBarActivity {
    public static int korttinumero = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geneerinen_kortti);
        TextView tvTeksti = (TextView)findViewById(R.id.tvTeksti);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String teksti = "";
        try {
            URL url = new URL("http://proto284.haaga-helia.fi/passibe/KorttiServlet?getkortti=" + korttinumero);
            try {
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder str = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    teksti = teksti + line;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        tvTeksti.setText(teksti);
        RadioButton radio1 = (RadioButton) findViewById(R.id.rbValinta1);
        RadioButton radio2 = (RadioButton) findViewById(R.id.rbValinta2);
        RadioButton radio3 = (RadioButton) findViewById(R.id.rbValinta3);

        if(radio1.isChecked()) {
            try {
                URL url = new URL("http://proto284.haaga-helia.fi/passibe/VastausServlet?savekortti=" + korttinumero + "&valinta=1");
                try {
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        if(radio2.isChecked()) {
            try {
                URL url = new URL("http://proto284.haaga-helia.fi/passibe/VastausServlet?savekortti=" + korttinumero + "&valinta=2");
                try {
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        if(radio3.isChecked()) {
            try {
                URL url = new URL("http://proto284.haaga-helia.fi/passibe/KorttiServlet?savekortti=" + korttinumero + "&valinta=3");
                try {
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        Button button = (Button) findViewById(R.id.btSeuraava);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                korttinumero++;
                if(korttinumero == 4) {
                    korttinumero = 1;
                }
                Intent intent = new Intent(GeneerinenKorttiActivity.this, GeneerinenKorttiActivity.class);
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
}
