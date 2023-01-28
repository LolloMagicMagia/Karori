package com.example.karori.SearchClasses;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.karori.Adapter.SearchedIngredientsAdapter;
import com.example.karori.Listeners.IngredientIdListener;
import com.example.karori.Listeners.IngredientInfoListener;
import com.example.karori.Listeners.SearchIngredientsListener;
import com.example.karori.Models.IngredientInfoResponse;
import com.example.karori.Models.Nutrient;
import com.example.karori.Models.Nutrition;
import com.example.karori.Models.Property;
import com.example.karori.Models.SearchIngredientsResponse;
import com.example.karori.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RicercaEAggiungiActivity extends Fragment {
    private String pasto;
    private androidx.appcompat.widget.SearchView srchIngredient;
    private TextView txt_select;
    private RecyclerView rec_pasto;
    private ArrayList<String> flavio;

    ProgressDialog dialog;
    RequestManager manager;
    SearchedIngredientsAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_ricerca_aggiungi, container, false);
        pasto = getActivity().getIntent().getStringExtra("pasto");

        txt_select = view.findViewById(R.id.txt_select);
        int idPasto = Integer.parseInt(pasto);
        if (idPasto == 0)
            txt_select.setText("Hai Selezionato Colazione");
        if (idPasto == 1)
            txt_select.setText("Hai Selezionato Pranzo");
        if (idPasto == 2)
            txt_select.setText("Hai Selezionato Cena");

        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Loading");
        manager = new RequestManager(getActivity());

        srchIngredient = view.findViewById(R.id.srchIngredient);
        srchIngredient.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                txt_select.setVisibility(View.GONE);
                rec_pasto.setVisibility(View.VISIBLE);
                dialog.show();
                manager.getIngredientSearchResult(ingredientsListener, query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        rec_pasto = view.findViewById(R.id.colazione_cercata);

        return view;

    }
    private final SearchIngredientsListener ingredientsListener = new SearchIngredientsListener() {
        @Override
        public void didFetch(SearchIngredientsResponse response, String message) {
            dialog.dismiss();
            rec_pasto = getView().findViewById(R.id.colazione_cercata);
            rec_pasto.setHasFixedSize(true);
            rec_pasto.setLayoutManager(new GridLayoutManager(getActivity(), 1));
            adapter = new SearchedIngredientsAdapter(getActivity(), response.results, idListener);
            rec_pasto.setAdapter(adapter);
        }

        @Override
        public void didError(String error) {
            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
        }
    };

    private final IngredientIdListener idListener = new IngredientIdListener() {
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
                                Navigation.findNavController(getView()).navigate(R.id.action_ricercaEAggiungiActivity_to_ingredientInfoActivity,bundle);
                               /* startActivity(new Intent(getActivity(), IngredientInfoActivity.class)
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
        }

        @Override
        public void didError(String message) {
            return;
        }
    };

}