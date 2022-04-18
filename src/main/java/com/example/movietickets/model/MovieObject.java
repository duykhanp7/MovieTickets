package com.example.movietickets.model;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//




import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieObject {


    @SerializedName("page")
    public String page;

    @SerializedName("total_pages")//BẮT ĐẦU TỪ PAGE SỐ 1
    public String total_pages;

    @SerializedName("results")
    public List<MovieObject.Movie> movies;



    public MovieObject() {}

    public String getPage() {
        return this.page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(String total_pages) {
        this.total_pages = total_pages;
    }

    public List<MovieObject.Movie> getMovies() {
        return this.movies;
    }

    public void setMovies(List<MovieObject.Movie> movies) {
        this.movies = movies;
    }

    //OBJECT PHIM
    public static class Movie {

        //ID CỦA PHIM
        @SerializedName("id")
        public String id;
        //TÊN PHIM
        @SerializedName("title")
        public String title;
        //TÊN GỐC CỦA PHIM
        @SerializedName("original_title")
        public String original_title;
        //ĐƯỜNG DẪN ĐẾN POSTER CỦA PHIM
        @SerializedName("poster_path")
        public String poster_path;
        //THỂ LOẠI CỦA PHIM
        @SerializedName("genres")
        public List<GenresObject.Genres> genres;
        //NGÀY RA RẠP
        @SerializedName("release_date")
        public String release_date;
        //THỜI GIAN CHIẾU
        @SerializedName("runtime")
        public String runtime;
        //TAGLINE (KHẨU HIỆU) CỦA PHIM
        @SerializedName("tagline")
        public String tagline;

        public Movie(){}

        public String getId() {
            return this.id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getOriginal_title() {
            return this.original_title;
        }

        public void setOriginal_title(String original_title) {
            this.original_title = original_title;
        }

        public String getPoster_path() {
            return this.poster_path;
        }

        public List<GenresObject.Genres> getGenres() {
            return genres;
        }

        public void setGenres(List<GenresObject.Genres> genres) {
            this.genres = genres;
        }

        public String getRelease_date() {
            return release_date;
        }

        public void setRelease_date(String release_date) {
            this.release_date = release_date;
        }

        public String getRuntime() {
            return runtime;
        }

        public void setRuntime(String runtime) {
            this.runtime = runtime;
        }

        public String getTagline() {
            return tagline;
        }

        public void setTagline(String tagline) {
            this.tagline = tagline;
        }

        public void setPoster_path(String poster_path) {
            this.poster_path = poster_path;
        }
    }
}
