package com.example.dam.recetario;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.dam.recetario.ingrediente.GestorIngrediente;
import com.example.dam.recetario.ingrediente.Ingrediente;

import java.util.List;

public class IncluirIngredientes extends AppCompatActivity{

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
            gestorI = new GestorIngrediente(this);
            gestorI.openWrite();

            vl = (LinearLayout) findViewById(R.id.lVerticalIngredientes);
            lista = gestorI.selectIngredientes();
            for (Ingrediente ing : lista) {
                LinearLayout horizontal = new LinearLayout(this);
                CheckBox check = new CheckBox(this);
                TextView tv = new TextView(this);
                EditText et = new EditText(this);
                et.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                check.setText("");
                tv.setText(ing.getNombre());
                horizontal.addView(check);
                horizontal.addView(tv);
                horizontal.addView(et);
                vl.addView(horizontal);
            }
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
                    LinearLayout horizontal = new LinearLayout(IncluirIngredientes.this);
                    CheckBox check = new CheckBox(IncluirIngredientes.this);
                    TextView tv = new TextView(IncluirIngredientes.this);
                    EditText edit = new EditText(IncluirIngredientes.this);
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
