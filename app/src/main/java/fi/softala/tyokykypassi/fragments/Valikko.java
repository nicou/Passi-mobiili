package fi.softala.tyokykypassi.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Map;

import fi.softala.tyokykypassi.R;
import fi.softala.tyokykypassi.network.PassiClient;
import fi.softala.tyokykypassi.network.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by villeaaltonen on 27/10/2016.
 */

public class Valikko extends Fragment {

    private Valikko.OnFragmentInteractionListener mListener;
    private OnProfiiliNappiFragmentInteractionListener mProfileListener;
    private TextView topProgressText;
    private ImageView bottomProgressFill;
    private ImageView bottomProgressBackground;
    private TextView bottomProgressPercentage;

    public Valikko() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_valikko, container, false);
        final Button btnTyokykypassi = (Button) v.findViewById(R.id.btnTyokykypassi);
        final Button btnProfiili = (Button) v.findViewById(R.id.btnProfiiliNappi);

        btnTyokykypassi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction();
            }
        });

        btnProfiili.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProfileListener.onProfiiliNappiFragmentInteraction();
            }
        });

        topProgressText = (TextView) v.findViewById(R.id.topProgressText);
        bottomProgressFill = (ImageView) v.findViewById(R.id.bottomProgressFill);
        bottomProgressBackground = (ImageView) v.findViewById(R.id.bottomProgressBackground);
        bottomProgressPercentage = (TextView) v.findViewById(R.id.bottomProgressPercentage);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Valikko.OnFragmentInteractionListener) {
            mListener = (Valikko.OnFragmentInteractionListener) context;
            getProgress();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSaariFragmentInteractionListener");
        }
        if (context instanceof OnProfiiliNappiFragmentInteractionListener) {
            mProfileListener = (OnProfiiliNappiFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnProfiiliFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mProfileListener = null;
    }

    public void renderProgress(Map<String, Integer> progress) {
        if (!progress.containsKey("completed") && !progress.containsKey("total")) {
            topProgressText.setText("0 / 0");
        } else {
            topProgressText.setText(progress.get("completed") + " / " + progress.get("total"));
            double progressPercentage = Double.valueOf(progress.get("completed")) / Double.valueOf(progress.get("total"));
            renderProgressBar(progressPercentage);
        }
    }

    public void renderProgressBar(double progressPercentage) {
        Bitmap source = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.etenemispalkkiv2_varipalkki);
        Double croppedWidth = Math.floor(progressPercentage * source.getWidth());
        if (croppedWidth < 1) {
            croppedWidth = 1.0;
        }
        Bitmap progressBar = Bitmap.createBitmap(source, 0, 0, croppedWidth.intValue(), source.getHeight() - 10);
        DecimalFormat df = new DecimalFormat("#");

        bottomProgressFill.setImageBitmap(progressBar);
        bottomProgressPercentage.setText(df.format(progressPercentage * 100) + "%");
    }

    public void getProgress() {
        SharedPreferences mySharedPreferences = this.getActivity().getSharedPreferences("konfiguraatio", Context.MODE_PRIVATE);
        String base = mySharedPreferences.getString("token", null);

        PassiClient service = ServiceGenerator.createService(PassiClient.class, base);

        Call<Map<String, Integer>> call = service.getProgress();

        call.enqueue(new Callback<Map<String, Integer>>() {
            @Override
            public void onResponse(Call<Map<String, Integer>> call, Response<Map<String, Integer>> response) {
                if (response.isSuccessful()) {
                    Log.v("Valikko", "Haettiin progress: " + response.body());
                    renderProgress(response.body());
                } else {
                    Log.v("Valikko", "Progressin haku ei onnistunut.");
                }
            }

            @Override
            public void onFailure(Call<Map<String, Integer>> call, Throwable t) {
                Log.e("Valikko", "Progressin haku epäonnistui " + t.toString());
            }
        });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction();
    }

    public interface OnProfiiliNappiFragmentInteractionListener {
        // TODO: Update argument type and name
        void onProfiiliNappiFragmentInteraction();
    }
}
