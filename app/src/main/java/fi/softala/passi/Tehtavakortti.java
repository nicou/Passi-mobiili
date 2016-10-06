package fi.softala.passi;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class Tehtavakortti extends AppCompatActivity {

    File file;
    Uri fileUri;
    File kuva1, kuva2, kuva3, kuva4, kuva5;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    int id = 1;
    int kameraButtonPressed = 0;
    final int RC_TAKE_PHOTO = 1;
    int selectedId;
    RadioButton radioButton;
    String valinta1, valinta2, valinta3, valinta4, valinta5;
    String suunnitelmaString;
    String selostus1, selostus2, selostus3, selostus4, selostus5;
    Integer selectedOptionID1, selectedOptionID2, selectedOptionID3, selectedOptionID4, selectedOptionID5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tehtavakortti);


        final TabHost host = (TabHost) findViewById(R.id.tabHost);
        host.setup();


        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

                for (int i = 0; i < host.getTabWidget().getChildCount(); i++) {
                    host.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.valilehti_nappula);
                }

                host.getTabWidget().getChildAt(host.getCurrentTab()).setBackgroundColor(Color.TRANSPARENT);
            }

        });


        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.Johdanto);
        spec.setIndicator("Johdanto");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.Suunnitelma);
        spec.setIndicator("Suunnitelma");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Tab Three");
        spec.setContent(R.id.Toteutus);
        spec.setIndicator("Toteutus");
        host.addTab(spec);


        //Sets all tab titles to singleline
        int c = host.getTabWidget().getChildCount();
        for(int i = 0; c > i;i++){

            TextView title = (TextView) host.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            title.setSingleLine(true);
        }

        for (int i = 0; i < host.getTabWidget().getChildCount(); i++) {
            host.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.valilehti_nappula);
        }

        host.getTabWidget().getChildAt(host.getCurrentTab()).setBackgroundColor(Color.TRANSPARENT);

        ImageButton lahetaNappula = (ImageButton) findViewById(R.id.lahetaNappula);

        lahetaNappula.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    keraaTiedot();
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Tietojen keräys epäonnistui", Toast.LENGTH_LONG).show();
                }
            }
        });

        TextView tv = (TextView) findViewById(R.id.textView1);
        tv.setMovementMethod(new ScrollingMovementMethod());


    }

    // kun painetaan kameranappia riippuen mikä nappi
    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.kameraButton1:
                kameraButtonPressed = 1;
                break;
            case R.id.kameraButton2:
                kameraButtonPressed = 2;
                break;
            case R.id.kameraButton3:
                kameraButtonPressed = 3;
                break;
            case R.id.kameraButton4:
                kameraButtonPressed = 4;
                break;
            case R.id.kameraButton5:
                kameraButtonPressed = 5;
                break;
        }

        Intent kameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(Tehtavakortti.this.getExternalCacheDir(),
                String.valueOf(System.currentTimeMillis() + ".jpg"));
        fileUri = Uri.fromFile(file);
        kameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        Tehtavakortti.this.startActivityForResult(kameraIntent, RC_TAKE_PHOTO);

    }

    /*
        asettaa kuvan ottamisen jälkeen URIn mistä kuva löytyy stringiin
        riippuen mitä nappia on painettu ja vaihtaa painetun napin kuvan
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tehtavakortti.super.onActivityResult(requestCode, resultCode, data);
        ImageButton kameraButton1 = (ImageButton) findViewById(R.id.kameraButton1);
        ImageButton kameraButton2 = (ImageButton) findViewById(R.id.kameraButton2);
        ImageButton kameraButton3 = (ImageButton) findViewById(R.id.kameraButton3);
        ImageButton kameraButton4 = (ImageButton) findViewById(R.id.kameraButton4);
        ImageButton kameraButton5 = (ImageButton) findViewById(R.id.kameraButton5);

        if (requestCode == RC_TAKE_PHOTO && resultCode == RESULT_OK) {

            File stringUri = new File(file.toString());
            Context context = getApplicationContext();
            if (kameraButtonPressed == 1) {
                // vaihetaan nappulan taustakuva
                kuva1 = stringUri;
                kameraButton1.setBackground(ContextCompat.getDrawable(context, R.drawable.thumb_up)
                );
                kameraButton1.setEnabled(false);
            } else if (kameraButtonPressed == 2) {
                kuva2 = stringUri;
                kameraButton2.setBackground(ContextCompat.getDrawable(context, R.drawable.thumb_up)
                );
                kameraButton2.setEnabled(false);
            } else if (kameraButtonPressed == 3) {
                kuva3 = stringUri;
                kameraButton3.setBackground(ContextCompat.getDrawable(context, R.drawable.thumb_up)
                );
                kameraButton3.setEnabled(false);
            } else if (kameraButtonPressed == 4) {
                kuva4 = stringUri;
                kameraButton4.setBackground(ContextCompat.getDrawable(context, R.drawable.thumb_up)
                );
                kameraButton4.setEnabled(false);
            } else if (kameraButtonPressed == 5) {
                kuva5 = stringUri;
                kameraButton5.setBackground(ContextCompat.getDrawable(context, R.drawable.thumb_up)
                );
                kameraButton5.setEnabled(false);
            }
            kameraButtonPressed = 0;

        }

    }

    private int haeRadioVastaus(int kysymysnumero, String valinta) {
        final String ok = "Kaikki OK";
        final String puutteita = "Vaarallinen tai selkeitä puutteita";
        final String korjattavaa = "Korjattavaa löytyy";
        int laskuNumero = 0;

        Integer laskettuVastaus = 0;
        // Laskee radiobutton id. Vaihtoehtoinen tapa on "valintanumero + (kysymysnumero -1) * 3"
        // Molemmat vaativat kysymysnumeron lähtevän 1.
        if (valinta.equals(ok)) {
            laskuNumero = 1;
            laskettuVastaus = laskuNumero + (kysymysnumero -1) * 3;

        }
        if (valinta.equals(korjattavaa)) {
            laskuNumero = 2;
            laskettuVastaus = laskuNumero + (kysymysnumero -1) * 3;

        }
        if (valinta.equals(puutteita)) {
            laskuNumero = 3;
            laskettuVastaus = laskuNumero + (kysymysnumero -1) * 3;

        }

        return laskettuVastaus;
    }

    private void errorLuokka() {
        Toast.makeText(getApplicationContext(), "Vastaa kaikkiin kohtiin", Toast.LENGTH_LONG).show();
    }

    private void keraaTiedot() throws JsonProcessingException {
        EditText selostus;
        RadioGroup radioGroup1 = (RadioGroup) findViewById(R.id.radio1);
        RadioGroup radioGroup2 = (RadioGroup) findViewById(R.id.radio2);
        RadioGroup radioGroup3 = (RadioGroup) findViewById(R.id.radio3);
        RadioGroup radioGroup4 = (RadioGroup) findViewById(R.id.radio4);
        RadioGroup radioGroup5 = (RadioGroup) findViewById(R.id.radio5);

        if (radioGroup1.getCheckedRadioButtonId() == -1) {
            errorLuokka();
        } else {
            selectedId = radioGroup1.getCheckedRadioButtonId();
            radioButton = (RadioButton) findViewById(selectedId);
            valinta1 = radioButton.getText().toString();

            selectedOptionID1 = haeRadioVastaus(1, valinta1);

        }

        if (radioGroup2.getCheckedRadioButtonId() == -1) {
            errorLuokka();
        } else {
            selectedId = radioGroup2.getCheckedRadioButtonId();
            radioButton = (RadioButton) findViewById(selectedId);
            valinta2 = radioButton.getText().toString();

            selectedOptionID2 = haeRadioVastaus(2, valinta2);
        }

        if (radioGroup3.getCheckedRadioButtonId() == -1) {
            errorLuokka();
        } else {
            selectedId = radioGroup3.getCheckedRadioButtonId();
            radioButton = (RadioButton) findViewById(selectedId);
            valinta3 = radioButton.getText().toString();
            selectedOptionID3 = haeRadioVastaus(3, valinta3);
        }

        if (radioGroup4.getCheckedRadioButtonId() == -1) {
            errorLuokka();
        } else {
            selectedId = radioGroup4.getCheckedRadioButtonId();
            radioButton = (RadioButton) findViewById(selectedId);
            valinta4 = radioButton.getText().toString();
            selectedOptionID4 = haeRadioVastaus(4, valinta4);
        }

        if (radioGroup5.getCheckedRadioButtonId() == -1) {
            errorLuokka();
        } else {
            selectedId = radioGroup5.getCheckedRadioButtonId();
            radioButton = (RadioButton) findViewById(selectedId);
            valinta5 = radioButton.getText().toString();
            selectedOptionID5 = haeRadioVastaus(5, valinta5);
        }

        EditText suunnitelma = (EditText) findViewById(R.id.suunnitelmaKentta);
        suunnitelmaString = suunnitelma.getText().toString();

        selostus = (EditText) findViewById(R.id.selostusKentta1);
        selostus1 = selostus.getText().toString();

        selostus = (EditText) findViewById(R.id.selostusKentta2);
        selostus2 = selostus.getText().toString();

        selostus = (EditText) findViewById(R.id.selostusKentta3);
        selostus3 = selostus.getText().toString();

        selostus = (EditText) findViewById(R.id.selostusKentta4);
        selostus4 = selostus.getText().toString();

        selostus = (EditText) findViewById(R.id.selostusKentta5);
        selostus5 = selostus.getText().toString();

        rakennaNotifikaatio();

    }

    private void rakennaNotifikaatio() {

        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(Tehtavakortti.this);

        Intent valikkoNakyma = new Intent(Tehtavakortti.this, ValikkoActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                valikkoNakyma, 0);

        mBuilder.setContentTitle("Vastaus")
                .setContentText("Tallennetaan...")
                .setSmallIcon(R.drawable.ic_cloud_upload_white_24dp)
                .setContentIntent(pendingIntent);

        new PoistaVastaus().execute();

    }

    //väliaikainen ghetto poistamaan edellinen vastaus
    private class PoistaVastaus extends AsyncTask<String, Integer, Integer> {
        Integer paluukoodi = 0;

        @Override
        protected Integer doInBackground(String... path) {

            SharedPreferences mySharedPreferences = getSharedPreferences("konfiguraatio", Context.MODE_PRIVATE);
            String base = mySharedPreferences.getString("token", "");

            PassiClient passiClient = ServiceGenerator.createService(PassiClient.class, base);
            Call<ResponseBody> call = passiClient.poistaVastaus(5);

            try {
                Response response = call.execute();
                paluukoodi = response.code();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return paluukoodi;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            new UploadVastaus().execute();

        }
    }

    private class UploadVastaus extends AsyncTask<String, Integer, Integer> {
        Integer paluukoodi = 0;
        final ProgressDialog progressDialog = new ProgressDialog(Tehtavakortti.this,
                R.style.AppTheme_Dark_Dialog);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Displays the progress bar for the first time.
            mBuilder.setProgress(100, 0, false);
            mNotifyManager.notify(id, mBuilder.build());

            progressDialog.setIndeterminate(true);
            progressDialog.setTitle("Vastauksen lähetys");
            progressDialog.setMessage("Lähetetään...");
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(String... path) {

            SharedPreferences mySharedPreferences = getSharedPreferences("konfiguraatio", Context.MODE_PRIVATE);

            List<Etappi> etappiLista = new ArrayList<>();

            Etappi etappi = new Etappi();

            etappi.setWaypointID(1);
            etappi.setSelectedOptionID(selectedOptionID1);
            etappi.setImageURL("/images/pic1.jpg");
            etappi.setAnswerText(selostus1);

            etappiLista.add(etappi);

            etappi = new Etappi();

            etappi.setWaypointID(2);
            etappi.setSelectedOptionID(selectedOptionID2);
            etappi.setImageURL("/images/pic2.jpg");
            etappi.setAnswerText(selostus2);

            etappiLista.add(etappi);

            etappi = new Etappi();

            etappi.setWaypointID(3);
            etappi.setSelectedOptionID(selectedOptionID3);
            etappi.setImageURL("/images/pic3.jpg");
            etappi.setAnswerText(selostus3);

            etappiLista.add(etappi);

            etappi = new Etappi();

            etappi.setWaypointID(4);
            etappi.setSelectedOptionID(selectedOptionID4);
            etappi.setImageURL("/images/pic4.jpg");
            etappi.setAnswerText(selostus4);

            etappiLista.add(etappi);

            etappi = new Etappi();

            etappi.setWaypointID(5);
            etappi.setSelectedOptionID(selectedOptionID5);
            etappi.setImageURL("/images/pic5.jpg");
            etappi.setAnswerText(selostus5);

            etappiLista.add(etappi);

            Vastaus vastaus = new Vastaus();

            vastaus.setPlanningText(suunnitelmaString);
            vastaus.setWorksheetID(1);
            vastaus.setGroupID(1);
            vastaus.setUserID(5);
            vastaus.setAnswerpoints(etappiLista);

            String base = mySharedPreferences.getString("token", "");

            PassiClient kayttajaService = ServiceGenerator.createService(PassiClient.class, base);
            Call<ResponseBody> call = kayttajaService.tallennaVastaus(vastaus);

            try {
                Response response = call.execute();
                paluukoodi = response.code();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return paluukoodi;
        }


        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if (result == 201) {
                mBuilder.setContentText("Vastaus tallennettu");
                Toast.makeText(getApplicationContext(), "Vastaus tallennettu!", Toast.LENGTH_LONG).show();
                //siirry valikkosivulle kun uploadattu servulle
                Intent intent = new Intent(Tehtavakortti.this, ValikkoActivity.class);
                startActivity(intent);
            } else if (result == 409) {
                // do smthing
            } else {
                mBuilder.setContentText("Tallennus epäonnistui");
                Toast.makeText(getApplicationContext(), "Tallennus epäonnistui!", Toast.LENGTH_LONG).show();
            }



            mBuilder.setProgress(0, 0, false);
            mNotifyManager.notify(id, mBuilder.build());
        }
    }

    private class UploadKuva extends AsyncTask<String, Integer, Integer> {
        Integer paluukoodi = null;

        protected Integer doInBackground(String... path) {
            SharedPreferences mySharedPreferences = getSharedPreferences("konfiguraatio", Context.MODE_PRIVATE);

            try {
                List<File> kuvat = new ArrayList<>();

                kuvat.add(0, kuva1);
                kuvat.add(2, kuva2);
                kuvat.add(3, kuva3);
                kuvat.add(4, kuva4);
                kuvat.add(5, kuva5);

                for (int i = 0; kuvat.size() > i; i++) {


                    String pohja;
                    String urlIlmantunnusta = "http://proto384.haaga-helia.fi/passi-rest/answer/";
                    String base = mySharedPreferences.getString("token", "");
                    String basicAuth = "Basic " + base;
                    URL urli = new URL(urlIlmantunnusta);
                    HttpURLConnection clientti = (HttpURLConnection) urli.openConnection();
                    clientti.setRequestMethod("POST");
                    clientti.setRequestProperty("Authorization", basicAuth);
                    clientti.setRequestProperty("Content-Type", "image/jpeg");
                    clientti.setDoOutput(true);
                    clientti.setDoInput(true);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    File kuva = kuvat.get(i);

                    File ukko = saveBitmapToFile(kuva);

                    baos.flush();
                    baos.close();

                    paluukoodi = clientti.getResponseCode();
                    String paluukoodiString = Integer.toString(clientti.getResponseCode());

                    Log.e("Paluukoodi lähety: ", paluukoodiString);

                    if (clientti != null) {
                        clientti.disconnect();
                    }
                }
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    public File saveBitmapToFile(File file) {
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE = 75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }


    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(Tehtavakortti.this, TehtavakortinValintaActivity.class);
        startActivity(intent);

    }


}
