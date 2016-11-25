package fi.softala.tyokykypassi.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import fi.softala.tyokykypassi.R;

/**
 * Created by villeaaltonen on 27/10/2016.
 */

public class Valikko extends Fragment {

    private Valikko.OnFragmentInteractionListener mListener;
    private OnProfiiliNappiFragmentInteractionListener mProfileListener;

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

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Valikko.OnFragmentInteractionListener) {
            mListener = (Valikko.OnFragmentInteractionListener) context;
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
