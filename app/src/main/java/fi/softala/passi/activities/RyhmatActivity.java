package fi.softala.passi.activities;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import fi.softala.passi.R;
import fi.softala.passi.fragments.Ryhmat;
import fi.softala.passi.fragments.Tehtavakortit;
import fi.softala.passi.models.Ryhma;

public class RyhmatActivity extends AppCompatActivity implements Ryhmat.OnFragmentInteractionListener, Tehtavakortit.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ryhmat);

        if (findViewById(R.id.activity_ryhmat_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            Ryhmat ryhmatFragment = new Ryhmat();
            ryhmatFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_ryhmat_container, ryhmatFragment)
                    .commit();
        }

    }

    @Override
    public void onFragmentInteraction(Ryhma ryhma) {
        Tehtavakortit tehtavakortit = new Tehtavakortit();
        Bundle args = new Bundle();
        args.putParcelable("ryhma", ryhma);
        tehtavakortit.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.activity_ryhmat_container, tehtavakortit);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(int ryhmaID, String korttiJSON) {
        Intent intent = new Intent(getApplicationContext(), Tehtavakortti.class);
        intent.putExtra("Tehtavakortti", korttiJSON);
        intent.putExtra("ryhmaID", String.valueOf(ryhmaID));
        startActivity(intent);
    }
}
