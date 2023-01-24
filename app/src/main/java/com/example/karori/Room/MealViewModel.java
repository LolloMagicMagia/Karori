package com.example.karori.Room;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MealViewModel extends AndroidViewModel {
    private MealRepository mealRepository;
    private LiveData<List<Meal>> meals;

    public MealViewModel(@NonNull Application application) {
        super(application);
        mealRepository = new MealRepository(application);
        meals = mealRepository.getMeals();
    }

    public LiveData<List<Meal>> getMeals() {
        return meals;
    }

    public void insert(Meal meal) {
        mealRepository.insert(meal);
    }

    public void update(Meal meal) {
        mealRepository.update(meal);
    }

    public void delete(Meal meal) {
        mealRepository.delete(meal);
    }
}
