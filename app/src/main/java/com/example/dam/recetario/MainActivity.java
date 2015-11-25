package com.example.dam.recetario;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.dam.recetario.receta.GestorReceta;
import com.example.dam.recetario.receta.Receta;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static String TAG= "MIRECETARIO";

    private Adaptador ad;
    private android.widget.ListView lv;
    private List<Receta> recetas;
    private GestorReceta gestorR;
    private final int EDITAR=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gestorR = new GestorReceta(this);
    }

    @Override
    protected void onResume() {
        gestorR.openWrite();
        super.onResume();
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.agregar: {
                Intent i = new Intent(this, AgregarReceta.class);
                startActivity(i);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch(requestCode) {
                case EDITAR:

                    break;
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = menuInfo.position;
        View v = menuInfo.targetView;
        switch (item.getItemId()) {
            case R.id.menu_editar: {
                Intent i = new Intent(this, EditarReceta.class);
                Bundle b = new Bundle();
                b.putSerializable("receta", recetas.get(position));
                i.putExtras(b);
                startActivityForResult(i, EDITAR);
                return true;
            }
            case R.id.menu_borrar: {
                    ad.delete(recetas.get(position));
                return true;
            }
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contextual, menu);
    }

    public void init(){
        this.lv = (ListView) findViewById(R.id.listView);

        recetas = gestorR.selectRecetas();
        ad = new Adaptador(this,(ArrayList<Receta>) recetas);
        lv.setAdapter(ad);
        lv.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent i = new Intent(MainActivity.this, Mostrar.class);
                        i.putExtra("receta", gestorR.selectRecetas().get(position));
                        startActivity(i);
                    }
                }
        );

//        gestorR.close();
        this.registerForContextMenu(lv);

    }



}
