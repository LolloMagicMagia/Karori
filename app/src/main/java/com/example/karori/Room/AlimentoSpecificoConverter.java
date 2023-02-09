package com.example.karori.Room;

import androidx.room.TypeConverter;

import com.example.karori.menuFragment.AlimentoSpecifico;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AlimentoSpecificoConverter {

    @TypeConverter
    public static ArrayList<AlimentoSpecifico> fromString(String value) {
        Type listType = new TypeToken<ArrayList<AlimentoSpecifico>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<AlimentoSpecifico> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}
