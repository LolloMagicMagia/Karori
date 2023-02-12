package com.example.karori.menuFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.karori.R;
import com.example.karori.Room.Meal;
import com.example.karori.Room.MealViewModel;

import java.util.ArrayList;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHoleder> {
    private ArrayList<AlimentoSpecifico> alimentiList;
    private RecyclerViewClickListener listener;

    private Meal meal;
    private Button remove;

    private MealViewModel mealViewModel;

    public recyclerAdapter(ArrayList<AlimentoSpecifico> alimento, RecyclerViewClickListener listener, Meal meal, MealViewModel mealViewModel){
        this.alimentiList=alimento;
        this.listener=listener;
        this.meal = meal;
        this.mealViewModel = mealViewModel;
    }



    public class MyViewHoleder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView nome;
        private TextView calorie;
        private TextView quantità;
        private TextView proteine;
        private CardView card;

        private TextView carboidrati;

        private TextView grassi;


        public MyViewHoleder(final View view){
            super(view);
            nome=view.findViewById(R.id.nomeCiboPopUp);
            calorie=view.findViewById(R.id.CaloriePopUp);
            quantità=view.findViewById(R.id.quantitàPopUp);
            grassi=view.findViewById(R.id.GrassiPopUp);
            carboidrati=view.findViewById(R.id.CarboidratiPopUp);
            proteine=view.findViewById(R.id.ProteinePopUp);
            remove = view.findViewById(R.id.remove_btn);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view,getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public recyclerAdapter.MyViewHoleder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new MyViewHoleder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.MyViewHoleder holder, int position) {
         String nome=alimentiList.get(position).getNome();
         String calorie=alimentiList.get(position).getCalorie();
         String quantità=alimentiList.get(position).getQuantità();
         String proteine=alimentiList.get(position).getProteine();
         String carboidrati=alimentiList.get(position).getCarboidrati();
         String grassi=alimentiList.get(position).getGrassi();
         Log.d("pinoins", alimentiList.get(position).getId());
         Log.d("pinoins", alimentiList.get(position).getTipo());

         holder.nome.setText(nome);
         holder.calorie.setText(calorie);
         holder.quantità.setText(quantità);
         holder.proteine.setText(proteine);
         holder.carboidrati.setText(carboidrati);
         holder.grassi.setText(grassi);

         final int pos = position;
         remove.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 meal.remove(alimentiList.get(pos).getId());
                 mealViewModel.update(meal);
             }
         });
    }

    @Override
    public int getItemCount() {
        return alimentiList.size();
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }
}
