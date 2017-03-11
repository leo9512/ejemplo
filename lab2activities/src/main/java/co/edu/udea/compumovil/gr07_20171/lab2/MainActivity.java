package co.edu.udea.compumovil.gr07_20171.lab2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import co.edu.udea.compumovil.gr07_20171.lab2.DB.BDHelper;
import co.edu.udea.compumovil.gr07_20171.lab2.DB.DataBase;
import co.edu.udea.compumovil.gr07_20171.lab2.Fragments.about;
import co.edu.udea.compumovil.gr07_20171.lab2.Fragments.event;
import co.edu.udea.compumovil.gr07_20171.lab2.Fragments.event_detail;
import co.edu.udea.compumovil.gr07_20171.lab2.Fragments.login;
import co.edu.udea.compumovil.gr07_20171.lab2.Fragments.new_event_fragment;
import co.edu.udea.compumovil.gr07_20171.lab2.Fragments.profile;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RecyclerAdapter.OnHeadlineSelectedListener {

    private FragmentManager fragmentManager;
    private FragmentTransaction ft;
    private Fragment fragment;
    private BDHelper dBHelper;
    private SQLiteDatabase dBase;
    private FloatingActionButton fab;
    private Cursor cursorUser;
    private ContentValues valores;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager = getSupportFragmentManager();
                ft = fragmentManager.beginTransaction();
                fragment = new new_event_fragment();
                ft.replace(R.id.fragmentContainer, fragment);
                ft.commit();
                fab.hide();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        verificarSesion("Lugares");
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void verificarSesion(String etiqueta){
        dBHelper = new BDHelper(this);
        dBase =  dBHelper.getWritableDatabase();

        cursorUser = dBase.rawQuery("select * from " + DataBase.TABLE_USER + " where " + DataBase.column_user.STATUS + " = 'ENABLE'", null);

        fragmentManager = getSupportFragmentManager();
        ft = fragmentManager.beginTransaction();

        if(cursorUser.moveToFirst()){
            //actualizar la imagem
            if(etiqueta.equalsIgnoreCase("Events")){
                fragment = new event();
                fab.show();
            }else if(etiqueta.equalsIgnoreCase("Profile")){
                fragment = new profile();
                fab.hide();
            }else if(etiqueta.equalsIgnoreCase("About")){
                fragment = new about();
                fab.hide();
            }

        }else{
            fragment = new login();
            fab.hide();
        }
        ft.replace(R.id.fragmentContainer, fragment);
        ft.commit();
        dBase.close();
    }

    private void cerrarSesion(){
        dBHelper = new BDHelper(this);
        dBase =  dBHelper.getWritableDatabase();
        cursorUser = dBase.rawQuery("select * from " + DataBase.TABLE_USER + " where " + DataBase.column_user.STATUS + " = 'ACTIVO'", null);
        if(cursorUser.moveToFirst()){
            String idUser = cursorUser.getString(cursorUser.getColumnIndex(DataBase.column_user.ID));
            valores = new ContentValues();
            valores.put(DataBase.column_user.STATUS,"INACTIVO");
            dBase.updateWithOnConflict(DataBase.TABLE_USER, valores, DataBase.column_user.ID + "=" + idUser, null, SQLiteDatabase.CONFLICT_IGNORE);
            Toast.makeText(this,"Done",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Closed session",Toast.LENGTH_SHORT).show();
        }
        verificarSesion("Events");
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.opc_about:
                verificarSesion("About");
                break;
            case R.id.opc_signOut:
                cerrarSesion();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_events:
                verificarSesion("Events");
                break;
            case R.id.nav_profile:
                verificarSesion("Profile");
                break;
            case R.id.nav_about:
                verificarSesion("About");
                break;
            case R.id.signOut:
                cerrarSesion();
                break;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onArticleSelected(String datos[]) {
        event_detail newFragment = new event_detail();
        newFragment.setDatos(datos);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragmentContainer, newFragment);

        transaction.addToBackStack(null);


        // Commit the transaction
        transaction.commit();
    }
}

