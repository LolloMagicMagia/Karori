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

    public LiveData<Meal> getMeal(int id) {
        return mealDao.getMeal(id);
    }

    public Meal getMeal1(int id) {
        return mealDao.getMeal1(id);
    }

    public void insert(Meal meal) {
        new InsertAsyncTask(mealDao).execute(meal);
    }

    public void delete(int meal) {
        new DeleteAsyncTask(mealDao).execute(meal);
    }

    public void update(Meal meal) {
        new UpdateAsyncTask(mealDao).execute(meal);
    }

    public LiveData<List<Meal>> getAllMeals() {
        return meals;
    }

    private static class InsertAsyncTask extends AsyncTask<Meal, Void, Void> {

        private MealDao mealDao;

        InsertAsyncTask(MealDao dao) {
            mealDao = dao;
        }

        @Override
        protected Void doInBackground(final Meal... params) {
            mealDao.insert(params[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Meal, Void, Void> {

        private MealDao mealDao;

        DeleteAsyncTask(MealDao dao) {
            mealDao = dao;
        }

        @Override
        protected Void doInBackground(final Meal... params) {
            mealDao.delete(params[0]);
            return null;
        }

        public void execute(int meal) {
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Meal, Void, Void> {

        private MealDao mealDao;

        UpdateAsyncTask(MealDao dao) {
            mealDao = dao;
        }

        @Override
        protected Void doInBackground(final Meal... params) {
            mealDao.update(params[0]);
            return null;
        }

        public void execute(int meal) {
        }
    }
}
