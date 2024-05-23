package com.taller5.talle5v2.Login;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.taller5.talle5v2.R;
import com.taller5.talle5v2.basededatos.BaseDeDatos;
import com.taller5.talle5v2.basededatos.CrudUsuarios;

import java.io.ByteArrayOutputStream;

public class RegistroActivity extends AppCompatActivity {
    //variables para guardar la foto de la camara
    Button b1;
    ImageView iv1;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    TextView textUsuario, textPassword, textMail,textTelefono;
    Spinner spinnerGenero;
    byte[] byteArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        textMail = findViewById(R.id.TextoEmail);
        textUsuario = findViewById(R.id.TextoNombre);
        textPassword = findViewById(R.id.TextoPassword);
        spinnerGenero = findViewById(R.id.spinnerGenero);
        textTelefono= findViewById(R.id.editTextNumTel);

        // creacion de la base de datos
        BaseDeDatos baseDeDatos = new BaseDeDatos(RegistroActivity.this);
        SQLiteDatabase sqLiteDatabase = baseDeDatos.getWritableDatabase();
        // Configurar el spinner de género
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.generos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenero.setAdapter(adapter);
        b1 = (Button)findViewById(R.id.btn_capture_photo);
        iv1 = (ImageView)findViewById(R.id.imageView);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
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
    public void cancelarRegistro(View view) {
        // Crear una intención para volver a LoginActivity
        Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Finalizar la actividad actual
    }
    public void botonRegistrar (View view ){
        if (!validarCampos()) {
            return; // Detener el registro si algún campo está vacío
        }else {
        String nombre, usuario, contraseña, genero,telefono;
        CrudUsuarios crudUsuarios = new CrudUsuarios(RegistroActivity.this);

        nombre = textUsuario.getText().toString();
        usuario = textMail.getText().toString();
        contraseña = textPassword.getText().toString();
        genero = spinnerGenero.getSelectedItem().toString();
        telefono= textTelefono.getText().toString();
        Log.d(TAG, "Estas son las variables" + usuario + contraseña + nombre);
        if (crudUsuarios.existeUsuario(usuario) || crudUsuarios.existeCorreo(usuario)) {
            // Mostrar un mensaje de error al usuario
            Toast.makeText(RegistroActivity.this, "El usuario o correo electrónico ya están en uso", Toast.LENGTH_LONG).show();
        } else {
                try {
                    //inserto nuevo usuario para mi db

                    //crudUsuarios.InsertarUsuario(nombre, usuario, contraseña, "X", "1234");
                    long id = crudUsuarios.InsertarUsuario(nombre, usuario, contraseña,genero, telefono,byteArray);
                    //genero la intencion para salir cuando el registro es exitoso.
                    /*Intent intent = new Intent(RegistroActivity.this,LoginActivity.class);
                    Toast.makeText(RegistroActivity.this,"EXITO AMIGO!!! en el REgistro",Toast.LENGTH_LONG);
                    startActivity(intent);*/
                    if (id != -1) {
                        // Registro exitoso
                        Toast.makeText(RegistroActivity.this, "Registro exitoso", Toast.LENGTH_LONG).show();
                        // Generar la intención para ir a la pantalla de inicio de sesión
                        Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        // Error durante la inserción
                        Toast.makeText(RegistroActivity.this, "Error al registrar usuario", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {

                    throw new RuntimeException(e);

                    }
                }

        }
    }
    private boolean validarCampos() {
        if (textUsuario.getText().toString().trim().isEmpty()) {
            textUsuario.setError("El nombre de usuario es requerido");
            return false;
        }
        if (textMail.getText().toString().trim().isEmpty()) {
            textMail.setError("El correo electrónico es requerido");
            return false;
        }
        if (textPassword.getText().toString().trim().isEmpty()) {
            textPassword.setError("La contraseña es requerida");
            return false;
        }
        if (spinnerGenero.getSelectedItemPosition() == 0) { // Asumiendo que el primer ítem es un "Seleccione un género"
            Toast.makeText(this, "Seleccione un género", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (textTelefono.getText().toString().trim().isEmpty()) {
            textTelefono.setError("El número de teléfono es requerido");
            return false;
        }
        if (byteArray == null || byteArray.length == 0) {
            Toast.makeText(this, "La foto es requerida", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}