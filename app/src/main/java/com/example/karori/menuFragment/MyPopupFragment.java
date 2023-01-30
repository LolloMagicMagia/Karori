package com.example.karori.menuFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.karori.R;

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
    private recyclerAdapter.RecyclerViewClickListener listener;
    //////////////////////////

    private RecyclerView mRecyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pop_up_riassunto, container, false);
        mRecyclerView=view.findViewById(R.id.recycler_view_riassuntoM);
        ///////////////prova recycler view
        al1=new AlimentoSpecifico("banana","30","5","70");
        al2=new AlimentoSpecifico("banana","30","5","70");
        al3=new AlimentoSpecifico("banana","30","5","70");
        al4=new AlimentoSpecifico("banana","30","5","70");
        al5=new AlimentoSpecifico("banana","30","5","70");
        al6=new AlimentoSpecifico("banana","30","5","70");
        al7=new AlimentoSpecifico("banana","30","5","70");

        ////////////////////////////////////

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
                Log.d("CAMBIO","PARTE OLTO"+position);
            }
        };
    }



}
