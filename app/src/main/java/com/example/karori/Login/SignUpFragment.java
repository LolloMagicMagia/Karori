package com.example.karori.Login;


import static com.example.karori.util.SharedPreferencesUtil.writeFloatData;
import static com.example.karori.util.SharedPreferencesUtil.writeIntData;
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
    private EditText editTextAge;
    private EditText editTextWeight;
    private EditText editTextHeight;



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
        editTextAge = view.findViewById(R.id.ageTextView);
        editTextHeight = view.findViewById(R.id.heightTextView);
        editTextWeight = view.findViewById(R.id.weightTextView);

        final Button buttonRegistration = view.findViewById(R.id.RegisterUserButton);
        final Button buttonback = view.findViewById(R.id.backbuttonsignup);

        buttonback.setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_loginFragment2));

        buttonRegistration.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPsw.getText().toString().trim();
            String cnfpsw =editTextCnfPsw.getText().toString().trim();
            String ageSt = editTextAge.getText().toString().trim();
            String heightSt = editTextHeight.getText().toString().trim();
            String weightSt = editTextWeight.getText().toString().trim();
            int age = 0;
            float weight = 0;
            int height = 0;
            float kilocalorie = 0;



            if (isEmailOk(email) & isPasswordOk(password, cnfpsw) & numberOk(ageSt,heightSt,weightSt)) {
                weight = Float.parseFloat(weightSt);
                height = Integer.parseInt(heightSt);
                age = Integer.parseInt(ageSt);

                if (!userViewModel.isAuthenticationError()) {

                    int finalAge = age;
                    float finalWeight = weight;
                    int finalHeight = height;
                    float finalKilocalorie = (float) (1.2*(66+(13.7* weight)+(5+height)-(6.8+age)));

                    userViewModel.getUserMutableLiveData(email, password, false).observe(
                            getViewLifecycleOwner(), result -> {
                                if (result.isSuccess()) {
                                    User user = ((Result.UserResponseSuccess) result).getData();
                                    saveLoginData(email, password, user.getIdToken(), finalAge, finalWeight, finalHeight, finalKilocalorie);
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

    private void saveLoginData(String email, String password, String idToken, int age, float weight, int height, float kilocalorie) {
        writeStringData("com.example.karori.save_file.txt", "email:", email);

        writeStringData("com.example.karori.save_file.txt", "idToken:", idToken);

        writeIntData("com.example.karori.save_file.txt", "age: ", age);

        writeFloatData("com.example.karori.save_file.txt", "weight: ", weight);

        writeIntData("com.example.karori.save_file.txt", "height: ", height);

        writeFloatData("com.example.karori.save_file.txt", "kilocalorie: ", kilocalorie);



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

    private boolean numberOk(String age, String weight, String height){
        boolean check = true;
        int i= 0;
        if (age.isEmpty()){
            editTextAge.setError("The age is required");
            editTextAge.requestFocus();
            check = false;
        }
        else if (isNumeric(age)){
            check = check & true;
        }else{
            editTextAge.setError("Please insert a number!");
            editTextAge.requestFocus();
            check = false;
        }try {
            i = Integer.parseInt(age);
        } catch (NumberFormatException e){
            editTextAge.setError("the age must be int");
            editTextAge.requestFocus();
            check = false;
        }

        if (weight.isEmpty()){
            editTextWeight.setError("The weight is required");
            editTextWeight.requestFocus();
            check = false;
        }
        else if (isNumeric(weight)){
            check = check & true;
        }else{
            editTextWeight.setError("Please insert a number!");
            editTextWeight.requestFocus();
            check = false;
        }


        if (height.isEmpty()){
            editTextHeight.setError("The age is required");
            editTextHeight.requestFocus();
            check = false;
        }
        else if (isNumeric(height)){
            check = check & true;
        }else {
            editTextHeight.setError("Please insert a number!");
            editTextHeight.requestFocus();
            check = false;
        }
        try {
            i = Integer.parseInt(age);
            check = check & true;
        } catch (NumberFormatException e){
            editTextAge.setError("the height must be int");
            editTextAge.requestFocus();
            check = false;
    }
        return  check;
    }


    public boolean isNumeric(String str) {
        return str.matches("\\d+(\\.\\d+)?");
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
