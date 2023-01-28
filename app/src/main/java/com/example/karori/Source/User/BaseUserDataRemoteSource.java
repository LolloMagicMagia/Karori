package com.example.karori.Source.User;

import com.example.karori.data.User.User;
import com.example.karori.repository.User.UserResponseCallback;

public abstract class BaseUserDataRemoteSource {
    protected UserResponseCallback userResponseCallback;

    public void setUserResponseCallback(UserResponseCallback userResponseCallback) {
        this.userResponseCallback = userResponseCallback;
    }
    public abstract void saveUserData(User user);
}
