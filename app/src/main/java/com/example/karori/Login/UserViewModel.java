package com.example.karori.Login;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.karori.Models.Result;
import com.example.karori.data.User.User;
import com.example.karori.repository.User.IUserRepository;

public class UserViewModel extends ViewModel {
    private final IUserRepository userRepository;
    private MutableLiveData<Result> userMutableLiveData;
    private boolean authenticationError;

    public UserViewModel(IUserRepository userRepository) {
        this.userRepository= userRepository;
        authenticationError =false;
    }

    public MutableLiveData<Result> getUserMutableLiveData(
            String email, String password, boolean isUserRegistered) {
        if (userMutableLiveData == null) {
            getUserData(email, password, isUserRegistered);
       }

        return userMutableLiveData;
    }

    public MutableLiveData<Result> getGoogleUserMutableLiveData(String token) {
            getUserData(token);
            return userMutableLiveData;
    }

    public User getLoggedUser() {
        return userRepository.getLoggedUser();
    }

    public MutableLiveData<Result> logout() {
        if (userMutableLiveData == null) {
            userMutableLiveData = userRepository.logout();
        } else {
            userRepository.logout();
        }

        return userMutableLiveData;
    }

    public void getUser(String email, String password, boolean isUserRegistered) {
        Log.d("logout","giusto7");
        userRepository.getUser(email, password, isUserRegistered);
    }

    public void getUserInformation(String idToken){
        userRepository.getUserInformation(idToken);
    }

    public boolean isAuthenticationError() {
        return authenticationError;
    }

    public void setAuthenticationError(boolean authenticationError) {
        this.authenticationError = authenticationError;
    }

    private void getUserData(String email, String password, boolean isUserRegistered) {
        Log.d("logout","giusto6");
            userMutableLiveData = userRepository.getUser(email, password, isUserRegistered);

    }

    private void getUserData(String token) {
        userMutableLiveData = userRepository.getGoogleUser(token);
    }



}
