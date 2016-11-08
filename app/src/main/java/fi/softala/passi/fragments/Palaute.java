package fi.softala.passi.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import fi.softala.passi.R;

/**
 * Created by villeaaltonen on 08/11/2016.
 */

public class Palaute extends android.support.v4.app.Fragment {

    private Palaute.OnFragmentInteractionListener mListener;
    private final int PALAUTETUT_TEHTAVAKORTIT = 1;
    private final int PALAUTTAMATTOMAT_TEHTAVAKORTIT = 2;

    public Palaute() {
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
        View v = inflater.inflate(R.layout.fragment_palaute, container, false);
        ImageView palautetut = (ImageView) v.findViewById(R.id.button_palaute);
        ImageView palauttamattomat = (ImageView) v.findViewById(R.id.button_palauttamatta);
        palautetut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFragmentInteraction(PALAUTETUT_TEHTAVAKORTIT);
            }
        });
        palauttamattomat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFragmentInteraction(PALAUTTAMATTOMAT_TEHTAVAKORTIT);
            }
        });
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Palaute.OnFragmentInteractionListener) {
            mListener = (Palaute.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        void onFragmentInteraction(int valinta);
    }

}
