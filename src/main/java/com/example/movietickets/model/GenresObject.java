package com.example.movietickets.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GenresObject {

    //DANH SÁCH CÁC THỂ LOẠI PHIM
    @SerializedName("genres")
    public List<Genres> genres;


    public List<Genres> getGenres() {
        return genres;
    }

    public void setGenres(List<Genres> genres) {
        this.genres = genres;
    }

    //OBJECT THỂ LOẠI PHIM
    public static class Genres {

        //ID CỦA LOẠI PHIM ĐÓ
        @SerializedName("id")
        public String id;
        //TÊN CỦA LOẠI PHIM ĐÓ
        @SerializedName("name")
        public String name;

        public Genres(){}

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
