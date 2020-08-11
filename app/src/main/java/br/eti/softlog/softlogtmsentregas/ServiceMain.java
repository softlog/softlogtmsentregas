package br.eti.softlog.softlogtmsentregas;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

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

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                startMyOwnForeground();
            else
                startForeground(1, new Notification());

        }

        return(super.onStartCommand(intent,flags,startId));
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "softlog.eti.br";
        String channelName = "SConfirmei";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_softlog)
                .setContentTitle("sConfirmei está em execução")
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
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
