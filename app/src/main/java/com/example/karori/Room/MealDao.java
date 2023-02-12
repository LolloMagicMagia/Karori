package com.example.karori.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.time.LocalDate;
import java.util.List;

@Dao
public interface MealDao {

    @Query("SELECT * FROM meals WHERE id = :id")
    LiveData<Meal> getMeal(int id);

    @Query("SELECT * FROM meals WHERE date = :date AND type = :type")
    LiveData<Meal> getMealFromDate(Long date, String type);


    @Query("SELECT * FROM meals WHERE id = :id")
    Meal getMeal1(int id);

    @Insert
    void insert(Meal meal);

    @Delete
    void delete(Meal meal);

    @Query("SELECT * FROM meals")
    LiveData<List<Meal>> getAllMeals();

    @Query("SELECT * FROM meals WHERE date = :date")
    LiveData<List<Meal>> getDayMeals(LocalDate date);

    @Update
    void update(Meal meal);

    @Query("SELECT * FROM meals WHERE date = :date AND type = :type")
    Meal getMealFromDate1(Long date, String type);
}
