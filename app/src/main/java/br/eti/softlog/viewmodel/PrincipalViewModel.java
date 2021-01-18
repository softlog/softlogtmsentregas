package br.eti.softlog.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import br.eti.softlog.model.Documento;
import br.eti.softlog.model.Entregas;
import br.eti.softlog.model.Pessoa;
import br.eti.softlog.softlogtmsentregas.DataAdapterEntregas;
import br.eti.softlog.softlogtmsentregas.EntregasApp;
import br.eti.softlog.softlogtmsentregas.ExtractOcorrenciaJson;
import br.eti.softlog.softlogtmsentregas.ExtractRomaneioJson;
import br.eti.softlog.softlogtmsentregas.Manager;
import br.eti.softlog.utils.Util;
import br.eti.softlog.worker.SendOcorrenciasWorker;

public class PrincipalViewModel extends AndroidViewModel {

    private Manager manager;

    private MutableLiveData<List<Entregas>> entregasLiveData;
    private MutableLiveData<List<Entregas>> entreguesLiveData;

    private List<Entregas> entregas;
    private List<Entregas> entregues;

    private ExtractRomaneioJson extractRomaneioJson;
    private ExtractOcorrenciaJson extractOcorrenciaJson;

    private WorkManager mWorkManager;
    private LiveData<List<WorkInfo>> mSavedWorkInfo;

    private EntregasApp app;

    public PrincipalViewModel(Application application){
        super(application);

        manager = new Manager((EntregasApp) application);
        entregasLiveData = new MutableLiveData<>();
        entreguesLiveData = new MutableLiveData<>();
        app = (EntregasApp) application;
        mWorkManager = WorkManager.getInstance(application.getApplicationContext());

    }

    public List<Entregas> getEntregas(){
        return entregas;
    }

    public List<Entregas> getEntregues(){
        return entregues;
    }

    public void loadDocumentos(Date Data){

        app.getDaoSession().clear();
        entregas = manager.findEntregasByDataStatus(Data, false);
        Log.d("WorkManager","Documentos para entregar " + String.valueOf(getEntregas().size()));
        entregasLiveData.setValue(entregas);

        entregues = manager.findEntregasByDataStatus(Data, true);
        Log.d("WorkManager","Documentos Finalizados " + String.valueOf(getEntregues().size()));
        entreguesLiveData.setValue(entregues);

    }



    public MutableLiveData getEntregasLiveData(){
        return entregasLiveData;
    }

    public MutableLiveData getEntreguesLiveData(){
        return entreguesLiveData;
    }

    public void sendOcorrencias() {

        boolean upload_mobile = Prefs.getBoolean("upload_oco_mobile",true);

        NetworkType networkType;

        if (!upload_mobile)
            networkType = NetworkType.UNMETERED;
        else
            networkType = NetworkType.CONNECTED;

        // Create Network constraint
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(networkType)
                .build();


        int intervalo = Integer.valueOf(Prefs.getString("interval_sync","15"));

        if (intervalo < 15)
            intervalo = 15;


        PeriodicWorkRequest periodicSyncDataWork =
                new PeriodicWorkRequest.Builder(SendOcorrenciasWorker.class, intervalo, TimeUnit.MINUTES)
                        .addTag("SENDOCORRENCIAS")
                        .setConstraints(constraints)
                        // setting a backoff on case the work needs to retry
                        .setBackoffCriteria(BackoffPolicy.LINEAR, PeriodicWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
                        .build();

        mWorkManager.enqueueUniquePeriodicWork(
                "SYNC_OCORRENCIAS",
                ExistingPeriodicWorkPolicy.KEEP, //Existing Periodic Work policy
                periodicSyncDataWork //work request
        );

        mSavedWorkInfo = mWorkManager.getWorkInfosByTagLiveData("SENDOCORRENCIAS");

    }

    public LiveData<List<WorkInfo>> getOutputWorkInfo() {
        return mSavedWorkInfo;
    }



}
