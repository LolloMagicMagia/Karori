package com.example.karori.menuFragment;

import static com.example.karori.util.SharedPreferencesUtil.writeFloatData;
import static com.example.karori.util.SharedPreferencesUtil.writeIntData;
import static com.example.karori.util.SharedPreferencesUtil.writeStringData;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.karori.Login.UserViewModel;
import com.example.karori.R;
import com.example.karori.Login.WelcomeActivity;
import com.example.karori.Source.User.UserDataRemoteDataSource;
import com.example.karori.data.User.User;
import com.example.karori.Login.Forgot_Password_Fragment;
import com.example.karori.util.SharedPreferencesUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class FragmentProfilo extends Fragment {

    private EditText editTextGoal;
    private EditText editTextAge;
    private TextView textViewKilocalorie;
    private Switch modifica_switch;
    private NumberPicker numberPickerWeight;
    private NumberPicker numberPickerHeight;
    private UserViewModel userViewModel;
    private TextView textViewMail;
    private UserDataRemoteDataSource userDataRemoteDataSource;

    CardView reset_psw_card;
    Button log_out;
    GoogleSignInOptions gOptions;
    GoogleSignInClient gClient;
    Forgot_Password_Fragment changePw;

    private FirebaseAuth mAuth;

    public FragmentProfilo() {}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.setAuthenticationError(false);

        Activity activity = getActivity();
        SharedPreferencesUtil sharedPreferencesUtil = activity != null ? new SharedPreferencesUtil(activity.getApplication()) : null;
        userDataRemoteDataSource = sharedPreferencesUtil != null ? new UserDataRemoteDataSource(sharedPreferencesUtil) : null;*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profilo, container, false);

    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextAge = view.findViewById(R.id.textView13);
        editTextGoal = view.findViewById(R.id.textView12);
        textViewKilocalorie = view.findViewById(R.id.textView14);
        numberPickerHeight = view.findViewById(R.id.height);
        numberPickerWeight = view.findViewById(R.id.weight);
        textViewMail = view.findViewById(R.id.textView15);


        final Switch modifica_switch = view.findViewById(R.id.switch_modifche);

        editTextAge.setEnabled(false);
        editTextGoal.setEnabled(false);

        numberPickerWeight.setMaxValue(1500);
        numberPickerWeight.setMinValue(450);
        numberPickerWeight.setEnabled(false);

        numberPickerHeight.setMaxValue(230);
        numberPickerHeight.setMinValue(100);
        numberPickerHeight.setEnabled(false);


        /*if(userViewModel.getLoggedUser()!= null) {
            User user = userViewModel.getLoggedUser();

            editTextGoal.setText(user.getGoal());
            editTextAge.setText(user.getAge());
            textViewKilocalorie.setText(user.getKilocalorie());
            numberPickerWeight.setValue(user.getWeight());
            numberPickerHeight.setValue(user.getHeight());
            textViewMail.setText(user.getEmail());

            modifica_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        editTextGoal.setEnabled(true);
                        editTextAge.setEnabled(true);
                        numberPickerHeight.setEnabled(true);
                        numberPickerWeight.setEnabled(true);

                        numberPickerHeight.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                                user.setWeight(newValue);
                            }
                        });


                        numberPickerWeight.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                            @Override
                            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                                user.setHeight(newValue);
                            }
                        });


                    } else {
                        user.setGoal(Integer.parseInt(editTextGoal.getText().toString().trim()));
                        user.setAge(Integer.parseInt(editTextAge.getText().toString().trim()));
                        user.setKilocalorie((int) (1.2 * (66 + (13.7 * (user.getGoal() / 100)) + (5 + user.getHeight()) - (6.8 + user.getAge()))));
                        editTextGoal.setEnabled(false);
                        editTextAge.setEnabled(false);
                        numberPickerHeight.setEnabled(false);
                        numberPickerWeight.setEnabled(false);

                        editTextGoal.setText(user.getGoal());
                        editTextAge.setText(user.getAge());
                        textViewKilocalorie.setText(user.getKilocalorie());
                        numberPickerWeight.setValue(user.getWeight());
                        numberPickerHeight.setValue(user.getHeight());
                        textViewMail.setText(user.getEmail());

                        saveLoginData(user.getEmail(), user.getIdToken(), user.getWeight(), user.getHeight(), user.getKilocalorie(), user.getAge(), user.getGoal());
                    }
                }
            });

            mAuth = FirebaseAuth.getInstance();
            log_out = view.findViewById(R.id.LogOut);
            changePw = new Forgot_Password_Fragment();
            gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            gClient = GoogleSignIn.getClient(getContext(), gOptions);
            GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(getContext());
            log_out.setOnClickListener(v -> {
                if (gAccount != null) {
                    gClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent i = new Intent(getActivity(), WelcomeActivity.class);
                            getActivity().finish();
                            startActivity(i);
                        }
                    });
                } else {
                    userViewModel.logout();
                    if (userViewModel.getLoggedUser() == null) {
                        Snackbar.make(view,
                                requireActivity().getString(R.string.logout),
                                Snackbar.LENGTH_SHORT).show();
                        Intent i = new Intent(getActivity(), WelcomeActivity.class);
                        getActivity().finish();
                        startActivity(i);
                    } else {
                        Snackbar.make(view,
                                requireActivity().getString(R.string.unexpected_error),
                                Snackbar.LENGTH_SHORT).show();

                    }
                }


            });

            reset_psw_card = (CardView) view.findViewById(R.id.cardViewPw);
            reset_psw_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mAuth.sendPasswordResetEmail("montilorenzo62@gmail.com").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "mail send. check your email", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), "try again! Something wrong happened", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });

        }*/
        /*editTextGoal.setText("ERROR");
        editTextAge.setText("ERROR");
        textViewKilocalorie.setText("ERROR");
        numberPickerWeight.setValue(0);
        numberPickerHeight.setValue(0);
        textViewMail.setText("ERROR");*/
        numberPickerWeight.setValue(450);
        numberPickerHeight.setValue(170);

        modifica_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==true){
                    editTextAge.setEnabled(true);
                    editTextGoal.setEnabled(true);
                    numberPickerWeight.setEnabled(true);
                    numberPickerHeight.setEnabled(true);
                }
                else{
                    numberPickerWeight.setEnabled(false);
                    numberPickerHeight.setEnabled(false);
                    editTextAge.setEnabled(false);
                    editTextGoal.setEnabled(false);
                    //invia i dati a firebase o a ROOM
                }
            }
        });


    }

    private void saveLoginData(String email, String idToken, int weight, int height, int kilocalorie, int age, int goal) {
        writeStringData("com.example.karori.save_file.txt", "email:", email);

        writeStringData("com.example.karori.save_file.txt", "idToken:", idToken);

        writeIntData("com.example.karori.save_file.txt", "age: ", age);

        writeFloatData("com.example.karori.save_file.txt", "weight: ", weight);

        writeIntData("com.example.karori.save_file.txt", "height: ", height);

        writeFloatData("com.example.karori.save_file.txt", "kilocalorie: ", kilocalorie);

        writeFloatData("com.example.karori.save_file.txt", "goal: ", goal);

        userDataRemoteDataSource.SaveinfoUser(idToken, weight, height, kilocalorie, age, goal);

    }

}
