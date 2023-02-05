package com.example.karori.menuFragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.karori.R;
import com.example.karori.SearchClasses.SearchActivity;

import java.util.ArrayList;

public class MyPopupFragment extends DialogFragment {
    private ArrayList<AlimentoSpecifico> mAlimentoSpecificoArrayList;
    /////////////////////////
    AlimentoSpecifico al1;
    AlimentoSpecifico al2;
    AlimentoSpecifico al3;
    AlimentoSpecifico al4;
    AlimentoSpecifico al5;
    AlimentoSpecifico al6;
    AlimentoSpecifico al7;
    public int parteDelGiorno;
    private recyclerAdapter.RecyclerViewClickListener listener;
    private String pasto = "";
    //////////////////////////

    private RecyclerView mRecyclerView;

    public MyPopupFragment(int position){
        if(position==0){
            parteDelGiorno=0;
        }
        else if(position==1){
            parteDelGiorno=1;
        }
        else{
            parteDelGiorno=2;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pop_up_riassunto, container, false);
        mRecyclerView=view.findViewById(R.id.recycler_view_riassuntoM);
        TextView riassunti=(TextView) view.findViewById(R.id.Riassunti);
        //trasparente fragment
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ///////////////prova recycler view
        //alimento specifico deve avere: nome, id, calorie, proteine, grassi, carboidrati, tipo, quantita, unita di misura
        al1=new AlimentoSpecifico("banana","9266","30","5","70","colazione","90","100");
        al2=new AlimentoSpecifico("banana","2047","30","5","70","colazione","90","100");
        al3=new AlimentoSpecifico("banana","9268","30","5","70","colazione","90","100");
        al4=new AlimentoSpecifico("banana","9269","30","5","70","colazione","90","100");
        al5=new AlimentoSpecifico("banana","9270","30","5","70","colazione","90","100");
        al6=new AlimentoSpecifico("banana","9270","30","5","70","colazione","90","100");
        al7=new AlimentoSpecifico("banana","9270","30","5","70","colazione","90","100");
        ////////////////////////////////////
        setParteDelGiorno(parteDelGiorno,riassunti);

        mAlimentoSpecificoArrayList=new ArrayList<>();

        setmAlimentoInfo();
        setAdapter();

        return view;
    }

    private void setmAlimentoInfo(){
        mAlimentoSpecificoArrayList.add(al1);
        mAlimentoSpecificoArrayList.add(al2);
        mAlimentoSpecificoArrayList.add(al3);
        mAlimentoSpecificoArrayList.add(al4);
        mAlimentoSpecificoArrayList.add(al5);
        mAlimentoSpecificoArrayList.add(al6);
        mAlimentoSpecificoArrayList.add(al7);
    }

    private void setAdapter(){
        setOnClickListener();
        recyclerAdapter adapter=new recyclerAdapter(mAlimentoSpecificoArrayList,listener);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
    }

    private void setOnClickListener(){
        listener = new recyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                AlimentoSpecifico totti = mAlimentoSpecificoArrayList.get(position);
                if (totti.getTipo() == "colazione") {
                    pasto = "0";
                }
                if (totti.getTipo() == "pasto") {
                    pasto = "1";
                }
                if (totti.getTipo() == "cena") {
                    pasto = "2";
                }
                intent.putExtra("id", totti.getId()) //id
                        .putExtra("amount", "22") //quantita
                        .putExtra("unit", "g") //unita di misura
                        .putExtra("selected", "colazione") //tipo
                        .putExtra("mode", "update"); //lasciare invariato
                intent.putExtra("pasto", pasto); //codice per ricercaeaggiungi, lasciare
                intent.putExtra("skip", "true"); //lasciare
                intent.putExtra("popUp","1");
                startActivity(intent);
            }
        };
    }

    public void setParteDelGiorno(int pdg, TextView riassunti){
        if(pdg==0){
            riassunti.setText("Mattina");
        }
        else if(pdg==1){
            riassunti.setText("Pomeriggio");
        }
        else{
            riassunti.setText("Cena");
        }

}

}
