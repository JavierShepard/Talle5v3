package com.taller5.talle5v2.Login;




import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.taller5.talle5v2.PaginasDePeliculas.ListaResenasActivity;
import com.taller5.talle5v2.R;
import com.taller5.talle5v2.basededatos.CrudUsuarios;

public class LoginActivity extends AppCompatActivity {
    TextView textUsuario, textPassword;
    private CrudUsuarios crudUsuarios;
    Button btnRegistro, btnLogin;
    private int intentosFallidos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        crudUsuarios = new CrudUsuarios(this);
        //conexion con el layout
        textPassword = findViewById(R.id.textPassword);
        textUsuario = findViewById(R.id.textUsuario);
        btnLogin = findViewById(R.id.buttonLoggin);
        btnRegistro = findViewById(R.id.buttonRegistro);


        // boton para ir a la pagina registro.

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent llamarRegistro = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(llamarRegistro);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail, password,usuario;
                mail = textUsuario.getText().toString();
                password = textPassword.getText().toString();
                usuario = textUsuario.getText().toString();
                Toast.makeText(LoginActivity.this, "Prueba", Toast.LENGTH_LONG);
                // Obtener el usuario_id del usuario
                int id = crudUsuarios.getUsuarioId(mail);

                if (crudUsuarios.isValidUser(mail, password)) {
                    //Toast.makeText(LoginActivity.this,"Su ingreso es correcto",Toast.LENGTH_LONG);
                   // Intent llamarAIndex = new Intent(LoginActivity.this, IndexPeliculas.class);
                    Intent llamarAlistaReseña = new Intent(LoginActivity.this, ListaResenasActivity.class);
                    llamarAlistaReseña.putExtra("mail",mail);
                    llamarAlistaReseña.putExtra("password",password);
                    llamarAlistaReseña.putExtra("usuario",usuario);
                    Toast.makeText(LoginActivity.this, "Contraseña y/o usuario correcto", Toast.LENGTH_LONG).show();
                    startActivity(llamarAlistaReseña);

                } else {
                    // Incrementar el contador de intentos fallidos
                    intentosFallidos++;
                    if (intentosFallidos >= 3) {
                        // Si hay 3 o más intentos fallidos, cerrar la aplicación
                        Toast.makeText(LoginActivity.this, "Demasiados intentos fallidos. Cerrando la aplicación.", Toast.LENGTH_LONG).show();
                        finishAffinity();
                    } else {
                        // Mostrar Snackbar con mensaje de error
                        Toast.makeText(LoginActivity.this, "Contraseña y/o usuario incorrecto. Intento"+ intentosFallidos + "/3", Toast.LENGTH_LONG).show();

                        // Limpiar los campos de entrada
                        textUsuario.setText("");
                        textPassword.setText("");
                    }
                }
            }
        });


    }

    public void showSnackBar(String msg) {
        Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG);

    }

}

