package com.example.karori.Login;

import static com.example.karori.util.SharedPreferencesUtil.writeStringData;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.karori.Models.Result;
import com.example.karori.R;
import com.example.karori.data.User.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.List;

public class SignUpFragment extends Fragment {

    private static final String TAG = SignUpFragment.class.getSimpleName();

    private EditText editTextEmail;
    private EditText editTextPsw;
    private EditText editTextCnfPsw;
    private UserViewModel userViewModel;


    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.setAuthenticationError(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextEmail = view.findViewById(R.id.Mail_input_SU);
        editTextPsw= view.findViewById(R.id.Psw_input_su);
        editTextCnfPsw= view.findViewById(R.id.ConfirmPsw_Input_SU);

        final Button buttonRegistration = view.findViewById(R.id.RegisterUserButton);
        final Button buttonback = view.findViewById(R.id.backbuttonsignup);

        buttonback.setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_loginFragment2));

        buttonRegistration.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPsw.getText().toString().trim();
            String cnfpsw =editTextCnfPsw.getText().toString().trim();

            if (isEmailOk(email) & isPasswordOk(password, cnfpsw)) {
                if (!userViewModel.isAuthenticationError()) {
                    userViewModel.getUserMutableLiveData(email, password, false).observe(
                            getViewLifecycleOwner(), result -> {
                                if (result.isSuccess()) {
                                    User user = ((Result.UserResponseSuccess) result).getData();
                                    saveLoginData(email, password, user.getIdToken());
                                    userViewModel.setAuthenticationError(false);
                                    Navigation.findNavController(view).navigate(
                                            R.id.action_signUpFragment_to_loginFragment2);
                                } else {
                                    userViewModel.setAuthenticationError(true);
                                    Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                            getErrorMessage(((Result.Error) result).getMessage()),
                                            Snackbar.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    userViewModel.getUser(email, password, false);
                }
            } else {
                userViewModel.setAuthenticationError(true);
                Snackbar.make(requireActivity().findViewById(android.R.id.content),
                        R.string.check_login_data_message, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private String getErrorMessage(String message) {
        switch(message) {
            case "passwordIsWeak":
                return requireActivity().getString(R.string.error_password);
            case "userCollisionError":
                return requireActivity().getString(R.string.error_user_collision_message);
            default:
                return requireActivity().getString(R.string.unexpected_error);
        }
    }

    private void saveLoginData(String email, String password, String idToken) {
        writeStringData("com.example.karori.save_file.txt", "email:", email);
        ;
        writeStringData("com.example.karori.save_file.txt", "idToken:", idToken);
    }

    private boolean isEmailOk(String email) {
        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Insert a valid email");
            editTextEmail.requestFocus();
            return false;
        }

        return true;
    }

    private boolean isPasswordOk(String password, String cnfpsw) {
        if (password.isEmpty()) {
            editTextPsw.setError("A password is required!");
            editTextPsw.requestFocus();
            return false;
        }
        if (cnfpsw.isEmpty()) {
            editTextPsw.setError("Confirm your password");
            editTextPsw.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            editTextPsw.setError("The password is too short, enter a password that is at least 6 characters long ");
            editTextPsw.requestFocus();
            return false;
        }
        if (cnfpsw.length() < 6) {
            editTextPsw.setError("Check the confirmed paasword");
            editTextPsw.requestFocus();
            return false;
        }
        if (password != null && cnfpsw != null) {
            int i = password.compareTo(cnfpsw);

            if (i != 0) {
                editTextPsw.setError("Password and confirmed password are different!");
                editTextCnfPsw.requestFocus();
                editTextPsw.requestFocus();
                return false;
            }
        }

        return true;
    }

    }




































        /*
        final Button register = view.findViewById(R.id.RegisterUserButton);
        final Button back = view.findViewById(R.id.backbuttonsignup);

        editTextEmail = view.findViewById(R.id.Mail_input_SU);
        editTextPsw= view.findViewById(R.id.Psw_input_su);
        editTextCnfPsw= view.findViewById(R.id.ConfirmPsw_Input_SU);
        editTextName= view.findViewById(R.id.Name_input_SU);

        assert back != null;
        back.setOnClickListener(v->
                Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_loginFragment2));


        register.setOnClickListener(v -> {
            registerUser(view);

        });



    }

    @SuppressLint("SuspiciousIndentation")
    private void registerUser(View view) {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPsw.getText().toString().trim();
        String cnfpassword = editTextCnfPsw.getText().toString().trim();
        String name = editTextName.getText().toString().trim();

        if(name.isEmpty()){
            editTextName.setError("A name is required!");
            editTextName.requestFocus();
            return;
        }

        if(email.isEmpty()){
            editTextEmail.setError("An email is required!");
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

        if(cnfpassword.isEmpty()){
            editTextCnfPsw.setError("Repeat the password!");
            editTextCnfPsw.requestFocus();
            return;
        }

        if(password.length() < 6){
            editTextPsw.setError("The password is too short, enter a password that is at least 6 characters long ");
            editTextPsw.requestFocus();
            return;
        }

        if(password != null && cnfpassword != null){
            int i = password.compareTo(cnfpassword);

            if(i!=0) {
                editTextPsw.setError("Password and confirmed password are different!");
                editTextCnfPsw.requestFocus();
                editTextPsw.requestFocus();

                return;
            }
        }

        mAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.isSuccessful()) {
                            SignInMethodQueryResult result = task.getResult();
                            List<String> signInMethods = result.getSignInMethods();
                            if (!signInMethods.isEmpty()) {
                                Toast.makeText(getContext(), "Email already used", Toast.LENGTH_LONG).show();
                            }
                            else if(signInMethods.isEmpty()){
                                mAuth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                Toast.makeText(getContext(), "User has been registered successfully", Toast.LENGTH_LONG).show();
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    public void run() {
                                                        Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_loginFragment2);
                                                    }
                                                }, 1000);
                                            }
                                        });
                            }
                            else{
                                Toast.makeText(getContext(), "Registration failed! try again.", Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            Toast.makeText(getContext(), "Task not Successful", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }*/
