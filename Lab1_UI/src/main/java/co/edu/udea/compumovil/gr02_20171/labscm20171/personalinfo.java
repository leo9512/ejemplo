package co.edu.udea.compumovil.gr02_20171.labscm20171;


        import android.app.DatePickerDialog;
        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.DatePicker;
        import android.widget.EditText;
        import android.widget.RadioButton;
        import android.widget.Spinner;
        import android.widget.TextView;

        import java.util.Calendar;

public class personalinfo extends AppCompatActivity {

    EditText etfecha, nombre, apellido;;
    Button btfecha, next;
    RadioButton M, F;
    TextView efecha;
    Spinner SpEsco;
    String sex;
    private int dia, mes , año;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalinfo);

        //Crear spinner
        SpEsco = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> contenido= ArrayAdapter.createFromResource(this,R.array.escolaridad,android.R.layout.simple_spinner_item);
        contenido.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpEsco.setAdapter(contenido);

        etfecha= (EditText) findViewById(R.id.efecha);
        nombre = (EditText) findViewById(R.id.nombre);
        apellido = (EditText) findViewById(R.id.apellido);
        efecha = (TextView) findViewById(R.id.efecha);
        btfecha=(Button) findViewById(R.id.btnCalendario);
        next = (Button) findViewById(R.id.next);

        //accion cuando se da click en el botón del calendario
        btfecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // se crea una instancia de la clase calendario y se obtiene la fecha actual
                final Calendar c = Calendar.getInstance();
                año = c.get(Calendar.YEAR); // año actual
                mes = c.get(Calendar.MONTH); // mes actual
                dia= c.get(Calendar.DAY_OF_MONTH); // dia actual
                // dialogo del Datepicker
                datePickerDialog = new DatePickerDialog(personalinfo.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // modifica el día, mes y año en el campo de texto
                                etfecha.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, año, mes, dia);
                datePickerDialog.show();
            }
        });





        //next
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent cartero = new Intent(personalinfo.this, contactinfo.class);
                cartero.putExtra("nombre", nombre.getText().toString() );
                cartero.putExtra("apellido", apellido.getText().toString());
                cartero.putExtra("fecha", efecha.getText().toString());
                cartero.putExtra("sexo", sex);
                cartero.putExtra("escolaridad", SpEsco.getSelectedItem().toString());
                startActivity(cartero);
            }
        });
    }

    //Se mira que sexo se ha seleccionado
    public void OnRadioButtonClick(View v){
        // se mira si el radio button ha sido cliqueado
        boolean checked= ((RadioButton) v).isChecked();
        //mirar que RadioButton ha sido cliqueado
        switch (v.getId()){
            case R.id.F:
                if(checked){
                    sex="Femenino";
                }
                break;
            case R.id.M:
                if (checked){
                    sex="Masculino";
                }
                break;
        }
    }


}
