package com.taller5.talle5v2.parseo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Movie {

    @SerializedName("Title")
    @Expose
    private String title;
    private byte[] imageBytes; // Nuevo campo para los bytes de la imagen
    @SerializedName("Year")
    @Expose
    private String year;
    @SerializedName("Plot")
    @Expose
    private String plot;

    public Movie() {

    }

    // Constructor
    public Movie(String title, String year, String plot, byte[] imagebytes) {
        this.title = title;
        this.year = year;
        this.plot = plot;
        this.imageBytes = imagebytes;
    }

    // Getters y setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }
}
