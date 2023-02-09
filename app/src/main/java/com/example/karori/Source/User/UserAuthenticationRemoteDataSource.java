package com.example.karori.Source.User;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.karori.data.User.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserAuthenticationRemoteDataSource extends BaseUserAuthenticationRemoteDataSource {

    private final FirebaseAuth firebaseAuth;
    private static final String TAG = UserAuthenticationRemoteDataSource.class.getSimpleName();


    public UserAuthenticationRemoteDataSource() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public User getLoggedUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null ){

            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            String idToken = firebaseUser.getUid();
            String name = firebaseUser.getDisplayName();
            String email = firebaseUser.getEmail();

            User user = new User(name,email, idToken);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("users").child(idToken);

            /*ValueEventListener userListener =(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User userLogged = snapshot.getValue(User.class);
                            user.setAge(userLogged.getAge());
                            user.setGoal(userLogged.getGoal());
                            user.setKilocalorie(userLogged.getKilocalorie());
                            user.setWeight(userLogged.getWeight());
                            user.setHeight(userLogged.getHeight());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            user.setAge(35);
                        }

                    }
            );
            databaseReference.addValueEventListener(userListener);

            */
            return user;
            };
            Log.d(TAG, "non entra da nessuna parte");
            return null;
    }

    @Override
    public void logout() {
        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null ){
                    firebaseAuth.removeAuthStateListener(this);
                    Log.d(TAG, "logout dell'utente eseguito");
                    userResponseCallback.onSuccessLogout();
                }
            }
        };
        firebaseAuth.addAuthStateListener(authStateListener);
        firebaseAuth.signOut();

    }

    @Override
    public void signUp(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser == null) {
                    userResponseCallback.onFailureFromAuthentication(getErrorMessage(task.getException()));
                }
                else {
                    userResponseCallback.onSuccessFromAuthentication(new User(
                            firebaseUser.getDisplayName(), firebaseUser.getEmail(), firebaseUser.getUid()));
                }
            }
            else {
                    userResponseCallback.onFailureFromAuthentication(getErrorMessage(task.getException()));
            }
        });

    }

    @Override
    public void signIn(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser == null){
                    userResponseCallback.onFailureFromAuthentication(getErrorMessage(task.getException()));
                }
                else{
                    userResponseCallback.onSuccessFromAuthentication(new User(
                            firebaseUser.getDisplayName(), firebaseUser.getEmail(), firebaseUser.getUid()));
                }
            }
            else{
                userResponseCallback.onFailureFromAuthentication(getErrorMessage(task.getException()));
            }
        });

    }

    @Override
    public void signInWithGoogle(String idToken) {
        if(idToken != null){
            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
            firebaseAuth.signInWithCredential(firebaseCredential).addOnCompleteListener(task -> {
               if(task.isSuccessful()){
                   Log.d(TAG, "signInWithCredential() isSuccessful");
                   FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                   if(firebaseUser == null){
                       userResponseCallback.onFailureFromAuthentication(getErrorMessage(task.getException()));
                   }
                   else{
                       userResponseCallback.onSuccessFromAuthentication(new User(
                               firebaseUser.getDisplayName(), firebaseUser.getEmail(), firebaseUser.getUid()));
                   }
               }
               else {
                   Log.w(TAG,"signInWithCredential() failure", task.getException());
               userResponseCallback.onFailureFromAuthentication(getErrorMessage(task.getException()));
               }
            });
        }
    }

    private String getErrorMessage(Exception exception) {
        if (exception instanceof FirebaseAuthWeakPasswordException) {
            return "WeakPassword";
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            return "InvalidCredentials";
        } else if (exception instanceof FirebaseAuthInvalidUserException) {
            return "InvalidUser";
        } else if (exception instanceof FirebaseAuthUserCollisionException) {
            return "UserCollision";
        }
        return "Errore inatteso";
    }


}

