package co.edu.udea.compumovil.gr07_20171.lab2.Fragments;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import co.edu.udea.compumovil.gr07_20171.lab2.DB.BDHelper;
import co.edu.udea.compumovil.gr07_20171.lab2.DB.DataBase;
import co.edu.udea.compumovil.gr07_20171.lab2.R;
import co.edu.udea.compumovil.gr07_20171.lab2.RegisterActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class login extends Fragment {

    private Button btnRegistrase;
    private Button btnIniciarSesion;
    private EditText txtUsuario;
    private EditText txtContraseña;
    private BDHelper dBHelper;
    private SQLiteDatabase db;

    public login() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        btnRegistrase = (Button)v.findViewById(R.id.btnRegistrarse);
        btnIniciarSesion = (Button)v.findViewById(R.id.btnLogin);
        txtUsuario = (EditText) v.findViewById(R.id.txtUsuarioLogin);
        txtContraseña = (EditText) v.findViewById(R.id.txtContraseñaLogin);
        btnRegistrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick2(2);
            }
        });
        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick2(1);
            }
        });

        return v;
    }


    public void onClick2(int view) {
        switch (view){
            case 2:
                dBHelper = new BDHelper(getActivity().getApplicationContext());
                db =  dBHelper.getWritableDatabase();
                String usuario = txtUsuario.getText().toString();
                String contraseña = txtContraseña.getText().toString();

                Cursor cursorUser = db.rawQuery("select * from " + DataBase.TABLE_USER + " where " + DataBase.column_user.USER + " = '" + usuario + "' AND " + DataBase.column_user.PASSWORD + " = '" + contraseña+"'", null);

                if(cursorUser.moveToFirst()) {
                    String idUser = cursorUser.getString(cursorUser.getColumnIndex(DataBase.column_user.ID));
                    ContentValues valores = new ContentValues();
                    valores.put(DataBase.column_user.STATUS, "ENABLE");
                    db.updateWithOnConflict(DataBase.TABLE_USER, valores, DataBase.column_user.ID + "=" + idUser, null, SQLiteDatabase.CONFLICT_IGNORE);
                    Toast.makeText(getActivity().getApplicationContext(), "Welcome", Toast.LENGTH_SHORT).show();

                    //refrescar activity
                    FragmentManager fragmentManager;
                    FragmentTransaction ft;
                    Fragment fragment;
                    fragmentManager = getActivity().getSupportFragmentManager();
                    ft = fragmentManager.beginTransaction();

                    fragment = new profile();
                    ft.replace(R.id.fragmentContainer, fragment);
                    ft.commit();
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "Incorrect Data", Toast.LENGTH_SHORT).show();
                }
                db.close();

                break;
            case 1:
                Intent intent = new Intent(getContext(), RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
}
