package com.example.karori.menuFragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.karori.R;

public class RiassuntoSera extends Fragment {
    private TextView tgrassis;
    private TextView tsaturis;
    private TextView tcarboidratis;
    private TextView tcalories;
    private static final String KEY_GRASSIS = "GRASSIS";
    private static final String KEY_SATURIS = "SATURIS";
    private static final String KEY_CARBOIDRATIS = "CARBOIDRATIS";
    private static final String KEY_CALORIES = "CALORIES";
    CardView card;
    Dialog myDialog;

    public RiassuntoSera() {
        // Required empty public constructor
    }

    public static RiassuntoSera newInstance() {
        return new RiassuntoSera();
    }

    public static RiassuntoSera newInstance(String param1, String param2) {
        RiassuntoSera fragment = new RiassuntoSera();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_riassunto_sera, container, false);
        tgrassis= (TextView) view.findViewById(R.id.grassiN);
        tsaturis= (TextView) view.findViewById(R.id.CalorieN);
        tcalories= (TextView) view.findViewById(R.id.proteineN);
        tcarboidratis= (TextView) view.findViewById(R.id.carboidratiN);

        card = (CardView) view.findViewById(R.id.cardviewSera) ;
        myDialog = new Dialog(getContext());

        //sera, dovrà andare a prendere i valori nel database inizialmente
        setValoriRiassuntivi(savedInstanceState);

        return view;
    }

    private void setValoriRiassuntivi(Bundle savedInstanceState){
        //vado nel databese
        if(savedInstanceState==null) {
            tgrassis.setText("20");
            tsaturis.setText("60");
            tcarboidratis.setText("30");
            tcalories.setText("30");
        }
        else{
            tgrassis.setText("" + savedInstanceState.get(KEY_GRASSIS));
            tsaturis.setText("" + savedInstanceState.get(KEY_SATURIS));
            tcarboidratis.setText("" + savedInstanceState.get(KEY_CARBOIDRATIS));
            tcalories.setText("" + savedInstanceState.get(KEY_CALORIES));
        }
    }
}
