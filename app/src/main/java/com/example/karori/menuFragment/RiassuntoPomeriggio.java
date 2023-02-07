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

public class RiassuntoPomeriggio extends Fragment {
    private TextView tgrassip;
    private TextView tsaturip;
    private TextView tcarboidratip;
    private TextView tcaloriep;
    private static final String KEY_GRASSIP = "GRASSIP";
    private static final String KEY_SATURIP = "SATURIP";
    private static final String KEY_CARBOIDRATIP = "CARBOIDRATIP";
    private static final String KEY_CALORIEP = "CALORIEP";

    CardView card;
    Dialog myDialog;

    public RiassuntoPomeriggio() {
    }

    public static RiassuntoPomeriggio newInstance() {
        return new RiassuntoPomeriggio();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_riassunto_pomeriggio, container, false);
        tgrassip= (TextView) view.findViewById(R.id.grassiP);
        tsaturip= (TextView) view.findViewById(R.id.SaturiP);
        tcaloriep= (TextView) view.findViewById(R.id.proteineP);
        tcarboidratip= (TextView) view.findViewById(R.id.carboidratiP);
        card = (CardView) view.findViewById(R.id.cardviewPomeriggio) ;
        myDialog = new Dialog(getContext());


        //pomeriggio,dovrà andare a prendere i valori nel database inizialmente
        setValoriRiassuntivi(savedInstanceState);

        return view;
    }

    private void setValoriRiassuntivi(Bundle savedInstanceState){
        //vado nel databese
        if(savedInstanceState==null) {
            tgrassip.setText("20");
            tsaturip.setText("10");
            tcarboidratip.setText("70");
            tcaloriep.setText("40");
        }
        else{
            tgrassip.setText("" + savedInstanceState.get(KEY_GRASSIP));
            tsaturip.setText("" + savedInstanceState.get(KEY_SATURIP));
            tcarboidratip.setText("" + savedInstanceState.get(KEY_CARBOIDRATIP));
            tcaloriep.setText("" + savedInstanceState.get(KEY_CALORIEP));
        }
    }
}
