package com.example.karori.menuFragment;
import android.annotation.SuppressLint;
import android.app.Activity;
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

import com.example.karori.Login.UserViewModel;
import com.example.karori.Login.UserViewModelFactory;
import com.example.karori.R;
import com.example.karori.Room.Meal;
import com.example.karori.Room.MealViewModel;
import com.example.karori.Source.User.UserDataRemoteDataSource;
import com.example.karori.data.User.User;
import com.example.karori.repository.User.IUserRepository;
import com.example.karori.util.ServiceLocator;
import com.example.karori.util.SharedPreferencesUtil;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FragmentCalendar extends Fragment {
    private TextView show_selected_date;
    private Button calendar;
    double car;
    private UserViewModel userViewModel;
    double fatt;
    private UserDataRemoteDataSource userDataRemoteDataSource;
    List<String> dataUserCalendar;
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
        IUserRepository userRepository = ServiceLocator.getInstance().
                getUserRepository(requireActivity().getApplication());
        userViewModel = new ViewModelProvider(
                requireActivity(),
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);
        userViewModel.setAuthenticationError(false);
        Activity activity = getActivity();
        SharedPreferencesUtil sharedPreferencesUtil = activity != null ? new SharedPreferencesUtil(activity.getApplication()) : null;
        userDataRemoteDataSource = sharedPreferencesUtil != null ? new UserDataRemoteDataSource(sharedPreferencesUtil) : null;

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
        TextView height=view.findViewById(R.id.pesoCalendar);
        TextView weight=view.findViewById(R.id.altezzaCalendar);
        TextView weightGoal=view.findViewById(R.id.pesoGoalCalendar);
        TextView age=view.findViewById(R.id.ageCalendar);
        TextView calNeeds=view.findViewById(R.id.calorieNeedsCalendar);
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
                        DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("dd MMM yyyy");
                        DateTimeFormatter formatter4 = DateTimeFormatter.ofPattern("d MMM yyyy");
                        show_selected_date.setText(""+materialDatePicker.getHeaderText());
                        materialDatePicker.getArguments();
                        try {
                            date = LocalDate.parse(dateString, formatter);
                            Log.d("formatter","emulatore1 "+dateString);
                        }catch(Exception e){
                            try{
                                date = LocalDate.parse(dateString, formatter2);
                                Log.d("formatter","emulatore2 "+dateString);
                            }catch(Exception ex){
                                try{
                                date = LocalDate.parse(dateString, formatter3);
                                Log.d("formatter","cellulare1 "+dateString);}
                                catch(Exception ex1){
                                    date = LocalDate.parse(dateString, formatter4);
                                    Log.d("formatter","cellulare2 "+dateString);}
                                }
                            }

                        Log.d("formatter",""+date);
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


        dataUserCalendar=new ArrayList<>();
        Log.d("firebase","entrato");
        //ciao
        User loggedUser = userViewModel.getLoggedUser();
        Log.d("firebase","idtoken "+loggedUser.getIdToken());
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("users").child(loggedUser.getIdToken());
        Log.d("firebase","reference "+reference);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataUserCalendar.clear();
                Log.d("firebase",snapshot.getChildren().toString());
                for(DataSnapshot sn:snapshot.getChildren()){
                    Log.d("firebase",""+sn);
                    dataUserCalendar.add(sn.getValue().toString());
                }
                weight.setText(dataUserCalendar.get(5));
                height.setText(dataUserCalendar.get(3));
                age.setText(dataUserCalendar.get(0));;
                weightGoal.setText(dataUserCalendar.get(2));
                calNeeds.setText(dataUserCalendar.get(4));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
