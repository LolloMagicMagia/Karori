package com.example.karori.Source.User;

import com.example.karori.data.User.User;

public abstract class UserCallback {
    public void onUserRetrieved(User user) {


    }

    public abstract void onSuccess(User user);
    public abstract void onFailure(Exception e);
}

