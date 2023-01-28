package com.example.karori.util;

import android.app.Application;

import com.example.karori.Source.User.BaseUserAuthenticationRemoteDataSource;
import com.example.karori.Source.User.BaseUserDataRemoteSource;
import com.example.karori.Source.User.UserAuthenticationRemoteDataSource;
import com.example.karori.Source.User.UserDataRemoteDataSource;
import com.example.karori.repository.User.IUserRepository;
import com.example.karori.repository.User.UserRepository;

public class ServiceLocator {
    private static volatile ServiceLocator INSTANCE = null;

    private ServiceLocator() {}

    public static ServiceLocator getInstance() {
        if (INSTANCE == null) {
            synchronized(ServiceLocator.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ServiceLocator();
                }
            }
        }
        return INSTANCE;
    }

    public IUserRepository getUserRepository(Application application) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(application);

        BaseUserAuthenticationRemoteDataSource userRemoteAuthenticationDataSource =
                new UserAuthenticationRemoteDataSource();

        BaseUserDataRemoteSource userDataRemoteDataSource =
                new UserDataRemoteDataSource(sharedPreferencesUtil);


        return new UserRepository(userRemoteAuthenticationDataSource,
                userDataRemoteDataSource);
    }
}

