package fi.softala.passi;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Turvallisuuskavely_johdantoActivity extends AppCompatActivity {

    TabHost tabHost;
    File file;
    Uri fileUri;
    String stringUri;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    int id = 1;
    int kameraButtonPressed = 0;
    final int RC_TAKE_PHOTO = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turvallisuuskavely_johdanto);


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


        TextView tv = (TextView) findViewById(R.id.textView1);
        tv.setMovementMethod(new ScrollingMovementMethod());


    }

    // kun painetaan kameranappia
    public void onButtonClick(View view) {
        if (view.getId() == R.id.kameraButton1) {
            kameraButtonPressed = 1;
        } else if (view.getId() == R.id.kameraButton2) {
            kameraButtonPressed = 2;
        } else if (view.getId() == R.id.kameraButton3) {
            kameraButtonPressed = 3;
        } else if (view.getId() == R.id.kameraButton4) {
            kameraButtonPressed = 4;
        } else if (view.getId() == R.id.kameraButton5) {
            kameraButtonPressed = 5;
        }
        Intent kameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(Turvallisuuskavely_johdantoActivity.this.getExternalCacheDir(),
                String.valueOf(System.currentTimeMillis() + ".jpg"));
        fileUri = Uri.fromFile(file);
        kameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        Turvallisuuskavely_johdantoActivity.this.startActivityForResult(kameraIntent, RC_TAKE_PHOTO);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Turvallisuuskavely_johdantoActivity.super.onActivityResult(requestCode, resultCode, data);
        ImageButton kameraButton1 = (ImageButton) findViewById(R.id.kameraButton1);
        ImageButton kameraButton2 = (ImageButton) findViewById(R.id.kameraButton2);
        ImageButton kameraButton3 = (ImageButton) findViewById(R.id.kameraButton3);
        ImageButton kameraButton4 = (ImageButton) findViewById(R.id.kameraButton4);
        ImageButton kameraButton5 = (ImageButton) findViewById(R.id.kameraButton5);

        if (requestCode == RC_TAKE_PHOTO && resultCode == RESULT_OK) {
            stringUri = fileUri.toString();
            Log.d("Passi ", stringUri);
            Context context = getApplicationContext();
            if (kameraButtonPressed == 1) {
                // vaihetaan nappulan taustakuva
                kameraButton1.setBackground(ContextCompat.getDrawable(context, R.drawable.red_face_pressed)
                );
                kameraButton1.setEnabled(false);
            } else if (kameraButtonPressed == 2) {
                kameraButton2.setBackground(ContextCompat.getDrawable(context, R.drawable.red_face_pressed)
                );
                kameraButton2.setEnabled(false);
            } else if (kameraButtonPressed == 3) {
                kameraButton3.setBackground(ContextCompat.getDrawable(context, R.drawable.red_face_pressed)
                );
                kameraButton3.setEnabled(false);
            } else if (kameraButtonPressed == 4) {
                kameraButton4.setBackground(ContextCompat.getDrawable(context, R.drawable.red_face_pressed)
                );
                kameraButton4.setEnabled(false);
            } else if (kameraButtonPressed == 5) {
                kameraButton5.setBackground(ContextCompat.getDrawable(context, R.drawable.red_face_pressed)
                );
                kameraButton5.setEnabled(false);
            }
            kameraButtonPressed = 0;

        }
    }

    private void startUpload() {
        Log.v("Passi", "polku kuvaan" + stringUri);

        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(Turvallisuuskavely_johdantoActivity.this);
        Intent untent = new Intent(Turvallisuuskavely_johdantoActivity.this, ValikkoActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                untent, 0);
        mBuilder.setContentTitle("Vastaus")
                .setContentText("Tallennetaan...")
                .setSmallIcon(R.drawable.ic_cloud_upload_white_24dp)
                .setContentIntent(pendingIntent);


        new Turvallisuuskavely_johdantoActivity.UploadImage().execute(stringUri);
    }

    private class UploadImage extends AsyncTask<String, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Displays the progress bar for the first time.
            mBuilder.setProgress(100, 0, false);
            mNotifyManager.notify(id, mBuilder.build());
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // Update progress
            mBuilder.setProgress(100, values[0], false);
            mNotifyManager.notify(id, mBuilder.build());
            super.onProgressUpdate(values);
        }

        @Override
        protected Integer doInBackground(String... path) {
            int i;

            File image = saveBitmapToFile(file);
            String url = "http://proto284.haaga-helia.fi/passibe/KuvaVastaanottoServlet";

            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM).addFormDataPart("file", "image.png",
                            RequestBody.create(MediaType.parse("image/png"), image))
                    .build();

            Request request = new Request.Builder().url(url).post(formBody).build();

            Response response = null;

            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            int kahvi = 0;

            Log.d("Passi", "Jee " + response);

            if (response != null) {
                if (response.isSuccessful()) {
                    kahvi = 1;
                }
            }

            response.close();
            for (i = 0; i <= 100; i += 5) {
                // Sets the progress indicator completion percentage
                publishProgress(Math.min(i, 100));

            }
            return kahvi;
        }


        @Override
        protected void onPostExecute(Integer result) {
            Log.d("Passi", "Jee jöö " + result);
            super.onPostExecute(result);
            if (result == 1) {
                mBuilder.setContentText("Vastaus tallennettu");
            } else {
                mBuilder.setContentText("Tallennus epäonnistui");
            }
            // Removes the progress bar
            mBuilder.setProgress(0, 0, false);
            mNotifyManager.notify(id, mBuilder.build());
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

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }


    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(Turvallisuuskavely_johdantoActivity.this, TehtavakortinValintaActivity.class);
        startActivity(intent);

    }


}
