package com.example.dam.recetario;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.dam.recetario.ingrediente.GestorIngrediente;
import com.example.dam.recetario.receta.GestorReceta;
import com.example.dam.recetario.receta.Receta;
import com.example.dam.recetario.receta_ingrediente.GestorRecetaIngrediente;
import com.example.dam.recetario.receta_ingrediente.RecetaIngrediente;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class AgregarReceta extends AppCompatActivity{

    private List<String[]> ingreParaTabla;
    final int INGREDIENTES = 0;
    final int CAMERA = 1;
    final int GALLERY = 2;
    Dialog alert;

    private android.widget.TextView tvIngredientes;
    private android.widget.EditText etNombre;
    private android.widget.EditText etInstrucciones;
    private ImageView ivFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_receta);
        this.ivFoto = (ImageView) findViewById(R.id.ivFoto);
        this.etInstrucciones = (EditText) findViewById(R.id.etInstrucciones);
        this.etNombre = (EditText) findViewById(R.id.etNombre);
        this.tvIngredientes = (TextView) findViewById(R.id.tvIngredientes);
        ingreParaTabla = new ArrayList<>();
        ivFoto.setTag("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch(requestCode) {
                case INGREDIENTES:
                        String[] ingredientes = data.getExtras().getStringArray("ingre");
                        tvIngredientes.setText("");
                        for (int i = 0; i < ingredientes.length; i+=2) {
                            tvIngredientes.append(ingredientes[i]+" "+ingredientes[i+1]+"\n");
                            ingreParaTabla.add(new String[]{ingredientes[i],ingredientes[i+1]});
                        }
                    break;
                case CAMERA:
                        FileOutputStream out = null;
                        try {
                            Bundle extras = data.getExtras();
                            Bitmap bmp = (Bitmap) extras.get("data");
                            File f = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                            String path = f.getPath() + "/" + new GregorianCalendar().getTime().toString().trim() + ".jpg";
                            out = new FileOutputStream(path);
                            Log.v(MainActivity.TAG, ": "+path);
                            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
                            ivFoto.setTag(path);
                            ivFoto.setImageBitmap(bmp);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                case GALLERY:
                        try {
                            Uri selectedImage = data.getData();
                            Bitmap bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
                            String path = bitMapToString(bmp);
                            ivFoto.setTag(path);
                            ivFoto.setImageBitmap(bmp);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    break;
            }
        }
    }

    private String bitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public void ingredientes(View v){
        Intent i = new Intent(this, IncluirIngredientes.class);
        startActivityForResult(i, INGREDIENTES);
    }

    public void chooser (View v){
//        alert = new Dialog(this);
//        alert.setTitle("Eleccion");
//
//        final View view = LayoutInflater.from(this).inflate(R.layout.dialog_incluir_foto, null);
//
//        alert.setContentView(view);
//
//        alert.show();
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePhotoIntent, CAMERA);
    }

    public void eleccionGaleria(View v){
        Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pickIntent.setType("image/*");
        startActivityForResult(pickIntent, GALLERY);
        alert.cancel();
    }
    public void eleccionCamara(View v){
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePhotoIntent, CAMERA);
        alert.cancel();
    }

    public void terminarReceta(View v){
        GestorReceta gestorR = new GestorReceta(this);
        gestorR.openWrite();
        GestorRecetaIngrediente gestorC = new GestorRecetaIngrediente(this);
        gestorC.openWrite();
        GestorIngrediente gestorI = new GestorIngrediente(this);
        gestorI.openRead();

        String foto="";
        if(!ivFoto.getTag().toString().isEmpty()){
            foto=ivFoto.getTag().toString();
        }

        Receta r = new Receta(etNombre.getText().toString(),foto,etInstrucciones.getText().toString());
        long idReceta = gestorR.insert(r);

        for (String[] ing : ingreParaTabla) {
            long idIngrediente=gestorI.selectIdIngrediente(ing[0]);
            RecetaIngrediente c = new RecetaIngrediente(idReceta,idIngrediente,ing[1]);
            gestorC.insert(c);
        }

        gestorC.close();
        gestorI.close();
        gestorR.close();
        finish();
    }
    public void cancel(View v){
        finish();
    }
}
