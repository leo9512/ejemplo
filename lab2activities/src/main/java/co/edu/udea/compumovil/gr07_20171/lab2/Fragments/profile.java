package co.edu.udea.compumovil.gr07_20171.lab2.Fragments;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;

import co.edu.udea.compumovil.gr07_20171.lab2.DB.BDHelper;
import co.edu.udea.compumovil.gr07_20171.lab2.DB.DataBase;
import co.edu.udea.compumovil.gr07_20171.lab2.R;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;

/**
 * A simple {@link Fragment} subclass.
 */
public class profile extends Fragment {

    private ImageView imgFoto;
    private TextView txtUsuario;
    private TextView txtEdad;
    private ImageButton imgBtnFoto;
    private ImageButton imgBtnUsuario;
    private ImageButton imgBtnEdad;
    private Button btnActualizar;

    private static String APP_DIRECTORY = "MyPictureApp/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "PictureApp";

    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;

    private String mPath;
    private LinearLayout linearPerfil;

    private BDHelper dBHelper;
    private SQLiteDatabase dataBase;

    private Uri path;
    private byte[] imagenUser;
    private Cursor cursorUser;



    public profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        imgFoto = (ImageView)v.findViewById(R.id.imgFotoUsuario);
        txtUsuario = (TextView)v.findViewById(R.id.txtValorUsuario);
        txtEdad = (TextView) v.findViewById(R.id.txtValorEdad);


        imgBtnFoto = (ImageButton) v.findViewById(R.id.btnAgregarFotoUsuario);
        imgBtnUsuario = (ImageButton) v.findViewById(R.id.btnImgUsuario);
        imgBtnEdad = (ImageButton) v.findViewById(R.id.btnImgEdad);
        btnActualizar = (Button) v.findViewById(R.id.btnActualizarPerfil);

        linearPerfil = (LinearLayout) v.findViewById(R.id.drawer_layout);

        if(mayRequestStoragePermission())
            imgBtnFoto.setEnabled(true);
        else
            imgFoto.setEnabled(false);


        dBHelper = new BDHelper(getActivity().getApplicationContext());
        dataBase =  dBHelper.getWritableDatabase();

        cursorUser = dataBase.rawQuery("select * from " + DataBase.TABLE_USER + " where " + DataBase.column_user.STATUS + " = 'ENABLE'", null);

        if(cursorUser.moveToFirst()){
            String u = cursorUser.getString(cursorUser.getColumnIndex(DataBase.column_user.USER)); //se obtiene el valor de la columna USER, los usuarios, aqui puede ser BaseDeDatos.Column_User.EMAIL para obtener los correos
            String e = cursorUser.getString(cursorUser.getColumnIndex(DataBase.column_user.AGE));
            byte im [] = cursorUser.getBlob(cursorUser.getColumnIndex(DataBase.column_user.PHOTO_USER));
            txtUsuario.setText(u);
            txtEdad.setText(e);
            if (im != null){
                Bitmap bm = BitmapFactory.decodeByteArray(im,0,im.length);
                imgFoto.setImageBitmap(bm);
            }


        }
        cursorUser.close();
        dataBase.close();

        imgBtnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOpciones();
            }
        });

        imgBtnEdad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtEdad.setEnabled(true);
            }
        });

        imgBtnUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtUsuario.setEnabled(true);

            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dBHelper = new BDHelper(getActivity().getApplicationContext());
                dataBase =  dBHelper.getWritableDatabase();

                cursorUser = dataBase.rawQuery("select * from " + DataBase.TABLE_USER + " where " + DataBase.column_user.STATUS + " = 'ENABLE'", null);

                if (cursorUser.moveToFirst()){
                    String edad = txtEdad.getText().toString();
                    String usuario = txtUsuario.getText().toString();

                    String idUser = cursorUser.getString(cursorUser.getColumnIndex(DataBase.column_user.ID));

                    ContentValues values = new ContentValues();
                    values.put(DataBase.column_user.AGE, edad);
                    values.put(DataBase.column_user.USER, usuario);
                    if(imagenUser != null){
                        values.put(DataBase.column_user.PHOTO_USER, imagenUser);
                    }

                    dataBase.updateWithOnConflict(DataBase.TABLE_USER, values, DataBase.column_user.ID + "=" + idUser, null, SQLiteDatabase.CONFLICT_IGNORE);
                    Toast.makeText(getActivity().getApplicationContext(),"Datos actualizados",Toast.LENGTH_SHORT).show();
                    txtEdad.setEnabled(false);
                    txtUsuario.setEnabled(false);
                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                }
                cursorUser.close();
            }
        });
        dataBase.close();

        return v;
    }

    private void showOpciones(){
        final CharSequence[] option = {"Tomar foto", "Elegir de galeria", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Eleige una opción");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(option[which] == "Tomar foto"){
                    abrirCamera();
                }else if(option[which] == "Elegir de galeria"){
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), SELECT_PICTURE);
                }else {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    private void abrirCamera(){
        File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if(!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if(isDirectoryCreated){
            Long timestamp = System.currentTimeMillis() / 1000;
            String imageName = timestamp.toString() + ".jpg";

            mPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY
                    + File.separator + imageName;

            File newFile = new File(mPath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
            startActivityForResult(intent, PHOTO_CODE);

        }
    }

    private boolean mayRequestStoragePermission() {

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        if((getActivity().checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (getActivity().checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED))
            return true;

        if((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(CAMERA))){
            Snackbar.make(linearPerfil, "Los permisos son necesarios para poder usar la aplicación",
                    Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
                }
            });
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
        }

        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case PHOTO_CODE:
                    MediaScannerConnection.scanFile(getContext(),
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });


                    Bitmap bitmap = BitmapFactory.decodeFile(mPath);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                    imagenUser = stream.toByteArray();
                    imgFoto.setImageBitmap(bitmap);
                    break;
                case SELECT_PICTURE:
                    path = data.getData();
                    imgFoto.setImageURI(path);
                    Bitmap bitM = ((BitmapDrawable)imgFoto.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                    bitM.compress(Bitmap.CompressFormat.PNG, 100, stream2);

                    imagenUser = stream2.toByteArray();
                    imgFoto.setImageBitmap(bitM);
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == MY_PERMISSIONS){
            if(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getActivity(), "Permisos aceptados", Toast.LENGTH_SHORT).show();
                imgBtnFoto.setEnabled(true);
            }
        }else{
            showExplanation();
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Permisos denegados");
        builder.setMessage("Para usar las funciones de la app necesitas aceptar los permisos");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getActivity().finish();
            }
        });

        builder.show();

    }

}
