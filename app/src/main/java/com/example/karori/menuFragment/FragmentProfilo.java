package com.example.karori.menuFragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.karori.R;
import com.example.karori.WelcomeActivity;
import com.example.karori.data.model.User;
import com.example.karori.ui.Forgot_Password_Fragment;
import com.example.karori.ui.SummaryActivity;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentProfilo extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    CardView card;
    Button LogOut;
    GoogleSignInOptions gOptions;
    GoogleSignInClient gClient;
    Forgot_Password_Fragment changePw;

    private TextView textWeight;
    private TextView textHeight;
    private NumberPicker numberPickerHeight;
    private NumberPicker numberPickerWeight;
    private int valueHeight;
    private int valueWeight;

    private User user;

    private FirebaseAuth mAuth;

    //aggiungere le icone alle varie cardview

    public FragmentProfilo() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //si dovrà fare con ROOM
        if (savedInstanceState != null) {
            valueHeight = savedInstanceState.getInt(ARG_PARAM1, 0);
            valueWeight = savedInstanceState.getInt(ARG_PARAM2, 0);
        } else {
            valueHeight = 170;
            valueWeight = 80;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profilo, container, false);
        mAuth = FirebaseAuth.getInstance();
        card=(CardView) view.findViewById(R.id.cardViewPw);

        ///google
        LogOut=(Button)view.findViewById(R.id.LogOut);

        changePw=new Forgot_Password_Fragment();
        
        gOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gClient= GoogleSignIn.getClient(getContext(), gOptions);

        GoogleSignInAccount gAccount=GoogleSignIn.getLastSignedInAccount(getContext());
        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(getActivity(), WelcomeActivity.class));
                    }
                });
            }
        });

        //il sendPasswordResetEmail dovrà essere gestito con una classe intermezza e non in questo modo spartano
        //per firebase. Perchè l'email è salvata in room e quindi il campo dove ho messo la mia email varierà
        //in base al dispositivo in cui sono.
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.sendPasswordResetEmail("montilorenzo62@gmail.com").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getContext(), "mail send. check your email", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getContext(), "try again! Something wrong happened", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });




        textWeight = (TextView) view.findViewById(R.id.weightTextView);
        textHeight = (TextView) view.findViewById(R.id.heightTextView);
        numberPickerHeight = (NumberPicker) view.findViewById(R.id.height);
        numberPickerWeight = (NumberPicker) view.findViewById(R.id.weight);

        numberPickerWeight.setMaxValue(130);
        numberPickerWeight.setMinValue(50);

        numberPickerHeight.setMaxValue(200);
        numberPickerHeight.setMinValue(150);

        numberPickerWeight.setValue(valueWeight);
        numberPickerHeight.setValue(valueHeight);

        numberPickerHeight.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                valueHeight = newValue;
            }
        });

        numberPickerWeight.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                valueWeight = newValue;
            }
        });


        return view;
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(ARG_PARAM1, valueHeight);
        savedInstanceState.putInt(ARG_PARAM2, valueWeight);
    }
}
