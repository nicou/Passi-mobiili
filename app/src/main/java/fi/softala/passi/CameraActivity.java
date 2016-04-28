package fi.softala.passi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.mime.HttpMultipartMode;
//import org.apache.http.entity.mime.MultipartEntity;
//import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
// jukan saatoa
public class CameraActivity extends Activity {
    final private static String STILL_IMAGE_FILE = "mypic.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // create the View for our camera
        final CameraView cameraView = new CameraView(getApplicationContext());
        // the preview frame
        FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
        frame.addView(cameraView);

        Button shared = (Button) findViewById(R.id.shared);
        shared.setOnClickListener(new View.OnClickListener() {
            // stores image to shared Gallery via content Provider

            public void onClick(View v) {
                // capture the image, specify who handles the image taken
                cameraView.capture(new Camera.PictureCallback() {

                    public void onPictureTaken(byte[] data, Camera camera) {
                        Log.v("My Camera", "Image received from camera");
                        try {
                            Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
                            String fileUrl = MediaStore.Images.Media.insertImage(getContentResolver(), bm, "Camera Still Image", "Taken by example app");

                            if (fileUrl == null) {
                                Log.d("My Camera", "Image Insert failed");
                                return;
                            } else {
                                // Force the media scanner to go. Not required,
                                // but good for testing.
                                Uri picUri = Uri.parse(fileUrl);
                                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, picUri));
                            }
                        } catch (Exception e) {
                            Log.e("My Camera", "Error writing file", e);
                        }

                    }

                });
            }

        });

        Button capture = (Button) findViewById(R.id.capture);
        capture.setOnClickListener(new View.OnClickListener() {

            // stores image to a file local to our app
            public void onClick(View v) {
                cameraView.capture(new Camera.PictureCallback() {

                    public void onPictureTaken(byte[] data, Camera camera) {
                        Log.v("My Camera", "Image received from camera");
                        FileOutputStream fos;
                        try {
                            // Fully qualified path name.
                            // In this case, we use the Files subdirerctory
                            String pathForAppFiles = getFilesDir().getAbsolutePath();
                            pathForAppFiles = pathForAppFiles + "/" + STILL_IMAGE_FILE;
                            Log.d("Still image filename:", pathForAppFiles);

                            fos = openFileOutput(STILL_IMAGE_FILE, MODE_WORLD_READABLE);
                            fos.write(data);
                            fos.close();

                        } catch (Exception e) {
                            Log.e("Still", "Error writing file", e);
                        }
                        try {
                            File f = new File("/data/data/fi.softala.passi/files/", "mypic.jpg");
                            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        // URL url = new URL("http://proto280.haaga-helia.fi/");
                        //jukka
                        //billgates

                        /*
                        HttpClient httpclient = new DefaultHttpClient();
                        String url = "http://proto280.haaga-helia.fi/tkpassibackend/api2/addAnswer?id=1&answerNumber=1&answerText=Natiivista&user_id=2";

                        HttpPost httppost = new HttpPost(url);

                        MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                        File file = new File("/data/data/fi.softala.passi/files/mypic.jpg");
                        Log.d("EDIT USER PROFILE", "UPLOAD: file length = " + file.length());
                        Log.d("EDIT USER PROFILE", "UPLOAD: file exist = " + file.exists());
                        mpEntity.addPart("avatar", new FileBody(file, "application/octet"));


                        httppost.setEntity(mpEntity);
                        try {
                            HttpResponse response = httpclient.execute(httppost);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        */
                    }
                });
            }
        });

        Button paper = (Button) findViewById(R.id.wallpaper);
        paper.setOnClickListener(new View.OnClickListener() {
            // sets the image to a wallpaper
            public void onClick(View v) {
                cameraView.capture(new Camera.PictureCallback() {

                    public void onPictureTaken(byte[] data, Camera camera) {
                        Log.v("My Camera", "Image received from camera");
                        ByteArrayInputStream bais = new ByteArrayInputStream(data);
                        try {
                            int height = getWallpaperDesiredMinimumHeight();
                            int width = getWallpaperDesiredMinimumWidth();
                            Toast.makeText(getApplicationContext(), "Wallpaper size = " + width + "x" + height, Toast.LENGTH_LONG).show();
                            Log.v("My Camera", "Wallpaper size=" + width + "x" + height);
                            setWallpaper(bais);
                            // this method is actually deprecated but works
                            // new method is to use WallpaperManager.set()
                        } catch (Exception e) {
                            Log.e("My Camera", "Setting wallpaper failed.", e);
                        }
                    }
                });
            }
        });

    }

}