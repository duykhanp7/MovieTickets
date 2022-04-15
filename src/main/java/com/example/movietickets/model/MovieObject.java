package com.example.movietickets.model;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//




import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieObject {
    @SerializedName("page")
    public String page;

    @SerializedName("total_pages")//BẮT ĐẦU TỪ PAGE SỐ 1
    public String total_pages;

    @SerializedName("results")
    public List<MovieObject.Movie> movies;

    public MovieObject() {
    }

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

    public static class Movie {

        @SerializedName("id")
        public String id;

        @SerializedName("title")
        public String title;

        @SerializedName("original_title")
        public String original_title;

        @SerializedName("poster_path")
        public String poster_path;

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

        public void setPoster_path(String poster_path) {
            this.poster_path = poster_path;
        }
    }
}
