package com.taller5.talle5v2.PaginasDePeliculas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.taller5.talle5v2.ModificacionUsuarioActivity;
import com.taller5.talle5v2.R;
import com.taller5.talle5v2.basededatos.CrudUsuarios;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ModificacionResenaActivity extends AppCompatActivity {
    private EditText editTextPuntuacion;
    private TextView textViewReseña, textViewPelicula;
    private CrudUsuarios crudUsuarios;
    private ImageView iv1;
    // Dentro de la clase ModificacionResenaActivity
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private int idResena,puntuacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificacion_resena);

        // Inicializa los elementos de UI
        editTextPuntuacion = findViewById(R.id.editTextNumberPuntuacion);
        textViewPelicula = findViewById(R.id.textViewPelicula);
        textViewReseña = findViewById(R.id.textViewResena);
        crudUsuarios = new CrudUsuarios(this);
        iv1 = findViewById(R.id.imageView6);
        editTextPuntuacion.setFilters(new InputFilter[]{ new InputFilterMinMax(1, 10)});
        String usuario = getIntent().getStringExtra("usuario");
        String peliculaResena = getIntent().getStringExtra("reseña");
        String pelicula2 = getIntent().getStringExtra("pelicula");



        // Buscar la última ocurrencia de ": " para dividir la cadena en película y reseña
        int lastIndex = peliculaResena.lastIndexOf("Reseña: ");
        if (lastIndex != -1) {
            String pelicula = peliculaResena.substring(0, lastIndex-1);
            String reseña = peliculaResena.substring(lastIndex + 7); // Agregar 2 para omitir ": "
            idResena = crudUsuarios.obtenerIdResena(pelicula);
            // Muestra la película y la reseña en los elementos correspondientes
            textViewPelicula.setText(pelicula);
            textViewReseña.setText(reseña);
        }
        byte[] imagen = crudUsuarios.obtenerImagenResenaPorId(idResena);
        puntuacion= crudUsuarios.obtenerPuntuacionPorId(idResena);
        editTextPuntuacion.setText(String.valueOf(puntuacion));
        if (imagen != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imagen, 0, imagen.length);
            iv1.setImageBitmap(bitmap);
        } else {
            // Si no se encuentra la imagen, puedes mostrar una imagen predeterminada o dejar el ImageView vacío
            iv1.setImageResource(R.drawable.login); // Cambia "imagen_predeterminada" por el ID de tu imagen predeterminada
        }
        // Botón para eliminar la reseña
        Button btnEliminarResena = findViewById(R.id.btnEliminarResena);
        btnEliminarResena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idResena != -1) {
                    // Llamar al método para eliminar la reseña por su ID
                    boolean eliminado = crudUsuarios.eliminarReseñaPorId(idResena);

                    if (eliminado) {
                        Toast.makeText(ModificacionResenaActivity.this, "Reseña eliminada correctamente", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("usuario", usuario);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    } else {
                        Toast.makeText(ModificacionResenaActivity.this, "Error al eliminar la reseña", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Mostrar un mensaje de error si no se pudo obtener el ID de la reseña
                    Toast.makeText(ModificacionResenaActivity.this, "Error: No se pudo obtener el ID de la reseña", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Botón para actualizar la reseña
        Button btnActualizarResena = findViewById(R.id.btnActualizarResena);
        btnActualizarResena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nuevaPuntuacion = Integer.parseInt(editTextPuntuacion.getText().toString());
                String nuevaPelicula = textViewPelicula.getText().toString();
                String nuevaReseña = textViewReseña.getText().toString();
                byte[] nuevaImagen = obtenerImagenBytes(); // Obtener la nueva imagen de imageView6

                // Llamar al método en la base de datos para actualizar la reseña, incluida la imagen
                boolean actualizado = crudUsuarios.actualizarReseña(idResena, nuevaPuntuacion, nuevaPelicula, nuevaReseña);

                // Actualizar la imagen de la reseña
                boolean imagenActualizada = crudUsuarios.actualizarImagenResena(idResena, nuevaImagen);

                if (actualizado) {
                    Toast.makeText(ModificacionResenaActivity.this, "Reseña actualizada correctamente", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("usuario", usuario);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else {
                    Toast.makeText(ModificacionResenaActivity.this, "Error al actualizar la reseña", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Botón para volver a la lista de reseñas
        Button btnVolverLista = findViewById(R.id.btnVolverLista);
        btnVolverLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModificacionResenaActivity.this, ListaResenasActivity.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
                finish();
            }
        });
       // Botón para seleccionar una foto
        Button btnSeleccionarFoto = findViewById(R.id.btnSeleccionarFoto);
        btnSeleccionarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia una intención para seleccionar una imagen del dispositivo
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });
    }

    private byte[] obtenerImagenBytes() {
        // Obtener la imagen del ImageView
        ImageView imageView = findViewById(R.id.imageView6);
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        // Convertir la imagen Bitmap a un array de bytes
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                // Obtener la URI de la imagen seleccionada
                Uri selectedImageUri = data.getData();

                // Convertir la URI a un Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

                // Mostrar el Bitmap en el ImageView
                ImageView imageView = findViewById(R.id.imageView6);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
