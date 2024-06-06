package com.taller5.talle5v2.PaginasDePeliculas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import com.taller5.talle5v2.Login.LoginActivity;
import com.taller5.talle5v2.Login.ModificacionUsuarioActivity;
import com.taller5.talle5v2.R;
import com.taller5.talle5v2.basededatos.CrudUsuarios;

public class ListaResenasActivity extends AppCompatActivity {
    private ListView listView;
    private ImageView iv1;
    private CrudUsuarios crudUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_resenas);
        listView = findViewById(R.id.listViewReseñas);
        iv1 = findViewById(R.id.iv2);
        String usuario = getIntent().getStringExtra("usuario");
        byte[] imagen = getIntent().getByteArrayExtra("imagen");
        String mail = getIntent().getStringExtra("mail");

        Button btnModificarUsuario = findViewById(R.id.btnModificarUsuario);
        Button btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaResenasActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        Button btnGenerarResena = findViewById(R.id.btnGenerarResena);

        crudUsuarios = new CrudUsuarios(this);
        TextView textViewWelcomeMessage = findViewById(R.id.textViewWelcomeMessage);
        textViewWelcomeMessage.setText("Bienvenido, " + usuario);
        List<String> reseñas = crudUsuarios.obtenerReseñasPorUsuario(usuario);

        // Crear un adaptador personalizado para tu ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item_layout, reseñas);
        // Asignar el adaptador a tu ListView
        listView.setAdapter(adapter);
        // Agregar el oyente de clics para los elementos de la lista
        listView.setOnItemClickListener((parent, view, position, id) -> {

            String reseñaSeleccionada = reseñas.get(position);
            // Dividir la reseña para obtener la película y la reseña como textos separados
            String[] partes = reseñaSeleccionada.split(": ");
            String pelicula = partes[0];
            String reseña = String.join(": ", Arrays.copyOfRange(partes, 1, partes.length));

            // Luego, crea un Intent para abrir ModificacionResenaActivity y pasa los datos de la reseña seleccionada
            Intent intent = new Intent(ListaResenasActivity.this, ModificacionResenaActivity.class);

            intent.putExtra("pelicula", pelicula);
            intent.putExtra("reseña", reseña);
            intent.putExtra("usuario", usuario);
            intent.putExtra("imagen", imagen);

            startActivityForResult(intent, 1);
        });
        // Obtener y establecer la imagen del usuario
        obtenerYEstablecerImagenUsuario(usuario);
        // Obtener el número de teléfono usando el nuevo método
        String telefono = crudUsuarios.obtenerNumeroTelefono(usuario);

        btnGenerarResena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaResenasActivity.this, GeneraResenaActivity.class);
                intent.putExtra("mail", getIntent().getStringExtra("mail"));
                intent.putExtra("password", getIntent().getStringExtra("password"));
                intent.putExtra("usuario",getIntent().getStringExtra("usuario"));
                startActivityForResult(intent, 1);
            }
        });
        btnModificarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaResenasActivity.this, ModificacionUsuarioActivity.class);

                intent.putExtra("mail",mail);
                intent.putExtra("password", getIntent().getStringExtra("password"));
                intent.putExtra("usuario",getIntent().getStringExtra("usuario"));
                intent.putExtra("telefono",telefono);

                startActivityForResult(intent, 1);
            }
        });
    }
    public void abrirModificacionUsuario(View view) {
        Intent intent = new Intent(this, ModificacionUsuarioActivity.class);
        startActivity(intent);
    }
    // Método para obtener y establecer la imagen del usuario
    private void obtenerYEstablecerImagenUsuario(String usuario) {
        CrudUsuarios crudUsuarios = new CrudUsuarios(this);
        byte[] imagenUsuario = crudUsuarios.obtenerImagenUsuario(usuario);
        if (imagenUsuario != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imagenUsuario, 0, imagenUsuario.length);
            iv1.setImageBitmap(bitmap);
        } else {
            // Si no se encuentra la imagen, puedes mostrar una imagen predeterminada o dejar el ImageView vacío
            iv1.setImageResource(R.drawable.login  ); // Cambia "imagen_predeterminada" por el ID de tu imagen predeterminada
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String usuario = data.getStringExtra("usuario");
                byte[] imagen = data.getByteArrayExtra("imagen");
                if (usuario != null) {
                    actualizarListaReseñas(usuario);
                    if (imagen != null) {
                        iv1.setImageBitmap(BitmapFactory.decodeByteArray(imagen, 0, imagen.length));
                    }
                } else {
                    // Manejar el caso en el que no se reciba el usuario
                    Toast.makeText(this, "Error: No se recibió el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private void actualizarListaReseñas(String usuario) {
        // Obtener las reseñas actualizadas del usuario
        //String usuario = getIntent().getStringExtra("usuario");
        List<String> reseñas = crudUsuarios.obtenerReseñasPorUsuario(usuario);

        // Crear un nuevo adaptador con las reseñas actualizadas
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item_layout, reseñas);

        // Actualizar el adaptador del ListView
        listView.setAdapter(adapter);
    }

}