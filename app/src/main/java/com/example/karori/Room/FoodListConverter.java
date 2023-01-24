package com.example.karori.Room;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class FoodListConverter {
    @TypeConverter
    public String fromFoodList(Map<Integer, String> foodList) {
        Gson gson = new Gson();
        String json = gson.toJson(foodList);
        return json;
    }

    @TypeConverter
    public Map<Integer, String> toFoodList(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<Integer, String>>(){}.getType();
        Map<Integer, String> foodList = gson.fromJson(json, type);
        return foodList;
    }
}

