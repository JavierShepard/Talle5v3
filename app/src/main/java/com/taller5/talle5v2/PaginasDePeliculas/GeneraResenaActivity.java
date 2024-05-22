package com.taller5.talle5v2.PaginasDePeliculas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;
import android.Manifest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.taller5.talle5v2.R;
import com.taller5.talle5v2.basededatos.CrudUsuarios;
import com.google.android.gms.location.LocationRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class GeneraResenaActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private TextView textPelicula, textReseña, puntuacion;
    private String ubicacionInsert = "";
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    //guardar imagen de la peli seleccionada
    private ImageView imagePeli;
    private byte[] imagenSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genera_resena);

        textPelicula= findViewById(R.id.editTextTextPelicula);
        textReseña = findViewById(R.id.editTextTextReseña);
        puntuacion =findViewById(R.id.puntuacion);
        imagePeli = findViewById(R.id.imagePeli);
        puntuacion.setFilters(new InputFilter[]{ new InputFilterMinMax(1, 10)});
        // Establecer el color del texto en tus EditText a blanco
       /* puntuacion.setTextColor(Color.WHITE);
        textPelicula.setTextColor(Color.WHITE);
        textReseña.setTextColor(Color.WHITE);*/

        Button btnVolverListaResenas = findViewById(R.id.btnVolverListaResenas);

        // Recibir los datos enviados desde LoginActivity
        Intent intent = getIntent();
        if (intent != null) {
            String mail = intent.getStringExtra("mail");
            String password = intent.getStringExtra("password");
            int usuario_id = intent.getIntExtra("usuario_id",-1);

        }
        // Inicializar el cliente de ubicación
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Configurar solicitud de ubicación
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000); // Intervalo de actualización de ubicación: 5 segundos


        // Configurar callback de ubicación
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        // Obtener la ubicación actual y convertirla a una cadena legible
                        ubicacionInsert = location.getLatitude() + ", " + location.getLongitude();
                        //Toast.makeText(GeneraResenaActivity.this, "Ubicación obtenida: " + ubicacionInsert, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        // Solicitar permisos de ubicación si no están otorgados
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

       // requestLocationPermission();
        btnVolverListaResenas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el nombre de usuario
                String usuario = getIntent().getStringExtra("usuario");

                // Establecer el resultado y pasar el nombre de usuario de vuelta
                Intent returnIntent = new Intent();
                returnIntent.putExtra("usuario", usuario);
                setResult(Activity.RESULT_OK, returnIntent);
                finish(); // Finalizar la actividad y volver a ListaResenasActivity
            }
        });

        Button btnSeleccionarImagen = findViewById(R.id.btnSeleccionarImagen);
        btnSeleccionarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia una intención para seleccionar una imagen del dispositivo
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });
    }
    public void botonRegistrarReseña(View view){
        String pelicula = textPelicula.getText().toString();
        String reseña = textReseña.getText().toString();
        //int puntuacion = Integer.parseInt(this.puntuacion.getText().toString());
        String puntuacionStr = puntuacion.getText().toString();

        // Validar que todos los campos estén llenos
        // Verificar cada campo individualmente
        if (pelicula.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese el nombre de la película", Toast.LENGTH_SHORT).show();
            return;
        }

        if (reseña.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese la reseña", Toast.LENGTH_SHORT).show();
            return;
        }

        if (puntuacionStr.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese la puntuación", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imagenSeleccionada == null) {
            Toast.makeText(this, "Por favor, seleccione una imagen", Toast.LENGTH_SHORT).show();
            return;
        }
        // Convertir la puntuación a entero
        int puntuacion = Integer.parseInt(puntuacionStr);
        if (imagenSeleccionada != null) {
            CrudUsuarios crudUsuarios = new CrudUsuarios(GeneraResenaActivity.this);
            try {
                Intent intent = getIntent();
                if (intent != null) {
                    String mail = intent.getStringExtra("mail");
                    String password = intent.getStringExtra("password");
                    int usuario_id = intent.getIntExtra("usuario_id", -1);
                    String usuario = intent.getStringExtra("usuario");

                    crudUsuarios.insertReseña(usuario_id, usuario, mail, pelicula, reseña, puntuacion, imagenSeleccionada, ubicacionInsert);

                    Toast.makeText(this, "Reseña insertada correctamente", Toast.LENGTH_SHORT).show();

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("usuario", usuario);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Error al insertar la reseña", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Por favor, seleccione una imagen", Toast.LENGTH_SHORT).show();
        }
    }
    // Método para manejar el resultado de la solicitud de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso de ubicación concedido, iniciar la actualización de ubicación
                startLocationUpdates();
            }
        }
    }

    // Método para iniciar la actualización de ubicación
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    // Método para detener la actualización de ubicación
    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Verifica si el resultado proviene de la selección de imagen y si fue exitoso
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                try {
                    // Obtiene la URI de la imagen seleccionada
                    Uri imageUri = data.getData();

                    // Convierte la URI de la imagen en un arreglo de bytes
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        byteArrayOutputStream.write(buffer, 0, bytesRead);
                    }
                    imagenSeleccionada = byteArrayOutputStream.toByteArray();

                    // Muestra la imagen seleccionada en el ImageView
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imagenSeleccionada, 0, imagenSeleccionada.length);
                    imagePeli.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

