package br.eti.softlog.softlogtmsentregas;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Paulo Sérgio Alves on 2018/03/26.
 */

public class ExtractOcorrenciaJson {

    EntregasApp app;
    Manager manager;
    private Context mContext;

    public ExtractOcorrenciaJson(Context context) {
        mContext = context;
        app = (EntregasApp)context.getApplicationContext();
        manager = new Manager(app);
    }

    public void extract(String response){
        try {
            JSONObject oJson = new JSONObject(response);
            JSONArray ocorrencias = oJson.getJSONArray("ocorrencias");

            for (int i = 0; i <ocorrencias.length(); i++ ){
                JSONObject ocorrencia = ocorrencias.getJSONObject(i);

                //Log.d("Ocorrencia",ocorrencia.toString());
                Long idOcorrencia = ocorrencia.getLong("id_ocorrencia");
                String ocorrenciaDesc = ocorrencia.getString("ocorrencia");
                int pendencia = ocorrencia.getInt("pendencia");
                int ativo = ocorrencia.getInt("aplicativo_mobile");

                boolean bPendencia;

                if (pendencia == 1)
                    bPendencia = true;
                else
                    bPendencia = false;

                boolean bAtivo;

                if (ativo == 1)
                    bAtivo = true;
                else
                    bAtivo = false;

                manager.addOcorrencia(idOcorrencia,ocorrenciaDesc,bPendencia, bAtivo);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
