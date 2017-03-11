package co.edu.udea.compumovil.gr07_20171.lab2.Fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;

import co.edu.udea.compumovil.gr07_20171.lab2.DB.BDHelper;
import co.edu.udea.compumovil.gr07_20171.lab2.DB.DataBase;
import co.edu.udea.compumovil.gr07_20171.lab2.R;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class new_event_fragment extends Fragment {
    private static String APP_DIRECTORY = "MyPictureApp/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "PictureApp";

    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;

    private ImageView imgEvento;
    private ImageButton btnAgregarFoto;
    private EditText txtNombre, txtDescripcion, txtInformacion, txtUbicacion,  txtFecha;
    private RatingBar ratingBarPuntuacion;
    private Button btnAgregarLugar;
    private byte[] imagen;
    private LinearLayout linearLayout;
    private String mPath="";
    private Uri path;

    public new_event_fragment(){

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_event, container, false);

        linearLayout = (LinearLayout) v.findViewById(R.id.nuevoEvento);
        imgEvento = (ImageView) v.findViewById(R.id.imgEvento);
        btnAgregarFoto = (ImageButton)v.findViewById(R.id.btnAgregarFoto);
        txtNombre = (EditText) v.findViewById(R.id.txtNombreEvento);
        txtDescripcion = (EditText) v.findViewById(R.id.txtDescripcion);
        txtInformacion = (EditText) v.findViewById(R.id.txtInformacion);
        txtUbicacion = (EditText) v.findViewById(R.id.txtUbicacion);
        ratingBarPuntuacion = (RatingBar) v.findViewById(R.id.ratingBarEvento);
        btnAgregarLugar = (Button) v.findViewById(R.id.btnAgregarEvento);
        txtFecha= (EditText) v.findViewById(R.id.txtDate);

        if(mayRequestStoragePermission())
            btnAgregarFoto.setEnabled(true);
        else
            btnAgregarFoto.setEnabled(false);

        btnAgregarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOptions();
            }
        });

        btnAgregarLugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BDHelper dBHelper = new BDHelper(getActivity().getApplicationContext());
                SQLiteDatabase dataBase = dBHelper.getWritableDatabase();

                String nombre_evento = txtNombre.getText().toString();
                String responsable = txtDescripcion.getText().toString();
                String infoGeneral = txtInformacion.getText().toString();
                String ubicacion = txtUbicacion.getText().toString();
                float puntuacion = ratingBarPuntuacion.getRating();
                String fecha = txtFecha.getText().toString();

                if (nombre_evento.equals("") || responsable.equals("") || infoGeneral.equals("") || ubicacion.equals("") || fecha.equals("")){
                    Toast.makeText(getActivity().getApplicationContext(), "Ninguno de los campos puede estar vacío", Toast.LENGTH_SHORT).show();
                }else{
                    ContentValues values = new ContentValues();
                    values.put(DataBase.column_event.NAME, nombre_evento);
                    values.put(DataBase.column_event.MANAGER, responsable);
                    values.put(DataBase.column_event.PHOTO_EVENT, imagen);
                    values.put(DataBase.column_event.SCORE, puntuacion);
                    values.put(DataBase.column_event.DATE, fecha);
                    values.put(DataBase.column_event.LOCATION, ubicacion);
                    values.put(DataBase.column_event.GENERAL_INFORMATION, infoGeneral);

                    //
                    if(mPath.equalsIgnoreCase("") && path == null){
                        Toast.makeText(getActivity().getApplicationContext(), "Debe seleccionar una imagen",Toast.LENGTH_SHORT).show();
                    }else{
                        dataBase.insertWithOnConflict(DataBase.TABLE_EVENT,null,values,SQLiteDatabase.CONFLICT_IGNORE);
                        mPath = "";
                        Toast.makeText(getActivity().getApplicationContext(),"Lugar guardado",Toast.LENGTH_SHORT).show();
                        FragmentManager fragmentManager;
                        FragmentTransaction ft;
                        Fragment fragment;
                        fragmentManager = getActivity().getSupportFragmentManager();
                        ft = fragmentManager.beginTransaction();

                        fragment = new new_event_fragment();
                        ft.replace(R.id.fragmentContainer, fragment);
                        ft.commit();
                    }
                }
            }
        });
        return v;
    }
    private void openCamera() {
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

    private void showOptions() {
        final CharSequence[] option = {"Tomar foto", "Elegir de galeria", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Eleige una opción");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(option[which] == "Tomar foto"){
                    openCamera();
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

    private boolean mayRequestStoragePermission() {

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        if((getActivity().checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (getActivity().checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED))
            return true;

        if((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(CAMERA))){
            Snackbar.make(linearLayout, "Los permisos son necesarios para poder usar la aplicación",
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

                    imagen = stream.toByteArray();
                    imgEvento.setImageBitmap(bitmap);

                    break;
                case SELECT_PICTURE:
                    path = data.getData();
                    imgEvento.setImageURI(path);
                    Bitmap bitM = ((BitmapDrawable)imgEvento.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                    bitM.compress(Bitmap.CompressFormat.PNG, 100, stream2);

                    imagen = stream2.toByteArray();
                    imgEvento.setImageBitmap(bitM);

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
                btnAgregarFoto.setEnabled(true);
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
