package com.example.karori.Room;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.karori.R;

public class TestRoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_room);
        MealViewModel mealViewModel = new ViewModelProvider(this).get(MealViewModel.class);


    }

    /*private void addFood(MealViewModel mealViewModel, Date date, String type, Map<String, Object> food) {
        LiveData<List<Meal>> meals = mealViewModel.getMeals();
        meals.observe(this, new androidx.lifecycle.Observer<List<Meal>>() {
            @Override
            public void onChanged(List<Meal> meals) {
                Meal currentMeal = null;
                for (Meal meal : meals) {
                    if (meal.getDate().equals(date) && meal.getType().equals(type)) {
                        currentMeal = meal;
                        break;
                    }
                }
                if (currentMeal == null) {
                    currentMeal = new Meal(date, type);
                    mealViewModel.insert(currentMeal);
                }
                currentMeal.add(food);
                mealViewModel.update(currentMeal);
            }
        });
    }*/



}