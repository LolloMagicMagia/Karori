package com.example.karori.menuFragment;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.karori.R;
import com.example.karori.Room.Meal;
import com.example.karori.Room.MealViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FragmentCalendar extends Fragment {
    private TextView show_selected_date;
    private Button calendar;
    double cocar;
    double cofatt;
    double coprotetins;
    double cocalories;

    double prcar;
    double prfatt;
    double prproteins;
    double prcalories;

    double secar;
    double sefatt;
    double seproteins ;
    double secalories;

    public FragmentCalendar() {
        // Required empty public constructor
    }


    public static FragmentCalendar newInstance(String param1, String param2) {
        FragmentCalendar fragment = new FragmentCalendar();
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
        View view=inflater.inflate(R.layout.fragment_calendar_corretto, container, false);
        show_selected_date=view.findViewById(R.id.show_selected_date);
        calendar=view.findViewById(R.id.calendar);
        TextView carboidrati=view.findViewById(R.id.carbohhydratesCalendar);
        TextView fat=view.findViewById(R.id.fatCalendar);
        TextView proteine=view.findViewById(R.id.proteineCalendar);
        TextView calorie=view.findViewById(R.id.calorieCalendar);

        MealViewModel mealViewModel = new ViewModelProvider(this).get(MealViewModel.class);

        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT A DATE");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();


        //calendario
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });
        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {


                        // if the user clicks on the positive
                        // button that is ok button update the
                        // selected date
                        show_selected_date.setText(""+materialDatePicker.getHeaderText());
                        String dateString = materialDatePicker.getHeaderText();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
                        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("MMM d, yyyy");
                        materialDatePicker.getArguments();
                        LocalDate date;
                        try {
                            date = LocalDate.parse(dateString, formatter);
                        }catch(Exception e){
                            date = LocalDate.parse(dateString, formatter2);
                        }
                        Log.d("calendar",""+date);
                        // in the above statement, getHeaderText
                        // will return selected date preview from the
                        // dialog
                            mealViewModel.getMealFromDate(date, "colazione").observe(getActivity(), new Observer<Meal>() {
                                @Override
                                public void onChanged(Meal meal) {
                                    if (meal == null) {
                                        cocar=0;
                                        cofatt=0;
                                        coprotetins=0;
                                        cocalories=0;
                                    }else{
                                        cocar=meal.getCarboidratiTot();
                                        cofatt=meal.getGrassiTot();
                                        coprotetins=meal.getProteineTot();
                                        cocalories=meal.getCalorieTot();
                                    }
                                }
                            });
                            mealViewModel.getMealFromDate(date, "pranzo").observe(getActivity(), new Observer<Meal>() {
                                @Override
                                public void onChanged(Meal meal) {
                                    if(meal==null){
                                        prcar=0;
                                        prfatt=0;
                                        prproteins=0;
                                        prcalories=0;
                                    }else{
                                        prcar=meal.getCarboidratiTot();
                                        prfatt=meal.getGrassiTot();
                                        prproteins=meal.getProteineTot();
                                        prcalories=meal.getCalorieTot();
                                    }

                                }
                            });
                        mealViewModel.getMealFromDate(date, "cena").observe(getActivity(), new Observer<Meal>() {
                            @Override
                            public void onChanged(Meal meal) {
                                if(meal==null){
                                    secar=0;
                                    sefatt=0;
                                    seproteins=0;
                                    secalories=0;
                                }else {
                                    secar=meal.getCarboidratiTot();
                                    sefatt=meal.getGrassiTot();
                                    seproteins=meal.getProteineTot();
                                    secalories=meal.getCalorieTot();
                                }

                            }
                        });


                        Log.d("calendar",""+materialDatePicker.getHeaderText());
                        Log.d("calendar","car "+secar);
                        Log.d("calendar","car "+prcar);
                        Log.d("calendar","car "+cocar);
                        Log.d("calendar","fatt "+sefatt);
                        Log.d("calendar","fatt "+prfatt);
                        Log.d("calendar","fatt "+cofatt);
                        Log.d("calendar","proteins "+seproteins);
                        Log.d("calendar","proteins "+prproteins);
                        Log.d("calendar","proteins "+coprotetins);

                        carboidrati.setText(Double.toString(cocar+prcar+secar));
                        fat.setText(Double.toString(cofatt+prfatt+sefatt));
                        proteine.setText(Double.toString(coprotetins+prproteins+seproteins));
                        calorie.setText(Double.toString(cocalories+prcalories+secalories));
                    }
                });


        return view;
    }

}
