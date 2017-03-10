package co.edu.udea.compumovil.gr07_20171.lab2;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;

public class SplashActivity extends Activity {

    // Modifica la duracion del splash en pantalla
    private static final long SPLASH_SCREEN_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //  orientacion
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // quita la barra de titulo
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                // inicia el Login
                Intent mainIntent = new Intent().setClass(
                        SplashActivity.this, MainActivity.class);
                startActivity(mainIntent);

                // cierra la actividad para que no se pueda regresar a ella
                finish();
            }
        };

        // Simula el proceso de cargar
        Timer timer = new Timer();        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }

}
