package com.example.karori.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.karori.Listeners.IngredientIdListener;
import com.example.karori.Listeners.RecipeIdListener;
import com.example.karori.Models.RecipeResult;
import com.example.karori.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchedRecipesAdapter extends RecyclerView.Adapter<SearchedRecipesViewHolder> {
    Context context;
    ArrayList<RecipeResult> results;
    RecipeIdListener idListener;

    public SearchedRecipesAdapter(Context context, ArrayList<RecipeResult> results, RecipeIdListener idListener) {
        this.context = context;
        this.results = results;
        this.idListener = idListener;
    }

    @NonNull
    @Override
    public SearchedRecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchedRecipesViewHolder(LayoutInflater.from(context).inflate(R.layout.ricette, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchedRecipesViewHolder holder, int position) {
        if (results.get(position).title == null) {
            holder.txt_title.setText("Daniela Micucci");
        } else {
            holder.txt_title.setText(results.get(position).title);
        }
        holder.txt_title.setSelected(true);
        Picasso.get().load(results.get(position).image).into(holder.ricetta_image);
        holder.ricette_card.setOnClickListener(new View.OnClickListener() {
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
class SearchedRecipesViewHolder extends RecyclerView.ViewHolder {
    CardView ricette_card;
    TextView txt_title;
    ImageView ricetta_image;
    public SearchedRecipesViewHolder(@NonNull View itemView) {
        super(itemView);
        ricette_card = itemView.findViewById(R.id.ricette_card);
        txt_title = itemView.findViewById(R.id.testo_ricetta);
        ricetta_image = itemView.findViewById(R.id.ricetta_image);
    }
}