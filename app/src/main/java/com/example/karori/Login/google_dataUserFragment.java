package com.example.karori.Login;

import static com.example.karori.util.SharedPreferencesUtil.writeFloatData;
import static com.example.karori.util.SharedPreferencesUtil.writeIntData;
import static com.example.karori.util.SharedPreferencesUtil.writeStringData;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karori.R;
import com.example.karori.Source.User.UserDataRemoteDataSource;
import com.example.karori.data.User.User;
import com.example.karori.repository.User.IUserRepository;
import com.example.karori.util.ServiceLocator;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class google_dataUserFragment extends Fragment {

    private UserViewModel userViewModel;
    private EditText editTextAge;
    private EditText editTextWeight;
    private EditText editTextHeight;
    private UserDataRemoteDataSource userDataRemoteDataSource;
    private EditText editTextGoal;
    private DatabaseReference databaseReference;

    private static final String TAG = LoginFragment.class.getSimpleName();



    public google_dataUserFragment() {
        // Required empty public constructor
    }

    public static google_dataUserFragment newInstance(String param1, String param2) {
        google_dataUserFragment fragment = new google_dataUserFragment();
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




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_google_data_user, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

            databaseReference = FirebaseDatabase.getInstance().getReference();

            final Button googlebutton = view.findViewById(R.id.informationGoogleButton);

            googlebutton.setOnClickListener(v ->{
                editTextAge=view.findViewById(R.id.ageGTextView);
                editTextHeight=view.findViewById(R.id.heightGTextView);
                editTextWeight= view.findViewById(R.id.weightGTextView);
                editTextGoal = view.findViewById(R.id.ageGTextView);

                String age =editTextAge.getText().toString().trim();
                String height = editTextHeight.getText().toString().trim();
                String weight = editTextWeight.getText().toString().trim();
                String goal = editTextGoal.getText().toString().trim();

                User user = new User();

                if (numberOk(age, weight, height, goal)){
                    user.setAge(Integer.parseInt(age));
                    user.setEmail("");
                    user.setHeight(Integer.parseInt(height));
                    user.setWeight(Integer.parseInt(weight));
                    user.setGoal(Integer.parseInt(goal));
                    user.setKilocalorie((int) (1.2*(66+(13.7* (user.getGoal()/10))+(5*user.getHeight())-(6.8*user.getAge()))));
                }

                writeNewUser(user);

            });

            Navigation.findNavController(view).navigate(
                    R.id.action_google_dataUserFragment_to_loginFragment);
    }

    public void writeNewUser(User user) {
        String userId = user.getIdToken();
        databaseReference.child("users").child(userId).setValue(user);
    }


    private boolean numberOk(String age, String weight, String height, String goal){
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
            i = Integer.parseInt(height);
            check = check & true;
        } catch (NumberFormatException e){
            editTextAge.setError("the height must be int");
            editTextAge.requestFocus();
            check = false;
        }
        if (goal.isEmpty()){
            editTextGoal.setError("The weight goal is required");
            editTextGoal.requestFocus();
            check = false;
        }
        else if (isNumeric(goal)){
            check = check & true;
        }else{
            editTextGoal.setError("Please insert a number!");
            editTextGoal.requestFocus();
            check = false;
        }
        return  check;
    }


    public boolean isNumeric(String str) {
        return str.matches("\\d+(\\.\\d+)?");
    }

}

