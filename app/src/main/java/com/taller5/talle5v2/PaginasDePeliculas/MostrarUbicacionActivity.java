package com.taller5.talle5v2.PaginasDePeliculas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.taller5.talle5v2.R;

public class MostrarUbicacionActivity extends AppCompatActivity implements OnMapReadyCallback {
    private TextView textViewUbicacion;
    private MapView mapView;
    private GoogleMap googleMap;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_ubicacion);


        textViewUbicacion = findViewById(R.id.textViewUbicacion);
        mapView = findViewById(R.id.mapView);
        // Obtener la ubicación enviada desde la actividad anterior
        Intent intent = getIntent();
        String ubicacion = intent.getStringExtra("ubicacion");
        String usuario = getIntent().getStringExtra("usuario");
        // Mostrar la ubicación en el TextView
        if (ubicacion != null) {
            textViewUbicacion.setText(ubicacion);
        }
        // Inicializar el MapView
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
        // Botón para volver a la lista de reseñas
        Button btnVolverLista = findViewById(R.id.btnVolverLista2);
        btnVolverLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MostrarUbicacionActivity.this, ModificacionResenaActivity.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
                finish();
            }
        });
    }
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        // Parsear la ubicación recibida (ejemplo: "37.7749,-122.4194")
        Intent intent = getIntent();
        String ubicacion = intent.getStringExtra("ubicacion");

        if (ubicacion != null) {
            String[] latLng = ubicacion.split(",");
            double latitude = Double.parseDouble(latLng[0]);
            double longitude = Double.parseDouble(latLng[1]);

            // Mostrar la ubicación en el mapa
            LatLng location = new LatLng(latitude, longitude);
            googleMap.addMarker(new MarkerOptions().position(location).title("Ubicación"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }
}