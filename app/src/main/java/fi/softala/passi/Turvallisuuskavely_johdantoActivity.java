package fi.softala.passi;

import android.content.ContentProvider;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import java.io.File;

public class Turvallisuuskavely_johdantoActivity extends ActionBarActivity {

    TabHost tabHost;
    File file;
    Uri fileUri;
    String stringUri;
    final int RC_TAKE_PHOTO = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turvallisuuskavely_johdanto);


        final TabHost host= (TabHost)findViewById(R.id.tabHost);
        host.setup();

        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

                for(int i = 0; i < host.getTabWidget().getChildCount(); i++) {
                    host.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.valilehti_nappula);
                }

                host.getTabWidget().getChildAt(host.getCurrentTab()). setBackgroundColor(Color.TRANSPARENT);
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

        for(int i = 0; i < host.getTabWidget().getChildCount(); i++) {
            host.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.valilehti_nappula);
        }

        host.getTabWidget().getChildAt(host.getCurrentTab()). setBackgroundColor(Color.TRANSPARENT);



        TextView tv = (TextView) findViewById(R.id.textView1);
        tv.setMovementMethod(new ScrollingMovementMethod());
        final ImageButton kameraButton = (ImageButton) findViewById(R.id.kameraButton1);
        kameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                 file = new File(Turvallisuuskavely_johdantoActivity.this.getExternalCacheDir(),
                         String.valueOf(System.currentTimeMillis() + ".jpg"));
                 fileUri = Uri.fromFile(file);
                kameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                Turvallisuuskavely_johdantoActivity.this.startActivityForResult(kameraIntent, RC_TAKE_PHOTO);

            }

        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Turvallisuuskavely_johdantoActivity.super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_TAKE_PHOTO && resultCode == RESULT_OK){
            stringUri = fileUri.toString();
            Log.e("Passi ", stringUri);

        }
    }






    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(Turvallisuuskavely_johdantoActivity.this, TehtavakortinValintaActivity.class);
        startActivity(intent);

    }





}
