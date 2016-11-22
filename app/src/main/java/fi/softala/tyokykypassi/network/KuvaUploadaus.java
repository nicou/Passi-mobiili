package fi.softala.tyokykypassi.network;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fi.softala.tyokykypassi.activities.VahvistusActivity;
import fi.softala.tyokykypassi.utilities.ImageManipulation;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by villeaaltonen on 17/11/2016.
 */

public class KuvaUploadaus extends IntentService {
    private static final String PROGRESS_EVENT = "PROGRESS";
    private static final String PROGRESS_KEY = "PROGRESS_KEY";
    private final static int NOTIFICATION_ID = 1;
    int kuvaLkm;

    public KuvaUploadaus() {
        super("Uploadkuvaservice");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        final NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
        mBuilder.setSmallIcon(android.R.drawable.stat_sys_upload)
                .setContentTitle("Tehtäväkortti")
                .setContentText("Vastausta tallennetaan...");
        mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
        Bundle extras = intent.getExtras();
        if (extras != null) {
            ArrayList<String> kuvapolut = extras.getStringArrayList("kuvat");
            Log.d("KuvaUploadaus", "polkuja on " + kuvapolut.size());
            List<File> kuvat = new ArrayList<>();
            for (String polku : kuvapolut) {
                File kuva = new File(polku);
                kuvat.add(kuva);
            }
            final int kuvatKoko = kuvat.size();

            final int MAX_PROGRESS = 100;
            int paluukoodi = 200;
            SharedPreferences mySharedPreferences = getSharedPreferences("konfiguraatio", Context.MODE_PRIVATE);

            String base = mySharedPreferences.getString("token", "");
            PassiClient passiClient = ServiceGenerator.createService(PassiClient.class, 300, base);
            File kuva;
            String kuvaNimi;

            for (File kuvaEnnenMuutosta : kuvat) {
                ImageManipulation.pienennaKuvaa(kuvaEnnenMuutosta, 600, 600);
            }

            for (int i = 0; i < kuvat.size(); i++) {
                mBuilder.setProgress(100, 0, false);
                kuva = kuvat.get(i);
                kuvaNimi = kuva.getName().split("\\.")[0];
                kuvaLkm = i + 1;
                try {
                    final long totalSize = kuva.length();
                    RequestBody responseBody = new CountingFileRequestBody(kuva, "image/jpeg", new CountingFileRequestBody.ProgressListener() {
                        @Override
                        public void transferred(long num) {
                            float progress = (num / (float) totalSize) * 100;
                            if ((int) progress % (MAX_PROGRESS / 10) == 0) {
                                mBuilder.setContentText("Tallennetaan kuvaa " + kuvaLkm + "/" + kuvatKoko);
                                mBuilder.setProgress(MAX_PROGRESS, (int) progress, false);
                                mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
                                sendMessage((int) progress);
                            }
                        }
                    });
                    Call<ResponseBody> call = passiClient.tallennaKuva(kuvaNimi, responseBody);
                    Response response = call.execute();
                    if (response.isSuccessful()) {
                        Intent vahvistusNakyma = new Intent(this, VahvistusActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                                vahvistusNakyma, 0);
                        mBuilder.setContentIntent(pendingIntent);
                        mBuilder.setContentText("Vastaus tallennettu");
                        Toast.makeText(getApplicationContext(), "Vastaus tallennettu", Toast.LENGTH_LONG).show();
                    } else {
                        mBuilder.setContentText("Tallennus epäonnistui");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("KuvaUploadaus", "Levy error " + e.toString());
                }

                mBuilder.setSmallIcon(android.R.drawable.stat_sys_upload_done);
                mBuilder.setProgress(0, 0, false);
                mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
            }
        }


    }

    private void sendMessage(int progress) {
        Log.d("progress", progress + "");
        Intent intent = new Intent(PROGRESS_EVENT);
        intent.putExtra(PROGRESS_KEY, progress);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
