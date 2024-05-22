package com.taller5.talle5v2;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.taller5.talle5v2.PaginasDePeliculas.ListaResenasActivity;
import com.taller5.talle5v2.R;
import com.taller5.talle5v2.basededatos.CrudUsuarios;

import java.io.ByteArrayOutputStream;

public class ModificacionUsuarioActivity extends AppCompatActivity {
    private EditText editTextEmail,editTextPassword,editTextNumeroTel;
    private ImageView iv1;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    byte[] byteArray;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificacion_usuario);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextTextPassword);
        editTextNumeroTel = findViewById(R.id.editTextPhone);
        iv1 = findViewById(R.id.imageView3);

        // Obtener los datos del Intent
        Intent intent = getIntent();
        String usuario = getIntent().getStringExtra("usuario");
        String mail = getIntent().getStringExtra("mail");
        String password = intent.getStringExtra("password");
        String telefono = intent.getStringExtra("telefono");
        b1 = (Button)findViewById(R.id.btn_capture_photo);
        iv1 = (ImageView)findViewById(R.id.imageView3);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Si no se tiene el permiso, solicitarlo al usuario
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camerai = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(camerai, 123);
            }
        });

        // Inicializar los campos con los valores recibidos
        editTextEmail.setText(mail);
        editTextPassword.setText(password);
        editTextNumeroTel.setText(telefono);
        obtenerYEstablecerImagenUsuario(usuario);
        Button btnGuardarCambios = findViewById(R.id.btnGuardarCambios);
        Button btnVolverLogin = findViewById(R.id.buttonVolver);
        //btnGuardarCambios.setOnClickListener(view -> guardarCambios());
        btnGuardarCambios.setOnClickListener(view -> guardarCambios());

        btnVolverLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = getIntent().getStringExtra("usuario");
                Intent intent = new Intent(ModificacionUsuarioActivity.this, ListaResenasActivity.class);
                intent.putExtra("usuario",usuario);
                // startActivity(intent);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Se busca que el codigo conincida
        if (requestCode == 123) {
            // Se guarda la foto en una variable tipo bitmap
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
            // Se muestra la imagen en el imageview
            iv1.setImageBitmap(photo);
        }
    }
    public void guardarCambios() {
        String nuevoEmail = editTextEmail.getText().toString();
        String nuevaContraseña = editTextPassword.getText().toString();
        String nuevoNumeroTel = editTextNumeroTel.getText().toString();
        String usuario = getIntent().getStringExtra("usuario");
        // Obten más valores de otros campos si es necesario

        // Actualiza la información del usuario en la base de datos
        CrudUsuarios crudUsuarios = new CrudUsuarios(this);
        // Suponiendo que tengas un método en CrudUsuarios para actualizar el usuario
        boolean actualizado = crudUsuarios.actualizarUsuario(usuario,nuevoEmail,nuevaContraseña,nuevoNumeroTel,byteArray);

        if (actualizado) {
            Toast.makeText(this, "Cambios guardados correctamente", Toast.LENGTH_SHORT).show();
            // Aquí puedes cerrar la actividad si deseas volver a la pantalla anterior
            Intent intent = new Intent(this, ListaResenasActivity.class);
            intent.putExtra("usuario",usuario);
            intent.putExtra("imagen", byteArray);
           // startActivity(intent);
            setResult(Activity.RESULT_OK, intent);
             finish();
        } else {
            Toast.makeText(this, "Error al guardar cambios", Toast.LENGTH_SHORT).show();
        }
    }
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
}