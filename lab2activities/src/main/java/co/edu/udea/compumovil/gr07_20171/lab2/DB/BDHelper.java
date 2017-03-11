package co.edu.udea.compumovil.gr07_20171.lab2.DB;

import android.content.Context;
import android.database.sqlite.*;
import  android.util.Log;


public class BDHelper extends SQLiteOpenHelper {

    public BDHelper(Context context){
        super(context, DataBase.DB_NAME,null,DataBase.BD_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //se crea tabla para usuario
        String create_table_user= String.format("create table %s (%s integer primary key autoincrement, "+
                                                                 "%s text,"+
                                                                 "%s text,"+
                                                                 "%s text,"+
                                                                 "%s int,"+
                                                                 "%s blob,"+
                                                                 "%s text);",
                                                                  DataBase.TABLE_USER,
                                                                  DataBase.column_user.ID,
                                                                  DataBase.column_user.USER,
                                                                  DataBase.column_user.EMAIL,
                                                                  DataBase.column_user.PASSWORD,
                                                                  DataBase.column_user.AGE,
                                                                  DataBase.column_user.PHOTO_USER,
                                                                  DataBase.column_user.STATUS);
        db.execSQL(create_table_user);

        //se crea tabla para eventos
        String create_table_event= String.format("create table %s (%s integer primary key autoincrement, "+
                        "%s text,"+
                        "%s text,"+
                        "%s blob,"+
                        "%s int,"+
                        "%s text,"+
                        "%s text,"+
                        "%s text);",
                DataBase.TABLE_EVENT,
                DataBase.column_event.CODE,
                DataBase.column_event.NAME,
                DataBase.column_event.MANAGER,
                DataBase.column_event.PHOTO_EVENT,
                DataBase.column_event.SCORE,
                DataBase.column_event.DATE,
                DataBase.column_event.MANAGER,
                DataBase.column_event.GENERAL_INFORMATION);

        db.execSQL(create_table_event);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("Tag","OnUpgade");

    }
}
