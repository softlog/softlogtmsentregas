package br.eti.softlog.softlogtmsentregas;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;

import br.eti.softlog.model.Documento;
import br.eti.softlog.model.Locations;
import br.eti.softlog.model.Romaneio;
import br.eti.softlog.model.TrackingGps;
import br.eti.softlog.utils.Util;

public class ServiceTracking extends Service implements LocationListener {

    EntregasApp myapp;
    DataSync d;
    private Timer timer = new Timer();
    public List<Worker> threads = new ArrayList<Worker>();
    private int startId;
    public Util util;

    public ServiceTracking() {

    }

    @Override
    public void onCreate() {
        super.onCreate();

        myapp = (EntregasApp) getApplicationContext();
        util = new Util();

        startId = -1;
        //this.startservice();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        myapp = (EntregasApp) getApplicationContext();

        if (this.startId == -1) {

            this.startId = startId;
            Worker worker = new Worker(startId);
            worker.start();
            threads.add(worker);
        }

        return(super.onStartCommand(intent,flags,startId));
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        for(int i = 0, tam = threads.size();i < tam; i++){
            threads.get(i).ativo = false;
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    class Worker extends Thread {
        public int count;
        public int startId;
        public boolean ativo;
        private FusedLocationProviderClient mFusedLocationClient;

        public Worker(int startId) {
            this.startId = startId;
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(myapp.getApplicationContext());
        }



        public void run() {
            timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {

                    exec();

                }
            }, 0, 20000);
        }

        private void exec() {

            if (myapp.getUsuario() == null) {
                return;
            }

            if (ActivityCompat.checkSelfPermission(myapp.getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(myapp.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(
                            (Executor) ServiceTracking.this,
                            new OnSuccessListener<Location>() {
                                @Override public void onSuccess(Location location) {
                                    if (location != null) {
                                        Double latitude = location.getLatitude();
                                        Double longitude = location.getLongitude();

                                        final String sLatitude = String.valueOf(latitude);
                                        final String sLongitude = String.valueOf(longitude);

                                        TrackingGps trackingGps = new TrackingGps();
                                        trackingGps.setLatitude(latitude);
                                        trackingGps.setLongitude(longitude);
                                        trackingGps.setSincrozinado(false);
                                        trackingGps.setMotoristaCpf(myapp.getUsuario().getCpf().toString());

                                        //Grava a Data Atual da Localizacao
                                        Date date = new Date();
                                        String cDate = util.getDateTimeFormatYMD(date);
                                        trackingGps.setDataLocalizacao(cDate);

                                        //Verifica qual o último veiculo
                                        Romaneio romaneio = myapp.getDaoSession().
                                                getRomaneioDao().queryBuilder().orderDesc().limit(1).unique();

                                        if (romaneio != null){
                                            trackingGps.setPlacaVeiculo(romaneio.getVeiculo().getPlacaVeiculo());
                                        }

                                        myapp.getDaoSession().insert(trackingGps);

                                    }

                                }


                            });
        }
    }
}
