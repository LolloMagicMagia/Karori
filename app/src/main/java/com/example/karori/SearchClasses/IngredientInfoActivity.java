package com.example.karori.SearchClasses;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.karori.Listeners.IngredientInfoListener;
import com.example.karori.Models.IngredientInfoResponse;
import com.example.karori.Models.Nutrient;
import com.example.karori.Models.Nutrition;
import com.example.karori.Models.Property;
import com.example.karori.R;
import com.example.karori.Room.Meal;
import com.example.karori.Room.MealViewModel;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Calendar;
import java.util.Date;

public class IngredientInfoActivity extends Fragment implements LifecycleOwner {
    int id;
    int amount;
    String unit;
    TextView aggiungi;
    TextView txt_nome, txt_amount, txt_cal, txt_prot, txt_fat, txt_sat_fat, txt_carbo;
    TextView txt_net_carbo, txt_sugar, txt_chole, txt_sodium, txt_vit_c, txt_manga, txt_fiber;
    TextView txt_vit_b6, txt_copper, txt_vit_b1, txt_folate, txt_pota, txt_magne, txt_vit_b3;
    TextView txt_vit_b5, txt_vit_b2, txt_iron, txt_calc, txt_vit_a, txt_zinc, txt_phospho, txt_vit_k;
    TextView txt_sele, txt_vit_e, txt_gly_ind, txt_gly_load;
    TextView txt_unit;
    ImageView img_foto;
    RequestManager manager;
    ProgressDialog dialog;
    Map<String, Object> importanti = new HashMap<String, Object>();

    private static boolean isObserverActive = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_ingredient_info, container, false);
        MealViewModel mealViewModel = new ViewModelProvider(this).get(MealViewModel.class);

        initializeViews(view);


        id = Integer.parseInt(getArguments().getString("id"));
        amount = Integer.parseInt(getArguments().getString("amount"));
        unit = getArguments().getString("unit");
        manager = new RequestManager(getActivity());
        manager.getIngredientInfos(infoListener, id, amount, unit);
        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Loading Information");
        dialog.show();


        aggiungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date currentTime = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(currentTime);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                currentTime = cal.getTime();
                Date finalCurrentTime = currentTime;
                /*Toast.makeText(IngredientInfoActivity.this, importanti.get("id").toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(IngredientInfoActivity.this, importanti.get("amount").toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(IngredientInfoActivity.this, importanti.get("nome alimento").toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(IngredientInfoActivity.this, importanti.get("Fat").toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(IngredientInfoActivity.this, importanti.get("Protein").toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(IngredientInfoActivity.this, importanti.get("Calories").toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(IngredientInfoActivity.this, importanti.get("Carbohydrates").toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(IngredientInfoActivity.this, importanti.get("image").toString(), Toast.LENGTH_SHORT).show();*/


                mealViewModel.getMeal(1).observe(getActivity(), new Observer<Meal>() {
                    @Override
                    public void onChanged(Meal meal) {
                        if(!isObserverActive) {
                            isObserverActive = true;
                            if (meal != null) {
                                meal.add(importanti);
                                mealViewModel.update(meal);
                            } else {
                                try {
                                    Date currentTime = new Date();
                                    Meal meal1 = new Meal(currentTime, "pranzo");
                                    meal1.add(importanti);
                                    mealViewModel.insert(meal1);
                                } catch (Exception e) {
                                    Log.d("Tag", "meal1 è già presente dentro il database");
                                }
                            }
                        }
                    }
                });

                isObserverActive = false;
                mealViewModel.getAll().observe(getActivity(), new Observer<List<Meal>>() {
                    @Override
                    public void onChanged(List<Meal> meals) {
                        Log.d("Tag", "stampa");
                        StringBuilder result = new StringBuilder();
                        for (Meal meal : meals) {
                            Log.d("TAG", String.valueOf(meal.getId()) + "    " + String.valueOf(meal.getCalorieTot()));
                        }

                    }
                });

            }
        });
        return view;
    }

    private void initializeViews(View view) {
        txt_unit = view.findViewById(R.id.txt_unit);
        aggiungi = view.findViewById(R.id.aggiungi);
        txt_gly_ind = view.findViewById(R.id.txt_gly_ind);
        txt_gly_load = view.findViewById(R.id.txt_gly_load);
        txt_sodium = view.findViewById(R.id.txt_sodium);
        txt_vit_c = view.findViewById(R.id.txt_vit_c);
        txt_manga = view.findViewById(R.id.txt_manga);
        txt_fiber = view.findViewById(R.id.txt_fiber);
        txt_vit_b6 = view.findViewById(R.id.txt_vit_b6);
        txt_vit_b1 = view.findViewById(R.id.txt_vit_b1);
        txt_vit_b2 = view.findViewById(R.id.txt_vit_b2);
        txt_vit_b3 = view.findViewById(R.id.txt_vit_b3);
        txt_vit_b5 = view.findViewById(R.id.txt_vit_b5);
        txt_copper = view.findViewById(R.id.txt_copper);
        txt_folate = view.findViewById(R.id.txt_folate);
        txt_pota = view.findViewById(R.id.txt_pota);
        txt_magne = view.findViewById(R.id.txt_magne);
        txt_iron = view.findViewById(R.id.txt_iron);
        txt_calc = view.findViewById(R.id.txt_calc);
        txt_vit_a = view.findViewById(R.id.txt_vit_a);
        txt_zinc = view.findViewById(R.id.txt_zinc);
        txt_phospho = view.findViewById(R.id.txt_phospho);
        txt_vit_k = view.findViewById(R.id.txt_vit_k);
        txt_sele = view.findViewById(R.id.txt_sele);
        txt_vit_e = view.findViewById(R.id.txt_vit_e);
        txt_amount = view.findViewById(R.id.txt_amount);
        txt_fat = view.findViewById(R.id.txt_fat);
        txt_nome = view.findViewById(R.id.txt_nome);
        txt_cal = view.findViewById(R.id.txt_cal);
        txt_prot = view.findViewById(R.id.txt_prot);
        txt_sat_fat =view. findViewById(R.id.txt_sat_fat);
        txt_carbo = view.findViewById(R.id.txt_carbo);
        txt_net_carbo = view.findViewById(R.id.txt_net_carbo);
        txt_sugar = view.findViewById(R.id.txt_sugar);
        txt_chole = view.findViewById(R.id.txt_chole);
        img_foto = view.findViewById(R.id.img_foto);
    }


    private final IngredientInfoListener infoListener = new IngredientInfoListener() {
        @Override
        public void didFetch(IngredientInfoResponse response, String message) {
            txt_unit.setText(unit);
            importanti.put("id", id);
            importanti.put("amount", amount);
            txt_amount.setText(String.valueOf(amount));
            txt_nome.setText(response.name);
            importanti.put("nome alimento", txt_nome.getText().toString());
            String imageUrl = "https://spoonacular.com/cdn/ingredients_250x250/" + response.image;
            importanti.put("image", imageUrl);
            Picasso.get().load(imageUrl).into(img_foto);
            Nutrition nutr = response.nutrition;
            List<Property> properties = nutr.properties;
            for (Property p: properties) {
                if(p.name.equals("Glycemic Index"))
                    txt_gly_ind.setText(String.valueOf(p.amount));
                if(p.name.equals("Glycemic Load"))
                    txt_gly_load.setText(String.valueOf(p.amount));
            }
            List<Nutrient> nutris = nutr.nutrients;
            for (Nutrient n: nutris) {
                if (n.name.equals("Fat")) {
                    txt_fat.setText(String.valueOf(n.amount) + n.unit);
                    importanti.put(String.valueOf(n.name), n.amount);
                }
                if (n.name.equals("Calories")) {
                    txt_cal.setText(String.valueOf(n.amount) + n.unit);
                    importanti.put(String.valueOf(n.name), n.amount);
                }
                if (n.name.equals("Protein")) {
                    txt_prot.setText(String.valueOf(n.amount) + n.unit);
                    importanti.put(String.valueOf(n.name), n.amount);
                }
                if (n.name.equals("Carbohydrates")) {
                    txt_carbo.setText(String.valueOf(n.amount) + n.unit);
                    importanti.put(String.valueOf(n.name), n.amount);
                }
                if (n.name.equals("Sugar"))
                    txt_sugar.setText(String.valueOf(n.amount)+n.unit);
                if (n.name.equals("Cholesterol"))
                    txt_chole.setText(String.valueOf(n.amount)+n.unit);
                if (n.name.equals("Net Carbohydrates"))
                    txt_net_carbo.setText(String.valueOf(n.amount)+n.unit);
                if (n.name.equals("Saturated Fat"))
                    txt_sat_fat.setText(String.valueOf(n.amount)+n.unit);
                if(n.name.equals("Vitamin B6"))
                    txt_vit_b6.setText(String.valueOf(n.amount)+n.unit);
                if(n.name.equals("Sodium"))
                    txt_sodium.setText(String.valueOf(n.amount)+n.unit);
                if(n.name.equals("Vitamin C"))
                    txt_vit_c.setText(String.valueOf(n.amount)+n.unit);
                if(n.name.equals("Manganese"))
                    txt_manga.setText(String.valueOf(n.amount)+n.unit);
                if(n.name.equals("Fiber"))
                    txt_fiber.setText(String.valueOf(n.amount)+n.unit);
                if(n.name.equals("Vitamin B1"))
                    txt_vit_b1.setText(String.valueOf(n.amount)+n.unit);
                if(n.name.equals("Vitamin B2"))
                    txt_vit_b2.setText(String.valueOf(n.amount)+n.unit);
                if(n.name.equals("Vitamin B3"))
                    txt_vit_b3.setText(String.valueOf(n.amount)+n.unit);
                if(n.name.equals("Vitamin B5"))
                    txt_vit_b5.setText(String.valueOf(n.amount)+n.unit);
                if(n.name.equals("Copper"))
                    txt_copper.setText(String.valueOf(n.amount)+n.unit);
                if(n.name.equals("Folate"))
                    txt_folate.setText(String.valueOf(n.amount)+n.unit);
                if(n.name.equals("Potassium"))
                    txt_pota.setText(String.valueOf(n.amount)+n.unit);
                if(n.name.equals("Magnesium"))
                    txt_magne.setText(String.valueOf(n.amount)+n.unit);
                if(n.name.equals("Iron"))
                    txt_iron.setText(String.valueOf(n.amount)+n.unit);
                if(n.name.equals("Calcium"))
                    txt_calc.setText(String.valueOf(n.amount)+n.unit);
                if(n.name.equals("Vitamin A"))
                    txt_vit_a.setText(String.valueOf(n.amount)+n.unit);
                if(n.name.equals("Zinc"))
                    txt_zinc.setText(String.valueOf(n.amount)+n.unit);
                if(n.name.equals("Phosphorus"))
                    txt_phospho.setText(String.valueOf(n.amount)+n.unit);
                if(n.name.equals("Vitamin K"))
                    txt_vit_k.setText(String.valueOf(n.amount)+n.unit);
                if(n.name.equals("Selenium"))
                    txt_sele.setText(String.valueOf(n.amount)+n.unit);
                if(n.name.equals("Vitamin E"))
                    txt_vit_e.setText(String.valueOf(n.amount)+n.unit);
            }
            dialog.dismiss();
        }

        @Override
        public void didError(String message) {
            return;
        }
    };


}