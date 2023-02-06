package com.example.karori.SearchClasses;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karori.Adapter.RecipeIngredientsAdapter;
import com.example.karori.Listeners.IngredientIdListener;
import com.example.karori.Listeners.IngredientInfoListener;
import com.example.karori.Listeners.RecipeInfoListener;
import com.example.karori.Models.IngredientInfoResponse;
import com.example.karori.Models.RecipeInfoResponse;
import com.example.karori.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeInfo extends Fragment implements LifecycleOwner {
    //ingredients_list
    //ricette_simili
    int id;
    String selezionato = "";
    private ArrayList<String> flavio;
    TextView nome_ricetta, riassunto, link, details;
    ImageView img_ricetta;
    RecyclerView rec_ricette;
    RecyclerView rec_ingredienti;
    ProgressDialog dialog;
    RequestManager manager;
    RecipeIngredientsAdapter ingredientsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_info, container, false);

        id = Integer.parseInt(getArguments().getString("id"));
        selezionato = getArguments().getString("selected");

        initializeViews(view);

        manager = new RequestManager(getActivity());
        manager.getRecipeInfos(recipeInfoListener, id, false); //includeNutrition

        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Loading Information");
        dialog.show();

        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (riassunto.getMaxLines() == 8) {
                    riassunto.setMaxLines(1000);
                    details.setText("Hide Infos");
                } else {
                    riassunto.setMaxLines(8);
                    details.setText("More Infos");
                }
            }
        });


        return view;
    }

    private void initializeViews(View view) {
        details = view.findViewById(R.id.details);
        link = view.findViewById(R.id.txt_ricetta_link);
        nome_ricetta = view.findViewById(R.id.txt_ricetta_nome);
        riassunto = view.findViewById(R.id.riassunto);
        img_ricetta = view.findViewById(R.id.img_ricetta);
        rec_ricette = view.findViewById(R.id.similar_recipes);
        rec_ingredienti = view.findViewById(R.id.ingredienti);
    }

    private final RecipeInfoListener recipeInfoListener = new RecipeInfoListener() {
        @Override
        public void didFetch(RecipeInfoResponse response, String message) {
            dialog.dismiss();
            rec_ingredienti.setVisibility(View.VISIBLE);
            nome_ricetta.setText(response.title);
            riassunto.setText(response.summary);
            link.setText(response.sourceUrl);
            String str = response.summary;
            riassunto.setText(str.replaceAll("\\<.*?\\>", "")); //da html a leggibile
            Picasso.get().load(response.image).into(img_ricetta);
            rec_ingredienti.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            ingredientsAdapter = new RecipeIngredientsAdapter(getActivity(), response.extendedIngredients, ingredientsIdListener);
            rec_ingredienti.setAdapter(ingredientsAdapter);
        }

        @Override
        public void didError(String message) {
            //
        }
    };

    private final IngredientIdListener ingredientsIdListener = new IngredientIdListener() {
        @Override
        public void onClickedIngredient(String id) {
            manager.getIngredientInfos(infoListener, Integer.parseInt(id), 1, "");
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setTitle("Insert Amount");
            final EditText input = new EditText(getActivity());
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            input.setRawInputType(Configuration.KEYBOARD_12KEY);
            final ListView popupUnit = new ListView(getActivity());
            alertDialog.setView(input);
            alertDialog.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    if(input.getText().toString().equals("") || input.getText().toString().equals(null)) {
                        Toast.makeText(getActivity(), "Insert a valid amount", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else {
                        dialog.dismiss();
                        AlertDialog.Builder alert2 = new AlertDialog.Builder(getActivity());
                        alert2.setTitle("Insert a measure unit");
                        String[] array = flavio.toArray(new String[0]);
                        alert2.setView(popupUnit);
                        alert2.setItems(array, new DialogInterface.OnClickListener() {


                            ////////////////////////////////////////////////
                            ////////devo capire se posso fare tutti string
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Bundle bundle = new Bundle();
                                bundle.putString("id", id);
                                bundle.putString("amount", input.getText().toString());
                                bundle.putString("unit", array[which]);
                                bundle.putString("selected", selezionato);
                                bundle.putString("mode", "add");
                                Navigation.findNavController(getView()).navigate(R.id.action_recipeInfo_to_ingredientInfoFragment,bundle);
                               /* startActivity(new Intent(getActivity(), IngredientInfoFragment.class)
                                        .putExtra("id", id)
                                        .putExtra("amount", input.getText().toString())
                                        .putExtra("unit", array[which]));*/
                            }
                        });
                        alert2.show();
                    }
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    return;
                }
            });
            alertDialog.show();
        }
    };

    private final IngredientInfoListener infoListener = new IngredientInfoListener() {
        @Override
        public void didFetch(IngredientInfoResponse response, String message) {
            flavio = response.possibleUnits;
            dialog.dismiss();
        }

        @Override
        public void didError(String message) {
            Toast.makeText(getActivity(), "riprova", Toast.LENGTH_SHORT).show();
        }
    };

}