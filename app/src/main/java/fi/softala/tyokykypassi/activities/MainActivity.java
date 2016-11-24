package fi.softala.tyokykypassi.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import fi.softala.tyokykypassi.R;
import fi.softala.tyokykypassi.fragments.Ryhmat;
import fi.softala.tyokykypassi.fragments.Saarinakyma;
import fi.softala.tyokykypassi.fragments.Tehtavakortit;
import fi.softala.tyokykypassi.fragments.Valikko;
import fi.softala.tyokykypassi.models.Ryhma;

public class MainActivity extends ToolbarActivity implements Ryhmat.OnRyhmatFragmentInteractionListener, Tehtavakortit.OnTehtavakortitFragmentInteractionListener, Valikko.OnFragmentInteractionListener, Saarinakyma.OnSaariFragmentInteractionListener, Ryhmat.OnRyhmatLisaaInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valikko);

        checkkaaTunnukset();

        if (findViewById(R.id.activity_ryhmat_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            Valikko valikko = new Valikko();
            valikko.setArguments(getIntent().getExtras());
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_ryhmat_container, valikko)
                    .commit();
        }


        ImageButton imHome = (ImageButton) findViewById(R.id.home);
        ImageButton imFeedback = (ImageButton) findViewById(R.id.feedback);
        ImageButton imLogout = (ImageButton) findViewById(R.id.logout);

        imHome.setOnClickListener(this);
        imFeedback.setOnClickListener(this);
        imLogout.setOnClickListener(this);

    }

    @Override
    public void onFragmentInteraction() {


//        //SAARINÄKYMÄ!!!!
//        Intent intent  = new Intent(getApplicationContext(), SaarinakymaActivity.class);
//        startActivity(intent);

        Ryhmat ryhmaFragment = new Ryhmat();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.activity_ryhmat_container, ryhmaFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void onRyhmatFragmentInteraction(Ryhma ryhma) {

        Saarinakyma saariFragment = new Saarinakyma();
        Bundle args = new Bundle();
        args.putParcelable("ryhma", ryhma);
        saariFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.activity_ryhmat_container, saariFragment);
        transaction.addToBackStack(null);
        transaction.commit();


    }

    @Override
    public void onRyhmatLisaaFragmentInteraction() {
        Intent intent = new Intent(getApplicationContext(), LiityRyhmaActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSaariFragmentInteraction(Integer ryhmaID, Integer kategoriaID){
        Tehtavakortit tehtavakortit = new Tehtavakortit();
        Bundle args = new Bundle();
        args.putInt("ryhmaID", ryhmaID);
        args.putInt("kategoriaID", kategoriaID);
        tehtavakortit.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.activity_ryhmat_container, tehtavakortit);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void onTehtavakortitFragmentInteraction(int ryhmaID, String korttiJSON) {

        Intent intent = new Intent(getApplicationContext(), TehtavakorttiActivity.class);
        intent.putExtra("TehtavakorttiActivity", korttiJSON);
        intent.putExtra("ryhmaID", String.valueOf(ryhmaID));
        startActivity(intent);
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
                Intent palaute = new Intent(getApplicationContext(), PalauteActivity.class);
                startActivity(palaute);
                break;

        }
    }


    public void kirjauduUlos() {
        new AlertDialog.Builder(MainActivity.this).setMessage("Kirjaudu ulos?")
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

    public void checkkaaTunnukset() {
        SharedPreferences mySharedPreferences = getSharedPreferences("konfiguraatio", Context.MODE_PRIVATE);

        String base = mySharedPreferences.getString("token", "");

        Log.d("Passi", "Tokeni " + base);
        if (base.length() <= 0) {
            Log.d("Passi", "Tokenia ei löytynyt " + base);
            Intent intent = new Intent(MainActivity.this, UusiKayttajaActivity.class);
            startActivity(intent);
        }
    }



}
