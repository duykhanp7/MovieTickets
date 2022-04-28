package com.example.movietickets.interf;


import com.example.movietickets.model.MovieObject;
import com.example.movietickets.model.TempObject;
import com.example.movietickets.utils.Utils;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface API {
    
    
    API api = new Retrofit.Builder()
            .baseUrl(Utils.API_MOVIE_DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(API.class);


    //LẤY DANH SÁCH PHIM ĐANG CHIẾU TỪ API THEO TRANG
    //https://api.themoviedb.org/3/movie/now_playing?api_key=904b3059ddd54e71c45dc72502d59375&page=2
    @GET("/3/movie/now_playing")
    Call<MovieObject> getMoviesPopular(@Query("api_key") String api_key,@Query("page") String page);


    //TÌM KIẾM MOVIE THEO TÊN PHIM
    //https://api.themoviedb.org/3/search/movie?api_key=904b3059ddd54e71c45dc72502d59375&query=the avenger&page=3
    @GET("/3/search/movie")
    Call<MovieObject> getMoviesByKeyword(@Query("api_key") String api_key, @Query("query") String query,@Query("page") String page);

    //LẤY ID, TÊN CÁC THỂ LOẠI PHIM
    //https://api.themoviedb.org/3/genre/movie/list?api_key=904b3059ddd54e71c45dc72502d59375


    //https://api.themoviedb.org/3/movie/550?api_key=904b3059ddd54e71c45dc72502d59375
    //LẤY THỜI GIAN CHIẾU VÀ THỜI GIAN PHIM RA RẠP
    @GET("/3/movie/{id}")
    Call<TempObject> getDetailMovie(@Path("id") String id,@Query("api_key") String api_key);
}

