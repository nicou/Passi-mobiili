package fi.softala.tyokykypassi.activities;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import fi.softala.tyokykypassi.R;


public class ToolbarActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tool_bar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    //Hoitaa toolbarin iconien klikkauksen
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;

            case R.id.logout:
                kirjauduUlos();
                break;

            case R.id.feedback:
                intent = new Intent(getApplicationContext(), PalauteActivity.class);
                startActivity(intent);
                break;

        }
    }

    public void kirjauduUlos() {
        new AlertDialog.Builder(ToolbarActivity.this).setMessage("Kirjaudu ulos?")
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

}