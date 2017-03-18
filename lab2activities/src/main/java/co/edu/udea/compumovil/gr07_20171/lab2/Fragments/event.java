package co.edu.udea.compumovil.gr07_20171.lab2.Fragments;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.ByteArrayOutputStream;

import co.edu.udea.compumovil.gr07_20171.lab2.DB.BDHelper;
import co.edu.udea.compumovil.gr07_20171.lab2.DB.DataBase;
import co.edu.udea.compumovil.gr07_20171.lab2.R;
import co.edu.udea.compumovil.gr07_20171.lab2.RecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class event extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private SQLiteDatabase dataBase;
    private BDHelper dBHelper;

    public event() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_event, container, false);

        recyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        dBHelper = new BDHelper(getActivity().getApplicationContext());
        dataBase =  dBHelper.getWritableDatabase();

        Cursor cursorEvent = dataBase.rawQuery("select * from " + DataBase.TABLE_EVENT,null);

        if(cursorEvent.getCount() < 5){
            insertarEventos();
            cursorEvent = dataBase.rawQuery("select * from " + DataBase.TABLE_EVENT,null);
        }


        int cont = 0;
        String[] titulos = new String[cursorEvent.getCount()];
        String[] detalles = new String[cursorEvent.getCount()];
        byte[][] imagenes = new byte[cursorEvent.getCount()][];
        float[] puntos = new float[cursorEvent.getCount()];
        String[] ids = new String[cursorEvent.getCount()];
        String[] fechas = new String[cursorEvent.getCount()];
        String[] managers = new String[cursorEvent.getCount()];
        String[] ubicaciones = new String[cursorEvent.getCount()];


        while (cursorEvent.moveToNext()){
            titulos[cont] = cursorEvent.getString(cursorEvent.getColumnIndex(DataBase.column_event.NAME));
            detalles[cont] = cursorEvent.getString(cursorEvent.getColumnIndex(DataBase.column_event.GENERAL_INFORMATION));
            imagenes[cont] = cursorEvent.getBlob(cursorEvent.getColumnIndex(DataBase.column_event.PHOTO_EVENT));
            puntos[cont] = Float.parseFloat(cursorEvent.getString(cursorEvent.getColumnIndex(DataBase.column_event.SCORE)));
            ids[cont] = cursorEvent.getString(cursorEvent.getColumnIndex(DataBase.column_event.CODE));
            fechas[cont] = cursorEvent.getString(cursorEvent.getColumnIndex(DataBase.column_event.DATE));
            managers[cont] = cursorEvent.getString(cursorEvent.getColumnIndex(DataBase.column_event.MANAGER));
            ubicaciones[cont] = cursorEvent.getString(cursorEvent.getColumnIndex(DataBase.column_event.LOCATION));

            cont++;
            //Actualizar los parametros del Adapter

        }


        adapter = new RecyclerAdapter(titulos,detalles,imagenes,puntos,fechas,managers,ubicaciones,ids);
        recyclerView.setAdapter(adapter);
        return v;
    }

    private void insertarEventos(){
        ContentValues valores;

        String[] name = {"Ruta 1","Ruta 2","Ruta 3","Ruta 4","Ruta 5"};
        String[] descripcion = {
                "Ciclopaseo pasando por lugares rurales",
                "Travesía en Guatapé",
                "Principiantes en Copacabana",
                "Rodando con las mujeres",
                "Rodando con los parceros"};
        String[] ubicacion={"ubicacion1","ubicacion2","ubicacion3","ubicacion4","ubicacion5"};
        String[] manager={"Pedro","Pablo","Jacinto","José","Antonio"};
        String[] fechas={"2017/03/24","2017/04/2","2017/04/8","2017/04/12","2017/04/18"};
        int[] images = {
                R.drawable.oie_transparent,
                R.drawable.guatape,
                R.drawable.copacabana,
                R.drawable.mujeres,
                R.drawable.hombres};
        float[] puntos = {3f,5f,3.4f,4.5f,3.5f};

        Bitmap bmpImagen;
        ByteArrayOutputStream byteImagen;

        for (int i=0; i<5;i++){
            valores = new ContentValues();
            bmpImagen = BitmapFactory.decodeResource(getResources(), images[i]);
            byteImagen = new ByteArrayOutputStream();
            bmpImagen.compress(Bitmap.CompressFormat.PNG, 100, byteImagen);

            valores.put(DataBase.column_event.NAME,name[i]);
            valores.put(DataBase.column_event.GENERAL_INFORMATION,descripcion[i]);
            valores.put(DataBase.column_event.LOCATION,ubicacion[i]);
            valores.put(DataBase.column_event.MANAGER,manager[i]);
            valores.put(DataBase.column_event.DATE,fechas[i]);;
            valores.put(DataBase.column_event.SCORE,puntos[i]);
            valores.put(DataBase.column_event.PHOTO_EVENT, byteImagen.toByteArray());

            dataBase.insertWithOnConflict(DataBase.TABLE_EVENT, null, valores, SQLiteDatabase.CONFLICT_IGNORE);
        }
    }


}
