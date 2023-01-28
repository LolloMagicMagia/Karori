package com.example.karori.repository.User;

import androidx.lifecycle.MutableLiveData;

import com.example.karori.Models.Result;
import com.example.karori.data.User.User;

public interface IUserRepository {
    MutableLiveData<Result> getUser(String email, String password, boolean isUserRegistered);
    MutableLiveData<Result> getGoogleUser(String idToken);
    MutableLiveData<Result> getUserInformation(int height, int weight, int kilocalorie, int goal);
    MutableLiveData<Result> logout();
    User getLoggedUser();
    void signUp(String email, String password);
    void signIn(String email, String password);
    void saveUserInformation(int height, int weight, int kilocalorie, int goal);
    void signInWithGoogle(String idToken);

}
