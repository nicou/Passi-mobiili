package fi.softala.tyokykypassi.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import fi.softala.tyokykypassi.BuildConfig;
import fi.softala.tyokykypassi.R;
import fi.softala.tyokykypassi.adapters.KorttiAdapter;
import fi.softala.tyokykypassi.models.Etappi;
import fi.softala.tyokykypassi.models.Vastaus;
import fi.softala.tyokykypassi.models.Worksheet;
import fi.softala.tyokykypassi.models.WorksheetWaypoints;
import fi.softala.tyokykypassi.network.KuvaUploadaus;
import fi.softala.tyokykypassi.network.PassiClient;
import fi.softala.tyokykypassi.network.ServiceGenerator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TehtavakorttiActivity extends ToolbarActivity {
    KorttiAdapter kAdapter;
    File file;
    final List<File> otetutKuvat = new ArrayList<>();
    private ProgressDialog progressDialog;
    int vastausID;
    final int RC_TAKE_PHOTO = 1;
    String suunnitelmaString;
    private static final int MY_PERMISSION_USE_CAMERA = 10;
    private static final int MY_PERMISSION_WRITE_EXTERNAL = 20;
    private Context mContext;
    int groupID, userID;
    String tehtavakorttiOtsikkoString;
    private ImageButton mCamera;
    HashMap<Integer, Etappi> etappiList = new HashMap<>();
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

    public void poistaVastaus() {

        progressDialog = new ProgressDialog(TehtavakorttiActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Tallennetaan vastausta...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        SharedPreferences mySharedPreferences = getSharedPreferences("konfiguraatio", Context.MODE_PRIVATE);
        String base = mySharedPreferences.getString("token", "");

        PassiClient passiClient = ServiceGenerator.createService(PassiClient.class, base);

        Call<ResponseBody> call = passiClient.poistaVastaus(vastausID, userID);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() || response.code() == 404) {
                    uploadVastaus();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(TehtavakorttiActivity.this, "Vanhan poisto epäonnistui", Toast.LENGTH_SHORT).show();
                Log.e("Tehtavakortti", "Poisto epäonnistui " + t.toString());
            }
        });
    }

    public void uploadVastaus() {
        EditText suunnitelma = (EditText) findViewById(R.id.suunnitelmaKentta);
        suunnitelmaString = suunnitelma.getText().toString();
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
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    ArrayList<String> kuvapolut = new ArrayList<String>();
                    for (File kuva : otetutKuvat) {
                        Log.d("Kuva", "Path = " + kuva.getAbsolutePath());
                        kuvapolut.add(kuva.getAbsolutePath());
                    }
                    if (otetutKuvat.size() >= 1) {
                        Intent upload = new Intent(TehtavakorttiActivity.this, KuvaUploadaus.class);
                        upload.putExtra("kuvat", kuvapolut);
                        TehtavakorttiActivity.this.startService(upload);
                    }
                    Intent vahvistus = new Intent(TehtavakorttiActivity.this, VahvistusActivity.class);
                    vahvistus.putExtra("nimi",tehtavakorttiOtsikkoString);
                    poistaValiaikainenVastaus();
                    progressDialog.dismiss();
                    startActivity(vahvistus);
                } else {
                    Toast.makeText(getApplicationContext(), "Tallennus epäonnistui!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Tehtavakortti", "Vastauksen tallennus epäonnistui" + t.toString());
                Toast.makeText(TehtavakorttiActivity.this, "Vastauksen tallennus epäonnistui", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void laheta() {
        lisaaKuvaUri();
        boolean ok = kentatOk();
        if (ok) {
            poistaVastaus();
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

        for (WorksheetWaypoints w : waypoint) {
            Etappi e = etappiList.get(w.getWaypointID());
            e.setPhotoEnabled(w.getWaypointPhotoEnabled());
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
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            fileUri = FileProvider.getUriForFile(TehtavakorttiActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);

            Log.v("Passi", "Kuva otettu " + fileUri);
            kameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        } else {
            kameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            Log.v("Passi", "Kuva otettu " + Uri.fromFile(file).toString());
        }
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
        tehtavakorttiOtsikkoString = kortti.getWorksheetHeader();

        TextView textViewOtsikko = (TextView) findViewById(R.id.otsikko);
        textViewOtsikko.setText(tehtavakorttiOtsikkoString);

        if (tehtavakorttiOtsikkoString.length() > maxPituus) {
            textViewOtsikko.setTextSize(textSize);
        }
        waypoint = kortti.getWorksheetWaypoints();
        haeVastaus(kortti);
        waypointListLength = waypoint.size();
        Log.d("Passi", "Etappilistan pituus " + waypointListLength);

        kAdapter = new KorttiAdapter(kortti, new KorttiAdapter.OnItemClickListener() {
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
        }, new KorttiAdapter.OnTallennaListener() {
            @Override
            public void onClick() {
                tallennaVastaus();
            }
        });

        recyclerview.setAdapter(kAdapter);
        recyclerview.setItemViewCacheSize(waypointListLength);
        recyclerview.setHasFixedSize(true);

        //Johdanto teksti
        TextView textViewJohdanto = (TextView) findViewById(R.id.textView1);
        textViewJohdanto.setText(johdantoString + "\n");

        //Suunitelma teksti
        TextView textViewSuunitelma = (TextView) findViewById(R.id.suunitelmaTeksti);
        textViewSuunitelma.setText(suunitelmaString);

    }

    private void poistaValiaikainenVastaus() {
        File vastaukset = this.getFileStreamPath(String.valueOf(vastausID));
        boolean poistettu = vastaukset.delete();
        if (!poistettu) {
            Log.e("Poisto", "Tiedoston poisto epäonnistui");
        }
    }

    private void tallennaVastaus() {
        EditText suunnitelma = (EditText) findViewById(R.id.suunnitelmaKentta);

        if (etappiList.size() > 0) {
            lisaaKuvaUri();
            FileOutputStream fos;
            try {
                fos = this.openFileOutput(String.valueOf(groupID + "_" + vastausID + "_" + userID), Context.MODE_PRIVATE);
                Log.d("TehtavakorttiActivity", "Tallennetaan vastaus " + groupID + "_" + vastausID + "_" + userID);
                ObjectOutputStream os;

                os = new ObjectOutputStream(fos);
                Iterator iterator = etappiList.entrySet().iterator();
                Map.Entry pair = (Map.Entry) iterator.next();
                Etappi etappi = (Etappi) pair.getValue();
                etappi.setSuunnitelma(suunnitelma.getText().toString().trim());
                etappiList.put(etappi.getWaypointID(), etappi);
                os.writeObject(etappiList);
                os.close();
                fos.close();
                Toast.makeText(this, "Tallennus onnistui", Toast.LENGTH_SHORT).show();
                tallennaKuvat();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Tallennus epäonnistui", Toast.LENGTH_SHORT).show();
                Log.e("Passi", "Tehtäväkortin tallennus epäonnistui");
            }

        } else if (suunnitelma.getText().toString().trim().length() > 0) {
            Toast.makeText(this, "Aloita vähintään 1 tehtävä ennen tallennusta", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Ei tallennettavaa", Toast.LENGTH_SHORT).show();
        }
    }

    private void haeVastaus(Worksheet kortti) {
        FileInputStream fis;
        try {
            File vastaukset = this.getFileStreamPath(String.valueOf(groupID + "_" + vastausID + "_" + userID));
            if (vastaukset.exists()) {
                fis = this.openFileInput(String.valueOf(groupID + "_" + vastausID + "_" + userID));
                Log.d("TehtavakorttiActivity", "Haettiin vastaus " + groupID + "_" + vastausID + "_" + userID);
                ObjectInputStream is = new ObjectInputStream(fis);
                etappiList = (HashMap<Integer, Etappi>) is.readObject();
                Log.d("Passi", "Haettu etapit" + etappiList.toString());
                for (Object o : etappiList.entrySet()) {
                    Map.Entry pair = (Map.Entry) o;
                    Etappi etappi = (Etappi) pair.getValue();
                    Log.d("Hashmap", etappi.toString());
                    if (etappi.getSuunnitelma() != null) {

                        EditText suunnitelma = (EditText) findViewById(R.id.suunnitelmaKentta);
                        suunnitelma.setText(etappi.getSuunnitelma());
                    }

                    for (WorksheetWaypoints ww : kortti.getWorksheetWaypoints()) {
                        if (ww.getWaypointID() == etappi.getWaypointID()) {
                            if (etappi.getImageURL() != null) {
                                ww.setWanhaKuvaUrl(etappi.getImageURL());
                            }
                            ww.setWanhaRadioButtonValinta(etappi.getSelectedOptionID());
                            ww.setWanhaVastaus(etappi.getAnswerText());
                        }
                    }
                }
                Toast.makeText(this, "Haettu tallennetut tiedot", Toast.LENGTH_SHORT).show();
                is.close();
                fis.close();
            }
            haeKuvat();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void haeKuvat() {
        for (Object o : etappiList.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            Etappi etappi = (Etappi) pair.getValue();
            if (etappi.getImageURL() != null) {
                File kuva = new File(getExternalCacheDir(), etappi.getImageURL());
                if (kuva.exists()) {
                    Log.d("Kuva", "löytyy path  = " + kuva.getAbsolutePath());

                    otetutKuvat.add(kuva);
                }
            }

        }
    }

    private void tallennaKuvat() {
        FileOutputStream fos;
        for (File kuva : otetutKuvat) {
            try {
                fos = this.openFileOutput(kuva.getName(), Context.MODE_PRIVATE);
                ObjectOutputStream os;
                os = new ObjectOutputStream(fos);
                os.writeObject(kuva);
                os.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Tallennus epäonnistui", Toast.LENGTH_SHORT).show();
                Log.e("Passi", "Tehtäväkortin tallennus epäonnistui");
            }
        }

    }
}




