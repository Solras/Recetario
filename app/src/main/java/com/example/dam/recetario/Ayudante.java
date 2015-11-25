package com.example.dam.recetario;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Ayudante extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "recetario.sqlite";
    public static final int DATABASE_VERSION = 1;

    public Ayudante(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table if exists " + Recetario.TablaReceta.TABLA;
        db.execSQL(sql);
        sql = "drop table if exists " + Recetario.TablaIngrediente.TABLA;
        db.execSQL(sql);
        sql = "drop table if exists " + Recetario.TablaRecetaIngrediente.TABLA;
        db.execSQL(sql);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v("SQLAAD","onCreate");
        String sql;
        sql = "create table " + Recetario.TablaReceta.TABLA +
                " (" + Recetario.TablaReceta._ID +
                " integer primary key autoincrement, " +
                Recetario.TablaReceta.NOMBRE + " text, " +
                Recetario.TablaReceta.INSTRUCCIONES + " text, " +
                Recetario.TablaReceta.FOTO + " text)";
        Log.v("SQLAAD", sql);
        db.execSQL(sql);
        sql = "create table " + Recetario.TablaIngrediente.TABLA +
                " (" + Recetario.TablaIngrediente._ID +
                " integer primary key autoincrement, " +
                Recetario.TablaIngrediente.NOMBRE + " text)";
        Log.v("SQLAAD", sql);
        db.execSQL(sql);
        sql = "create table " + Recetario.TablaRecetaIngrediente.TABLA +
                " (" + Recetario.TablaRecetaIngrediente._ID +
                " integer primary key autoincrement, " +
                Recetario.TablaRecetaIngrediente.IDRECETA + " text, " +
                Recetario.TablaRecetaIngrediente.IDINGREDIENTE + " text, " +
                Recetario.TablaRecetaIngrediente.CANTIDAD + " text)";
        Log.v("SQLAAD", sql);
        db.execSQL(sql);
    }
}