package com.example.karori.Login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.karori.R;
import com.example.karori.Source.User.UserDataRemoteDataSource;

public class google_dataUserFragment extends Fragment {

    private UserViewModel userViewModel;
    private EditText editTextGAge;
    private EditText editTextWeight;
    private EditText editTextHeight;
    private UserDataRemoteDataSource userDataRemoteDataSource;
    private EditText goalGTextView;


    public google_dataUserFragment() {
        // Required empty public constructor
    }

    public static google_dataUserFragment newInstance(String param1, String param2) {
        google_dataUserFragment fragment = new google_dataUserFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_google_data_user, container, false);
    }
}