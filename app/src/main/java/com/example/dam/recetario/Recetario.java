package com.example.dam.recetario;

import android.provider.BaseColumns;


public class Recetario {
    private Recetario(){
    }
    public static abstract class TablaReceta implements
            BaseColumns{
        public static final String TABLA = "recetario";
        public static final String NOMBRE = "nombre";
        public static final String FOTO = "foto";
        public static final String INSTRUCCIONES = "instrucciones";
    }
    public static abstract class TablaIngrediente implements
            BaseColumns{
        public static final String TABLA = "ingrediente";
        public static final String NOMBRE = "nombre";
    }
    public static abstract class TablaRecetaIngrediente implements
            BaseColumns{
        public static final String TABLA = "recetaIngrediente";
        public static final String IDRECETA = "idreceta";
        public static final String IDINGREDIENTE = "idingrediente";
        public static final String CANTIDAD = "cantidad";
    }
}