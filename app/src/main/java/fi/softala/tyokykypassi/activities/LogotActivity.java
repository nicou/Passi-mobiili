package fi.softala.tyokykypassi.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import fi.softala.tyokykypassi.R;

public class LogotActivity extends ToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logot);

        ImageButton imHome = (ImageButton) findViewById(R.id.home);
        ImageButton imFeedback = (ImageButton) findViewById(R.id.feedback);
        ImageButton imLogout = (ImageButton) findViewById(R.id.logout);

        imHome.setOnClickListener(this);
        imFeedback.setOnClickListener(this);
        imLogout.setOnClickListener(this);

        TextView logotext1 = (TextView) findViewById(R.id.logoteksti1);
        logotext1.setText("Työkykypassi – mobiilisovellus on kehitetty Työterveyslaitoksen koordinoimassa Combo -hankkeessa. " +
                "Sovelluksen tarkoituksena on vahvistaa opiskelijoiden työkykyosaamista ammatillisissa oppilaitoksissa. " +
                "Hanketta on rahoittanut Euroopan sosiaalirahasto (ESR) / Pohjois-Pohjanmaan elinkeino-, liikenne- ja ympäristökeskus. " +
                "Sovellukseen kehittämiseen ovat osallistuneet Haaga-Helia Ammattikorkeakoulu, Työterveyslaitos, Saku Ry ja Stadin ammattiopisto.");



        TextView logotext2 = (TextView) findViewById(R.id.logoteksti2);
        logotext2.setText("Sovelluksen sisällössä on hyödynnetty Alpo.fi-sivujen tehtäviä, jotka on aiemmin laadittu yhteistyössä seuraavien tahojen kanssa:");

    }

}
