package br.eti.softlog.viewmodel;

import android.app.Application;
import android.content.Context;

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

    private MutableLiveData<List<Documento>> documentosLiveData;

    private MutableLiveData<List<Pessoa>> entregasLiveData;
    private MutableLiveData<List<Pessoa>> entreguesLiveData;


    private List<Documento> documentos;
    private List<Pessoa> entregas;
    private List<Pessoa> entregues;

    private ExtractRomaneioJson extractRomaneioJson;
    private ExtractOcorrenciaJson extractOcorrenciaJson;

    private WorkManager mWorkManager;
    private LiveData<List<WorkInfo>> mSavedWorkInfo;

    public PrincipalViewModel(Application application){
        super(application);
        manager = new Manager((EntregasApp) application);
        documentosLiveData = new MutableLiveData<>();
        entregasLiveData = new MutableLiveData<>();
        entreguesLiveData = new MutableLiveData<>();

        mWorkManager = WorkManager.getInstance(application.getApplicationContext());

    }

    public List<Documento> getDocumentos(){
        return documentos;
    }

    public List<Pessoa> getEntregas(){
        return entregas;
    }

    public List<Pessoa> getEntregues(){
        return entregues;
    }

    public void loadDocumentos(Date Data){
        documentos = manager.findDocumentoByDataRomaneio(Data);
        documentosLiveData.setValue(documentos);

        entregas = manager.findPessoasByDataRomaneio(Data, false);
        entreguesLiveData.setValue(entregas);

        entregues = manager.findPessoasByDataRomaneio(Data, true);
        entreguesLiveData.setValue(entregues);

    }



    public MutableLiveData getDocumentosLiveData(){
        return documentosLiveData;
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
