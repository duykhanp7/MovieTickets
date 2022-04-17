package com.example.movietickets.model;

import com.google.gson.annotations.SerializedName;

public class TempObject {

    @SerializedName("release_date")
    public String release_date;

    @SerializedName("tagline")
    public String tagline;

    public TempObject(){}


    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

}
