package fi.softala.passi.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import fi.softala.passi.R;
import fi.softala.passi.adapters.KorttiAdapter;
import fi.softala.passi.models.Etappi;
import fi.softala.passi.models.Vastaus;
import fi.softala.passi.models.Worksheet;
import fi.softala.passi.models.WorksheetWaypoints;
import fi.softala.passi.network.CountingFileRequestBody;
import fi.softala.passi.network.PassiClient;
import fi.softala.passi.network.ServiceGenerator;
import fi.softala.passi.utilities.ImageManipulation;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class TehtavakorttiActivity extends ToolbarActivity {
    KorttiAdapter kAdapter;
    File file;
    final List<File> otetutKuvat = new ArrayList<>();
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    final int id = 1;
    int vastausID;
    final int RC_TAKE_PHOTO = 1;
    String suunnitelmaString;
    private static final int MY_PERMISSION_USE_CAMERA = 10;
    private static final int MY_PERMISSION_WRITE_EXTERNAL = 20;
    private Context mContext;
    int groupID, userID;
    private ImageButton mCamera;
    final HashMap<Integer, Etappi> etappiList = new HashMap<>();
    int waypointListLength;
    List<WorksheetWaypoints> waypoint = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tehtavakortti);
        SharedPreferences mySharedPreferences = getSharedPreferences("konfiguraatio", Context.MODE_PRIVATE);

        //Luo iconeille listenerit ja lähettää valikkoActivityyn, jossa id:n perusteella toiminnot
        ImageButton imHome = (ImageButton) findViewById(R.id.home);
        ImageButton imFeedback = (ImageButton) findViewById(R.id.feedback);
        ImageButton imLogout = (ImageButton) findViewById(R.id.logout);

        imHome.setOnClickListener(this);
        imFeedback.setOnClickListener(this);
        imLogout.setOnClickListener(this);

        final TabHost host = (TabHost) findViewById(R.id.tabHost);
        host.setup();


        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

                for (int i = 0; i < host.getTabWidget().getChildCount(); i++) {
                    host.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.valilehti_nappula);
                }

                host.getTabWidget().getChildAt(host.getCurrentTab()).setBackgroundColor(Color.TRANSPARENT);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(host.getApplicationWindowToken(), 0);
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
        for (int i = 0; c > i; i++) {

            TextView title = (TextView) host.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            title.setSingleLine(true);
        }

        for (int i = 0; i < host.getTabWidget().getChildCount(); i++) {
            host.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.valilehti_nappula);
        }

        host.getTabWidget().getChildAt(host.getCurrentTab()).setBackgroundColor(Color.TRANSPARENT);


        TextView tv = (TextView) findViewById(R.id.textView1);
        groupID = Integer.parseInt(getIntent().getStringExtra("ryhmaID"));
        userID = Integer.parseInt(mySharedPreferences.getString("userID", ""));
        tv.setMovementMethod(new ScrollingMovementMethod());
        Log.d("Passi", "ryhma " + groupID + " user " + userID);
        taytaTiedotTehtavakorteista();
    }

    public void laheta() {
        lisaaKuvaUri();
        boolean ok = kentatOk();
        if (ok) {
            new PoistaVastaus().execute();
            Intent intent = new Intent(TehtavakorttiActivity.this, MainActivity.class);
            startActivity(intent);

        } else {
            Toast.makeText(getApplicationContext(), "Vastaa kaikkiin kohtiin", Toast.LENGTH_SHORT).show();
        }
    }

    /*
        asettaa otetun kuvan
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        TehtavakorttiActivity.super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_TAKE_PHOTO && resultCode == RESULT_OK) {
            otetutKuvat.add(file);
            mCamera.setBackground(ContextCompat.getDrawable(mContext, R.drawable.thumb_up)
            );
            mCamera.setEnabled(false);

        }
    }

    //atm vastaus pitää poistaa enne uuden lisäämistä samaan vastaukseen
    private class PoistaVastaus extends AsyncTask<String, Integer, Integer> {
        Integer paluukoodi = 0;

        @Override
        protected void onPreExecute() {
            mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(TehtavakorttiActivity.this);

            Intent valikkoNakyma = new Intent(TehtavakorttiActivity.this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(TehtavakorttiActivity.this, 0,
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

            Call<ResponseBody> call = passiClient.poistaVastaus(userID);

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

    public boolean kentatOk() {

        EditText suunnitelma = (EditText) findViewById(R.id.suunnitelmaKentta);
        if (suunnitelma.getText().length() == 0) {
            Log.e("Passi", "Suunnitelma on liian lyhyt");
            return false;
        }
        if (etappiList.size() < waypointListLength) {
            Log.e("Passi", "Etappilista on " + etappiList.size() + " kun pitäisi olla " + waypointListLength);
            return false;
        }

        for(WorksheetWaypoints w : waypoint){
            Etappi e = etappiList.get(w.getWaypointID());
            e.setPhotoEnabled( w.getWaypointPhotoEnabled());
        }
        for (Etappi e :
                etappiList.values()) {
            if (e.getAnswerText() == null
                    || e.getImageURL() == null && e.getPhotoEnabled()
                    || e.getSelectedOptionID() == null
                    || e.getWaypointID() == null) {
                Log.e("Passi", "Etappilista sai nullin " + e.toString());
                return false;
            } else {
                if (e.getAnswerText().length() == 0) {
                    Log.e("Passi", "Etappilista vastaus on " + e.getAnswerText());
                    return false;
                }
            }
        }
        return true;

    }

    /*
        Ota kuvasta esim 5-12 ensin id 5, jotta tietää oikean
        kohdan etappilistasta johon laittaa kuvan nimi
     */
    public void lisaaKuvaUri() {
        for (File kuva :
                otetutKuvat) {
            String[] nimi = kuva.getName().split("-");
            int etappiID = Integer.parseInt(nimi[0]);
            etappiList.get(etappiID).setImageURL(kuva.getName());
        }
    }

    private class UploadVastaus extends AsyncTask<String, Integer, Integer> {
        Integer paluukoodi = 0;

        @Override
        protected void onPreExecute() {
            EditText suunnitelma = (EditText) findViewById(R.id.suunnitelmaKentta);
            suunnitelmaString = suunnitelma.getText().toString();
        }

        @Override
        protected Integer doInBackground(String... path) {

            // treelist järjestää oikeaan muotoon
            Map<Integer, Etappi> eList = new TreeMap<>(etappiList);
            eList.putAll(etappiList);
            Log.d("Passi", "Etappi treelist " + eList.toString());

            // tästä haluttu arraylist muoto oikeassa järjestyksessä
            ArrayList<Etappi> etappiArrayList = new ArrayList<>();
            for (Etappi e : eList.values()
                    ) {
                etappiArrayList.add(e);
            }


            Log.d("Passi", "Etappi sorted arraylist " + etappiArrayList.toString());

            SharedPreferences mySharedPreferences = getSharedPreferences("konfiguraatio", Context.MODE_PRIVATE);


            Vastaus vastaus = new Vastaus();
            Log.d("Passi", "Lähetetään ryhmään " + groupID + " käyttäjänä " + userID);
            vastaus.setAnswerpoints(etappiArrayList);
            vastaus.setPlanningText(suunnitelmaString);
            vastaus.setWorksheetID(vastausID);
            vastaus.setGroupID(groupID);
            vastaus.setUserID(userID);

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
        final List<File> kuvat = otetutKuvat;
        final Integer MAX_PROGRESS = 100;

        protected Integer doInBackground(String... path) {
            SharedPreferences mySharedPreferences = getSharedPreferences("konfiguraatio", Context.MODE_PRIVATE);

            String base = mySharedPreferences.getString("token", "");
            PassiClient passiClient = ServiceGenerator.createService(PassiClient.class, 300, base);
            File kuva;
            String kuvaNimi;

            for (File kuvaEnnenMuutosta : kuvat) {
                ImageManipulation.pienennaKuvaa(kuvaEnnenMuutosta);
            }

            for (int i = 0; i < kuvat.size(); i++) {
                kuvaLkm = 1 + i;
                mBuilder.setProgress(100, 0, false);
                kuva = kuvat.get(i);
                kuvaNimi = kuva.getName().split("\\.")[0];
                progressValue = 0;
                try {
                    totalSize = kuva.length();
                    RequestBody responseBody = new CountingFileRequestBody(kuva, "image/jpeg", new CountingFileRequestBody.ProgressListener() {
                        @Override
                        public void transferred(long num) {
                            float progress = (num / (float) totalSize) * 100;
                            progressValue = (int) progress;
                            if (progressValue % (MAX_PROGRESS / 10) == 0) {
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
                Intent vahvistusNakyma = new Intent(TehtavakorttiActivity.this, VahvistusActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(TehtavakorttiActivity.this, 0,
                        vahvistusNakyma, 0);
                mBuilder.setContentIntent(pendingIntent);
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

    /*
        Kysy lupia kunnes käyttäjä hyväksyy
     */
    private boolean hankiLuvat() {
        if (ActivityCompat.checkSelfPermission(TehtavakorttiActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(TehtavakorttiActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSION_USE_CAMERA);

        } else if (ActivityCompat.checkSelfPermission(TehtavakorttiActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(TehtavakorttiActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSION_WRITE_EXTERNAL);

        } else {

            return true;

        }
        return false;
    }

    /*
        Tallenna kuva nimellä "etappiID-käyttäjäID.jpg"
     */
    public void kuvanOtto(int waypointID, Context context) {
        Uri fileUri;
        Intent kameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String kuvaNimi = waypointID + "-" + userID + ".jpg";
        mContext = context;
        file = new File(TehtavakorttiActivity.this.getExternalCacheDir(),
                String.valueOf(kuvaNimi));
        fileUri = Uri.fromFile(file);
        Log.d("Passi", "Kuva otettu " + fileUri);
        kameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        TehtavakorttiActivity.this.startActivityForResult(kameraIntent, RC_TAKE_PHOTO);
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


    private void taytaTiedotTehtavakorteista() {

        final int maxPituus = 27;
        final int textSize = 15;

        Gson gson = new Gson();
        String korttiJSON = getIntent().getStringExtra("TehtavakorttiActivity");
        final Worksheet kortti = gson.fromJson(korttiJSON, Worksheet.class);
        vastausID = kortti.getWorksheetID();
        RecyclerView recyclerview = (RecyclerView) findViewById(R.id.etappi_recycler_view);

        recyclerview.setLayoutManager(new LinearLayoutManager(this));


        String johdantoString = kortti.getWorksheetPreface();
        String suunitelmaString = kortti.getWorksheetPlanning();
        String tehtavakorttiOtsikkoString = kortti.getWorksheetHeader();

        TextView textViewOtsikko = (TextView) findViewById(R.id.otsikko);
        textViewOtsikko.setText(tehtavakorttiOtsikkoString);

        if(tehtavakorttiOtsikkoString.length() > maxPituus) {
            textViewOtsikko.setTextSize(textSize);
        }
        waypoint = kortti.getWorksheetWaypoints();

        waypointListLength = waypoint.size();
        Log.d("Passi", "Etappilistan pituus " + waypointListLength);

        kAdapter = new KorttiAdapter(waypoint, new KorttiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(WorksheetWaypoints points, Context context, ImageButton camera) {

                boolean hankittuLuvat = hankiLuvat();
                if (hankittuLuvat) {
                    int waypointID = points.getWaypointID();
                    Etappi e = etappiList.get(waypointID);
                    if (e == null) {
                        e = new Etappi();
                    }
                    e.setWaypointID(waypointID);
                    etappiList.put(waypointID, e);
                    mCamera = camera;
                    kuvanOtto(waypointID, context);
                }

            }
        }, new KorttiAdapter.onRadioButtonCheckChange() {
            @Override
            public void onCheck(int waypointId, int radioID) {
                Etappi e = etappiList.get(waypointId);
                if (e == null) {
                    e = new Etappi();
                }
                e.setWaypointID(waypointId);
                e.setSelectedOptionID(radioID);
                etappiList.put(waypointId, e);

                Log.d("Passi", "Listana " + etappiList.toString());
            }
        }, new KorttiAdapter.OnTextChangeListener() {
            @Override
            public void onTextChange(int waypointId, String text) {
                Etappi e = etappiList.get(waypointId);
                if (e == null) {
                    e = new Etappi();
                }
                e.setAnswerText(text);
                etappiList.put(waypointId, e);

                Log.d("Passi", "Listana " + etappiList.toString());
            }
        }, new KorttiAdapter.OnClickListener() {
            @Override
            public void onClick() {
                laheta();
            }
        });

        recyclerview.setAdapter(kAdapter);
        recyclerview.setItemViewCacheSize(waypointListLength);
        recyclerview.setHasFixedSize(true);

        //Johdanto teksti
        TextView textViewJohdanto = (TextView) findViewById(R.id.textView1);
        textViewJohdanto.setText(johdantoString);

        //Suunitelma teksti
        TextView textViewSuunitelma = (TextView) findViewById(R.id.suunitelmaTeksti);
        textViewSuunitelma.setText(suunitelmaString);

    }


}





