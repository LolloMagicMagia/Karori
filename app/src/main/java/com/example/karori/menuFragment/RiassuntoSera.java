package com.example.karori.menuFragment;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
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

public class RiassuntoSera extends Fragment {
    private TextView tgrassis;
    private UserViewModel userViewModel;
    private UserDataRemoteDataSource userDataRemoteDataSource;
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
                    if (userViewModel.getLoggedUser() != null) {
                        User loggedUser = userViewModel.getLoggedUser();
                        DatabaseReference reference = FirebaseDatabase.getInstance()
                                .getReference().child("users")
                                .child(loggedUser.getIdToken());
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
                        LocalDate date=LocalDate.now();
                        DatabaseReference newReference = reference.child("zDates");
                        DatabaseReference dateReference = newReference.child(date.format(formatter).toLowerCase());
                        DatabaseReference typeReference = dateReference.child("Cena");
                        typeReference.child("Calorie").setValue(tcalories.getText());
                        typeReference.child("Proteine").setValue(tproteines.getText());
                        typeReference.child("Grassi").setValue(tgrassis.getText());
                        typeReference.child("Carboidrati").setValue(tcarboidratis.getText());
                    }
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
