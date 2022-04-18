package com.example.movietickets.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


//CLASS TẠM THỜI ĐỂ LẤY TAGLINE, NGÀY RA RẠP(release_date), THỜI GIAN CHIẾU(runtime), THỂ LOẠI(genres) CỦA PHIM
public class TempObject {
    @SerializedName("release_date")
    public String release_date;

    @SerializedName("tagline")
    public String tagline;

    @SerializedName("runtime")
    public String runtime;

    @SerializedName("genres")
    public List<GenresObject.Genres> genres;

    public TempObject(){}


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

    public List<GenresObject.Genres> getGenres() {
        return genres;
    }

    public void setGenres(List<GenresObject.Genres> genres) {
        this.genres = genres;
    }

}
