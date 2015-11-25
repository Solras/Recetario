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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dam.recetario.ingrediente.GestorIngrediente;
import com.example.dam.recetario.ingrediente.Ingrediente;
import com.example.dam.recetario.receta.GestorReceta;
import com.example.dam.recetario.receta.Receta;
import com.example.dam.recetario.receta_ingrediente.GestorRecetaIngrediente;
import com.example.dam.recetario.receta_ingrediente.RecetaIngrediente;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class EditarReceta extends AppCompatActivity{

    private List<String[]> nuevosIngre;
    final int INGREDIENTES = 0;
    final int CAMERA = 1;
    final int GALLERY = 2;
    Dialog alert;

    private TextView tvIngredientes;
    private EditText etNombre;
    private EditText etInstrucciones;
    private ImageView ivFoto;
    private Receta miReceta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_receta);
        this.ivFoto = (ImageView) findViewById(R.id.ivFoto);
        this.etInstrucciones = (EditText) findViewById(R.id.etInstrucciones);
        this.etNombre = (EditText) findViewById(R.id.etNombre);
        this.tvIngredientes = (TextView) findViewById(R.id.tvIngredientes);
        nuevosIngre = new ArrayList<>();
        miReceta = (Receta) getIntent().getExtras().getSerializable("receta");


        GestorRecetaIngrediente gestorC = new GestorRecetaIngrediente(this);
        gestorC.openRead();
        GestorIngrediente gestorI = new GestorIngrediente(this);
        gestorI.openRead();
        etNombre.setText(miReceta.getNombre());
        List<RecetaIngrediente> recetaIngredientes = gestorC.selectCantidadesReceta(miReceta);
        List<String> ingr= new ArrayList<>(), cantidades= new ArrayList<>();
        for (RecetaIngrediente cant : recetaIngredientes) {
            ingr.add(gestorI.selectNombreIngrediente(cant.getIdIngrediente()));
            cantidades.add(cant.getCantidad());
        }
        tvIngredientes.setText("Ingredientes:\n");
        for (int i=0; i<cantidades.size(); i++){
            tvIngredientes.append(ingr.get(i) + " " + cantidades.get(i) +"\n");
        }
        etInstrucciones.setText(miReceta.getInstrucciones());
        Uri uri = Uri.parse(miReceta.getFoto());

        ivFoto.setImageURI(uri);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch(requestCode) {
                case INGREDIENTES:
                    if (resultCode == RESULT_OK) {
                        String[] ingredientes = data.getExtras().getStringArray("ingre");
                        tvIngredientes.setText("");
                        for (int i = 0; i < ingredientes.length; i+=2) {
                            tvIngredientes.append(ingredientes[i]+" "+ingredientes[i+1]+"\n");
                            nuevosIngre.add(new String[]{ingredientes[i],ingredientes[i+1]});
                        }
                    }
                    break;
                case CAMERA:
                    if(resultCode == RESULT_OK) {
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
                    }
                        break;
                case GALLERY:
                    if(resultCode == RESULT_OK){
                        try {
                            Uri selectedImage = data.getData();
                            Bitmap bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
                            String path = bitMapToString(bmp);
                            ivFoto.setTag(path);
                            ivFoto.setImageBitmap(bmp);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
        Intent i = new Intent(this, EditarIngredientes.class);
        Bundle b = new Bundle();
        b.putSerializable("receta", miReceta);
        i.putExtras(b);
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

        String foto=miReceta.getFoto();
        if(ivFoto.getTag()!= null && !ivFoto.getTag().toString().equals(foto)){
            miReceta.setFoto(ivFoto.getTag().toString());
        }
        String nombre = miReceta.getNombre();
        if(!etNombre.getText().toString().equals(nombre)){
            miReceta.setNombre(etNombre.getText().toString());
        }
        String instrucciones = miReceta.getInstrucciones();
        if(!etInstrucciones.getText().toString().equals(instrucciones)){
            miReceta.setInstrucciones(etInstrucciones.getText().toString());
        }
        gestorR.update(miReceta);
        long idIngrediente=gestorI.selectIdIngrediente(nuevosIngre.get(0)[0]);
        List<RecetaIngrediente> lista = new ArrayList<>();
                gestorC.selectCantidadesIngrediente(miReceta.getId(),idIngrediente);
        Log.v(MainActivity.TAG, "idingrediente: " + idIngrediente);
        Log.v(MainActivity.TAG, "idReceta: "+miReceta.getId());
        for (RecetaIngrediente ri : lista) {
            Log.v(MainActivity.TAG, "ri: "+ri.getCantidad());
        }
//        for (String[] ing : nuevosIngre) {
//            long idIngrediente=gestorI.selectIdIngrediente(ing[0]);
//            RecetaIngrediente c = new RecetaIngrediente(idReceta,idIngrediente,ing[1]);
//            gestorC.insert(c);
//        }
//        RecetaIngrediente ingre = new RecetaIngrediente(miReceta.getId(),0,"");

        gestorC.close();
        gestorI.close();
        gestorR.close();
        finish();
    }

//    public Bitmap stringToBitMap(String encodedString) {
//        try {
//            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
//            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
//                    encodeByte.length);
//            return bitmap;
//        } catch (Exception e) {
//            e.getMessage();
//            return null;
//        }
//    }
    public void cancel(View v){
        finish();
    }
}
