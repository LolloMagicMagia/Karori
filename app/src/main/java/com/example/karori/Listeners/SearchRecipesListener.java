package com.example.karori.Listeners;

import com.example.karori.Models.SearchIngredientsResponse;
import com.example.karori.Models.SearchRecipesResponse;

public interface SearchRecipesListener {
    void didFetch(SearchRecipesResponse response, String message);
    void didError(String error);
}
