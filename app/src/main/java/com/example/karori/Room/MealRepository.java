package com.example.karori.Room;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    public LiveData<Meal> getMealFromDate(LocalDate date, String type) {
        Log.d("Date", String.valueOf(date));
        Long dateConv = DateConverter.localDateToTimestamp(date);
        return mealDao.getMealFromDate(dateConv, type);
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

    public LiveData<List<Meal>> getDayMeals(LocalDate date) { return mealDao.getDayMeals(date); }

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

///_-------------------------------------------------------------------------------------



    /*public void updateMeal(LocalDate date, String type, Map<String, Object> food){
        Long dateConv = DateConverter.localDateToTimestamp(date);
        Meal meal = mealDao.getMealFromDate1(dateConv, type);

        if (meal != null) {
            meal.add(food);
            new UpdateAsyncTask(mealDao).execute(meal);
        } else {
            try {
                Log.d("GetMealFromDate", "MEAL IS NULL");
                LocalDate currentTime = LocalDate.now();
                Meal meal1 = new Meal(currentTime, type);
                meal1.add(food);
                new InsertAsyncTask(mealDao).execute(meal);
            } catch (Exception e) {
                Log.d("Tag", "meal1 è già presente dentro il database");
            }
        }
    }*/

    public void updateMeal(LocalDate date, String type, Map<String, Object> food){
        new UpdateMealAsyncTask(mealDao).execute(date, type, food);
    }

    private static class UpdateMealAsyncTask extends AsyncTask<Object, Void, Void> {
        private MealDao mealDao;
        UpdateMealAsyncTask(MealDao dao) {
            mealDao = dao;
        }

        @Override
        protected Void doInBackground(final Object... params) {
            LocalDate date = (LocalDate) params[0];
            String type = (String) params[1];
            Map<String, Object> food = (Map<String, Object>) params[2];

            Long dateConv = DateConverter.localDateToTimestamp(date);
            Meal meal = mealDao.getMealFromDate1(dateConv, type);

            if (meal != null) {
                meal.add(food);
                mealDao.update(meal);
            } else {
                try {
                    Log.d("GetMealFromDate", "MEAL IS NULL");
                    LocalDate currentTime = LocalDate.now();
                    Meal meal1 = new Meal(currentTime, type);
                    meal1.add(food);
                    mealDao.insert(meal1);
                } catch (Exception e) {
                    Log.d("Tag", "meal1 è già presente dentro il database");
                }
            }
            return null;
        }
    }



}
