package co.edu.udea.compumovil.gr02_20171.labscm20171;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;

public class otherinfo extends AppCompatActivity {
    Button mostrar;
    RatingBar rb1, rb2, rb3, rb4, rb5;
    CheckBox ch1, ch2,ch3, ch4, ch5;
    String cantar, verTV, bailar, leer, nadar, nombre, apellido,fecha,escolaridad,sexo,telefono, correo,pais, ciudad,direccion;
    TextView mostarDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otherinfo);

        rb1 = (RatingBar) findViewById(R.id.rating1);
        rb2 = (RatingBar)  findViewById(R.id.rating2);
        rb3 = (RatingBar)  findViewById(R.id.rating3);
        rb4 = (RatingBar)  findViewById(R.id.rating4);
        rb5 = (RatingBar)  findViewById(R.id.rating5);
        rb1.setEnabled(false);
        rb2.setEnabled(false);
        rb3.setEnabled(false);
        rb4.setEnabled(false);
        rb5.setEnabled(false);
        ch1 = (CheckBox) findViewById(R.id.leer);
        ch2 = (CheckBox) findViewById(R.id.verTV);
        ch3 = (CheckBox) findViewById(R.id.Bailar);
        ch4 = (CheckBox) findViewById(R.id.cantar);
        ch5 = (CheckBox) findViewById(R.id.nadar);
        mostrar= (Button) findViewById(R.id.mostrar);
        mostarDatos= (TextView) findViewById(R.id.mostrarDatos);

        nombre= getIntent().getStringExtra("nombre");
        apellido= getIntent().getStringExtra("apellido");
        fecha= getIntent().getStringExtra("fecha");
        escolaridad= getIntent().getStringExtra("escolaridad");
        sexo= getIntent().getStringExtra("sexo");
        telefono= getIntent().getStringExtra("telefono");
        correo= getIntent().getStringExtra("correo");
        pais= getIntent().getStringExtra("pais");
        ciudad= getIntent().getStringExtra("ciudad");
        direccion= getIntent().getStringExtra("direccion");


        mostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leer= String.valueOf(rb1.getRating());
                verTV= String.valueOf(rb2.getRating());
                bailar= String.valueOf(rb3.getRating());
                cantar= String.valueOf(rb4.getRating());
                nadar= String.valueOf(rb5.getRating());

                String datos= "Nombre: "+nombre+"\n Apellido: "+apellido+"\n Fecha de Nacimiento: "+fecha+"\n Sexo: "+sexo+"\n Escolaridad: "
                        +escolaridad+"\n Teléfono: "+telefono+"\n Correo: "+correo+"\n País: "+pais+"\n Ciudad: "+ciudad+"\n Dirección: "+direccion
                        +"\n Hobbies: "+"\n Leer: "+leer+"\n VerTV: "+verTV+"\n Bailar: "+bailar+"\n Cantar: "+cantar+"\n Nadar: "+nadar;

                mostarDatos.setText(datos);

            }
        });




    }
    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

//  verifica si activaron un checkbox
           switch(view.getId()) {
            case R.id.leer:
                if (checked)
                    rb1.setEnabled(true);
                else
                    rb1.setEnabled(false);
                break;
            case R.id.verTV:
                if (checked)
                    rb2.setEnabled(true);
                else
                    rb2.setEnabled(false);
                break;
            case R.id.Bailar:
                if (checked)
                    rb3.setEnabled(true);
                else
                    rb3.setEnabled(false);
                break;
            case R.id.cantar:
                if (checked)
                    rb4.setEnabled(true);
                else
                    rb4.setEnabled(false);
                break;
            case R.id.nadar:
                if (checked)
                    rb5.setEnabled(true);
                else
                    rb5.setEnabled(false);
                break;
        }
    }
}
