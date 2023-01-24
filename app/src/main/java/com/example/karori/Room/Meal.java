package com.example.karori.Room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "meals")
public class Meal {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "date")
    private Date date;

    @ColumnInfo(name = "type")
    private String type;

    @TypeConverters(FoodListConverter.class)
    @ColumnInfo(name = "food_list")
    private Map<Integer, String> foodList;

    @ColumnInfo(name = "calorie_total")
    private double calorieTot;

    @ColumnInfo(name = "proteine_total")
    private double proteineTot;

    @ColumnInfo(name = "grassi_total")
    private double grassiTot;

    @ColumnInfo(name = "carboidrati_total")
    private double carboidratiTot;

    public Meal(Date date, String type) {
        this.date = date;
        this.type = type;
        foodList = new HashMap<>();
        calorieTot = 0;
        proteineTot = 0;
        grassiTot = 0;
        carboidratiTot = 0;
    }

    public void add(Map<String, Object> food) {
        // aggiungi il nome e l'ID dell'alimento alla mappa
        String name = (String) food.get("nome_alimento");
        int id = (int) food.get("Id_alimento");
        foodList.put(id, name);

        // aggiorna il totale delle calorie, proteine, grassi e carboidrati
        calorieTot += (double) food.get("Calorie");
        proteineTot += (double) food.get("Proteine");
        grassiTot += (double) food.get("Grassi");
        carboidratiTot += (double) food.get("carboidrati");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public Map<Integer, String> getFoodList() {
        return foodList;
    }

    public double getCalorieTot() {
        return calorieTot;
    }

    public double getProteineTot() {
        return proteineTot;
    }

    public double getGrassiTot() {
        return grassiTot;
    }

    public double getCarboidratiTot() {
        return carboidratiTot;
    }
}


