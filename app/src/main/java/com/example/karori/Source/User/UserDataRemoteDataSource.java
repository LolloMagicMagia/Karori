package com.example.karori.Source.User;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.karori.data.User.User;
import com.example.karori.util.SharedPreferencesUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDataRemoteDataSource extends BaseUserDataRemoteSource{

    private static final String TAG = UserDataRemoteDataSource.class.getSimpleName();

    private final DatabaseReference databaseReference;
    private final SharedPreferencesUtil sharedPreferencesUtil;

    public UserDataRemoteDataSource(SharedPreferencesUtil sharedPreferencesUtil) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://karori-b3226-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference().getRef();
        this.sharedPreferencesUtil = sharedPreferencesUtil;
    }
    @Override
    public void saveUserData(User user) {
        databaseReference.child("users").child(user.getIdToken()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Log.d(TAG, "user already present in firebase rdb");
                    userResponseCallback.onSuccessFromRemoteDatabase(user);
                }
                else{
                    Log.d(TAG, "user is not present in firebase rdb");
                    databaseReference.child("users").child(user.getIdToken()).setValue(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                userResponseCallback.onSuccessFromRemoteDatabase(user);
                            }
                        })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    userResponseCallback.onFailureFromRemoteDatabase(e.getLocalizedMessage());
                                }
                            });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                userResponseCallback.onFailureFromRemoteDatabase(error.getMessage());
            }
        });
    }

    public void SaveinfoUser(String idToken, float weight, int height, float kilocalorie, int age, float goal){
        databaseReference.child("users").child(idToken).child("age").setValue(age);
        databaseReference.child("users").child(idToken).child("goal").setValue(goal);
        databaseReference.child("users").child(idToken).child("height").setValue(height);
        databaseReference.child("users").child(idToken).child("kilocalorie").setValue(kilocalorie);
        databaseReference.child("users").child(idToken).child("weight").setValue(weight);

    }
}
