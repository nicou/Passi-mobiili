package fi.softala.passi.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import fi.softala.passi.R;
import fi.softala.passi.adapters.KorttiAdapter;
import fi.softala.passi.models.Etappi;
import fi.softala.passi.models.Ryhma;
import fi.softala.passi.models.Vastaus;
import fi.softala.passi.models.Worksheet;
import fi.softala.passi.models.WorksheetWaypoints;
import fi.softala.passi.network.CountingFileRequestBody;
import fi.softala.passi.network.PassiClient;
import fi.softala.passi.network.ServiceGenerator;
import okhttp3.RequestBody;
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
    int currentapiVersion = android.os.Build.VERSION.SDK_INT;
    private static final int MY_PERMISSION_USE_CAMERA = 10;
    private static final int MY_PERMISSION_WRITE_EXTERNAL = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tehtavakortti);
        Gson gson = new Gson();
        String korttiJSON = getIntent().getStringExtra("Tehtavakortti");
        final Worksheet kortti = gson.fromJson(korttiJSON, Worksheet.class);

        ImageButton imHome = (ImageButton)findViewById(R.id.home);
        imHome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Tehtavakortti.this, fi.softala.passi.activities.ValikkoActivity.class);
                startActivity(intent);

            }
        });

        ImageButton imFeedback = (ImageButton)findViewById(R.id.feedback);
        imFeedback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Tehtavakortti.this, PalauteActivity.class);
                startActivity(intent);

            }
        });


        //Kirjaudu ulos toolbarista
        ImageButton imLogout = (ImageButton)findViewById(R.id.logout);
        imLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                kirjauduUlos();
            }
        });

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

        taytaTiedotTehtavakorteista();
        //Sets all tab titles to singleline
        int c = host.getTabWidget().getChildCount();
        for (int i = 0; c > i; i++) {

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

                    keraaTiedot();
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    Intent intent = new Intent(Tehtavakortti.this, ValikkoActivity.class);
                                    startActivity(intent);
                                }
                            }, 1000);

            }
        });

        TextView tv = (TextView) findViewById(R.id.textView1);

        tv.setMovementMethod(new ScrollingMovementMethod());

        /*
        EditText e = (EditText) findViewById(R.id.editText20);

        e.setText(kortti.getWorksheetWaypoints().get(0).getWaypointTask());
        e = (EditText) findViewById(R.id.editText21);
        e.setText(kortti.getWorksheetWaypoints().get(1).getWaypointTask());
        e = (EditText) findViewById(R.id.editText22);
        e.setText(kortti.getWorksheetWaypoints().get(2).getWaypointTask());
        e = (EditText) findViewById(R.id.editText23);
        e.setText(kortti.getWorksheetWaypoints().get(3).getWaypointTask());
        e = (EditText) findViewById(R.id.editText24);
        e.setText(kortti.getWorksheetWaypoints().get(4).getWaypointTask());

 */
        tv.setText(kortti.getWorksheetPreface());
    }

    // kun painetaan kameranappia riippuen mikä nappi
    public void onButtonClick(View view) {

        switch (view.getId()) {
            case R.id.kameraButton1:
                kameraButtonPressed = 1;
                break;
//            case R.id.kameraButton2:
//                kameraButtonPressed = 2;
//                break;
//            case R.id.kameraButton3:
//                kameraButtonPressed = 3;
//                break;
//            case R.id.kameraButton4:
//                kameraButtonPressed = 4;
//                break;
//            case R.id.kameraButton5:
//                kameraButtonPressed = 5;
//                break;
        }

        hankiLuvat();

    }

    /*
        asettaa kuvan ottamisen jälkeen URIn mistä kuva löytyy stringiin
        riippuen mitä nappia on painettu ja vaihtaa painetun napin kuvan
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tehtavakortti.super.onActivityResult(requestCode, resultCode, data);
        ImageButton kameraButton1 = (ImageButton) findViewById(R.id.kameraButton1);
//        ImageButton kameraButton2 = (ImageButton) findViewById(R.id.kameraButton2);
//        ImageButton kameraButton3 = (ImageButton) findViewById(R.id.kameraButton3);
//        ImageButton kameraButton4 = (ImageButton) findViewById(R.id.kameraButton4);
//        ImageButton kameraButton5 = (ImageButton) findViewById(R.id.kameraButton5);

        if (requestCode == RC_TAKE_PHOTO && resultCode == RESULT_OK) {

            File stringUri = new File(file.toString());
            Context context = getApplicationContext();
            if (kameraButtonPressed == 1) {
                // vaihetaan nappulan taustakuva
                kuva1 = stringUri;
                kameraButton1.setBackground(ContextCompat.getDrawable(context, R.drawable.thumb_up)
                );
                kameraButton1.setEnabled(false);
//            } else if (kameraButtonPressed == 2) {
//                kuva2 = stringUri;
//                kameraButton2.setBackground(ContextCompat.getDrawable(context, R.drawable.thumb_up)
//                );
//                kameraButton2.setEnabled(false);
//            } else if (kameraButtonPressed == 3) {
//                kuva3 = stringUri;
//                kameraButton3.setBackground(ContextCompat.getDrawable(context, R.drawable.thumb_up)
//                );
//                kameraButton3.setEnabled(false);
//            } else if (kameraButtonPressed == 4) {
//                kuva4 = stringUri;
//                kameraButton4.setBackground(ContextCompat.getDrawable(context, R.drawable.thumb_up)
//                );
//                kameraButton4.setEnabled(false);
//            } else if (kameraButtonPressed == 5) {
//                kuva5 = stringUri;
//                kameraButton5.setBackground(ContextCompat.getDrawable(context, R.drawable.thumb_up)
//                );
//                kameraButton5.setEnabled(false);
//            }
            kameraButtonPressed = 0;

        }

    }}

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

    private void keraaTiedot()  {
        EditText selostus;
        RadioGroup radioGroup1 = (RadioGroup) findViewById(R.id.radio1);
//        RadioGroup radioGroup2 = (RadioGroup) findViewById(R.id.radio2);
//        RadioGroup radioGroup3 = (RadioGroup) findViewById(R.id.radio3);
//        RadioGroup radioGroup4 = (RadioGroup) findViewById(R.id.radio4);
//        RadioGroup radioGroup5 = (RadioGroup) findViewById(R.id.radio5);

        if (radioGroup1.getCheckedRadioButtonId() == -1) {
            errorLuokka();
        } else {
            selectedId = radioGroup1.getCheckedRadioButtonId();
            radioButton = (RadioButton) findViewById(selectedId);
            valinta1 = radioButton.getText().toString();

            selectedOptionID1 = haeRadioVastaus(1, valinta1);

        }
//
//        if (radioGroup2.getCheckedRadioButtonId() == -1) {
//            errorLuokka();
//        } else {
//            selectedId = radioGroup2.getCheckedRadioButtonId();
//            radioButton = (RadioButton) findViewById(selectedId);
//            valinta2 = radioButton.getText().toString();
//
//            selectedOptionID2 = haeRadioVastaus(2, valinta2);
//        }
//
//        if (radioGroup3.getCheckedRadioButtonId() == -1) {
//            errorLuokka();
//        } else {
//            selectedId = radioGroup3.getCheckedRadioButtonId();
//            radioButton = (RadioButton) findViewById(selectedId);
//            valinta3 = radioButton.getText().toString();
//            selectedOptionID3 = haeRadioVastaus(3, valinta3);
//        }
//
//        if (radioGroup4.getCheckedRadioButtonId() == -1) {
//            errorLuokka();
//        } else {
//            selectedId = radioGroup4.getCheckedRadioButtonId();
//            radioButton = (RadioButton) findViewById(selectedId);
//            valinta4 = radioButton.getText().toString();
//            selectedOptionID4 = haeRadioVastaus(4, valinta4);
//        }
//
//        if (radioGroup5.getCheckedRadioButtonId() == -1) {
//            errorLuokka();
//        } else {
//            selectedId = radioGroup5.getCheckedRadioButtonId();
//            radioButton = (RadioButton) findViewById(selectedId);
//            valinta5 = radioButton.getText().toString();
//            selectedOptionID5 = haeRadioVastaus(5, valinta5);
//        }

        EditText suunnitelma = (EditText) findViewById(R.id.suunnitelmaKentta);
        suunnitelmaString = suunnitelma.getText().toString();

        selostus = (EditText) findViewById(R.id.selostusKentta1);
        selostus1 = selostus.getText().toString();

//        selostus = (EditText) findViewById(R.id.selostusKentta2);
//        selostus2 = selostus.getText().toString();
//
//        selostus = (EditText) findViewById(R.id.selostusKentta3);
//        selostus3 = selostus.getText().toString();
//
//        selostus = (EditText) findViewById(R.id.selostusKentta4);
//        selostus4 = selostus.getText().toString();
//
//        selostus = (EditText) findViewById(R.id.selostusKentta5);
//        selostus5 = selostus.getText().toString();

        new PoistaVastaus().execute();

    }

    //atm vastaus pitää poistaa enne uuden lisäämistä samaan vastaukseen
    private class PoistaVastaus extends AsyncTask<String, Integer, Integer> {
        Integer paluukoodi = 0;

        @Override
        protected void onPreExecute() {
            mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(Tehtavakortti.this);

            Intent valikkoNakyma = new Intent(Tehtavakortti.this, ValikkoActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(Tehtavakortti.this, 0,
                    valikkoNakyma, 0);

            mBuilder.setSmallIcon(android.R.drawable.stat_sys_upload)
                    .setContentTitle("Tehtäväkortti")
                    .setContentText("Vastausta tallennetaan...")
                    .setContentIntent(pendingIntent);
            mNotifyManager.notify(id, mBuilder.build());

            Toast.makeText(getApplicationContext(), "Vastausta tallennetaan", Toast.LENGTH_LONG).show();
        }

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
                mNotifyManager.notify(id, mBuilder.build());
                Toast.makeText(getApplicationContext(), "Tallennus epäonnistui!", Toast.LENGTH_LONG).show();
            }

        }
    }

    private class uploadKuvat extends AsyncTask<String, Integer, Integer> {
        Integer paluukoodi = 200;
        long totalSize;
        int progressValue;
        Integer kuvaLkm;
        List<File> kuvat = new ArrayList<>();
        final Integer MAX_PROGRESS = 100;
        private final Handler handler = new Handler();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            kuvat.add(kuva1);
            kuvat.add(kuva2);
            kuvat.add(kuva3);
            kuvat.add(kuva4);
            kuvat.add(kuva5);
        }

        protected Integer doInBackground(String... path) {
            SharedPreferences mySharedPreferences = getSharedPreferences("konfiguraatio", Context.MODE_PRIVATE);

            String base = mySharedPreferences.getString("token", "");
            PassiClient passiClient = ServiceGenerator.createService(PassiClient.class, 300, base);
            File kuva;
            String kuvaNimi;

            for (File kuvaEnnenMuutosta : kuvat) {
                pienennaKuvaa(kuvaEnnenMuutosta);
            }

            for (int i = 0; i < kuvat.size(); i++) {
                kuvaLkm = 1 + i;
                mBuilder.setProgress(100, 0, false);
                kuva = kuvat.get(i);
                //kuva = pienennaKuvaa(kuva);
                kuvaNimi = kuva.getName().split("\\.")[0];
                progressValue = 0;
                try {
                    totalSize = kuva.length();
                    RequestBody responseBody = new CountingFileRequestBody(kuva, "image/jpeg", new CountingFileRequestBody.ProgressListener() {
                        @Override
                        public void transferred(long num) {
                            float progress = (num / (float) totalSize) * 100;
                            progressValue = (int) progress;
                            if (progressValue % ( MAX_PROGRESS / 10 ) == 0) {
                                publishProgress(progressValue, kuvaLkm);
                            }
                        }
                    });
                    Call<ResponseBody> call = passiClient.tallennaKuva(kuvaNimi, responseBody);
                    Response response = call.execute();
                    if (response.isSuccessful()) {
                        paluukoodi = response.code();
                    } else {
                        paluukoodi = 400;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            return paluukoodi;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            mBuilder.setContentText("Tallennetaan kuvaa " + progress[1] + "/" + kuvat.size());
            mBuilder.setProgress(MAX_PROGRESS, progress[0], false);
            mNotifyManager.notify(id, mBuilder.build());
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result == 200) {
                mBuilder.setContentText("Vastaus tallennettu");
                Toast.makeText(getApplicationContext(), "Vastaus tallennettu", Toast.LENGTH_LONG).show();
            } else {
                mBuilder.setContentText("Tallennus epäonnistui");
            }
            mBuilder.setSmallIcon(android.R.drawable.stat_sys_upload_done);
            mBuilder.setProgress(0, 0, false);
            mNotifyManager.notify(id, mBuilder.build());
        }
    }

    public File pienennaKuvaa(final File file) {
        try {
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = 1;
            FileInputStream inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);

            outputStream.flush();
            outputStream.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private void hankiLuvat() {
        if (ActivityCompat.checkSelfPermission(Tehtavakortti.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Tehtavakortti.this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSION_USE_CAMERA);

        } else if (ActivityCompat.checkSelfPermission(Tehtavakortti.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Tehtavakortti.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSION_WRITE_EXTERNAL);

        } else {

            kuvanOtto();

        }
    }

    public void kuvanOtto() {
        Intent kameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String kuvaNimi = kameraButtonPressed + "-" + userID + ".jpg";

        file = new File(Tehtavakortti.this.getExternalCacheDir(),
                String.valueOf(kuvaNimi));
        fileUri = Uri.fromFile(file);
        Log.d("Passi", "Kuva otettu " + fileUri);
        kameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        Tehtavakortti.this.startActivityForResult(kameraIntent, RC_TAKE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSION_USE_CAMERA:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    hankiLuvat();

                } else {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.CAMERA)) {
                        new AlertDialog.Builder(this).
                                setTitle("Oikeudet kameraan").
                                setMessage("Sovellus tarvitsee luvan käyttää kameraa").show();
                    } else {
                        new AlertDialog.Builder(this).
                                setTitle("Oikeudet evätty").
                                setMessage("Hyväksyäksesi luvan käyttää kameraa" +
                                        ", mene puhelimen asetuksiin ja " +
                                        "salli kameran käyttäminen sovelluksessa").show();
                    }

                }

                break;
            case MY_PERMISSION_WRITE_EXTERNAL:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    hankiLuvat();

                } else {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        new AlertDialog.Builder(this).
                                setTitle("Oikeudet kuvien käyttöön").
                                setMessage("Sovellus tarvitsee toimiakseen luvan tallentaa kuvat väliaikaisesti puhelimen muistiin").show();
                    } else {
                        new AlertDialog.Builder(this).
                                setTitle("Oikeudet evätty").
                                setMessage("Et antanut lupaa, joten et voi täyttää tehtäväkorttia." +
                                        "Hyväksyäksesi luvan" +
                                        ", mene puhelimen asetuksiin ja " +
                                        "anna oikeudet sovellukselle").show();
                    }

                }

                break;
        }
    }

    private void kirjauduUlos() {
        new android.support.v7.app.AlertDialog.Builder(Tehtavakortti.this).setMessage("Kirjaudu ulos?")
                .setPositiveButton("Kyllä", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences mySharedPreferences = getSharedPreferences("konfiguraatio", Context.MODE_PRIVATE);
                        mySharedPreferences.edit()
                                .remove("tunnus")
                                .remove("token")
                                .apply();
                        Intent sisaanKirjautuminen = new Intent(getApplicationContext(), KirjautumisActivity.class);
                        startActivity(sisaanKirjautuminen);
                    }
                })
                .setNegativeButton("Peruuta", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                }).show();
    }

    private void taytaTiedotTehtavakorteista() {


        SharedPreferences sp = getSharedPreferences("konfiguraatio", Context.MODE_PRIVATE);

        String json = sp.getString("kortitJson","");


        Gson gson = new Gson();

        Type type = new TypeToken<List<Worksheet>>(){}.getType();
        List<Worksheet> wss = gson.fromJson(json, type);

        String johdantoString = null;
        String suunitelmaString = null;
        if(!json.isEmpty()){

            RecyclerView recyclerview = (RecyclerView) findViewById(R.id.my_recycler_view);
            recyclerview.setLayoutManager(new LinearLayoutManager(this));
            RecyclerView.LayoutManager RecyclerViewLayoutManager;
            RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());

            recyclerview.setLayoutManager(RecyclerViewLayoutManager);





            for (Worksheet ws : wss) {
                johdantoString = ws.getWorksheetPreface();
                suunitelmaString = ws.getWorksheetPlanning();

                List<WorksheetWaypoints> waypoint = ws.getWorksheetWaypoints();
                RecyclerView.Adapter adapter = new KorttiAdapter(waypoint);
                recyclerview.setAdapter(adapter);
            for(WorksheetWaypoints wp : waypoint){


            }
        }

        //Johdanto teksti
        TextView textViewJohdanto = (TextView) findViewById(R.id.textView1);
        textViewJohdanto.setText(johdantoString);

        //Suunitelma teksti
        TextView textViewSuunitelma = (TextView) findViewById(R.id.suunitelmaTeksti);
        textViewSuunitelma.setText(suunitelmaString);





    }


    }

}
