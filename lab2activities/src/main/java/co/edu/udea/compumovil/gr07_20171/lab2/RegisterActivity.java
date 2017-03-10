package co.edu.udea.compumovil.gr07_20171.lab2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import co.edu.udea.compumovil.gr07_20171.lab2.DB.BDHelper;
import co.edu.udea.compumovil.gr07_20171.lab2.DB.DataBase;

public class RegisterActivity extends AppCompatActivity {

    EditText txtUsuario;
    EditText txtContraseña;
    EditText txtCorreo;
    BDHelper dBHelper;
    SQLiteDatabase dataBase;
    ContentValues valores;
    int cont = 0;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtUsuario = (EditText)findViewById(R.id.txtUsuario);
        txtContraseña = (EditText)findViewById(R.id.txtContraseña);
        txtCorreo = (EditText)findViewById(R.id.txtCorreo);

    }

    public void registraUsuario(View view){
        dBHelper = new BDHelper(this);
        dataBase = dBHelper.getWritableDatabase(); //obtener una instancia de la base de Datos. No crea una bases de datos nueva en caso de que ya exista una anterior sino que devuelve esa instacia ya creada

        String user = txtUsuario.getText().toString();
        String pass = txtContraseña.getText().toString();
        String email =  txtCorreo.getText().toString();

        Cursor cursorU = dataBase.rawQuery("select * from " + DataBase.TABLE_USER + " where " + DataBase.column_user.USER + " = '" + user + "' or " + DataBase.column_user.EMAIL + " = '" + email+"'", null);

        if (user.equals("") || pass.equals("") || email.equals("")){
            Toast.makeText(this,"Ninugno de los campos puede estar vacío",Toast.LENGTH_SHORT).show();
        }else if(cursorU.moveToFirst()){
            Toast.makeText(this,"El nombre usuario o email ya estan en uso", Toast.LENGTH_SHORT).show();
        }else{
            valores = new ContentValues(); //se crea un nuevo elemento valores
            valores.put(DataBase.column_user.USER, user); //se insertan los ṕares de elementos: clave-valor
            valores.put(DataBase.column_user.PASSWORD, pass);
            valores.put(DataBase.column_user.EMAIL, email);

            //validaciones

            dataBase.insertWithOnConflict(DataBase.TABLE_USER , null, valores, SQLiteDatabase.CONFLICT_IGNORE);//se inserta en la base de datos el elemento valores en la tabla USER
            Toast.makeText(this, "Creado", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }

        //se cierra la BD
        dataBase.close();

    }
}
