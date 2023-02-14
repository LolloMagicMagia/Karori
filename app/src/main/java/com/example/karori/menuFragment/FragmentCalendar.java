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
import android.widget.Toast;

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
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    String dateStr = LocalDate.now().format(formatter);
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

        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        // if the user clicks on the positive
                        // button that is ok button update the
                        // selected date
                        //LocalDate date = LocalDate.parse(newStr, formatter);
                        //Toast.makeText(getActivity(), ""+dateStr, Toast.LENGTH_SHORT).show();
                        materialDatePicker.getArguments();
                        if (userViewModel.getLoggedUser() != null) {
                            String dateString = materialDatePicker.getHeaderText();
                            show_selected_date.setText("" + materialDatePicker.getHeaderText());
                            StringBuilder sb = new StringBuilder(dateString);
                            sb.insert(6, ',');
                            String newStr = sb.toString();
                            //12 feb, 2023
                            String numero = newStr.substring(0, 2); //numero giorno 12
                            String mese = newStr.substring(3, 6); //mese feb
                            String anno = newStr.substring(8, 12); //anno 2023
                            dateStr = mese + " " + numero + "," + " " + anno;
                            ArrayList<String> dataUser = new ArrayList<>();
                            User loggedUser = userViewModel.getLoggedUser();
                            DatabaseReference reference = FirebaseDatabase.getInstance()
                                    .getReference().child("users")
                                    .child(loggedUser.getIdToken());
                            DatabaseReference newReference = reference.child("zDates");
                            DatabaseReference dateReference = newReference.child(dateStr.toLowerCase());
                            Log.d("ciaoneciccione", dateReference.toString());
                            DatabaseReference colazReference = dateReference.child("Colazione");
                            Log.d("ciaoneciccione", colazReference.toString());
                            DatabaseReference pranzoReference = dateReference.child("Pranzo");
                            DatabaseReference cenaReference = dateReference.child("Cena");

                            if (dateReference.getParent() == null) {
                                carboidrati.setText("0,0");
                                fat.setText("0,0");
                                proteine.setText("0,0");
                            } else {
                                dateReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        dataUser.clear();
                                        Log.d("firebase", snapshot.getChildren().toString());
                                        for (DataSnapshot sn : snapshot.getChildren()) {
                                            Log.d("firebase", "" + sn);
                                            dataUser.add(sn.getValue().toString());
                                        }
                                        try {
                                            calories = Double.parseDouble(dataUser.get(3).replace(",", "."));
                                            //Toast.makeText(getActivity(), ""+calories, Toast.LENGTH_SHORT).show();
                                            calorie.setText(df.format(calories));
                                        } catch (Exception e) {
                                            calorie.setText("0");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        //
                                    }
                                });
                                colazReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        dataUser.clear();
                                        Log.d("firebase", snapshot.getChildren().toString());
                                        for (DataSnapshot sn : snapshot.getChildren()) {
                                            Log.d("firebase", "" + sn);
                                            dataUser.add(sn.getValue().toString());
                                        }
                                        try {
                                            car += Double.parseDouble(dataUser.get(1).replace(",", "."));
                                            fatt += Double.parseDouble(dataUser.get(2).replace(",", "."));
                                            proteins += Double.parseDouble(dataUser.get(3).replace(",", "."));
                                        } catch (Exception e) {
                                            car +=0;
                                            fatt +=0;
                                            proteins +=0;
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        //
                                    }
                                });
                                cenaReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        dataUser.clear();
                                        Log.d("firebase", snapshot.getChildren().toString());
                                        for (DataSnapshot sn : snapshot.getChildren()) {
                                            Log.d("firebase", "" + sn);
                                            dataUser.add(sn.getValue().toString());
                                        }
                                        try {
                                            car += Double.parseDouble(dataUser.get(1).replace(",", "."));
                                            fatt += Double.parseDouble(dataUser.get(2).replace(",", "."));
                                            proteins += Double.parseDouble(dataUser.get(3).replace(",", "."));
                                        } catch (Exception e) {
                                            car +=0;
                                            fatt +=0;
                                            proteins +=0;
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        //
                                    }
                                });
                                pranzoReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        dataUser.clear();
                                        Log.d("firebase", snapshot.getChildren().toString());
                                        for (DataSnapshot sn : snapshot.getChildren()) {
                                            Log.d("firebase", "" + sn);
                                            dataUser.add(sn.getValue().toString());
                                        }
                                        try {
                                            car += Double.parseDouble(dataUser.get(1).replace(",", "."));
                                            fatt += Double.parseDouble(dataUser.get(2).replace(",", "."));
                                            proteins += Double.parseDouble(dataUser.get(3).replace(",", "."));
                                            carboidrati.setText(df.format(car));
                                            fat.setText(df.format(fatt));
                                            proteine.setText(df.format(proteins));
                                        } catch (Exception e) {
                                            car += 0;
                                            fatt += 0;
                                            proteins += 0;
                                            carboidrati.setText(df.format(car));
                                            fat.setText(df.format(fatt));
                                            proteine.setText(df.format(proteins));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        //
                                    }
                                });
                                car = 0;
                                fatt = 0;
                                proteins = 0;
                            }
                        }
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
