package br.eti.softlog.softlogtmsentregas;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.idescout.sql.SqlScoutServer;

//import br.eti.softlog.whenmoving.MainTrackingActivity;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_TIME_OUT = 3000;
    EntregasApp myapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SqlScoutServer.create(this, getPackageName());

        new Handler().postDelayed(new Runnable() {
            /*
             * Exibindo splash com um timer.
             */
            @Override
            public void run() {
                // Esse método será executado sempre que o timer acabar
                // E inicia a activity principal
                myapp = (EntregasApp)getApplicationContext();

                if (myapp.getStatus()) {
                    Intent i = new Intent(SplashActivity.this,MainActivity.class );
                    //Intent i = new Intent(SplashActivity.this,PrincipalActivity.class );
                    //Intent i = new Intent(SplashActivity.this,MainActivityFreeTrackGps.class );
                    startActivity(i);
                } else {
                    Intent i = new Intent(SplashActivity.this,LoginActivity.class );
                    startActivity(i);
                }
                // Fecha esta activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
