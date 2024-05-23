package com.taller5.talle5v2.PaginasDePeliculas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.taller5.talle5v2.Adaptador.Adaptador;
import com.taller5.talle5v2.Adaptador.IConectividad;
import com.taller5.talle5v2.R;
import com.taller5.talle5v2.parseo.Movie;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IndexPeliculasActivity extends AppCompatActivity {
    Button btnIndex;
    private static final String API_KEY = "fc658499";
    private static final String MOVIE_TITLE = "NOMBRE_DE_LA_PELICULA";

    private TextView title,yearTextView,plotTextView;

    private Adaptador movieAdapter;
    private RecyclerView  movieRecyclerView;
    private List<Movie> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_peliculas);
        // Inicializar el RecyclerView y el adaptador
        movieList = new ArrayList<>();
        movieAdapter = new Adaptador(movieList);
        movieRecyclerView = findViewById(R.id.recyclerMovie);
        movieRecyclerView.setAdapter(movieAdapter);
        movieRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        title = findViewById(R.id.title);
        peliculas();



    }

    public void peliculas( ){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.omdbapi.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IConectividad iConectividad = retrofit.create(IConectividad.class);
       // Movie movie = new Movie();
        Call<Movie> movieCall = iConectividad.recuperarPeliculas();
        movieCall.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                //int statusCode= response.code();
                if (response.isSuccessful() && response.body() != null) {
                    Movie movie = response.body();
                    mostrarPelicula(movie);
                } else {
                    Log.d("IndexPeliculas", "Fallo al obtener la película");
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

            }
        });
    }

    private void mostrarPelicula(Movie movie) {
        // Agregar la película al adaptador del RecyclerView
        movieList.add(movie);
        movieAdapter.notifyDataSetChanged();
    }


}