package br.eti.softlog.worker;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import br.eti.softlog.model.ImagemOcorrencia;
import br.eti.softlog.model.OcorrenciaDocumento;
import br.eti.softlog.softlogtmsentregas.EntregasApp;
import br.eti.softlog.softlogtmsentregas.ExtractRomaneioJson;
import br.eti.softlog.softlogtmsentregas.Manager;
import br.eti.softlog.utils.AppSingleton;
import br.eti.softlog.utils.Util;
import br.eti.softlog.utils.VolleySingleton;

public class SendOcorrenciasWorker extends Worker {

    EntregasApp myapp;
    Manager manager;
    ExtractRomaneioJson extractRomaneioJson;
    Context context;



    public SendOcorrenciasWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        this.context = context;
        myapp = (EntregasApp) context;
        manager = new Manager(myapp);

    }

    @NonNull
    @Override
    public Result doWork() {



        final List<OcorrenciaDocumento> ocorrencias = manager.findOcorrenciaNaoSincronizada();

        Log.d("WorkManager","Enviando Ocorrências");

        if (ocorrencias.size()>0) {
            final JSONArray jaOcorrencias = new JSONArray();

            for (int i = 0; i < ocorrencias.size(); i++) {
                JSONObject joOcorrencia = new JSONObject();
                OcorrenciaDocumento ocorrencia = ocorrencias.get(i);
                try {

                    joOcorrencia.put("id", ocorrencia.getId().longValue());
                    joOcorrencia.put("id_ocorrencia", ocorrencia.getCodigoOcorrencia().intValue());
                    joOcorrencia.put("data_registro", ocorrencia.getDataRegistro());
                    joOcorrencia.put("data_ocorrencia", ocorrencia.getDataOcorrencia());
                    joOcorrencia.put("hora_ocorrencia", ocorrencia.getHoraOcorrencia());
                    joOcorrencia.put("nome_recebedor", ocorrencia.getNomeRecebedor());
                    joOcorrencia.put("documento_recebedor", ocorrencia.getDocumentoRecebedor());
                    joOcorrencia.put("observacoes", ocorrencia.getObservacoes());
                    joOcorrencia.put("latitude", ocorrencia.getLatitude());
                    joOcorrencia.put("longitude", ocorrencia.getLongitude());
                    joOcorrencia.put("chave_nfe", ocorrencia.getDocumento().getChaveNfe());
                    joOcorrencia.put("numero_nota_fiscal", ocorrencia.getDocumento().getNumeroNotaFiscal());
                    joOcorrencia.put("serie_nota_fiscal", ocorrencia.getDocumento().getSerie());
                    joOcorrencia.put("remetente_cnpj", ocorrencia.getDocumento().getRemetenteCnpj().toString());
                    joOcorrencia.put("id_nota_fiscal_imp", ocorrencia.getDocumento().getIdNotaFiscalImp());
                    joOcorrencia.put("id_romaneio", ocorrencia.getDocumento().getRomaneioId());
                    joOcorrencia.put("id_conhecimento_notas_fiscais", ocorrencia.getDocumento().getIdConhecimentoNotasFiscais());
                    joOcorrencia.put("id_conhecimento", ocorrencia.getDocumento().getIdConhecimento());
                    joOcorrencia.put("uuid_usuario", myapp.getUsuario().getUuid());

                    if (ocorrencia.getImagemOcorrencias().size() > 0) {

                        ImagemOcorrencia imagemOcorrencia = ocorrencia.getImagemOcorrencias().get(0);

                        String urlImagem = "https://sconfirmei.s3-sa-east-1.amazonaws.com/imagens/"
                                + String.valueOf(myapp.getUsuario().getCodigoAcesso())
                                + "/" + imagemOcorrencia.getNomeArquivo();

                        joOcorrencia.put("url_imagem", urlImagem);
                    }
                } catch (JSONException e) {

                    Util.appendLog("Envio Ocorrências", e.getMessage(), myapp.getFileLog());
                    e.printStackTrace();

                }

                jaOcorrencias.put(joOcorrencia);

            }

            JSONObject json = new JSONObject();
            try {
                json.put("ocorrencias", jaOcorrencias);
            } catch (JSONException e) {
                Util.appendLog("Envio Ocorrências", e.getMessage(), myapp.getFileLog());
            }

            String strJson = json.toString();

            //Log.d("Json", json.toString());

            final String codigo_acesso = String.valueOf(myapp.getUsuario().getCodigoAcesso());
            String url = "http://api.softlog.eti.br/api/softlog/ocorrencia";


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
                JSONObject jResultado = future.get(60000, TimeUnit.MINUTES);
                //Log Response and return Result.success() or your output data to
                // any observer
                String resultado;
                resultado = "";
                try {
                    resultado = jResultado.getString("resultado");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (resultado.equals("Ok")) {
                    for (int i = 0; i < ocorrencias.size(); i++) {
                        //LogUtil.i(TAG, "Log file path is: " + LogManager.getInstance().getFilePath());
                        OcorrenciaDocumento oco = ocorrencias.get(i);
                        oco.setSincronizado(true);
                        Date date = new Date();
                        oco.setDataSincronizacao(Util.getDateTimeFormatYMD(date));
                        Util.appendLog("Envio de ocorrências",
                                "Ocorrência Doc.N.: " + oco.getDocumento().getChaveNfe() + " enviada", myapp.getFileLog());
                        myapp.getDaoSession().update(oco);
                    }
                }

                Log.d("WorkManager", jResultado.toString());
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
            } catch (Exception e) {
                Log.d("WorkManager", e.getMessage());
                e.printStackTrace();
                return Result.failure();
            }

        } else {
            return Result.success();
        }

    }
}
