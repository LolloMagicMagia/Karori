package com.example.karori.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.karori.Listeners.IngredientIdListener;
import com.example.karori.Models.ExtendedIngredient;
import com.example.karori.Models.Result;
import com.example.karori.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class RecipeIngredientsAdapter extends RecyclerView.Adapter<RecipeIngredientsViewHolder>{
    Context context;
    ArrayList<ExtendedIngredient> results;
    IngredientIdListener idListener;

    public RecipeIngredientsAdapter(Context context, ArrayList<ExtendedIngredient> results, IngredientIdListener idListener) {
        this.context = context;
        this.results = results;
        this.idListener = idListener;
    }

    @NonNull
    @Override
    public RecipeIngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecipeIngredientsViewHolder(LayoutInflater.from(context).inflate(R.layout.recipe_ingredient_display, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeIngredientsViewHolder holder, int position) {
        holder.nome_ingrediente.setText(results.get(position).name);
        holder.quantita_unita.setText(results.get(position).amount+" "+results.get(position).unit);
        Picasso.get().load("https://spoonacular.com/cdn/ingredients_250x250/" + results.get(position).image)
                .into(holder.ingredient_image);
        holder.ingredienti_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idListener.onClickedIngredient(String.valueOf(results.get(holder.getAdapterPosition()).id));
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }
}

class RecipeIngredientsViewHolder extends RecyclerView.ViewHolder {
    CardView ingredienti_card;
    TextView nome_ingrediente, quantita_unita;
    ImageView ingredient_image;

    public RecipeIngredientsViewHolder(@NonNull View itemView) {
        super(itemView);
        ingredienti_card = itemView.findViewById(R.id.ingredienti_card);
        nome_ingrediente = itemView.findViewById(R.id.nome_ingrediente);
        quantita_unita = itemView.findViewById(R.id.quantita_unita);
        ingredient_image = itemView.findViewById(R.id.ingredient_image);
    }
}
