package com.taller5.talle5v2.basededatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.taller5.talle5v2.LoginActivity;
import com.taller5.talle5v2.parseo.Reseña;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CrudUsuarios extends BaseDeDatos {

    static Context context;
    public CrudUsuarios(@Nullable Context context) {
        super(context);
        this.context = context;
    }
    // Método para obtener el usuario_id a partir del mail

    public long InsertarUsuario( String usuario,String mail, String contraseña, String genero, String numeroTelefono, byte[] imagen){



            BaseDeDatos bd = new BaseDeDatos(context);
            SQLiteDatabase sqLiteDatabase = bd.getWritableDatabase();
            // vamos a empezar a insertar datos
            ContentValues values = new ContentValues();
            values.put("usuario",usuario);
            values.put("mail", mail);
            values.put("contraseña",contraseña);
            values.put("genero",genero);
            values.put("numeroTelefono",numeroTelefono);
            values.put("imagen", imagen);
            return sqLiteDatabase.insert(NOMBRE_TABLA,null,values);
    }
    // Método para insertar una reseña
    public long insertReseña(int usuario_id,String usuario,String mail, String pelicula, String reseña, int puntuacion, byte[] imagen, String ubicacion) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("usuario",usuario);
        values.put("mail",mail);
        values.put("pelicula", pelicula);
        values.put("reseña", reseña);
        values.put("puntuacion", puntuacion);
        values.put("imagen", imagen);

        values.put("ubicacion", ubicacion);
        return sqLiteDatabase.insert("reseñas", null, values);
    }
    public Cursor obtenerUsuarios() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + NOMBRE_TABLA, null);
    }

    public boolean isValidUser( String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                "Usuarios", // Nombre de la tabla
                null,
                "usuario = ? AND contraseña = ?", // Cláusula WHERE
                new String[]{username, password},
                null,
                null,
                null
        );
        boolean isValid = cursor.getCount() > 0;
        if (cursor.getCount()>0){
            Toast.makeText(context," Validacion correcta",Toast.LENGTH_LONG);
        }else {
            Toast.makeText(context," Validacion incorrecta",Toast.LENGTH_LONG);
        }

        cursor.close();
        db.close();
        return isValid;
    }
    public int getUsuarioId(String mail) {
        SQLiteDatabase db = this.getReadableDatabase();
        int usuario_id = -1; // Valor predeterminado en caso de que no se encuentre el usuario
        Cursor cursor = null;
        try {
            cursor = db.query(
                    "Usuarios", // Nombre de la tabla
                    new String[]{"usuario"}, // Columnas a consultar
                    "mail = ?", // Cláusula WHERE
                    new String[]{mail},
                    null,
                    null,
                    null
            );
            if (cursor != null && cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex("id");
                if (idIndex != -1) {
                    usuario_id = cursor.getInt(idIndex);
                } else {
                    // La columna "id" no existe en el cursor
                    // Manejar el error según sea necesario
                }
            }
        } catch (Exception e) {
            // Manejar la excepción según sea necesario
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return usuario_id;
    }


    // Método para verificar si un usuario ya existe en la base de datos
    public boolean existeUsuario(String usuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM usuarios WHERE usuario = ?", new String[]{usuario});
            return cursor.getCount() > 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    // Método para verificar si un correo electrónico ya existe en la base de datos
    public boolean existeCorreo(String correo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM usuarios WHERE mail = ?", new String[]{correo});
            return cursor.getCount() > 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }
    public List<String> obtenerReseñasPorUsuario(String usuario) {
        if (usuario == null) {
            return new ArrayList<>(); // Devolver una lista vacía si el usuario es nulo
        }

        SQLiteDatabase db = this.getReadableDatabase();
        List<String> reseñas = new ArrayList<>();

        Cursor cursor = null;
        try {
            cursor = db.query(
                    "reseñas", // Nombre de la tabla
                    new String[]{"pelicula", "reseña"}, // Columnas a consultar
                    "usuario = ?", // Cláusula WHERE
                    new String[]{usuario},
                    null,
                    null,
                    null
            );

            while (cursor != null && cursor.moveToNext()) {
                String pelicula = cursor.getString(cursor.getColumnIndexOrThrow("pelicula"));
                String reseña = cursor.getString(cursor.getColumnIndexOrThrow("reseña"));
                reseñas.add("Pelicula: " + pelicula + "\nReseña: " + reseña);
            }
        } catch (Exception e) {
            // Manejar la excepción según sea necesario
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return reseñas;
    }
    public byte[] obtenerImagenUsuario(String usuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        byte[] imagenUsuario = null;

        Cursor cursor = null;
        try {
            cursor = db.query(
                    "usuarios", // Nombre de la tabla
                    new String[]{"imagen"}, // Columnas a consultar
                    "usuario = ?", // Cláusula WHERE
                    new String[]{usuario},
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                imagenUsuario = cursor.getBlob(cursor.getColumnIndexOrThrow("imagen"));
            }
        } catch (Exception e) {
            // Manejar la excepción según sea necesario
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return imagenUsuario;
    }
    public boolean eliminarReseña(String usuario, String pelicula, String reseña) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] whereArgs = {usuario, pelicula, reseña};
        int rowsAffected = db.delete("reseñas", "usuario = ? AND pelicula = ? AND reseña = ?", whereArgs);
        db.close();
        return rowsAffected > 0;
    }
    public boolean actualizarUsuario(String usuario, String nuevoEmail, String nuevaContraseña, String nuevoNumeroTelefono,byte[] nuevaImagen) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mail", nuevoEmail);
        values.put("contraseña", nuevaContraseña);
        values.put("numeroTelefono", nuevoNumeroTelefono);
        values.put("imagen", nuevaImagen);

        // Definir la cláusula WHERE para identificar al usuario que se actualizará
        String whereClause = "usuario = ?";
        String[] whereArgs = {usuario};

        // Ejecutar la actualización
        int rowsAffected = db.update(NOMBRE_TABLA, values, whereClause, whereArgs);
        db.close();

        // Verificar si la actualización fue exitosa
        return rowsAffected > 0;
    }
    // Método para obtener la puntuación de una reseña por su ID
    public int obtenerPuntuacionPorId(int idResena) {
        SQLiteDatabase db = this.getReadableDatabase();
        int puntuacion = -1; // Valor predeterminado en caso de que no se encuentre la reseña

        Cursor cursor = null;
        try {
            cursor = db.query(
                    "reseñas", // Nombre de la tabla
                    new String[]{"puntuacion"}, // Columnas a consultar
                    "id = ?", // Cláusula WHERE
                    new String[]{String.valueOf(idResena)},
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                puntuacion = cursor.getInt(cursor.getColumnIndexOrThrow("puntuacion"));
            }
        } catch (Exception e) {
            // Manejar la excepción según sea necesario
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return puntuacion;
    }

    // Método para obtener el texto de una reseña por su ID
    public String obtenerTextoReseñaPorId(int idResena) {
        SQLiteDatabase db = this.getReadableDatabase();
        String textoReseña = ""; // Valor predeterminado en caso de que no se encuentre la reseña

        Cursor cursor = null;
        try {
            cursor = db.query(
                    "reseñas", // Nombre de la tabla
                    new String[]{"reseña"}, // Columnas a consultar
                    "id = ?", // Cláusula WHERE
                    new String[]{String.valueOf(idResena)},
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                textoReseña = cursor.getString(cursor.getColumnIndexOrThrow("reseña"));
            }
        } catch (Exception e) {
            // Manejar la excepción según sea necesario
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return textoReseña;
    }
    public int obtenerIdResena(String pelicula) {
        SQLiteDatabase db = this.getReadableDatabase();
        int idResena = -1; // Valor predeterminado en caso de que no se encuentre la reseña

        Cursor cursor = null;
        try {
            cursor = db.query(
                    "reseñas", // Nombre de la tabla
                    new String[]{"id"}, // Columnas a consultar
                    " pelicula = ?", // Cláusula WHERE
                    new String[]{pelicula},
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                idResena = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            }
        } catch (Exception e) {
            // Manejar la excepción según sea necesario
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return idResena;
    }
    public boolean eliminarReseñaPorId(int idResena) {
        SQLiteDatabase db = this.getWritableDatabase();
        String tablaResenas = "reseñas";
        String columnaId = "id";

        // Define la cláusula WHERE para la eliminación basada en el ID de la reseña
        String whereClause = columnaId + " = ?";
        String[] whereArgs = {String.valueOf(idResena)};

        try {
            // Realiza la eliminación de la reseña usando la cláusula WHERE
            int filasAfectadas = db.delete(tablaResenas, whereClause, whereArgs);

            // Verifica si se eliminó correctamente al menos una fila
            if (filasAfectadas > 0) {
                // Si se eliminó al menos una fila, devuelve true
                return true;
            }
        } finally {
            // Cierra la conexión de la base de datos
            db.close();
        }

        // Si no se pudo eliminar la reseña (o no se encontró ninguna reseña con el ID dado), devuelve false
        return false;
    }
    public boolean actualizarReseña(int idResena, int nuevaPuntuacion, String nuevaPelicula, String nuevaReseña) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("puntuacion", nuevaPuntuacion);
        values.put("pelicula", nuevaPelicula);
        values.put("reseña", nuevaReseña);

        // Definir la cláusula WHERE para identificar la reseña que se actualizará
        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(idResena)};

        // Ejecutar la actualización
        int rowsAffected = db.update("reseñas", values, whereClause, whereArgs);
        db.close();

        // Verificar si la actualización fue exitosa
        return rowsAffected > 0;
    }
    public String obtenerNumeroTelefono(String usuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        String numeroTelefono = null;

        Cursor cursor = null;
        try {
            cursor = db.query(
                    "usuarios", // Nombre de la tabla
                    new String[]{"numeroTelefono"}, // Columnas a consultar
                    "usuario = ?", // Cláusula WHERE
                    new String[]{usuario},
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                numeroTelefono = cursor.getString(cursor.getColumnIndexOrThrow("numeroTelefono"));
            }
        } catch (Exception e) {
            // Manejar la excepción según sea necesario
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return numeroTelefono;
    }
    public boolean actualizarImagenResena(int idResena, byte[] nuevaImagen) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("imagen", nuevaImagen);

        // Definir la cláusula WHERE para identificar la reseña que se actualizará
        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(idResena)};

        // Ejecutar la actualización
        int rowsAffected = db.update("reseñas", values, whereClause, whereArgs);
        db.close();

        // Verificar si la actualización fue exitosa
        return rowsAffected > 0;
    }
    public byte[] obtenerImagenResenaPorId(int idResena) {
        SQLiteDatabase db = this.getReadableDatabase();
        byte[] imagenResena = null;

        Cursor cursor = null;
        try {
            cursor = db.query(
                    "reseñas", // Nombre de la tabla
                    new String[]{"imagen"}, // Columnas a consultar
                    "id = ?", // Cláusula WHERE
                    new String[]{String.valueOf(idResena)},
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                imagenResena = cursor.getBlob(cursor.getColumnIndexOrThrow("imagen"));
            }
        } catch (Exception e) {
            // Manejar la excepción según sea necesario
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return imagenResena;
    }
}
