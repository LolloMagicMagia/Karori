package com.example.karori.Room;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.karori.SearchClasses.IngredientInfoFragment;
import com.example.karori.menuFragment.AlimentoSpecifico;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observer;


@TypeConverters({DateConverter.class,AlimentoSpecificoConverter.class})

@Entity(tableName = "meals", indices = {@Index(value = {"date","type"}, unique = true)})
public class Meal {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @TypeConverters({DateConverter.class})
    @ColumnInfo(name = "date")
    private LocalDate date;

    @ColumnInfo(name = "type")
    private String type;

    @TypeConverters(FoodListConverter.class)
    //@ColumnInfo(name = "food_list")
    private Map<Integer, Map<String, Object>> foodList;

    //@ColumnInfo(name = "calorie_total")
    private double calorieTot;

    //@ColumnInfo(name = "proteine_total")
    private double proteineTot;

    //@ColumnInfo(name = "grassi_total")
    private double grassiTot;

    //@ColumnInfo(name = "carboidrati_total")
    private double carboidratiTot;

    @TypeConverters(AlimentoSpecificoConverter.class)
    public ArrayList<AlimentoSpecifico> foodListPopUp;

    public Meal(LocalDate date, String type) {
        if(date != null && type != null){
            this.date = date;
            this.type = type;
            foodList = new HashMap<>();
            foodListPopUp=new ArrayList<>();
            calorieTot = 0;
            proteineTot = 0;
            grassiTot = 0;
            carboidratiTot = 0;
        }



    }

    public void add(Map<String, Object> food) {

        DecimalFormat df = new DecimalFormat("#.##");

        if(food.get("id") != null && food.get("Calories") != null && food.get("Protein") != null
                && food.get("Fat") != null && food.get("Carbohydrates") != null &&
                food.get("amount") != null && food.get("unit") != null
                && food.get("nome alimento") != null){

            int id = Integer.parseInt((String) food.get("id"));
            Log.d("franx", ""+id);
            //food.remove("id");
            foodList.put(id, food);
            Log.d("franx", ""+food.get("id"));
            Log.d("franx", ""+food.get("unit"));
            Log.d("franx", ""+food.get("amount"));

            AlimentoSpecifico al1=new AlimentoSpecifico(String.valueOf(food.get("nome alimento")),
                    (String) food.get("id"),
                    String.valueOf(df.format(food.get("Calories"))),
                    String.valueOf(food.get("amount")),String.valueOf(df.format(food.get("Protein"))),
                    type,String.valueOf(df.format(food.get("Fat"))),String.valueOf(df.format(food.get("Carbohydrates"))),
                    (String) food.get("unit"));

            foodListPopUp.add(al1);

            // aggiorna il totale delle calorie, proteine, grassi e carboidrati
            calorieTot += (double) food.get("Calories");
            proteineTot += (double) food.get("Protein");
            grassiTot += (double) food.get("Fat");
            carboidratiTot += (double) food.get("Carbohydrates");

        }else{
            Log.d("Error", "l'alimento non è stato aggiunto correttamente");
        }

    }

    public void remove(String idAlimento, int i){
        Log.d("Alimento", "qui");
        for (AlimentoSpecifico a: foodListPopUp) {
            Log.d("Alimento", "qui");
            if(a != null && a.getId() == idAlimento){
                if (Integer.parseInt(a.getQuantità()) <= i) {
                    foodListPopUp.remove(a);
                }
                if (i == 0) {
                    break;
                }
                int ex = Integer.parseInt(a.getQuantità());
                DecimalFormat df = new DecimalFormat("#,##0.00");
                a.setQuantità(String.valueOf(Integer.parseInt(a.getQuantità())-i));
                setCalorieTot(calorieTot - i*(Double.parseDouble(a.getCalorie().replace(",",".")) / ex));
                setCarboidratiTot(carboidratiTot - i*(Double.parseDouble(a.getCarboidrati().replace(",",".")) / ex));
                setProteineTot(proteineTot - i*(Double.parseDouble(a.getProteine().replace(",",".")) / ex));
                setGrassiTot(grassiTot - i*(Double.parseDouble(a.getGrassi().replace(",",".")) / ex));
                a.setCalorie(df.format(Double.parseDouble(a.getCalorie().replace(",", "."))
                        -i*(Double.parseDouble(a.getCalorie().replace(",",".")) / ex)));
                a.setGrassi(df.format(Double.parseDouble(a.getGrassi().replace(",","."))
                        -i*(Double.parseDouble(a.getGrassi().replace(",",".")) / ex)));
                a.setProteine(df.format(Double.parseDouble(a.getProteine().replace(",","."))
                        -i*(Double.parseDouble(a.getProteine().replace(",",".")) / ex)));
                a.setCarboidrati(df.format(Double.parseDouble(a.getCarboidrati().replace(",","."))
                        -i*(Double.parseDouble(a.getCarboidrati().replace(",",".")) / ex)));
                Log.d("Alimento", a.getNome());
                break;
            }

        }
        Log.d("Alimento", "qui");
    }

    public Map<String, String> getMealDetails() {
        Map<String, String> mealDetails = new HashMap<>();
        mealDetails.put("calorie_total", Double.toString(calorieTot));
        mealDetails.put("proteine_total", Double.toString(proteineTot));
        mealDetails.put("grassi_total", Double.toString(grassiTot));
        mealDetails.put("carboidrati_total", Double.toString(carboidratiTot));
        return mealDetails;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public Map<Integer, Map<String, Object>> getFoodList() {
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

    public ArrayList<AlimentoSpecifico> getFoodListPopUp() {
        return foodListPopUp;
    }


    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFoodList(Map<Integer, Map<String, Object>> foodList) {
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

    public void observe(View.OnClickListener onClickListener, Observer mealObserver) {
    }

    public void observe(IngredientInfoFragment ingredientInfoFragment, androidx.lifecycle.Observer<Meal> mealObserver) {
    }
}


