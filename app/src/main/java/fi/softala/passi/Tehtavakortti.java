package fi.softala.passi;

import android.app.NotificationManager;
import android.app.PendingIntent;
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
    final int userID = 12;
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
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    Intent intent = new Intent(Tehtavakortti.this, ValikkoActivity.class);
                                    startActivity(intent);
                                }
                            }, 1000);
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
        String kuvaNimi = kameraButtonPressed + "-" + userID + ".jpg";

        file = new File(Tehtavakortti.this.getExternalCacheDir(),
                String.valueOf(kuvaNimi));
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
                kameraButton1.setBackground(ContextCompat.getDrawable(context, R.drawable.red_face_pressed)
                );
                kameraButton1.setEnabled(false);
            } else if (kameraButtonPressed == 2) {
                kuva2 = stringUri;
                kameraButton2.setBackground(ContextCompat.getDrawable(context, R.drawable.red_face_pressed)
                );
                kameraButton2.setEnabled(false);
            } else if (kameraButtonPressed == 3) {
                kuva3 = stringUri;
                kameraButton3.setBackground(ContextCompat.getDrawable(context, R.drawable.red_face_pressed)
                );
                kameraButton3.setEnabled(false);
            } else if (kameraButtonPressed == 4) {
                kuva4 = stringUri;
                kameraButton4.setBackground(ContextCompat.getDrawable(context, R.drawable.red_face_pressed)
                );
                kameraButton4.setEnabled(false);
            } else if (kameraButtonPressed == 5) {
                kuva5 = stringUri;
                kameraButton5.setBackground(ContextCompat.getDrawable(context, R.drawable.red_face_pressed)
                );
                kameraButton5.setEnabled(false);
            }
            kameraButtonPressed = 0;

        }

    }

    private int haeRadioVastaus(int kysymysnumero, String valinta) {
        final String ok = "Kaikki OK";
        final String puutteita = "Vaarallinen tai selkeitä puuteita";
        final String korjattavaa = "Korjattavaa löytyy";
        int laskuNumero = 0;

        Integer laskettuVastaus = 0;
        // Laskee radiobutton id. Vaihtoehtoinen tapa on "valintanumero + (kysymysnumero -1) * 3"
        // Molemmat vaativat kysymysnumeron lähtevän 1.
        if (valinta.equals(ok)) {
            laskuNumero = 1;
            laskettuVastaus = laskuNumero + (kysymysnumero - 1) * 3;

        }
        if (valinta.equals(korjattavaa)) {
            laskuNumero = 2;
            laskettuVastaus = laskuNumero + (kysymysnumero - 1) * 3;

        }
        if (valinta.equals(puutteita)) {
            laskuNumero = 3;
            laskettuVastaus = laskuNumero + (kysymysnumero - 1) * 3;

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

        mBuilder.setSmallIcon(android.R.drawable.stat_sys_upload)
                .setContentTitle("Tehtäväkortti")
                .setContentText("Vastausta tallennetaan...")
                .setContentIntent(pendingIntent);

        Toast.makeText(getApplicationContext(), "Vastausta tallennetaan", Toast.LENGTH_LONG).show();

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
            Call<ResponseBody> call = passiClient.poistaVastaus(12);

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


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Displays the progress bar for the first time.
            mBuilder.setProgress(0, 0, true);
            mNotifyManager.notify(id, mBuilder.build());

        }

        @Override
        protected Integer doInBackground(String... path) {

            SharedPreferences mySharedPreferences = getSharedPreferences("konfiguraatio", Context.MODE_PRIVATE);

            List<Etappi> etappiLista = new ArrayList<>();

            Etappi etappi = new Etappi();

            etappi.setWaypointID(1);
            etappi.setSelectedOptionID(selectedOptionID1);
            etappi.setImageURL(kuva1.getName());
            etappi.setAnswerText(selostus1);

            etappiLista.add(etappi);

            etappi = new Etappi();

            etappi.setWaypointID(2);
            etappi.setSelectedOptionID(selectedOptionID2);
            etappi.setImageURL(kuva2.getName());
            etappi.setAnswerText(selostus2);

            etappiLista.add(etappi);

            etappi = new Etappi();

            etappi.setWaypointID(3);
            etappi.setSelectedOptionID(selectedOptionID3);
            etappi.setImageURL(kuva3.getName());
            etappi.setAnswerText(selostus3);

            etappiLista.add(etappi);

            etappi = new Etappi();

            etappi.setWaypointID(4);
            etappi.setSelectedOptionID(selectedOptionID4);
            etappi.setImageURL(kuva4.getName());
            etappi.setAnswerText(selostus4);

            etappiLista.add(etappi);

            etappi = new Etappi();

            etappi.setWaypointID(5);
            etappi.setSelectedOptionID(selectedOptionID5);
            etappi.setImageURL(kuva5.getName());
            etappi.setAnswerText(selostus5);

            etappiLista.add(etappi);

            Vastaus vastaus = new Vastaus();

            vastaus.setPlanningText(suunnitelmaString);
            vastaus.setWorksheetID(1);
            vastaus.setGroupID(3);
            vastaus.setUserID(12);
            vastaus.setAnswerpoints(etappiLista);

            String base = mySharedPreferences.getString("token", "");

            PassiClient passiClient = ServiceGenerator.createService(PassiClient.class, base);
            Call<ResponseBody> call = passiClient.tallennaVastaus(vastaus);

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

            if (result == 201) {
                new uploadKuvat().execute();
            } else if (result == 409) {
                // do smthing
            } else {
                mBuilder.setContentText("Tallennus epäonnistui");
                Toast.makeText(getApplicationContext(), "Tallennus epäonnistui!", Toast.LENGTH_LONG).show();
            }

        }
    }

    private class uploadKuvat extends AsyncTask<String, Integer, Integer> {
        Integer paluukoodi = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mBuilder.setContentText("Tallennetaan kuvia...");

        }

        protected Integer doInBackground(String... path) {
            SharedPreferences mySharedPreferences = getSharedPreferences("konfiguraatio", Context.MODE_PRIVATE);

            String base = mySharedPreferences.getString("token", "");
            PassiClient passiClient = ServiceGenerator.createService(PassiClient.class, 120, base);
            File kuva;
            String kuvaNimi;
            ArrayList<File> kuvat = new ArrayList<>();
            byte[] byteKuva;
            kuvat.add(kuva1);
            kuvat.add(kuva2);
            kuvat.add(kuva3);
            kuvat.add(kuva4);
            kuvat.add(kuva5);

            for (int i = 0; kuvat.size() > i; i++) {
                kuva = kuvat.get(i);
                kuvaNimi = kuva.getName().split("\\.")[0];
                Integer kuvaLkm = 1 + i;
                mBuilder.setContentText("Tallennetaan kuvaa " + kuvaLkm + "/5");
                try {
                    byteKuva = kuvastaByteksi(kuva);
                    Call<ResponseBody> call = passiClient.tallennaKuva("image/jpeg", kuvaNimi, byteKuva);
                    Response response = call.execute();
                    paluukoodi = response.code();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return paluukoodi;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            mBuilder.setContentText("Vastaus tallennettu");
            mBuilder.setSmallIcon(android.R.drawable.stat_sys_upload_done);
            mBuilder.setProgress(0, 0, false);
            mNotifyManager.notify(id, mBuilder.build());
            Toast.makeText(getApplicationContext(), "Vastaus tallennettu", Toast.LENGTH_LONG).show();
        }
    }

    public byte[] kuvastaByteksi(File file) {
        Bitmap bitmapKuva = BitmapFactory.decodeFile(file.getAbsolutePath());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmapKuva.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }


    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(Tehtavakortti.this, TehtavakortinValintaActivity.class);
        startActivity(intent);

    }


}
