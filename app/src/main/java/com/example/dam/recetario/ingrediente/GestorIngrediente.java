package com.example.dam.recetario.ingrediente;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.dam.recetario.Ayudante;
import com.example.dam.recetario.Recetario;

import java.util.ArrayList;
import java.util.List;

public class GestorIngrediente {

    private Ayudante adb;
    private SQLiteDatabase db;

    public GestorIngrediente(Context c){
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

    public long insert(Ingrediente ing){
        ContentValues cv = new ContentValues();
        cv.put(Recetario.TablaIngrediente.NOMBRE, ing.getNombre());
        return db.insert(Recetario.TablaIngrediente.TABLA, null, cv);
    }

    public void delete(Ingrediente ing){
        String condicion = Recetario.TablaIngrediente._ID + " = ?";
        String[] argumentos = { ing.getId() + "" };
        db.delete(Recetario.TablaIngrediente.TABLA, condicion, argumentos);
    }

    public void update(Ingrediente ing){
        ContentValues cv = new ContentValues();
        cv.put(Recetario.TablaIngrediente.NOMBRE,ing.getNombre());
        String condicion = Recetario.TablaIngrediente._ID + " = ?";
        String[] argumentos = { ing.getId() + "" };
        db.update(Recetario.TablaIngrediente.TABLA, cv, condicion, argumentos);
    }

    public Cursor getCursor() {
        return db.query(Recetario.TablaIngrediente.TABLA, null, null, null, null, null, null);
    }

    public Ingrediente getRow(Cursor c) {
        Ingrediente ing = new Ingrediente();
        ing.setId(c.getLong(c.getColumnIndex(Recetario.TablaIngrediente._ID)));
        ing.setNombre(c.getString(c.getColumnIndex(Recetario.TablaIngrediente.NOMBRE)));
        return ing;
    }

    public long selectIdIngrediente(String nombre){
        String[] columnas = {Recetario.TablaIngrediente._ID};
        String condicion = Recetario.TablaIngrediente.NOMBRE + " = ?";
        String[] argumentos = { nombre };
        Cursor cursor = db.query(Recetario.TablaIngrediente.TABLA, columnas, condicion, argumentos, null, null, null);
        cursor.moveToFirst();
        return cursor.getLong(cursor.getColumnIndex(Recetario.TablaIngrediente._ID));
    }

    public String selectNombreIngrediente(long id){
        String[] columnas = {Recetario.TablaIngrediente.NOMBRE};
        String condicion = Recetario.TablaIngrediente._ID + " = ?";
        String[] argumentos = { Long.toString(id) };
        Cursor cursor = db.query(Recetario.TablaIngrediente.TABLA, columnas, condicion, argumentos, null, null, null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(Recetario.TablaIngrediente.NOMBRE));
    }

    public List<Ingrediente> selectIngredientes(){
        List<Ingrediente> all = new ArrayList<>();
        Cursor cursor = getCursor();
        cursor.moveToFirst();
        Ingrediente ingrediente;
        while (!cursor.isAfterLast()) {
            ingrediente = getRow(cursor);
            all.add(ingrediente);
            cursor.moveToNext();
        }
        cursor.close();
        return all;
    }

}
