package com.example.movietickets.interf;

import com.example.movietickets.model.MovieObject;

public interface OnItemClickedListener {
    void onClicked(MovieObject.Movie item);
    void onButtonBuyClicked(MovieObject.Movie item);
    void onButtonAddToRoom(MovieObject.Movie item);
}
