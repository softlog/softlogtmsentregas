package br.eti.softlog.softlogtmsentregas;


import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrincipalFragmentSecond extends Fragment {


    Activity activity;
    RecyclerView lista_entregas;



    public PrincipalFragmentSecond() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_principal_finalizado,
                container, false);

        activity = getActivity();
        lista_entregas = view.findViewById(R.id.lista_entrega);

        lista_entregas.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        lista_entregas.setLayoutManager(layoutManager);
        lista_entregas.setAdapter(((PrincipalDraweActivity) activity).adapterView2);

        return view;


    }

}
