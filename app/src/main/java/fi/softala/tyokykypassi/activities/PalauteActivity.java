package fi.softala.tyokykypassi.activities;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import fi.softala.tyokykypassi.R;
import fi.softala.tyokykypassi.fragments.Palaute;
import fi.softala.tyokykypassi.fragments.Palautetut;
import fi.softala.tyokykypassi.fragments.Ryhmat;
import fi.softala.tyokykypassi.models.Answersheet;
import fi.softala.tyokykypassi.models.Category;
import fi.softala.tyokykypassi.models.Ryhma;
import retrofit2.Call;

public class PalauteActivity extends ToolbarActivity implements Palaute.OnFragmentInteractionListener, Ryhmat.OnRyhmatMenePalautteeseenListener, Ryhmat.OnRyhmatFragmentInteractionListener, Ryhmat.OnRyhmatLisaaInteractionListener {


    Call<Answersheet> vastaus;
    Call<List<Category>> call;
    boolean lopetettu;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (call != null) {
            if (call.isExecuted()) {
                call.cancel();
            }
        }

        if (vastaus != null) {
            if (vastaus.isExecuted()) {
                vastaus.cancel();
            }
        }

        lopetettu = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palaute);

        //Luo iconeille listenerit ja lähettää valikkoActivityyn, jossa id:n perusteella toiminnot
        ImageButton imHome = (ImageButton) findViewById(R.id.home);
        ImageButton imFeedback = (ImageButton) findViewById(R.id.feedback);
        ImageButton imLogout = (ImageButton) findViewById(R.id.logout);


        imHome.setOnClickListener(this);
        imFeedback.setOnClickListener(this);
        imLogout.setOnClickListener(this);

        if (findViewById(R.id.fragment_container_palaute) != null) {
            if (savedInstanceState != null) {
                return;
            }
            Ryhmat ryhmat = new Ryhmat();
            Bundle args = new Bundle();
            args.putBoolean("palaute", true);
            ryhmat.setArguments(args);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container_palaute, ryhmat)
                    .commit();

        }

    }

    @Override
    public void onFragmentInteraction(int valinta, List<Answersheet> kortit) {
        Log.d("jeejeejee", "valinta oli " + valinta);
        // palautetut tehtavakortit
        Palautetut palautetut = new Palautetut();
        Bundle args = new Bundle();
        if (valinta == 1) {
            args.putParcelableArrayList("kortit", (ArrayList<? extends Parcelable>) kortit);

        } else if (valinta == 2) {
            args.putParcelableArrayList("kortit", (ArrayList<? extends Parcelable>) kortit);

        }
        palautetut.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container_palaute, palautetut);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onRyhmatFragmentInteraction(Ryhma uri) {

    }

    @Override
    public void onRyhmatLisaaFragmentInteraction() {

    }

    @Override
    public void onRyhmatMenePalauteInteraction(int groupID) {
        Palaute palaute = new Palaute();
        Bundle args = new Bundle();
        args.putInt("groupID", groupID);
        palaute.setArguments(args);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_palaute, palaute)
                .addToBackStack(null)
                .commit();
    }

    private void palauteKamat() {

    }


}
