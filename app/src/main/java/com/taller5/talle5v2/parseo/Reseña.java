package com.taller5.talle5v2.parseo;

public class Reseña {
    private int id;
    private String pelicula;
    private String reseña;
    private byte[] imagen;
    private String ubicacion;

    public Reseña(String pelicula, String reseña, byte[] imagen, String ubicacion) {
        this.pelicula = pelicula;
        this.reseña = reseña;
        this.imagen = imagen;
        this.ubicacion = ubicacion;
    }
    public Reseña(int id) {
        this.id = id;

    }

    public int getId() {
        return id;
    }
    public String getPelicula() {
        return pelicula;
    }

    public void setPelicula(String pelicula) {
        this.pelicula = pelicula;
    }

    public String getReseña() {
        return reseña;
    }

    public void setReseña(String reseña) {
        this.reseña = reseña;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
}
