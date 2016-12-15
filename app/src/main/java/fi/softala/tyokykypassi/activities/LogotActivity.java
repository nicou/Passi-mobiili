package fi.softala.tyokykypassi.activities;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import fi.softala.tyokykypassi.R;
import fi.softala.tyokykypassi.utilities.ImageManipulation;

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


        TextView tekijat = (TextView) findViewById(R.id.tekijat);
        tekijat.setText("Kehittäjät: \n" +
                "Ville Aaltonen \n" +
                "Petteri Haimakainen \n" +
                "Julius Heinonen \n" +
                "Roope Heinonen \n" +
                "Sami Holopainen \n" +
                "Joakim Kajan \n" +
                "Vilhelmiina Karttunen \n" +
                "Teemu Laurila \n" +
                "Lucy Liu \n" +
                "Sanna Mäki \n" +
                "Mika Ropponen \n" +
                "Varvara Zhilibovskaya \n"
        );

        TextView logotext2 = (TextView) findViewById(R.id.logoteksti2);
        logotext2.setText("Sovelluksen sisällössä on hyödynnetty Alpo.fi-sivujen tehtäviä, jotka on aiemmin laadittu yhteistyössä seuraavien tahojen kanssa:");

        ImageView haaga = (ImageView) findViewById(R.id.haaga_logo);
        ImageView ttl = (ImageView) findViewById(R.id.ttl_logo);
        ImageView saku = (ImageView) findViewById(R.id.saku_logo);
        ImageView eu = (ImageView) findViewById(R.id.eu_logo);
        ImageView ely = (ImageView) findViewById(R.id.ely_logo);
        ImageView vipu = (ImageView) findViewById(R.id.vipuvoimaa_logo);
        ImageView sao = (ImageView) findViewById(R.id.stadin_ammattiopisto_logo);
        ImageView smart = (ImageView) findViewById(R.id.smart_moves_logo);
        ImageView ehyt = (ImageView) findViewById(R.id.ehyt_logo);
        ImageView mieli = (ImageView) findViewById(R.id.mieli_logo);
        ImageView ye = (ImageView) findViewById(R.id.ye_logo);
        ImageView sydan = (ImageView) findViewById(R.id.sydanliitto_logo);
        ImageView nuorten = (ImageView) findViewById(R.id.nuorten_akatemia_logo);
        ImageView tyokyky = (ImageView) findViewById(R.id.tyokykypassi_logo);

        haaga.setImageBitmap(
                ImageManipulation.decodeSampledBitmapFromResource(getResources(), R.drawable.kehittaja_haagahelia, 100, 70));
        ttl.setImageBitmap(
                ImageManipulation.decodeSampledBitmapFromResource(getResources(), R.drawable.kehittaja_ttl, 100, 70));
        saku.setImageBitmap(
                ImageManipulation.decodeSampledBitmapFromResource(getResources(), R.drawable.kehittaja_saku, 100, 70));
        eu.setImageBitmap(
                ImageManipulation.decodeSampledBitmapFromResource(getResources(), R.drawable.kehittaja_eu_esr, 100, 70));
        ely.setImageBitmap(
                ImageManipulation.decodeSampledBitmapFromResource(getResources(), R.drawable.kehittaja_ely, 100, 70));
        vipu.setImageBitmap(
                ImageManipulation.decodeSampledBitmapFromResource(getResources(), R.drawable.kehittaja_vipuvoimaa, 100, 70));
        sao.setImageBitmap(
                ImageManipulation.decodeSampledBitmapFromResource(getResources(), R.drawable.kehittaja_stadin_ammattiopisto, 100, 70));
        smart.setImageBitmap(
                ImageManipulation.decodeSampledBitmapFromResource(getResources(), R.drawable.yhteistyo_smart_moves, 100, 70));
        ehyt.setImageBitmap(
                ImageManipulation.decodeSampledBitmapFromResource(getResources(), R.drawable.yhteistyo_ehyt, 100, 70));
        mieli.setImageBitmap(
                ImageManipulation.decodeSampledBitmapFromResource(getResources(), R.drawable.yhteistyo_mieli, 100, 70));
        ye.setImageBitmap(
                ImageManipulation.decodeSampledBitmapFromResource(getResources(), R.drawable.yhteistyo_ye, 100, 70));
        sydan.setImageBitmap(
                ImageManipulation.decodeSampledBitmapFromResource(getResources(), R.drawable.yhteistyo_sydanliitto, 100, 70));
        nuorten.setImageBitmap(
                ImageManipulation.decodeSampledBitmapFromResource(getResources(), R.drawable.yhteistyo_nuorten_akatemia, 100, 70));
        tyokyky.setImageBitmap(
                ImageManipulation.decodeSampledBitmapFromResource(getResources(), R.drawable.yhteistyo_tyokykypassi, 100, 70));


    }

}
