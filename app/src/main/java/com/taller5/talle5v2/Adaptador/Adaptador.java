package com.taller5.talle5v2.Adaptador;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.taller5.talle5v2.R;
import com.taller5.talle5v2.parseo.Movie;

import java.util.List;

public class Adaptador extends RecyclerView.Adapter<Adaptador.MovieViewHolder>{

    private List<Movie> movies;


    public Adaptador(List<Movie> movies) {
        this.movies = movies;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.peliculas, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        //en adaptador conectar con la api
        holder.titleTextView.setText(movie.getTitle());
        holder.yearTextView.setText(movie.getYear());
        holder.plotTextView.setText(movie.getPlot());



    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        //y a lo visual o sea peliculas y declararlo
        TextView titleTextView;
        TextView yearTextView;
        TextView plotTextView;
        ImageView imageView; // Añadir ImageView para mostrar la imagen
        MovieViewHolder(View itemView) {
            super(itemView);
            //y a lo visual o sea peliculas
            titleTextView = itemView.findViewById(R.id.titleTextView);
            yearTextView = itemView.findViewById(R.id.yearTextView);
            plotTextView = itemView.findViewById(R.id.plotTextView);
            imageView = itemView.findViewById(R.id.imageView); // Inicializar ImageView

        }

        void bind(Movie movie) {
            titleTextView.setText(movie.getTitle());
            yearTextView.setText(movie.getYear());
            plotTextView.setText(movie.getPlot());
            // Convertir los bytes de la imagen en un Bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(movie.getImageBytes(), 0, movie.getImageBytes().length);

            // Mostrar el Bitmap en el ImageView
            imageView.setImageBitmap(bitmap);
        }
    }



}
