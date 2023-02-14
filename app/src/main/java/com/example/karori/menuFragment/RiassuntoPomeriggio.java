package com.example.karori.menuFragment;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class RiassuntoPomeriggio extends Fragment {
    private TextView tgrassip;
    private UserDataRemoteDataSource userDataRemoteDataSource;
    LocalDate date;
    private UserViewModel userViewModel;
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
        View view = inflater.inflate(R.layout.fragment_riassunto_pomeriggio, container, false);
        MealViewModel mealViewModel = new ViewModelProvider(this).get(MealViewModel.class);
        tgrassip= (TextView) view.findViewById(R.id.grassiP);
        df = new DecimalFormat("#,##0.00");
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
                    if (userViewModel.getLoggedUser() != null) {
                        User loggedUser = userViewModel.getLoggedUser();
                        DatabaseReference reference = FirebaseDatabase.getInstance()
                                .getReference().child("users")
                                .child(loggedUser.getIdToken());
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
                        date=LocalDate.now();
                        DatabaseReference newReference = reference.child("zDates");
                        DatabaseReference dateReference = newReference.child(date.format(formatter).toLowerCase());
                        DatabaseReference typeReference = dateReference.child("Pranzo");
                        typeReference.child("Calorie").setValue(tcaloriep.getText());
                        typeReference.child("Proteine").setValue(tproteinep.getText());
                        typeReference.child("Grassi").setValue(tgrassip.getText());
                        typeReference.child("Carboidrati").setValue(tcarboidratip.getText());
                    }
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
