package com.taller5.talle5v2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity2 extends AppCompatActivity {
    private final long SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);

        // Configura el contenido de la actividad

        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Este método se ejecutará una vez que se cumpla el tiempo definido
                Intent intent = new Intent(MainActivity2.this, LoginActivity.class);
                startActivity(intent);

                // Cierra esta actividad
                finish();
            }
        }, SPLASH_TIME_OUT);

}
}