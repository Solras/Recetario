package com.example.dam.recetario;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dam.recetario.receta.GestorReceta;
import com.example.dam.recetario.receta.Receta;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Adaptador extends ArrayAdapter<Receta>{

    //private Context ctx;
    private int res;
    private LayoutInflater lInflator;
    private ArrayList<Receta> values;
    private Context con;
    GestorReceta gestorR;

    static class ViewHolder{

        public TextView tv;
        public ImageView iv;

    }


    public Adaptador(Context context, ArrayList<Receta> objects) {
        super(context, R.layout.list_item, objects);
        this.res= R.layout.list_item;
        this.values= objects;
        this.con= context;
        lInflator=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.gestorR = new GestorReceta(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder gv= new ViewHolder();
        if(convertView==null){
            convertView= lInflator.inflate(res, null);

            TextView tv= (TextView) convertView.findViewById(R.id.tvList);
            gv.tv=tv;
            ImageView iv= (ImageView) convertView.findViewById(R.id.ivList);
            gv.iv= iv;

            convertView.setTag(gv);
        }else{
            gv= (ViewHolder) convertView.getTag();
        }

        gv.tv.setText(values.get(position).getNombre());

        gestorR.openRead();
        List<Receta> recetas = gestorR.selectRecetas();

        gestorR.close();
        recetas.get(position).getFoto();

        File f = new  File(recetas.get(position).getFoto());


        Bitmap myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());

        gv.iv.setImageBitmap(myBitmap);

//        gv.iv.setImageBitmap(stringToBitMap(values.get(position).getFoto()));


        return convertView;
    }

    public void delete(Receta r){
        gestorR.delete(r);
        ArrayList<Receta> recetas = (ArrayList) gestorR.selectRecetas();
        values = recetas;
        notifyDataSetChanged();
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
