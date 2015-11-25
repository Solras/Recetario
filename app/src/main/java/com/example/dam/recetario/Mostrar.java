package com.example.dam.recetario;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dam.recetario.ingrediente.GestorIngrediente;
import com.example.dam.recetario.receta.GestorReceta;
import com.example.dam.recetario.receta.Receta;
import com.example.dam.recetario.receta_ingrediente.GestorRecetaIngrediente;
import com.example.dam.recetario.receta_ingrediente.RecetaIngrediente;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Mostrar extends AppCompatActivity {

    private TextView tvNombre;
    private TextView tvMostrarIngredientes;
    private TextView tvMostrarInstrucciones;
    private android.widget.ImageView ivFotoMostrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar);
        this.ivFotoMostrar = (ImageView) findViewById(R.id.ivFotoMostrar);
        this.tvMostrarInstrucciones = (TextView) findViewById(R.id.tvMostrarInstrucciones);
        this.tvMostrarIngredientes = (TextView) findViewById(R.id.tvMostrarIngredientes);
        this.tvNombre = (TextView) findViewById(R.id.tvNombre);
        Receta r = (Receta) getIntent().getSerializableExtra("receta");

        GestorReceta gestorR = new GestorReceta(this);
        gestorR.openRead();
        GestorRecetaIngrediente gestorC = new GestorRecetaIngrediente(this);
        gestorC.openRead();
        GestorIngrediente gestorI = new GestorIngrediente(this);
        gestorI.openRead();

        List<RecetaIngrediente> recetaIngredientes = gestorC.selectCantidadesReceta(r);
        List<String> ingr= new ArrayList<>(), cantidades= new ArrayList<>();
        for (RecetaIngrediente cant : recetaIngredientes) {
            ingr.add(gestorI.selectNombreIngrediente(cant.getIdIngrediente()));
            cantidades.add(cant.getCantidad());
        }

        tvNombre.setText(r.getNombre());

        Uri uri = Uri.parse(r.getFoto());

        ivFotoMostrar.setImageURI(uri);

//        ivFotoMostrar.setImageBitmap(stringToBitMap(r.getFoto()));

        tvMostrarIngredientes.setText("Ingredientes:\n");
        for (int i=0; i<cantidades.size(); i++){
            tvMostrarIngredientes.append(ingr.get(i) + " - " + cantidades.get(i) +"\n");
        }
        tvMostrarInstrucciones.setText("Elaboración:\n" + r.getInstrucciones());
    }

    public Bitmap stringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

}
