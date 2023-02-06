package com.example.karori.Listeners;

import com.example.karori.Models.RecipeInfoResponse;

public interface RecipeInfoListener {
    void didFetch(RecipeInfoResponse response, String message);
    void didError(String message);
}
