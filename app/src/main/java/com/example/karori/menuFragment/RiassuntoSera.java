package com.example.karori.menuFragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.karori.R;
import com.example.karori.Room.Meal;
import com.example.karori.Room.MealViewModel;

import java.text.DecimalFormat;
import java.time.LocalDate;

public class RiassuntoSera extends Fragment {
    private TextView tgrassis;
    private TextView tproteines;
    private TextView tcarboidratis;
    private TextView tcalories;
    CardView card;
    Dialog myDialog;
    DecimalFormat df ;

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
        MealViewModel mealViewModel = new ViewModelProvider(this).get(MealViewModel.class);
        tgrassis= (TextView) view.findViewById(R.id.grassiN);
        df = new DecimalFormat("#,##0.00");
        tproteines= (TextView) view.findViewById(R.id.proteineN);
        tcalories= (TextView) view.findViewById(R.id.CalorieN);
        tcarboidratis= (TextView) view.findViewById(R.id.carboidratiN);

        card = (CardView) view.findViewById(R.id.cardviewSera) ;
        myDialog = new Dialog(getContext());
        setValoriRiassuntivi(mealViewModel);

        return view;
    }

    private void setValoriRiassuntivi(MealViewModel mealViewModel){

        LocalDate currentTime = LocalDate.now();
        mealViewModel.getMealFromDate(currentTime, "cena").observe(getActivity(), new Observer<Meal>() {
            @Override
            public void onChanged(Meal meal) {
                if(meal != null){
                    tgrassis.setText(df.format(meal.getGrassiTot()));
                    tproteines.setText(df.format(meal.getProteineTot()));
                    tcarboidratis.setText(df.format(meal.getCarboidratiTot()));
                    tcalories.setText(df.format(meal.getCalorieTot()));
                }else {
                    tgrassis.setText("0");
                    tproteines.setText("0");
                    tcarboidratis.setText("0");
                    tcalories.setText("0");
                }
            }
        });

    }
}
