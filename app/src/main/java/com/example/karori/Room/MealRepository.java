package com.example.karori.Room;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MealRepository {
    private MealDao mealDao;
    private LiveData<List<Meal>> meals;

    public MealRepository(Application application) {
        MealDatabase db = MealDatabase.getDatabase(application);
        mealDao = db.mealDao();
        meals = mealDao.getAllMeals();
    }

    public LiveData<List<Meal>> getMeals() {
        return meals;
    }

    public void insert(Meal meal) {
        new InsertMealAsyncTask(mealDao).execute(meal);
    }

    public void update(Meal meal) {
        new UpdateMealAsyncTask(mealDao).execute(meal);
    }

    public void delete(Meal meal) {
        new DeleteMealAsyncTask(mealDao).execute(meal);
    }

    private static class InsertMealAsyncTask extends AsyncTask<Meal, Void, Void> {
        private MealDao mealDao;

        InsertMealAsyncTask(MealDao dao) {
            mealDao = dao;
        }

        @Override
        protected Void doInBackground(Meal... meals) {
            mealDao.insert(meals[0]);
            return null;
        }
    }

    private static class UpdateMealAsyncTask extends AsyncTask<Meal, Void, Void> {
        private MealDao mealDao;

        UpdateMealAsyncTask(MealDao dao) {
            mealDao = dao;
        }

        @Override
        protected Void doInBackground(Meal... meals) {
            mealDao.update(meals[0]);
            return null;
        }
    }

    private static class DeleteMealAsyncTask extends AsyncTask<Meal, Void, Void> {
        private MealDao mealDao;

        DeleteMealAsyncTask(MealDao dao) {
            mealDao = dao;
        }

        @Override
        protected Void doInBackground(Meal... meals) {
            mealDao.delete(meals[0]);
            return null;
        }
    }
}
