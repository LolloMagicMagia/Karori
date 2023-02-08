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

public class RiassuntoPomeriggio extends Fragment {
    private TextView tgrassip;
    private TextView tproteinep;
    private TextView tcarboidratip;
    private TextView tcaloriep;
    private static final String KEY_GRASSIP = "GRASSIP";
    private static final String KEY_SATURIP = "SATURIP";
    private static final String KEY_CARBOIDRATIP = "CARBOIDRATIP";
    private static final String KEY_CALORIEP = "CALORIEP";

    CardView card;
    Dialog myDialog;
    DecimalFormat df ;

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
        MealViewModel mealViewModel = new ViewModelProvider(this).get(MealViewModel.class);
        tgrassip= (TextView) view.findViewById(R.id.grassiP);
        df = new DecimalFormat("#.00");
        tproteinep= (TextView) view.findViewById(R.id.proteineP);
        tcaloriep= (TextView) view.findViewById(R.id.calorieP);
        tcarboidratip= (TextView) view.findViewById(R.id.carboidratiP);
        card = (CardView) view.findViewById(R.id.cardviewPomeriggio) ;
        myDialog = new Dialog(getContext());
        setValoriRiassuntivi(mealViewModel);

        return view;
    }

    private void setValoriRiassuntivi(MealViewModel mealViewModel){

        LocalDate currentTime = LocalDate.now();
        mealViewModel.getMealFromDate(currentTime, "pranzo").observe(getActivity(), new Observer<Meal>() {
            @Override
            public void onChanged(Meal meal) {
                if(meal != null){
                    tgrassip.setText(df.format(meal.getGrassiTot()));
                    tproteinep.setText(df.format(meal.getProteineTot()));
                    tcarboidratip.setText(df.format(meal.getCarboidratiTot()));
                    tcaloriep.setText(df.format(meal.getCalorieTot()));
                }else {
                    tgrassip.setText("0");
                    tproteinep.setText("0");
                    tcarboidratip.setText("0");
                    tcaloriep.setText("0");
                }
            }
        });

    }
}
