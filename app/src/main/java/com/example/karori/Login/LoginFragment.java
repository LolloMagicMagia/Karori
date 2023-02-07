package com.example.karori.Login;

import static android.content.Context.MODE_PRIVATE;

import static com.example.karori.util.SharedPreferencesUtil.writeStringData;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
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
import android.widget.Toast;

import com.example.karori.Models.Result;
import com.example.karori.R;
import com.example.karori.SearchClasses.SearchActivity;
import com.example.karori.data.User.User;
import com.example.karori.menuFragment.SummaryActivity;
import com.example.karori.repository.User.IUserRepository;
import com.example.karori.util.ServiceLocator;
import com.example.karori.util.SharedPreferencesUtil;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class LoginFragment extends Fragment {

    private UserViewModel userViewModel;
    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPsw;

    private static final String TAG = LoginFragment.class.getSimpleName();
    private static boolean USE_NAVIGATION_COMPONENT = true;

    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;

    private ActivityResultLauncher<IntentSenderRequest> activityResultLauncher;
    private ActivityResultContracts.StartIntentSenderForResult startIntentSenderForResult;

    //il bottone di google è dissestante da firebase , quindi va bene lasciarlo qua
    Button provaSummaryActivity;
    Button button_google_login;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    boolean isLoggedIn;



    public LoginFragment() {
    }// Required empty public constructor

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IUserRepository userRepository = ServiceLocator.getInstance().
                getUserRepository(requireActivity().getApplication());
        userViewModel = new ViewModelProvider(
                requireActivity(),
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);

        oneTapClient = Identity.getSignInClient(requireActivity());
        signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.Web_id_client))
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                        .setAutoSelectEnabled(true)
                        .build();

        startIntentSenderForResult = new ActivityResultContracts.StartIntentSenderForResult();

        activityResultLauncher = registerForActivityResult(startIntentSenderForResult, activityResult -> {
            if (activityResult.getResultCode() == Activity.RESULT_OK) {
                Log.d(TAG, "result.getResultCode() == Activity.RESULT_OK");
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(activityResult.getData());
                    String idToken = credential.getGoogleIdToken();
                    if (idToken != null) {
                        // Got an ID token from Google. Use it to authenticate with Firebase.
                        userViewModel.getGoogleUserMutableLiveData(idToken).observe(getViewLifecycleOwner(), authenticationResult -> {
                            if (authenticationResult.isSuccess()) {
                                User user = ((Result.UserResponseSuccess) authenticationResult).getData();
                                saveLoginData(user.getEmail(), null, user.getIdToken());
                                userViewModel.setAuthenticationError(false);
                                retrieveUserInformationAndStartActivity(user, R.id.action_loginFragment_to_summaryActivity);
                            } else {
                                userViewModel.setAuthenticationError(true);
                                Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                        getErrorMessage(((Result.Error) authenticationResult).getMessage()),
                                        Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (ApiException e) {
                    Snackbar.make(requireActivity().findViewById(android.R.id.content),
                            requireActivity().getString(R.string.unexpected_error),
                            Snackbar.LENGTH_SHORT).show();
                }
            }
        });



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //lascia questo pezzo , mi serve per il debug della mia parte
        View view=inflater.inflate(R.layout.fragment_login, container, false);
        provaSummaryActivity=(Button)view.findViewById(R.id.provaSummaryActivity);
        provaSummaryActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SummaryActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (userViewModel.getLoggedUser() == null) {

            Log.d("Tag", "ciao login normale");

            editTextEmail = view.findViewById(R.id.usernameEditText);
            editTextPsw = view.findViewById(R.id.PswEditText);

            final Button buttonLogin = view.findViewById(R.id.buttonLogin);
            final Button buttonGoogleLogin = view.findViewById(R.id.button_google_login);
            final Button buttonRegistration = view.findViewById(R.id.sign_up_button);
            final Button buttonforgotpsw = view.findViewById(R.id.buttonForgotPsw);

            buttonforgotpsw.setOnClickListener(v->{
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_forgot_Password_Fragment);
            });

            buttonLogin.setOnClickListener(v -> {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPsw.getText().toString().trim();

                if (isEmailOk(email) & isPasswordOk(password)) {
                    if (!userViewModel.isAuthenticationError()) {

                        userViewModel.getUserMutableLiveData(email, password, true).observe(
                                getViewLifecycleOwner(), result -> {
                                    if (result.isSuccess()) {
                                        User user = ((Result.UserResponseSuccess) result).getData();
                                        saveLoginData(email, password, user.getIdToken());
                                        userViewModel.setAuthenticationError(false);
                                        retrieveUserInformationAndStartActivity(user, R.id.action_loginFragment_to_summaryActivity);
                                    } else {
                                        userViewModel.setAuthenticationError(true);
                                        Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                                getErrorMessage(((Result.Error) result).getMessage()),
                                                Snackbar.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        userViewModel.getUser(email, password, true);
                    }
                } else {
                    Snackbar.make(requireActivity().findViewById(android.R.id.content),
                            "Check the the data you inserted", Snackbar.LENGTH_SHORT).show();
                }
            });

            buttonRegistration.setOnClickListener(v ->
                    Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_signUpFragment));


            gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            gsc= GoogleSignIn.getClient(getContext(), gso);

            GoogleSignInAccount gAccount=GoogleSignIn.getLastSignedInAccount(getContext());

            //MI SONO GIA' LOGGATO CON GOOGLE
            if(gAccount != null ){
                Log.d("Tag", gAccount.getEmail());
                //SE MI SONO GIA' loggato allora l'email è già su firebase con i relativi valori
                //e vado a settare i valori in profilo come con il login normale



                //modifica questo
                Intent intent=new Intent(getActivity(),SummaryActivity.class);
                startActivity(intent);
                getActivity().finish();
            }

            //se non lo sono invece ed è la prima volta
            ActivityResultLauncher<Intent> activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if(result.getResultCode()== Activity.RESULT_OK){
                                Intent data = result.getData();
                                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                                try{
                                    /*vado a vedere tramite il metodo "GoogleSignIn.getLastSignedInAccount(getContext()).getEmail()"
                                    * se l'email è già salvata su firebase, se lo è allora prendo quei valori
                                    * se non è salvata apre un altro fragment che richiede i parametri per la formula
                                    * per poi andare a salvare su firebase tutto questo*/
                                    task.getResult(ApiException.class);
                                    Log.d("Tag", GoogleSignIn.getLastSignedInAccount(getContext()).getEmail());
                                    Intent intent = new Intent(getActivity(),SummaryActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }catch(ApiException e){
                                    Toast.makeText(getContext(),"Something went wrong",Toast.LENGTH_LONG);
                                }

                            }
                        }
                    });

            buttonGoogleLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent signInIntent=gsc.getSignInIntent();
                    activityResultLauncher.launch(signInIntent);

                }
            });



        } else {
            USE_NAVIGATION_COMPONENT = false;
            startActivityBasedOnCondition(SummaryActivity.class,
                    R.id.action_loginFragment_to_summaryActivity);
        }



    }
    public void onResume() {
        super.onResume();
        userViewModel.setAuthenticationError(false);
    }


    private void saveLoginData(String email, String password, String idToken) {
        writeStringData("com.example.karori.save_file.txt", "email:", email);
        ;
        writeStringData("com.example.karori.save_file.txt", "idToken:", idToken);

    }

    //TODO : da fare con ghero per passare le info dell 'utente loggato alla pagina principale!!
    private void retrieveUserInformationAndStartActivity(User user, int destination) {
                    USE_NAVIGATION_COMPONENT = false;
                    startActivityBasedOnCondition(SummaryActivity.class, destination);
                }



    private String getErrorMessage(String errorType) {
        switch (errorType) {
            case "invalidCredentials":
                return requireActivity().getString(R.string.error_login_password_message);
            case "invalidUserError":
                return requireActivity().getString(R.string.error_login_user_message);
            default:
                return requireActivity().getString(R.string.unexpected_error);
        }
    }

    private void startActivityBasedOnCondition(Class<?> destinationActivity, int destination) {
        if (USE_NAVIGATION_COMPONENT) {
            Navigation.findNavController(requireView()).navigate(destination);
        } else {
            Intent intent = new Intent(requireContext(), destinationActivity);
            startActivity(intent);
        }
        requireActivity().finish();
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

    private boolean isPasswordOk(String password) {
        if (password.isEmpty()) {
            editTextPsw.setError("A password is required!");
            editTextPsw.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            editTextPsw.setError("The password is too short, enter a password that is at least 6 characters long ");
            editTextPsw.requestFocus();
            return false;
        }
        return true;
    }

}

    /*private void userLogin() {
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


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    editor.putBoolean("isLoggedIn",true);
                    editor.apply();
                    Intent myInt=new Intent(getContext(), SummaryActivity.class);
                    startActivity(myInt);
                }else{
                    Toast.makeText(getContext(), "Login failed! try again.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
*/

// onviewcreated :
/*
        final Button buttonFrgPsw =(Button)  view.findViewById(R.id.buttonForgotPsw);
        final Button buttonSignUp = (Button) view.findViewById(R.id.sign_up_button);

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





// on createview
/* mAuth=FirebaseAuth.getInstance();
        //nome del file dove verranno salvati i dani
        prefs = getActivity().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        editor = prefs.edit();
        //prende la var isLoggedIn e se non esiste restituisce false
        isLoggedIn = prefs.getBoolean("isLoggedIn", false);
        */

/*  buttonGoogleLogin.setOnClickListener(v -> oneTapClient.beginSignIn(signInRequest)
                    .addOnSuccessListener(requireActivity(), new OnSuccessListener<BeginSignInResult>() {
                        @Override
                        public void onSuccess(BeginSignInResult result) {
                            Log.d(TAG, "onSuccess from oneTapClient.beginSignIn(BeginSignInRequest)");
                            IntentSenderRequest intentSenderRequest =
                                    new IntentSenderRequest.Builder(result.getPendingIntent()).build();
                            activityResultLauncher.launch(intentSenderRequest);
                        }
                    })
                    .addOnFailureListener(requireActivity(), new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // No saved credentials found. Launch the One Tap sign-up flow, or
                            // do nothing and continue presenting the signed-out UI.
                            Log.d(TAG, e.getLocalizedMessage());

                            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                    requireActivity().getString(R.string.error_no_google_account_found_message),
                                    Snackbar.LENGTH_SHORT).show();
                        }
                    }));*/