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
                        LocalDate date = LocalDate.parse(dateString, formatter);
                        // in the above statement, getHeaderText
                        // will return selected date preview from the
                        // dialog
                        /*mealViewModel.getMealFromDate(date, "colazione").observe(getActivity(), new Observer<Meal>() {
                            @Override
                            public void onChanged(Meal meal) {
                                if(meal != null){
                                    tgrassi.setText(df.format(meal.getGrassiTot()));
                                    tproteine.setText(df.format(meal.getProteineTot()));
                                    tcarboidrati.setText(df.format(meal.getCarboidratiTot()));
                                    tcalorie.setText(df.format(meal.getCalorieTot()));
                                }else {
                                    tgrassi.setText("0");
                                    tproteine.setText("0");
                                    tcarboidrati.setText("0");
                                    tcalorie.setText("0");
                                }
                            }
                        });*/


                    }
                });


        return view;
    }

}
