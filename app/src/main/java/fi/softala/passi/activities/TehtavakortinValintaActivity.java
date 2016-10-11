package fi.softala.passi.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import fi.softala.passi.R;

public class TehtavakortinValintaActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_USE_CAMERA = 10;
    private static final int MY_PERMISSION_WRITE_EXTERNAL = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tehtavakortin_valinta);
        Context context = getApplicationContext();

        Button button1 = (Button) findViewById(R.id.btnPaihteet1);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        Button button2 = (Button) findViewById(R.id.btnPaihteet2);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        Button button3 = (Button) findViewById(R.id.btnPaihteet);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        Button button4 = (Button) findViewById(R.id.btnRavitsemus);
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        Button button5 = (Button) findViewById(R.id.btnMitaOnTerveys);
        button5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        Button button6 = (Button) findViewById(R.id.btnMielenHyvinvointi);
        button6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


            }
        });

        Button button7 = (Button) findViewById(R.id.btnVaarakartta);
        button7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


            }
        });

        Button button8 = (Button) findViewById(R.id.btnKulttuuriPassi);
        button8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


            }
        });

        Button button9 = (Button) findViewById(R.id.btnLaadiTyoohjeet);
        button9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


            }
        });

        Button button10 = (Button) findViewById(R.id.btnOmaTapaLiikkua);
        button10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


            }
        });

//          Turvallisuuskävely
        Button button11 = (Button) findViewById(R.id.btnTurvallisuuskavely);
        button11.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hankiLuvat();
            }
        });

        Button button12 = (Button) findViewById(R.id.btnTyotaHakemassa);
        button12.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


            }
        });

    }

    private void hankiLuvat() {
        if (ActivityCompat.checkSelfPermission(TehtavakortinValintaActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(TehtavakortinValintaActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSION_USE_CAMERA);

        } else if (ActivityCompat.checkSelfPermission(TehtavakortinValintaActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(TehtavakortinValintaActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSION_WRITE_EXTERNAL);

        } else {

            tehtavaKorttiin();

        }
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
                                setTitle("Camera permission ").
                                setMessage("Sovellus tarvitsee luvan käyttää kameraa kuvien ottamiseen").show();
                    } else {
                        new AlertDialog.Builder(this).
                                setTitle("Camera permision denied").
                                setMessage("Et antanut lupaa joten et voi täyttää tehtäväkorttia." +
                                        " To enable it" +
                                        ", go on settings and " +
                                        "grant read contacts for the application").show();
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
                                setTitle("Kirjoitus lupa ").
                                setMessage("Sovellus tarvitsee luvan kirjoittamiseen jotta kuvat voidaan tallentaa").show();
                    } else {
                        new AlertDialog.Builder(this).
                                setTitle("Kirjoitus lupa kielletty").
                                setMessage("Et antanut lupaa joten et voi täyttää tehtäväkorttia." +
                                        " To enable it" +
                                        ", go on settings and " +
                                        "grant storage for the application").show();
                    }

                }

                break;
        }
    }

    private void tehtavaKorttiin() {
        Intent intent = new Intent(TehtavakortinValintaActivity.this, Tehtavakortti.class);
        startActivity(intent);
    }

    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(TehtavakortinValintaActivity.this, ValikkoActivity.class);
        startActivity(intent);

    }

}
