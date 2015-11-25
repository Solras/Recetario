package com.example.dam.recetario.receta;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.dam.recetario.Ayudante;
import com.example.dam.recetario.MainActivity;
import com.example.dam.recetario.Recetario;

import java.util.ArrayList;
import java.util.List;

public class GestorReceta {

    private Ayudante adb;
    private SQLiteDatabase db;

    public GestorReceta(Context c){
        adb = new Ayudante(c);
    }

    public void openWrite(){
        db = adb.getWritableDatabase();
    }

    public void openRead(){
        db = adb.getReadableDatabase();
    }

    public void close(){
        adb.close();
    }

    public long insert(Receta r){
        ContentValues cv = new ContentValues();
        cv.put(Recetario.TablaReceta.NOMBRE, r.getNombre());
        cv.put(Recetario.TablaReceta.FOTO, r.getFoto());
        cv.put(Recetario.TablaReceta.INSTRUCCIONES, r.getInstrucciones());
        return db.insert(Recetario.TablaReceta.TABLA, null, cv);
    }

    public void delete(Receta r){
        String condicion = Recetario.TablaReceta._ID + " = ?";
        String[] argumentos = { r.getId() + "" };
        db.delete(Recetario.TablaReceta.TABLA, condicion, argumentos);
    }

    public void update(Receta r){
        ContentValues cv = new ContentValues();
        cv.put(Recetario.TablaReceta.NOMBRE, r.getNombre());
        cv.put(Recetario.TablaReceta.FOTO, r.getFoto());
        cv.put(Recetario.TablaReceta.INSTRUCCIONES, r.getInstrucciones());
        String condicion = Recetario.TablaReceta._ID + " = ?";
        String[] argumentos = { r.getId() + "" };
        db.update(Recetario.TablaReceta.TABLA, cv, condicion, argumentos);
    }

    public Receta getRow(Cursor c) {
        Receta r = new Receta();
        r.setId(c.getLong(c.getColumnIndex(Recetario.TablaReceta._ID)));
        r.setNombre(c.getString(c.getColumnIndex(Recetario.TablaReceta.NOMBRE)));
        r.setFoto(c.getString(c.getColumnIndex(Recetario.TablaReceta.FOTO)));
        r.setInstrucciones(c.getString(c.getColumnIndex(Recetario.TablaReceta.INSTRUCCIONES)));
        return r;
    }

    public Cursor getCursor() {
        return db.query(Recetario.TablaReceta.TABLA, null, null, null, null, null, null);
    }

    public List<Receta> selectRecetas(){
        List<Receta> all = new ArrayList<Receta>();

        Cursor cursor = getCursor();
        cursor.moveToFirst();
        Receta receta;
        while (!cursor.isAfterLast()) {
            receta = getRow(cursor);
            all.add(receta);
            cursor.moveToNext();
        }
        cursor.close();
        return all;
    }
}
