package fi.softala.tyokykypassi.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import fi.softala.tyokykypassi.R;
import fi.softala.tyokykypassi.models.Ryhma;


public class Saarinakyma extends Fragment implements View.OnClickListener{

    private OnSaariFragmentInteractionListener mListener;
    ImageButton kategoria1;
    ImageButton kategoria2;
    ImageButton kategoria3;
    ImageButton kategoria4;
    ImageButton kategoria5;

    Ryhma ryhma;
    Integer ryhmaID;


    public Saarinakyma() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ryhma = getArguments().getParcelable("ryhma");
            ryhmaID = ryhma.getGroupID();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.content_saarinakyma, container, false);
        kategoria1 = (ImageButton) v.findViewById(R.id.kartta_nappi_1);
        kategoria2 = (ImageButton) v.findViewById(R.id.kartta_nappi_2);
        kategoria3 = (ImageButton) v.findViewById(R.id.kartta_nappi_3);
        kategoria4 = (ImageButton) v.findViewById(R.id.kartta_nappi_4);
        kategoria5 = (ImageButton) v.findViewById(R.id.kartta_nappi_5);

        kategoria1.setOnClickListener(this);
        kategoria2.setOnClickListener(this);
        kategoria3.setOnClickListener(this);
        kategoria4.setOnClickListener(this);
        kategoria5.setOnClickListener(this);


        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSaariFragmentInteractionListener) {
            mListener = (OnSaariFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSaariFragmentInteractionListener");
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

    public void haeRyhmanKortit (final Integer ryhmaID, final Integer kategoriaID){


        onItemClick(ryhmaID, kategoriaID);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.kartta_nappi_1:
                final Integer kategoria1 = 1;
                haeRyhmanKortit(ryhmaID, kategoria1);
                break;
            case R.id.kartta_nappi_2:
                final Integer kategoria2 = 2;
                haeRyhmanKortit(ryhmaID, kategoria2);
                break;
            case R.id.kartta_nappi_3:
                final Integer kategoria3 = 3;
                haeRyhmanKortit(ryhmaID, kategoria3);

                break;
            case R.id.kartta_nappi_4:
                final Integer kategoria4 = 4;
                haeRyhmanKortit(ryhmaID, kategoria4);

                break;
            case R.id.kartta_nappi_5:
                final Integer kategoria5 = 5;
                haeRyhmanKortit(ryhmaID, kategoria5);
                break;
            default:
                break;
        }
    }

    public interface OnSaariFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSaariFragmentInteraction(Integer ryhmaID, Integer kategoriaID);
    }

    public void onItemClick(Integer ryhmaID, Integer kategoriaID) {
        mListener.onSaariFragmentInteraction(ryhmaID, kategoriaID);
    }
    }
