package com.example.karori.menuFragment;

import static com.example.karori.util.SharedPreferencesUtil.writeFloatData;
import static com.example.karori.util.SharedPreferencesUtil.writeIntData;
import static com.example.karori.util.SharedPreferencesUtil.writeStringData;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

import com.example.karori.Login.SignUpFragment;
import com.example.karori.Login.UserViewModel;
import com.example.karori.Login.UserViewModelFactory;
import com.example.karori.R;
import com.example.karori.Login.WelcomeActivity;
import com.example.karori.Source.User.UserCallback;
import com.example.karori.Source.User.UserDataRemoteDataSource;
import com.example.karori.data.User.User;
import com.example.karori.Login.Forgot_Password_Fragment;
import com.example.karori.repository.User.IUserRepository;
import com.example.karori.util.ServiceLocator;
import com.example.karori.util.SharedPreferencesUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FragmentProfilo extends Fragment {

    private static final String TAG = SignUpFragment.class.getSimpleName();
    private EditText editTextGoal;
    private EditText editTextAge;
    private TextView textViewKilocalorie;
    private Switch modifica_switch;
    private NumberPicker numberPickerWeight;
    private NumberPicker numberPickerHeight;
    private UserViewModel userViewModel;
    Map<String,Object> userdata;
    private TextView textViewMail;
    private UserDataRemoteDataSource userDataRemoteDataSource;
    private String ageNew;
    List<String> dataUser;
    private int weightNew;
    private int heightNew;
    private String goalNew;
    private int kilocalorieNew;

    CardView reset_psw_card;
    Button log_out;
    GoogleSignInOptions gOptions;
    GoogleSignInClient gClient;
    Forgot_Password_Fragment changePw;

    private FirebaseAuth mAuth;

    public FragmentProfilo() {}

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

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        ageNew = "";
        weightNew = 0;
        heightNew = 0;
        goalNew = "";


        final Switch modifica_switch = view.findViewById(R.id.switch_modifche);

        numberPickerWeight.setMaxValue(1500);
        numberPickerWeight.setMinValue(300);
        numberPickerWeight.setEnabled(false);

        numberPickerHeight.setMaxValue(230);
        numberPickerHeight.setMinValue(100);
        numberPickerHeight.setEnabled(false);

        editTextAge.setEnabled(false);
        editTextGoal.setEnabled(false);

        if(userViewModel.getLoggedUser()!= null){
            dataUser=new ArrayList<>();
            Log.d("firebase","entrato");
            //ciao
            User loggedUser = userViewModel.getLoggedUser();
            Log.d("firebase","idtoken "+loggedUser.getIdToken());
            DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("users").child(loggedUser.getIdToken());
            Log.d("firebase","reference "+reference);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    dataUser.clear();
                    Log.d("firebase",snapshot.getChildren().toString());
                    for(DataSnapshot sn:snapshot.getChildren()){
                        Log.d("firebase",""+sn);
                        dataUser.add(sn.getValue().toString());
                    }
                    weightNew=Integer.parseInt(dataUser.get(5));
                    heightNew=Integer.parseInt(dataUser.get(3));
                    editTextGoal.setText(dataUser.get(2));
                    editTextAge.setText(dataUser.get(0));
                    textViewKilocalorie.setText(dataUser.get(4));;
                    numberPickerWeight.setValue(Integer.parseInt(dataUser.get(5)));
                    numberPickerHeight.setValue(Integer.parseInt(dataUser.get(3)));
                    textViewMail.setText(dataUser.get(1));
                    Log.d("firebase",dataUser.get(1));
                    Log.d("firebase",dataUser.get(2));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            /*User user= userDataRemoteDataSource.getUserInfo(loggedUser);*/
            ///////////////////////////////////////////
            /////////////////////////////////////////
            User user=new User();
            user.setIdToken(loggedUser.getIdToken());
            ///////////////////////////////////////////
            /////////////////////////////////////////

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
                                    heightNew = newValue;
                                    if(heightNew <100 || heightNew > 230){
                                        heightNew = Integer.parseInt(dataUser.get(3));
                                        Toast.makeText(getContext(), "Please insert height between 100cm and 230cm", Toast.LENGTH_LONG).show();
                                    }

                            }
                        });


                        numberPickerWeight.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                            @Override
                            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                                    weightNew = newValue;
                                if(weightNew <300 || weightNew > 1500){
                                    weightNew = Integer.parseInt(dataUser.get(5));
                                    Toast.makeText(getContext(), "Please insert height between 300hg and 1500hg", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                        ageNew = editTextAge.getText().toString().trim();
                        goalNew = editTextGoal.getText().toString().trim();


                    } else {

                        ageNew = editTextAge.getText().toString().trim();
                        goalNew = editTextGoal.getText().toString().trim();
                        user.setEmail(dataUser.get(1));
                        user.setHeight(Integer.parseInt(String.valueOf(heightNew)));
                        user.setWeight(Integer.parseInt(String.valueOf(weightNew)));
                        user.setGoal(Integer.parseInt(goalNew));
                        user.setAge(Integer.parseInt(ageNew));
                        user.setKilocalorie((int) (1.2 * (66 + (13.7 * (user.getGoal() / 10)) + (5 * user.getHeight()) - (6.8 * user.getAge()))));
                        kilocalorieNew = (int) (1.2 * (66 + (13.7 * (user.getGoal() / 10)) + (5 * user.getHeight()) - (6.8 * user.getAge())));
                        editTextGoal.setEnabled(false);
                        editTextAge.setEnabled(false);
                        numberPickerHeight.setEnabled(false);
                        numberPickerWeight.setEnabled(false);

                        editTextGoal.setText(String.valueOf(goalNew));
                        editTextAge.setText(String.valueOf(ageNew));
                        textViewKilocalorie.setText(String.valueOf(user.getKilocalorie()));
                        numberPickerWeight.setValue(weightNew);
                        numberPickerHeight.setValue(heightNew);
                        textViewMail.setText(user.getEmail());

                        saveLoginData(user.getEmail(), user.getIdToken(), weightNew, heightNew, user.getKilocalorie(), user.getAge(), user.getGoal());
                    }
                }
            });

        } else if (account!=null){
            Log.d("firebase","entratosbagliato1");
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            String gemail = account.getEmail();
            String gidToken = account.getIdToken();
            firebaseAuth.fetchSignInMethodsForEmail(gemail)
                    .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                            if (task.isSuccessful()) {
                                SignInMethodQueryResult result = task.getResult();
                                if (result.getSignInMethods().isEmpty()) {
                                    // non esiste un utente con la mail specificata
                                    Log.d(TAG, "utente senza mail non ancora registrato");
                                    //creare un utente su firebase con gmail e idtoken.
                                    saveLoginData(gemail, gidToken, 0, 0,0,0,0);
                                    Log.d(TAG, "utente senza mail registrato");
                                } else {
                                    // esiste un utente con la mail specificata


                                }
                            } else {
                                // c'è stato un errore durante la verifica
                            }
                        }
                    });

        }
        else{
            Log.d("firebase","entratosbagliato2");
            editTextGoal.setText("ERROR");
            editTextAge.setText("ERROR");
            textViewKilocalorie.setText("ERROR");
            numberPickerWeight.setValue(0);
            numberPickerHeight.setValue(0);
            textViewMail.setText("ERROR");
        }

        mAuth = FirebaseAuth.getInstance();
            log_out = view.findViewById(R.id.LogOut);
            changePw = new Forgot_Password_Fragment();
            log_out.setOnClickListener(v -> {
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


            });

            reset_psw_card = (CardView) view.findViewById(R.id.cardViewPw);
            reset_psw_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //devi cambiare email in base a quella con cui sei loggato

                    mAuth.sendPasswordResetEmail(dataUser.get(1)).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    }

    private void saveLoginData(String email, String idToken, int weight, int height, int kilocalorie, int age, int goal) {
        writeStringData("com.example.karori.save_file.txt", "email:", email);

        writeStringData("com.example.karori.save_file.txt", "idToken:", idToken);

        writeIntData("com.example.karori.save_file.txt", "age: ", age);

        writeFloatData("com.example.karori.save_file.txt", "weight: ", weight);

        writeIntData("com.example.karori.save_file.txt", "height: ", height);

        writeFloatData("com.example.karori.save_file.txt", "kilocalorie: ", kilocalorie);

        writeFloatData("com.example.karori.save_file.txt", "goal: ", goal);

        //non funziona
        userDataRemoteDataSource.SaveinfoUser(idToken, weight, height, kilocalorie, age, goal);

    }

}
