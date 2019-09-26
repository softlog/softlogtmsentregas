package br.eti.softlog.softlogtmsentregas;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ServiceMain extends Service {

    EntregasApp myapp;
    DataSync d;
    private Timer timer = new Timer();
    public List<Worker> threads = new ArrayList<Worker>();
    private int startId;
    public int period;

    public ServiceMain() {

    }


    @Override
    public void onCreate() {
        super.onCreate();
        //Log.d("Aviso","Criando Serviço Sincronização");
        myapp = (EntregasApp) getApplicationContext();
        d = new DataSync(myapp.getApplicationContext());
        startId = -1;
        //this.startservice();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        myapp = (EntregasApp) getApplicationContext();
        period = myapp.getConfigIntervalSync();

        if (this.startId == -1) {
            this.startId = startId;
            Worker worker = new Worker(startId);
            worker.start();
            threads.add(worker);
        }

        return(super.onStartCommand(intent,flags,startId));
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // Neste exemplo, iremos supor que o service será invocado apenas
        // atraves de startService()
        return null;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //Log.d("Aviso","Destruindo Serviço");
        for(int i = 0, tam = threads.size();i < tam; i++){
            threads.get(i).ativo = false;
        }
    }


    class Worker extends Thread {
        public int count;
        public int startId;
        public boolean ativo;



        public Worker(int startId) {
            this.startId = startId;
        }

        public void run() {
            timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                     exec();
                }
            }, 0, period * 1000 * 60);
        }

        private void exec()  {

            //Log.d("Aviso","Executando agenda de sincronização");
            if (myapp.getUsuario()==null) {
                return ;
            }

            d.sendOcorrencias();
            d.sendImagens();
            d.sendTracking();
        }
    }

}
