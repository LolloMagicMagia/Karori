package com.example.karori.Room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@TypeConverters({DateConverter.class})

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
        String name = (String) food.get("nome alimento");
        int id = (int) food.get("id");
        foodList.put(id, name);

        // aggiorna il totale delle calorie, proteine, grassi e carboidrati
        calorieTot += (double) food.get("Calories");
        proteineTot += (double) food.get("Protein");
        grassiTot += (double) food.get("Fat");
        carboidratiTot += (double) food.get("Carbohydrates");
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

    public void setDate(Date date) {
        this.date = date;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFoodList(Map<Integer, String> foodList) {
        this.foodList = foodList;
    }

    public void setCalorieTot(double calorieTot) {
        this.calorieTot = calorieTot;
    }

    public void setProteineTot(double proteineTot) {
        this.proteineTot = proteineTot;
    }

    public void setGrassiTot(double grassiTot) {
        this.grassiTot = grassiTot;
    }

    public void setCarboidratiTot(double carboidratiTot) {
        this.carboidratiTot = carboidratiTot;
    }
}


