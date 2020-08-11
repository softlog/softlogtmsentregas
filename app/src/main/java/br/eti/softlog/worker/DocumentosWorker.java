package br.eti.softlog.worker;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import br.eti.softlog.softlogtmsentregas.CustomVolleyRequestQueue;
import br.eti.softlog.softlogtmsentregas.EntregasApp;
import br.eti.softlog.softlogtmsentregas.ExtractRomaneioJson;
import br.eti.softlog.softlogtmsentregas.Manager;

import br.eti.softlog.utils.AppSingleton;
import br.eti.softlog.utils.Util;
import br.eti.softlog.utils.VolleySingleton;

public class DocumentosWorker extends Worker {

    EntregasApp myapp;
    Manager manager;
    ExtractRomaneioJson extractRomaneioJson;
    Context context;


    public DocumentosWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        myapp = (EntregasApp) context;
        manager = new Manager(myapp);
    }

    @NonNull
    @Override
    public Result doWork() {

        String data = getInputData().getString("data");
        String codigo_acesso = String.valueOf(myapp.getUsuario().getCodigoAcesso());
        String cpf = myapp.getUsuario().getCpf();

        String uuid = myapp.getUsuario().getUuid();
        String url = "http://api.softlog.eti.br/api/softlog/romaneio_v2/" + codigo_acesso +
                "/" + data + "/" +  uuid + "/" + manager.getUltimaAlteracaoRomaneio(data);

        Log.d("WorkManager", url);
        //Setup a RequestFuture object
        RequestFuture<JSONObject> future = RequestFuture.newFuture();

        //Pass the future into the JsonObjectRequest
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, future, future);

        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        //Add the request to the Request Queue
        VolleySingleton.getmInstance(context).addToRequestQueue(request);

        try {
            //Set an interval for the request to timeout. This will block the
            // worker thread and force it to wait for a response for 60 seconds
            // before timing out and raising an exception
            JSONObject response = future.get(60000, TimeUnit.MINUTES);
            //Log Response and return Result.success() or your output data to
            // any observer
            extractRomaneioJson = new ExtractRomaneioJson(context);
            extractRomaneioJson.extract(response);

            Log.d("WorkManager", response.toString());

            return Result.success();
        } catch (InterruptedException e) {
            Log.d("WorkManager", e.getMessage());
            e.printStackTrace();
            // exception handling
            return Result.failure();
        } catch (ExecutionException e) {
            Log.d("WorkManager", e.getMessage());
            e.printStackTrace();
            return Result.failure();
            // exception handling
        } catch (TimeoutException e) {
            Log.d("WorkManager", e.getMessage());
            e.printStackTrace();
            return Result.failure();
        } catch (Exception e){
            Log.d("WorkManager", e.getMessage());
            e.printStackTrace();
            return Result.failure();
        }

    }


}
