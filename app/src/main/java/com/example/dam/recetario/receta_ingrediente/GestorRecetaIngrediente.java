package com.example.dam.recetario.receta_ingrediente;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.dam.recetario.Ayudante;
import com.example.dam.recetario.MainActivity;
import com.example.dam.recetario.Recetario;
import com.example.dam.recetario.receta.Receta;

import java.util.ArrayList;
import java.util.List;

public class GestorRecetaIngrediente {

    private Ayudante adb;
    private SQLiteDatabase db;

    public GestorRecetaIngrediente(Context c){
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

    public long insert(RecetaIngrediente c){
        ContentValues cv = new ContentValues();
        cv.put(Recetario.TablaRecetaIngrediente.IDRECETA, c.getIdReceta());
        cv.put(Recetario.TablaRecetaIngrediente.IDINGREDIENTE, c.getIdIngrediente());
        cv.put(Recetario.TablaRecetaIngrediente.CANTIDAD, c.getCantidad());
        return db.insert(Recetario.TablaRecetaIngrediente.TABLA, null, cv);
    }

    public void delete(RecetaIngrediente c){
        String condicion = Recetario.TablaRecetaIngrediente._ID + " = ?";
        String[] argumentos = { c.getId() + "" };
        db.delete(Recetario.TablaRecetaIngrediente.TABLA, condicion, argumentos);
    }

    public void update(RecetaIngrediente c){
        ContentValues cv = new ContentValues();
        cv.put(Recetario.TablaRecetaIngrediente.IDRECETA,c.getIdReceta());
        cv.put(Recetario.TablaRecetaIngrediente.IDINGREDIENTE,c.getIdIngrediente());
        cv.put(Recetario.TablaRecetaIngrediente.CANTIDAD,c.getCantidad());
        String condicion = Recetario.TablaRecetaIngrediente._ID + " = ?";
        String[] argumentos = { c.getId() + "" };
        db.update(Recetario.TablaRecetaIngrediente.TABLA, cv, condicion, argumentos);
    }

    public Cursor getCursor() {
        return db.query(Recetario.TablaReceta.TABLA, null, null, null, null, null,null);
    }

    public RecetaIngrediente getRow(Cursor c) {
        RecetaIngrediente r = new RecetaIngrediente();
        r.setId(c.getLong(c.getColumnIndex(Recetario.TablaRecetaIngrediente._ID)));
        r.setIdReceta(c.getLong(c.getColumnIndex(Recetario.TablaRecetaIngrediente.IDRECETA)));
        r.setIdIngrediente(c.getLong(c.getColumnIndex(Recetario.TablaRecetaIngrediente.IDINGREDIENTE)));
        r.setCantidad(c.getString(c.getColumnIndex(Recetario.TablaRecetaIngrediente.CANTIDAD)));
        return r;
    }

    public List<RecetaIngrediente> selectCantidadesReceta(Receta r){
        String condicion = Recetario.TablaRecetaIngrediente.IDRECETA + " = ?";
        String[] argumentos = { Long.toString(r.getId()) };
        Cursor cursor = db.query(Recetario.TablaRecetaIngrediente.TABLA, null, condicion, argumentos, null, null, null);
        cursor.moveToFirst();
        List<RecetaIngrediente> all = new ArrayList<>();
        RecetaIngrediente recetaIngrediente;
        while (!cursor.isAfterLast()) {
            recetaIngrediente = getRow(cursor);
            all.add(recetaIngrediente);
            cursor.moveToNext();
        }
        cursor.close();
        return all;
    }
    public List<RecetaIngrediente> selectCantidadesIngrediente(long idReceta, long idIngrediente){
        String condicion = Recetario.TablaRecetaIngrediente.IDRECETA + " = ? and "
                + Recetario.TablaRecetaIngrediente.IDINGREDIENTE + " = ?";
        String[] argumentos = { Long.toString(idReceta),Long.toString(idIngrediente) };
        Cursor cursor = db.query(Recetario.TablaRecetaIngrediente.TABLA, null, condicion, argumentos, null, null, null);
        Log.v(MainActivity.TAG, "cursor: "+cursor.getCount());
        cursor.moveToFirst();
        List<RecetaIngrediente> all = new ArrayList<>();
        RecetaIngrediente recetaIngrediente;
        while (!cursor.isAfterLast()) {
            recetaIngrediente = getRow(cursor);
            Log.v(MainActivity.TAG, "row: "+recetaIngrediente.getId());
            Log.v(MainActivity.TAG, "row: "+recetaIngrediente.getIdReceta());
            Log.v(MainActivity.TAG, "row: "+recetaIngrediente.getIdIngrediente());
            Log.v(MainActivity.TAG, "row: "+recetaIngrediente.getCantidad());
            all.add(recetaIngrediente);
            cursor.moveToNext();
        }
        cursor.close();
        return all;
    }

    public List<RecetaIngrediente> selectCantidades(){
        List<RecetaIngrediente> all = new ArrayList<>();
        Cursor cursor = getCursor();
        cursor.moveToFirst();
        RecetaIngrediente recetaIngrediente;
        while (!cursor.isAfterLast()) {
            recetaIngrediente = getRow(cursor);
            all.add(recetaIngrediente);
            cursor.moveToNext();
        }
        cursor.close();
        return all;
    }

}
