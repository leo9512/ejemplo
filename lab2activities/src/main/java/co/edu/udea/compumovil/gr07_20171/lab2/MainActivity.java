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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RecyclerAdapter.OnHeadlineSelectedListener {

    private FragmentManager fragmentManager;
    private FragmentTransaction ft;
    private Fragment fragment;
    private BDHelper dBHelper;
    private SQLiteDatabase dataBase;
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
                fragment = new NuevoLugarFragment();
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
        dBHelper = new BdHelper(this);
        dataBase =  dBHelper.getWritableDatabase();

        cursorUser = dataBase.rawQuery("select * from " + BaseDeDatos.TABLE_USER + " where " + BaseDeDatos.Column_User.ESTAD0 + " = 'ACTIVO'", null);

        fragmentManager = getSupportFragmentManager();
        ft = fragmentManager.beginTransaction();

        if(cursorUser.moveToFirst()){
            //actualizar la imagem
            if(etiqueta.equalsIgnoreCase("Lugares")){
                fragment = new LugaresFragment();
                fab.show();
            }else if(etiqueta.equalsIgnoreCase("Perfil")){
                fragment = new PerfilFragment();
                fab.hide();
            }else if(etiqueta.equalsIgnoreCase("Acerca")){
                fragment = new AcercaDeFragment();
                fab.hide();
            }

        }else{
            fragment = new LoginFragment();
            fab.hide();
        }
        ft.replace(R.id.fragmentContainer, fragment);
        ft.commit();
        dataBase.close();
    }

    private void cerrarSesion(){
        dBHelper = new BdHelper(this);
        dataBase =  dBHelper.getWritableDatabase();
        cursorUser = dataBase.rawQuery("select * from " + BaseDeDatos.TABLE_USER + " where " + BaseDeDatos.Column_User.ESTAD0 + " = 'ACTIVO'", null);
        if(cursorUser.moveToFirst()){
            String idUser = cursorUser.getString(cursorUser.getColumnIndex(BaseDeDatos.Column_User.ID));
            valores = new ContentValues();
            valores.put(BaseDeDatos.Column_User.ESTAD0,"INACTIVO");
            dataBase.updateWithOnConflict(BaseDeDatos.TABLE_USER, valores, BaseDeDatos.Column_User.ID + "=" + idUser, null, SQLiteDatabase.CONFLICT_IGNORE);
            Toast.makeText(this,"Finalizado",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Ya has cerrado sesión",Toast.LENGTH_SHORT).show();
        }
        verificarSesion("Lugares");
    }

    public static void actualizarSesion(){
        //this.verificarSesion("Lugares");
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.opc_acercaDe:
                verificarSesion("Acerca");
                break;
            case R.id.opc_cerrarSesion:
                cerrarSesion();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //fab.show();
        switch (item.getItemId()){
            case R.id.nav_lugares:
                verificarSesion("Lugares");
                break;
            case R.id.nav_perfil:
                verificarSesion("Perfil");
                break;
            case R.id.nav_acercaDe:
                verificarSesion("Acerca");
                break;
            case R.id.cerrarSesion:
                cerrarSesion();
                break;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onArticleSelected(String datos[]) {
        DetalleLugarFragment newFragment = new DetalleLugarFragment();
        newFragment.setDatos(datos);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragmentContainer, newFragment);

        transaction.addToBackStack(null);
        //fab.hide();

        // Commit the transaction
        transaction.commit();
    }
}

