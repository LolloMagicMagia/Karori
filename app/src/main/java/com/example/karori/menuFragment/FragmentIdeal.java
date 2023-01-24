package com.example.karori.menuFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.karori.R;
import com.example.karori.data.model.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentIdeal extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private User user;

    private TextView textWeight;
    private TextView textHeight;
    private NumberPicker numberPickerHeight;
    private NumberPicker numberPickerWeight;
    private int valueHeight;
    private int valueWeight;

    private CircleImageView returnFromIdeal;

    public FragmentIdeal() {

    }

    public static FragmentProfilo newInstance(String param1, String param2) {
        FragmentProfilo fragment = new FragmentProfilo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //se è la prima volta che si avvia allora vai a prenderlo dal database non dal bundle. Questo
        //è di prova per vedere se mantiene il dato.
        if (savedInstanceState != null) {
            valueHeight = savedInstanceState.getInt(ARG_PARAM1, 0);
            valueWeight = savedInstanceState.getInt(ARG_PARAM2, 0);
        } else {
            valueHeight = 170;
            valueWeight = 80;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ideal, container, false);
        textWeight = (TextView) view.findViewById(R.id.weightTextView);
        textHeight = (TextView) view.findViewById(R.id.heightTextView);
        numberPickerHeight = (NumberPicker) view.findViewById(R.id.height);
        numberPickerWeight = (NumberPicker) view.findViewById(R.id.weight);
        returnFromIdeal=(CircleImageView)view.findViewById(R.id.returnFromIdeal);

        numberPickerWeight.setMaxValue(130);
        numberPickerWeight.setMinValue(50);

        numberPickerHeight.setMaxValue(200);
        numberPickerHeight.setMinValue(150);

        numberPickerWeight.setValue(valueWeight);
        numberPickerHeight.setValue(valueHeight);

        numberPickerHeight.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                valueHeight = newValue;
            }
        });

        numberPickerWeight.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                valueWeight = newValue;
            }
        });

        returnFromIdeal.setOnClickListener(view1 ->
                Navigation.findNavController(view).navigate(R.id.action_fragmentIdeal_to_fragmentProfilo));

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(ARG_PARAM1, valueHeight);
        savedInstanceState.putInt(ARG_PARAM2, valueWeight);
    }


}
