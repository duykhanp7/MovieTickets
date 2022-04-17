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
    //https://api.themoviedb.org/3/movie/now_playing?api_key=904b3059ddd54e71c45dc72502d59375&page=2
    @GET("/3/movie/popular")
    Call<MovieObject> getMoviesPopular(@Query("api_key") String api_key,@Query("page") String page);


    //TÌM KIẾM MOVIE THEO TÊN PHIM
    //https://api.themoviedb.org/3/search/movie?api_key=904b3059ddd54e71c45dc72502d59375&query=the avenger&page=3
    @GET("/3/search/movie")
    Call<MovieObject> getMoviesByKeyword(@Query("api_key") String api_key, @Query("query") String query,@Query("page") String page);

    //LẤY ID, TÊN CÁC THỂ LOẠI PHIM
    //https://api.themoviedb.org/3/genre/movie/list?api_key=904b3059ddd54e71c45dc72502d59375
}

