package com.example.karori.SearchClasses;

import android.content.Context;

import com.example.karori.Listeners.IngredientInfoListener;
import com.example.karori.Listeners.RecipeInfoListener;
import com.example.karori.Listeners.SearchIngredientsListener;
import com.example.karori.Listeners.SearchRecipesListener;
import com.example.karori.Models.IngredientInfoResponse;
import com.example.karori.Models.RecipeInfoResponse;
import com.example.karori.Models.SearchIngredientsResponse;
import com.example.karori.Models.SearchRecipesResponse;
import com.example.karori.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class RequestManager {
    //creiamo il retrofit object e il context
    Context context;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/") //Url da cui prendiamo tutti i dati
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public RequestManager(Context context) {
        this.context = context;
    }

    public void getIngredientSearchResult(SearchIngredientsListener listener, String query) {
        CallSearchIngredients callSearchIngredients = retrofit.create(CallSearchIngredients.class);
        Call<SearchIngredientsResponse> call = callSearchIngredients
                .callSearchIngredient(context.getString(R.string.api_key), query, "", "desc", "10");
        call.enqueue(new Callback<SearchIngredientsResponse>() {
            @Override
            public void onResponse(Call<SearchIngredientsResponse> call, Response<SearchIngredientsResponse> response) {
                if(!response.isSuccessful()) {
                    listener.didError(response.message());
                    return;
                }
                else {
                    listener.didFetch(response.body(), response.message());
                }
            }
            @Override
            public void onFailure(Call<SearchIngredientsResponse> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    public void getRecipeSearchResult(SearchRecipesListener listener, String query) {
        CallSearchRecipes callSearchRecipes = retrofit.create(CallSearchRecipes.class);
        Call<SearchRecipesResponse> call = callSearchRecipes.callSearchRecipes(context.getString(R.string.api_key), query, "", "desc", "10");
        call.enqueue(new Callback<SearchRecipesResponse>() {
            @Override
            public void onResponse(Call<SearchRecipesResponse> call, Response<SearchRecipesResponse> response) {
                if(!response.isSuccessful()) {
                    listener.didError(response.message());
                    return;
                }
                else {
                    listener.didFetch(response.body(), response.message());
                }
            }

            @Override
            public void onFailure(Call<SearchRecipesResponse> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    public void getIngredientInfos(IngredientInfoListener infoListener, int id, int amount, String unit) {
        CallIngredientsInformation callIngredientsInformation = retrofit.create(CallIngredientsInformation.class);
        Call<IngredientInfoResponse> call2 = callIngredientsInformation.callGetIngredientInfo(id,context.getString(R.string.api_key), amount, unit);
        call2.enqueue(new Callback<IngredientInfoResponse>() {
            @Override
            public void onResponse(Call<IngredientInfoResponse> call, Response<IngredientInfoResponse> response) {
                if(!response.isSuccessful()) {
                    infoListener.didError(response.message());
                    return;
                }
                else {
                    infoListener.didFetch(response.body(), response.message());
                }
            }
            @Override
            public void onFailure(Call<IngredientInfoResponse> call, Throwable t) {
                infoListener.didError(t.getMessage());
            }
        });
    }

    public void getRecipeInfos(RecipeInfoListener infoListener, int id, boolean includeNutrition) {
        CallRecipesInformation callRecipesInformation = retrofit.create(CallRecipesInformation.class);
        Call<RecipeInfoResponse> call = callRecipesInformation.callRecipeInfo(id,context.getString(R.string.api_key), includeNutrition);
        call.enqueue(new Callback<RecipeInfoResponse>() {
            @Override
            public void onResponse(Call<RecipeInfoResponse> call, Response<RecipeInfoResponse> response) {
                if(!response.isSuccessful()) {
                    infoListener.didError(response.message());
                    return;
                }
                else {
                    infoListener.didFetch(response.body(), response.message());
                }
            }
            @Override
            public void onFailure(Call<RecipeInfoResponse> call, Throwable t) {
                infoListener.didError(t.getMessage());
            }
        });
    }

    //interfacce per effettuare le differenti call
    private interface CallSearchIngredients {
        @GET("food/ingredients/search")
        Call<SearchIngredientsResponse> callSearchIngredient(
                @Query("apiKey") String apiKey,
                @Query("query") String query,
                @Query("sort") String sort,
                @Query("sortDirection") String sortDirection,
                @Query("number") String number
        );
    }

    private interface CallSearchRecipes {
        @GET("recipes/complexSearch")
        Call<SearchRecipesResponse> callSearchRecipes(
                @Query("apiKey") String apiKey,
                @Query("query") String query,
                @Query("sort") String sort,
                @Query("sortDirection") String sortDirection,
                @Query("number") String number
        );
    }

    private interface CallIngredientsInformation {
        @GET("food/ingredients/{id}/information")
        Call<IngredientInfoResponse> callGetIngredientInfo(
                @Path("id") int id,
                @Query("apiKey") String apiKey,
                @Query("amount") int amount,
                @Query("unit") String unit
        );
    }

    private interface CallRecipesInformation {
        @GET("recipes/{id}/information")
        Call<RecipeInfoResponse> callRecipeInfo(
                @Path("id") int id,
                @Query("apiKey") String apiKey,
                @Query("includeNutrition") boolean includeNutrition
        );
    }
}
