package com.taller5.talle5v2.Adaptador;

import com.taller5.talle5v2.parseo.Movie;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;

public interface IConectividad {

    //vamos a recuperar lo que hay en el end point
@GET("/?i=tt3896198&apikey=fc658499")
Call<Movie> recuperarPeliculas();
}
