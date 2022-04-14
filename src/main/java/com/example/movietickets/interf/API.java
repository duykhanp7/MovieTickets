package com.example.movietickets.interf;


import com.example.movietickets.model.MovieObject;
import com.example.movietickets.utils.Utils;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {
    
    
    API api = new Retrofit.Builder()
            .baseUrl(Utils.API_MOVIE_DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(API.class);


    //LẤY DANH SÁCH PHIM PHỔ BIẾN TỪ API THEO TRANG
    //https://api.themoviedb.org/3/movie/popular?api_key=904b3059ddd54e71c45dc72502d59375&page=2
    @GET("/3/movie/popular")
    Call<MovieObject> getMoviesPopular(@Query("api_key") String api_key,@Query("page") String page);
}

