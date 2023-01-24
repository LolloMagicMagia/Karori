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
import com.example.karori.ui.SummaryActivity;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentProfilo extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    private static final String KEY_INDEX1="imageSave";
    Uri uri;
    private Button button;
    Uri savedUri;
    CircleImageView profile;
    CircleImageView changeImage;
    Button cambioObiettivi;
    CardView card;
    Button signUp;
    GoogleSignInOptions gOptions;
    GoogleSignInClient gClient;

    private User user;

    //aggiungere le icone alle varie cardview

    ActivityResultLauncher<Intent> launcher=
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),(ActivityResult result)->{
                if(result.getResultCode()==RESULT_OK){
                    uri=result.getData().getData();
                    profile.setImageURI(uri);
                    // Use the uri to load the image
                }else if(result.getResultCode()== ImagePicker.RESULT_ERROR){
                    // Use ImagePicker.Companion.getError(result.getData()) to show an error
                }
            });

    public FragmentProfilo() {

    }

    public static FragmentProfilo newInstance(String param1, String param2) {
        FragmentProfilo fragment = new FragmentProfilo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(KEY_INDEX1, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            //dovrò farlo con ROOM
            savedUri = savedInstanceState.getParcelable(KEY_INDEX1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profilo, container, false);
        profile=(CircleImageView)view.findViewById(R.id.celebrityImage);
        cambioObiettivi=(Button)view.findViewById(R.id.cambioObiettivi);

        ///google
        signUp=(Button)view.findViewById(R.id.signUp);

        gOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gClient= GoogleSignIn.getClient(getContext(), gOptions);

        GoogleSignInAccount gAccount=GoogleSignIn.getLastSignedInAccount(getContext());
        signUp.setOnClickListener(new View.OnClickListener() {
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
        ////


        //per ora ho fatto cliccabile solo una card
        card=(CardView) view.findViewById(R.id.cardViewPeso);

        //ROOM
        if(savedUri!=null){
            profile.setImageURI(savedUri);
        }

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launcher.launch(ImagePicker.with(getActivity())
                        .crop()
                        .maxResultSize(1080,1080,true)
                        .galleryOnly()
                        .createIntent());
            }
        });

        cambioObiettivi.setOnClickListener(view1 ->
                Navigation.findNavController(view).navigate(R.id.action_fragmentProfilo_to_fragmentIdeal));


        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG","ciao");
            }
        });


        return view;
    }
    //Salvo l'immagine in un bundle. Però come faccio dall'uri a salvarlo nel database?
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(KEY_INDEX1, uri);
    }

}
