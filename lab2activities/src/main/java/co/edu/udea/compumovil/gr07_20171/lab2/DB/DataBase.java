package co.edu.udea.compumovil.gr07_20171.lab2.DB;

import android.provider.BaseColumns;

public class DataBase {

    public static final  String DB_NAME= "Lab2DB.db";
    public static final int BD_VERSION=1;
    public static final String TABLE_USER= "user";
    public static final String TABLE_EVENT= "event";

    public class column_user{
        public static final String ID= BaseColumns._ID;
        public static final String USER = "user";
        public static final String EMAIL= "email";
        public static final String PASSWORD= "password";
        public static final String AGE= "age";
        public static final String PHOTO_USER= "photo_user";
        public static final String STATUS= "status";

    }

    public class column_event{
        public static final String CODE= BaseColumns._ID;
        public static final String NAME = "name";
        public static final String MANAGER= "manager";
        public static final String PHOTO_EVENT= "photo_event";
        public static final String SCORE= "score";
        public static final String DATE= "date";
        public static final String LOCATION= "location";
        public static final String GENERAL_INFORMATION= "general_information";
    }
}
