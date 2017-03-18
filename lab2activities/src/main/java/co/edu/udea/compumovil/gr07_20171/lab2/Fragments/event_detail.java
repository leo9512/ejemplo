package co.edu.udea.compumovil.gr07_20171.lab2.Fragments;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.edu.udea.compumovil.gr07_20171.lab2.DB.BDHelper;
import co.edu.udea.compumovil.gr07_20171.lab2.DB.DataBase;
import co.edu.udea.compumovil.gr07_20171.lab2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class event_detail extends Fragment {

    private TextView  txtName;
    private TextView txtMan;
    private TextView txtLocation;
    private TextView txtInfo_general;

    private String id;

    private BDHelper dBHelper;
    private SQLiteDatabase dataBase;
    private Cursor cursorUser;

    public event_detail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_event_detail, container, false);

        txtName = (TextView)v.findViewById(R.id. txtName);
        txtMan = (TextView)v.findViewById(R.id.txtMan);
        txtLocation = (TextView)v.findViewById(R.id.txtLocation);
        txtInfo_general = (TextView)v.findViewById(R.id.txtInfo_general);

        dBHelper = new BDHelper(getActivity().getApplicationContext());
        dataBase =  dBHelper.getWritableDatabase();

        cursorUser = dataBase.rawQuery("select * from " + DataBase.TABLE_EVENT + " where " + DataBase.column_event.CODE + " = " + id, null);

        if(cursorUser.moveToFirst()){
            String nom = cursorUser.getString(cursorUser.getColumnIndex(DataBase.column_event.NAME));
            String man = cursorUser.getString(cursorUser.getColumnIndex(DataBase.column_event.MANAGER));
          //  String photo_ev = cursorUser.getString(cursorUser.getColumnIndex(DataBase.column_event.PHOTO_EVENT));
            // String score = cursorUser.getString(cursorUser.getColumnIndex(DataBase.column_event.SCORE));

            String location = cursorUser.getString(cursorUser.getColumnIndex(DataBase.column_event.LOCATION));
            String info = cursorUser.getString(cursorUser.getColumnIndex(DataBase.column_event.GENERAL_INFORMATION));



            txtName.setText( txtName.getText().toString()+": "+nom);
            txtMan.setText(txtMan.getText().toString()+": "+man);
            txtLocation.setText(txtLocation.getText().toString()+": "+location);
            txtInfo_general.setText(txtInfo_general.getText().toString()+": \n "+info+"\n");

        }
        return v;
    }

    public void setDatos(String datos[]) {
        id = datos[5];
    }

}
