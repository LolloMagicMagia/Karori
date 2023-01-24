package com.example.karori.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Meal.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})

public abstract class MealDatabase extends RoomDatabase {
    public abstract MealDao mealDao();

    private static MealDatabase INSTANCE;

    public static MealDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MealDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    MealDatabase.class, "meal_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
