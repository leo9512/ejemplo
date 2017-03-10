package co.edu.udea.compumovil.gr02_20171.labscm20171;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class contactinfo extends AppCompatActivity {

    EditText tel, email, country, city, address;
    String nombre, apellido,fecha,escolaridad,sexo;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactinfo);
        next= (Button) findViewById(R.id.next);

        tel = (EditText) findViewById(R.id.tel);
        email = (EditText) findViewById(R.id.email);
        country = (EditText) findViewById(R.id.country);
        city = (EditText) findViewById(R.id.city);
        address = (EditText) findViewById(R.id.address);



        nombre= getIntent().getStringExtra("nombre");
        apellido= getIntent().getStringExtra("apellido");
        fecha= getIntent().getStringExtra("fecha");
        escolaridad= getIntent().getStringExtra("escolaridad");
        sexo= getIntent().getStringExtra("sexo");

        next.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                Intent cartero = new Intent(contactinfo.this, otherinfo.class);

                cartero.putExtra("nombre", nombre);
                cartero.putExtra("apellido", apellido);
                cartero.putExtra("fecha", fecha);
                cartero.putExtra("sexo", sexo);
                cartero.putExtra("escolaridad", escolaridad);
                cartero.putExtra("telefono",tel.getText().toString());
                cartero.putExtra("correo",email.getText().toString());
                cartero.putExtra("pais",country.getText().toString());
                cartero.putExtra("ciudad",city.getText().toString());
                cartero.putExtra("direccion",address.getText().toString());
                startActivity(cartero);
            }
        });
    }

}
