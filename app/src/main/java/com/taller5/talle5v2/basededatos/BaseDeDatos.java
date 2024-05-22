package com.taller5.talle5v2.basededatos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BaseDeDatos extends SQLiteOpenHelper {
    public static final String NOMBRE= "Taller5.db";
    public static final String NOMBRE_TABLA="Usuarios";
    public static final String NOMBRE_TABLA2="Reseñas";
    public static final int NOMBRE_VERSION = 1;

    public BaseDeDatos(@Nullable Context context) {
        super(context,NOMBRE,null,NOMBRE_VERSION);

    }
    public SQLiteDatabase obtenerBaseDeDatos() {
        return this.getReadableDatabase();
    }
    private static final String CREATE_TABLE_USUARIOS = "CREATE TABLE usuarios (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "usuario TEXT UNIQUE," +
            "mail TEXT," +
            "contraseña TEXT," +
            "genero TEXT," +
            "numeroTelefono TEXT," +
            "imagen BLOB)";

    // Sentencia SQL para crear la tabla de reseñas
    private static final String CREATE_TABLE_RESENAS = "CREATE TABLE reseñas (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "usuario TEXT ," +
            "mail TEXT," +
            "pelicula TEXT," +
            "reseña TEXT," +
            "puntuacion INTEGER," +
            "imagen BLOB," +
            "ubicacion TEXT," +
            "FOREIGN KEY(usuario) REFERENCES usuarios(usuario))";

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Crear las tablas
        db.execSQL(CREATE_TABLE_USUARIOS);
        db.execSQL(CREATE_TABLE_RESENAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Borrar las tablas antiguas si existen
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS reseñas");
        onCreate(db);
    }

}


