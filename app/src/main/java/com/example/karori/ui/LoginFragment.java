package com.example.karori.ui;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.karori.R;
import com.example.karori.WelcomeActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {
    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPsw;
    private static final String TAG = LoginFragment.class.getSimpleName();
    private static final boolean USE_NAVIGATION_COMPONENT = true;
    private FirebaseAuth mAuth;
    Button provaSummaryActivity;
    Button button_google_login;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(){
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        button_google_login = (Button) view.findViewById(R.id.button_google_login);

        //bypass parte di lomboz
        provaSummaryActivity=view.findViewById(R.id.provaSummaryActivity);
        provaSummaryActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myInt=new Intent(getContext(), SummaryActivity.class);
                startActivity(myInt);
            }
        });

        return view;

    }
    //////////////////////////////////////////////

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Button buttonLogin = (Button) view.findViewById(R.id.buttonLogin);
        final Button buttonFrgPsw =(Button)  view.findViewById(R.id.buttonForgotPsw);
        final Button buttonSignUp = (Button) view.findViewById(R.id.sign_up_button);

        //nome del file dove verranno salvati i dani
        SharedPreferences prefs = getActivity().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        //prende la var isLoggedIn e se non esiste restituisce false
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        editTextEmail = view.findViewById(R.id.usernameEditText);
        editTextPsw = view.findViewById(R.id.PswEditText);

        buttonSignUp.setOnClickListener( v->
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_signUpFragment));

        buttonFrgPsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_forgot_Password_Fragment);
            }
        });


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(isLoggedIn){
                    Intent myInt=new Intent(getContext(), SummaryActivity.class);
                    startActivity(myInt);
                }
                else{
                    userLogin();
                    editor.putBoolean("isLoggedIn",true);
                }
            }
        });

        gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc= GoogleSignIn.getClient(getContext(), gso);

        GoogleSignInAccount gAccount=GoogleSignIn.getLastSignedInAccount(getContext());
        if(gAccount !=null){
            Intent intent=new Intent(getActivity(),SummaryActivity.class);
            startActivity(intent);
        }

        ActivityResultLauncher<Intent> activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode()== Activity.RESULT_OK){
                            Intent data = result.getData();
                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                            try{
                                task.getResult(ApiException.class);
                                Intent intent = new Intent(getActivity(),SummaryActivity.class);
                                startActivity(intent);
                            }catch(ApiException e){
                                Toast.makeText(getContext(),"Something went wrong",Toast.LENGTH_LONG);
                            }

                        }
                    }
                });

        button_google_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent=gsc.getSignInIntent();
                activityResultLauncher.launch(signInIntent);

            }
        });


    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPsw.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Insert a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPsw.setError("A password is required!");
            editTextPsw.requestFocus();
            return;
        }

        if(password.length() < 6){
            editTextPsw.setError("The password is too short, enter a password that is at least 6 characters long ");
            editTextPsw.requestFocus();
            return;
        }
        // TODO : verificare se la mail è presente nel database;


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent myInt=new Intent(getContext(), SummaryActivity.class);
                    startActivity(myInt);
                }else{
                    Toast.makeText(getContext(), "Login failed! try again.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}