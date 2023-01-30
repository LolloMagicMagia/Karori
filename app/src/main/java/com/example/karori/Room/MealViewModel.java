package com.example.karori.Room;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MealViewModel extends AndroidViewModel {
    private MealRepository mealRepository;
    private final LiveData<List<Meal>> meals;

    public MealViewModel(@NonNull Application application) {
        super(application);
        mealRepository = new MealRepository(application);
        meals = mealRepository.getAllMeals();
    }

    public LiveData<Meal> getMeal(int id) {
        return mealRepository.getMeal(id);
    }

    public Meal getMeal1(int id) {
        return mealRepository.getMeal1(id);
    }

    public void insert(Meal meal) {
        mealRepository.insert(meal);
    }

    public void delete(int person) {
        mealRepository.delete(person);
    }

    public LiveData<List<Meal>> getAll() {
        return meals;
    }

    public void update(Meal meal) {mealRepository.update(meal);}
}
