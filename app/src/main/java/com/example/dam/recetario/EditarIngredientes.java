package com.example.dam.recetario;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dam.recetario.ingrediente.GestorIngrediente;
import com.example.dam.recetario.ingrediente.Ingrediente;
import com.example.dam.recetario.receta.Receta;
import com.example.dam.recetario.receta_ingrediente.GestorRecetaIngrediente;
import com.example.dam.recetario.receta_ingrediente.RecetaIngrediente;

import java.util.ArrayList;
import java.util.List;

public class EditarIngredientes extends AppCompatActivity{

    private GestorIngrediente gestorI;
    private List<Ingrediente> lista;
    private LinearLayout vl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incluir_ingredientes);
        this.init();
    }

    public void init() {
        Receta miReceta = (Receta) getIntent().getSerializableExtra("receta");
        gestorI = new GestorIngrediente(this);
        gestorI.openWrite();

        GestorRecetaIngrediente gestorC = new GestorRecetaIngrediente(this);
        gestorC.openRead();

        List<RecetaIngrediente> recetaIngredientes = gestorC.selectCantidadesReceta(miReceta);
        List<String> cantidades= new ArrayList<>();
        for (RecetaIngrediente cant : recetaIngredientes) {
            cantidades.add(cant.getCantidad());
        }

        vl = (LinearLayout) findViewById(R.id.lVerticalIngredientes);
        lista = gestorI.selectIngredientes();
        for (Ingrediente ing : lista) {
            LinearLayout horizontal = new LinearLayout(this);
            CheckBox check = new CheckBox(this);
            check.setText("");
            if(searchid(ing.getId(), recetaIngredientes))
                check.setChecked(true);
            TextView tv = new TextView(this);
            tv.setText(ing.getNombre());
            EditText et = new EditText(this);
            et.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            et.setText(searchCantidad(ing.getId(),recetaIngredientes));
            horizontal.addView(check);
            horizontal.addView(tv);
            horizontal.addView(et);
            vl.addView(horizontal);
        }
    }

    private boolean searchid(long idIngrediente, List<RecetaIngrediente> recing){
        for (RecetaIngrediente cant : recing) {
            if(cant.getIdIngrediente()==idIngrediente)
                return true;
        }
        return false;
    }

    private String searchCantidad(long idIngrediente, List<RecetaIngrediente> recing){
        for (RecetaIngrediente cant : recing) {
            if(cant.getIdIngrediente()==idIngrediente)
                return cant.getCantidad();
        }
        return "";
    }

    public void nuevoIngrediente(View v) {

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Nuevo ingrediente");

        final View view = LayoutInflater.from(this).inflate(R.layout.dialog_nuevo_ingrediente, null);

        alert.setView(view);

        DialogInterface.OnClickListener listenerSearch = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                EditText et = (EditText) view.findViewById(R.id.etNew);
                String nuevo = et.getText().toString();
                Ingrediente ingre = new Ingrediente(nuevo);
                gestorI.insert(ingre);

                LinearLayout vl = (LinearLayout) findViewById(R.id.lVerticalIngredientes);
                LinearLayout horizontal = new LinearLayout(EditarIngredientes.this);
                CheckBox check = new CheckBox(EditarIngredientes.this);
                TextView tv = new TextView(EditarIngredientes.this);
                EditText edit = new EditText(EditarIngredientes.this);
                edit.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                check.setText("");
                tv.setText(nuevo);
                horizontal.addView(check);
                horizontal.addView(tv);
                horizontal.addView(edit);
                vl.addView(horizontal);
            }
        };
        alert.setPositiveButton("Agregar", listenerSearch);
        alert.setNegativeButton("Cancelar", null);
        alert.show();
    }

    public void incluir(View v) {
        int children = vl.getChildCount();
        String[] ingredientes = new String[0];

        for (int i = 0; i < children; i++) {
            LinearLayout hl = (LinearLayout) vl.getChildAt(i);
            CheckBox cb = (CheckBox) hl.getChildAt(0);
            TextView tv = (TextView) hl.getChildAt(1);
            EditText et = (EditText) hl.getChildAt(2);
            if (cb.isChecked()) {
                String[] masingredientes = new String[ingredientes.length + 2];
                int j = 0;
                if (ingredientes.length != 0) {
                    for (j = 0; j < ingredientes.length; j++) {
                        masingredientes[j] = ingredientes[j];
                    }
                }
                masingredientes[j] = tv.getText().toString();
                masingredientes[j+1] = et.getText().toString();
                ingredientes= masingredientes;
            }
        }

        gestorI.close();
        this.getIntent().putExtra("ingre", ingredientes);
        setResult(RESULT_OK, this.getIntent());
        finish();
    }
    public void cancel(View v){
        finish();
    }
}
