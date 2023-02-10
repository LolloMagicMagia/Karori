package com.example.karori.menuFragment;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.karori.R;
import com.example.karori.Room.Meal;
import com.example.karori.Room.MealViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class FragmentCalendar extends Fragment {
    private TextView show_selected_date;
    private Button calendar;
    double car;
    double fatt;
    double proteins;
    LocalDate date;
    double calories;
    DecimalFormat df ;

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
        if(savedInstanceState!=null){
            if(savedInstanceState.getString("dataSalvata")==null){
                date=LocalDate.now();
            }else{
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                date = LocalDate.parse(savedInstanceState.getString("dataSalvata"), formatter);
                Log.d("calendar funge",""+date);
            }
        }
        else{
            date=LocalDate.now();
        }
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
        df = new DecimalFormat("#,##0.00");

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

            mealViewModel.getDayMeals(date).observe(getActivity(), new Observer<List<Meal>>() {
                @Override
                public void onChanged(List<Meal> meals) {
                    if(meals != null){
                        car=0;
                        fatt=0;
                        proteins=0;
                        calories=0;
                        for(Meal meal: meals){
                            car=car+meal.getCarboidratiTot();
                            fatt=fatt+meal.getGrassiTot();
                            proteins=proteins+meal.getProteineTot();
                            calories=calories+meal.getCalorieTot();
                        }
                    }else {
                        car=0;
                        fatt=0;
                        proteins=0;
                        calories=0;
                    }
                    carboidrati.setText(df.format(car));
                    fat.setText(df.format(fatt));
                    proteine.setText(df.format(proteins));
                    calorie.setText(df.format(calories));
                }
            });
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
            String dateString=date.format(formatter);
            show_selected_date.setText(""+dateString);


        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {


                        // if the user clicks on the positive
                        // button that is ok button update the
                        // selected date
                        String dateString = materialDatePicker.getHeaderText();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
                        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("MMM d, yyyy");
                        show_selected_date.setText(""+materialDatePicker.getHeaderText());
                        materialDatePicker.getArguments();
                        try {
                            date = LocalDate.parse(dateString, formatter);
                        }catch(Exception e){
                            date = LocalDate.parse(dateString, formatter2);
                        }
                        Log.d("calendar",""+date);
                        // in the above statement, getHeaderText
                        // will return selected date preview from the
                        // dialog


                        mealViewModel.getDayMeals(date).observe(getActivity(), new Observer<List<Meal>>() {
                            @Override
                            public void onChanged(List<Meal> meals) {
                                if(meals != null){
                                    car=0;
                                    fatt=0;
                                    proteins=0;
                                    calories=0;
                                    for(Meal meal: meals){
                                        car=car+meal.getCarboidratiTot();
                                        fatt=fatt+meal.getGrassiTot();
                                        proteins=proteins+meal.getProteineTot();
                                        calories=calories+meal.getCalorieTot();
                                    }
                                }else {
                                    car=0;
                                    fatt=0;
                                    proteins=0;
                                    calories=0;
                                }
                                carboidrati.setText(df.format(car));
                                fat.setText(df.format(fatt));
                                proteine.setText(df.format(proteins));
                                calorie.setText(df.format(calories));
                            }
                        });
                    }
                });


        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if(date!=null){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedString = date.format(formatter);
        outState.putString("dataSalvata", formattedString);
        super.onSaveInstanceState(outState);}
        else{
            outState.putString("dataSalvata", null);
        }
    }
}
