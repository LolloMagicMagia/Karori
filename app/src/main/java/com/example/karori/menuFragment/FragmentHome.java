package com.example.karori.menuFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.karori.Adapter.AdapterChangeRiassunti;
import com.example.karori.Login.UserViewModel;
import com.example.karori.Login.UserViewModelFactory;
import com.example.karori.R;
import com.example.karori.Room.Meal;
import com.example.karori.Room.MealViewModel;
import com.example.karori.SearchClasses.SearchActivity;
import com.example.karori.Source.User.UserDataRemoteDataSource;
import com.example.karori.data.User.User;
import com.example.karori.repository.User.IUserRepository;
import com.example.karori.util.ServiceLocator;
import com.example.karori.util.SharedPreferencesUtil;
import com.google.android.material.tabs.TabLayout;
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

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;

public class FragmentHome extends Fragment {
    private static final String KEY_INDEX="index";
    private ImageButton mattina;
    private ImageButton pomeriggio;
    private ImageButton sera;
    private UserViewModel userViewModel;
    private UserDataRemoteDataSource userDataRemoteDataSource;
    private Button riassunti;
    int positionRiassunti;
    float defaultValue = -1;
    private int where_MPS;
    String fronsblix;
    double calories = 0;
    double calogero = 0;
    private TextView totProteineBar;
    private TextView cal_now;
    private CircularProgressIndicator progressBar;
    private String pasto = "";
    private Button bottoneChangeActivity;

    public static boolean first;

    public FragmentHome() {
    }

    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
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
        fronsblix="0";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_home, container, false);
        cal_now= (TextView) view.findViewById(R.id.caloriePercentuali);

        riassunti=view.findViewById(R.id.Riassunti);
        ViewPager2 viewPager = view.findViewById(R.id.pager);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        MealViewModel mealViewModel = new ViewModelProvider(this).get(MealViewModel.class);
        bottoneChangeActivity = view.findViewById(R.id.changeActivity);

        AdapterChangeRiassunti adapter = new AdapterChangeRiassunti(this);
        viewPager.setAdapter(adapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //PER COLORARE IL TESTO
                /* tablayout.setTabTextColors(Color.GRAY, Color.GREEN); // set the tab text colors for the both states of the tab.*/
                viewPager.setCurrentItem(tab.getPosition());
                fronsblix = Integer.toString(tab.getPosition());
                bottoneChangeActivity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), SearchActivity.class);
                        intent.putExtra("pasto", fronsblix);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //nothing..
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                String fronsblix = Integer.toString(tab.getPosition());
                bottoneChangeActivity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), SearchActivity.class);
                        intent.putExtra("pasto",fronsblix);
                        startActivity(intent);
                    }
                });
            }
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                positionRiassunti=position;
                tabLayout.getTabAt(position).select();
            }
        });

        riassunti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyPopupFragment popup = new MyPopupFragment(positionRiassunti);
                popup.show(getChildFragmentManager(), "popup");
            }
        });

        //room per settare il totale delle calorie
        LocalDate currentTime = LocalDate.now();
        mealViewModel.getDayMeals(currentTime).observe(getActivity(), new Observer<List<Meal>>() {
            @Override
            public void onChanged(List<Meal> meals) {
                if (meals != null) {
                    calories = 0;
                    for (Meal meal : meals) {
                        calories = calories + meal.getCalorieTot() + (calogero / 3);
                        User loggedUser = userViewModel.getLoggedUser();
                        if (loggedUser != null) {
                            DatabaseReference reference = FirebaseDatabase.getInstance()
                                    .getReference().child("users").child(loggedUser.getIdToken());
                            DatabaseReference newReference = reference.child("zDates");
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
                            LocalDate date = LocalDate.now();
                            DatabaseReference dateReference = newReference.child(date.format(formatter).toLowerCase());
                            DecimalFormat df = new DecimalFormat("#,##0.00");
                            dateReference.child("zCalAssunte")
                                    .setValue(calories);
                        }
                    }
                }
                if (userViewModel.getLoggedUser() != null) {
                    ArrayList<String> dataUser = new ArrayList<>();
                    User loggedUser = userViewModel.getLoggedUser();
                    DatabaseReference reference = FirebaseDatabase.getInstance()
                            .getReference().child("users").child(loggedUser.getIdToken());
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            dataUser.clear();
                            Log.d("firebase", snapshot.getChildren().toString());
                            for (DataSnapshot sn : snapshot.getChildren()) {
                                Log.d("firebase", "" + sn);
                                dataUser.add(sn.getValue().toString());
                            }
                            String caloriesTot = String.valueOf(Integer.parseInt(dataUser.get(4)));
                            progressBarUpdate(calories, Double.parseDouble(caloriesTot));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            //
                        }
                    });
                }
                else {
                    progressBarUpdate(calories,3000);
                    Toast.makeText(getContext(), "Default Calories Have Been Insterted, Check Your Connection", Toast.LENGTH_LONG).show();
                }
            }
        });



        //barra inizializzazione
        progressBar=(CircularProgressIndicator) view.findViewById(R.id.circular_progress);
        //così va da 0 a 100
        progressBar.setMaxProgress(100);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(KEY_INDEX, where_MPS);
    }

    public  void progressBarUpdate(double totProteineAssunte, double totProteine){
        DecimalFormat df = new DecimalFormat("#.##");
        if(totProteineAssunte>totProteine){
            cal_now.setText(">100 %");
            progressBar.setCurrentProgress(totProteine);
        }
        else{
            if (totProteineAssunte >= 0){
                cal_now.setText(String.valueOf(df.format(totProteineAssunte))+" / "+ String.valueOf(totProteine));
                progressBar.setCurrentProgress((int)((totProteineAssunte/totProteine)*100));
            }else{
                cal_now.setText(String.valueOf(0)+" / "+ String.valueOf(totProteine));
                progressBar.setCurrentProgress((int)((0/totProteine)*100));
            }
        }

    }
}
