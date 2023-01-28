package com.example.karori.Source.User;

import com.example.karori.data.User.User;
import com.example.karori.repository.User.UserResponseCallback;

public abstract class BaseUserAuthenticationRemoteDataSource{
    protected UserResponseCallback userResponseCallback;


    public void setUserResponseCallback(UserResponseCallback userResponseCallBack){
        this.userResponseCallback = userResponseCallBack;
    }

    public abstract User getLoggedUser();
    public abstract void logout();
    public abstract void signUp(String email, String password);
    public abstract void signIn(String email, String password);
    public abstract void signInWithGoogle(String idToken);
    }

