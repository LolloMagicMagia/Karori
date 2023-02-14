package com.example.karori.menuFragment;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class RiassuntoMattina extends Fragment {
    private static final String KEY_GRASSI = "GRASSIM";
    private static final String KEY_SATURI = "SATURIM";
    private static final String KEY_CARBOIDRATI = "CARBOIDRATIM";
    private static final String KEY_CALORIE = "CALORIEM";

    double calToAdd = 0;
    double carboToAdd = 0;
    double proteinToAdd = 0;
    double fatToAdd = 0;

    LocalDate date;
    private UserViewModel userViewModel;
    private TextView tgrassi;
    private TextView tproteine;
    private TextView tcarboidrati;
    private TextView tcalorie;
    CardView card;
    Dialog myDialog;
    DecimalFormat df ;
    private UserDataRemoteDataSource userDataRemoteDataSource;



    public RiassuntoMattina() {
        // Required empty public constructor
    }

    public static RiassuntoMattina newInstance() {
        return new RiassuntoMattina();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_riassunto_mattina, container, false);
        MealViewModel mealViewModel = new ViewModelProvider(this).get(MealViewModel.class);
        df = new DecimalFormat("#,##0.00");
        tgrassi= (TextView) view.findViewById(R.id.grassi);
        tproteine = (TextView) view.findViewById(R.id.Protein);
        tcalorie= (TextView) view.findViewById(R.id.Calorie);
        tcarboidrati= (TextView) view.findViewById(R.id.carboidrati);
        myDialog = new Dialog(getContext());

        //mattina, dovrà andare a prendere i valori nel database inizialmente
        setValoriRiassuntivi(mealViewModel);

        // Inflate the layout for this fragment
        return view;
    }

    private void setValoriRiassuntivi(MealViewModel mealViewModel) {

        LocalDate currentTime = LocalDate.now();
        Log.d("data", "" + currentTime);
        mealViewModel.getMealFromDate(currentTime, "colazione").observe(getActivity(), new Observer<Meal>() {
            @Override
            public void onChanged(Meal meal) {
                Boolean bool = true;
                if (meal != null) {
                    tgrassi.setText(df.format(meal.getGrassiTot()));
                    tproteine.setText(df.format(meal.getProteineTot()));
                    tcarboidrati.setText(df.format(meal.getCarboidratiTot()));
                    tcalorie.setText(df.format(meal.getCalorieTot()));
                    if (userViewModel.getLoggedUser() != null) {
                        User loggedUser = userViewModel.getLoggedUser();
                        DatabaseReference reference = FirebaseDatabase.getInstance()
                                .getReference().child("users")
                                .child(loggedUser.getIdToken());
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
                        date = LocalDate.now();
                        DatabaseReference newReference = reference.child("zDates");
                        DatabaseReference dateReference = newReference.child(date.format(formatter).toLowerCase());
                        DatabaseReference typeReference = dateReference.child("Colazione");
                        typeReference.child("Calorie").setValue(tcalorie.getText());
                        typeReference.child("Proteine").setValue(tproteine.getText());
                        typeReference.child("Grassi").setValue(tgrassi.getText());
                        typeReference.child("Carboidrati").setValue(tcarboidrati.getText());
                    }
                } else {
                        tgrassi.setText("0");
                        tproteine.setText("0");
                        tcarboidrati.setText("0");
                        tcalorie.setText("0");
                }
            }
        });

    }
}
