package br.eti.softlog.softlogtmsentregas;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.eti.softlog.model.ImagemOcorrencia;
import br.eti.softlog.model.OcorrenciaDocumento;
import br.eti.softlog.model.Regiao;
import br.eti.softlog.model.TrackingGps;
import br.eti.softlog.model.Veiculo;
import br.eti.softlog.utils.AppSingleton;
import br.eti.softlog.utils.Connectivity;
import br.eti.softlog.utils.Util;
import butterknife.internal.ListenerClass;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;
import static org.greenrobot.greendao.identityscope.IdentityScopeType.None;
import static org.osmdroid.tileprovider.util.StreamUtils.copy;

import br.eti.softlog.softlogtmsentregas.R;
import me.echodev.resizer.Resizer;

/**
 * Created by Paulo Sergio on 2018/03/01.
 */

public class DataSync {

    private static final String TAG = "DataSync";

    private Context mContext;

    EntregasApp myapp;
    private Manager manager;
    private boolean executando;
    Util util;
    Connectivity connectivity;

    public DataSync(Context context) {
        mContext = context;
        myapp = (EntregasApp) context.getApplicationContext();
        manager = new Manager(myapp);
        util = new Util();
        connectivity = new Connectivity();
    }

    public void getRomaneios(){
        //Se nao tiver, acessa api para verificar se existe usuario registrado
        // Request a string response from the provided URL.




        RequestQueue queue = Volley.newRequestQueue(mContext);

        Date d = myapp.getDate();
        Date ddata_expedicao = d;
        new Date(d.getTime());

        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formato.applyPattern("yyyy-MM-dd");
        String data_expedicao = formato.format(ddata_expedicao);
        String codigo_acesso = String.valueOf(myapp.getUsuario().getCodigoAcesso());
        String cpf = myapp.getUsuario().getCpf();

        String url = "http://api.softlog.eti.br/api/softlog/romaneio/" + codigo_acesso +
                "/" + data_expedicao + "/" + cpf;

        //url = "http://api.softlog.eti.br/api/softlog/protocolo/53/2018-02-01/0";
        //url = "http://api.softlog.eti.br/api/softlog/romaneio/81/2017-12-22/16286172840";

        //Log.d("Url",url);

        //Registro do usuario e criacao do banco de dados
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //Log.d("ERRO",error.toString());

                if (error.networkResponse.statusCode==404){
                    //Log.i("Erro","Sem protocolos");
                } else {
                    //Log.i("Erro","Ocorreu um erro");
                }
            }
        });

        AppSingleton.getInstance(myapp.getApplicationContext()).addToRequestQueue(stringRequest,"Login");
        //Log.d("Log","Processo Concluido!");
    }


    public void sendOcorrencias() {

        if (!connectivity.isConnected(mContext)) {
            return;
        }


        if (connectivity.isConnectedMobile(mContext)){
            boolean usar_rede_movel = myapp.getConfigUploadOcorrenciaMobile();

            if (!usar_rede_movel){
                return;
            }
        }





        final List<OcorrenciaDocumento> ocorrencias = manager.findOcorrenciaNaoSincronizada();

        if (ocorrencias.size()>0) {
            final JSONArray jaOcorrencias = new JSONArray();

            for (int i=0; i < ocorrencias.size(); i++){
                JSONObject joOcorrencia = new JSONObject();
                OcorrenciaDocumento ocorrencia = ocorrencias.get(i);
                try {
                    joOcorrencia.put("id",ocorrencia.getId().longValue());
                    joOcorrencia.put("id_ocorrencia",ocorrencia.getCodigoOcorrencia().intValue());
                    joOcorrencia.put("data_registro",ocorrencia.getDataRegistro());
                    joOcorrencia.put("data_ocorrencia",ocorrencia.getDataOcorrencia());
                    joOcorrencia.put("hora_ocorrencia", ocorrencia.getHoraOcorrencia());
                    joOcorrencia.put("nome_recebedor",ocorrencia.getNomeRecebedor());
                    joOcorrencia.put("documento_recebedor",ocorrencia.getDocumentoRecebedor());
                    joOcorrencia.put("observacoes",ocorrencia.getObservacoes());
                    joOcorrencia.put("latitude",ocorrencia.getLatitude());
                    joOcorrencia.put("longitude", ocorrencia.getLongitude());
                    joOcorrencia.put("chave_nfe",ocorrencia.getDocumento().getChaveNfe());
                    joOcorrencia.put("numero_nota_fiscal",ocorrencia.getDocumento().getNumeroNotaFiscal());
                    joOcorrencia.put("serie_nota_fiscal",ocorrencia.getDocumento().getSerie());
                    joOcorrencia.put("remetente_cnpj",ocorrencia.getDocumento().getRemetenteCnpj().toString());
                    joOcorrencia.put("id_nota_fiscal_imp",ocorrencia.getDocumento().getIdNotaFiscalImp());
                    joOcorrencia.put("id_romaneio",ocorrencia.getDocumento().getRomaneioId());
                    joOcorrencia.put("id_conhecimento_notas_fiscais",ocorrencia.getDocumento().getIdConhecimentoNotasFiscais());
                    joOcorrencia.put("id_conhecimento",ocorrencia.getDocumento().getIdConhecimento());

                    if (ocorrencia.getImagemOcorrencias().size()>0){
                        ImagemOcorrencia imagemOcorrencia = ocorrencia.getImagemOcorrencias().get(0);
                        String urlImagem = "http://api.softlog.eti.br/api/softlog/imagem/"
                                + myapp.getUsuario().getCodigoAcesso()
                                + "/" + imagemOcorrencia.getNomeArquivo();

                        joOcorrencia.put("url_imagem",urlImagem);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }



                jaOcorrencias.put(joOcorrencia);
            }

            JSONObject json = new JSONObject();
            try {
                json.put("ocorrencias",jaOcorrencias);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String strJson = json.toString();

            //Log.d("Json", json.toString());

            final String codigo_acesso = String.valueOf(myapp.getUsuario().getCodigoAcesso());
            String url = "http://api.softlog.eti.br/api/softlog/ocorrencia";


            //Registro do usuario e criacao do banco de dados
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            for(int i=0; i < ocorrencias.size();i++){
                                OcorrenciaDocumento oco = ocorrencias.get(i);
                                oco.setSincronizado(true);
                                Date date = new Date();
                                oco.setDataSincronizacao(util.getDateTimeFormatYMD(date));
                                myapp.getDaoSession().update(oco);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    //Log.d("ERRO",error.toString());
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> parameters = new HashMap<String,String>();
                    parameters.put("ocorrencias",jaOcorrencias.toString());
                    parameters.put("codigo_acesso",codigo_acesso);
                    return parameters;
                }

            };

            AppSingleton.getInstance(myapp.getApplicationContext()).addToRequestQueue(stringRequest,"Ocorrencias");
        }

    }



    public void sendImagens() {

        if (!connectivity.isConnected(mContext)) {
            return;
        }



        if (connectivity.isConnectedMobile(mContext)){
            boolean usar_rede_movel = myapp.getConfigUploadImageMobile();
            if (!usar_rede_movel){
                return;
            }

        }


        final List<ImagemOcorrencia> imagens = manager.findImagensNaoSincronizada();

        if (imagens.size()>0) {
            final JSONArray jaImagens = new JSONArray();

            for (int i=0; i < imagens.size(); i++){

                JSONObject joImagem = new JSONObject();
                ImagemOcorrencia image = imagens.get(i);
                if (image.getNomeArquivo() == null) {
                    continue;
                }

                String encoded;
                try {

                    String path = myapp.getApplicationContext().getFilesDir().toString();


                    File root = Environment.getExternalStorageDirectory();
                    //ImageView IV = (ImageView) findViewById(R.id."image view");
                    Bitmap bMap = BitmapFactory.decodeFile(path +"/" + image.getNomeArquivo());


                    if (bMap == null) {
                        continue;
                    }

                    if (bMap.getWidth() > myapp.getConfigResolucao()){
                        File file2 = null;
                        try {
                            file2 = file_from(Uri.fromFile(new File(path +"/" + image.getNomeArquivo())));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String nameFile2 = image.getNomeArquivo();

                        resize_image(file2,nameFile2.replace(".jpg",""), path);

                    }


                    bMap = BitmapFactory.decodeFile(path +"/" + image.getNomeArquivo());


                    if (bMap == null) {
                        continue;
                    }

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bMap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);


                    byte[] byteArray = byteArrayOutputStream .toByteArray();

                    encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

                    bMap = null;

                } catch (Error e2) {
                    e2.printStackTrace();
                    continue;
                }

                try {
                    joImagem.put("ocorrencia_documento_id",image.getOcorrenciaDocumentoId().longValue());
                    joImagem.put("nome_arquivo",image.getNomeArquivo());
                    joImagem.put("arquivo",encoded);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                jaImagens.put(joImagem);
                JSONObject json = new JSONObject();
                try {
                    json.put("imagens",jaImagens);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String strJson;
                try{
                    strJson = json.toString();
                } catch (OutOfMemoryError e){
                    alert("Imagem está muito grande, reconfigure a resolução de largura da mesma.");
                    return ;
                }
                //Log.d("Json", json.toString());

                final String codigo_acesso = String.valueOf(myapp.getUsuario().getCodigoAcesso());
                String url = "http://api.softlog.eti.br/api/softlog/imagem";


                //Registro do usuario e criacao do banco de dados
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                image.setSincronizado(true);
                                myapp.getDaoSession().update(image);
                                /*
                                for(int i=0; i < imagens.size();i++){
                                    ImagemOcorrencia img = imagens.get(i);
                                    img.setSincronizado(true);
                                    myapp.getDaoSession().update(img);
                                }*/
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //Log.d("ERRO",error.toString());
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> parameters = new HashMap<String,String>();
                        parameters.put("imagens",jaImagens.toString());
                        parameters.put("codigo_acesso",codigo_acesso);
                        return parameters;
                    }

                };

                AppSingleton.getInstance(myapp.getApplicationContext()).addToRequestQueue(stringRequest,"Ocorrencias");

            }

        }

    }

    public void sendTracking() {

        if (!connectivity.isConnected(mContext)) {
            return;
        }

        final List<TrackingGps> trackingGpss = manager.findTrackingGpsNaoSincronizado();

        if (trackingGpss.size()>0) {
            final JSONArray jaTracking = new JSONArray();

            for (int i=0; i < trackingGpss.size(); i++){
                JSONObject joTracking = new JSONObject();
                TrackingGps trackingGps = trackingGpss.get(i);

                try {
                    joTracking.put("motorista_cpf",trackingGps.getMotoristaCpf().toString());
                    joTracking.put("data_localizacao",trackingGps.getDataLocalizacao().toString());
                    joTracking.put("latitude",trackingGps.getLatitude().doubleValue());
                    joTracking.put("longitude",trackingGps.getLongitude().doubleValue());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                jaTracking.put(joTracking);
            }

            JSONObject json = new JSONObject();
            try {
                json.put("tracking",jaTracking);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String strJson = json.toString();

            //Log.d("Json", json.toString());

            final String codigo_acesso = String.valueOf(myapp.getUsuario().getCodigoAcesso());
            String url = "http://api.softlog.eti.br/api/softlog/tracking";

            //Registro do usuario e criacao do banco de dados
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            for(int i=0; i < trackingGpss.size();i++){
                                TrackingGps trackingGps = trackingGpss.get(i);
                                myapp.getDaoSession().delete(trackingGps);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    //Log.d("ERRO",error.toString());
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> parameters = new HashMap<String,String>();
                    parameters.put("tracking",jaTracking.toString());
                    parameters.put("codigo_acesso",codigo_acesso);
                    return parameters;
                }

            };
            AppSingleton.getInstance(myapp.getApplicationContext()).addToRequestQueue(stringRequest,"Tracking");
        }
    }

    private void alert(String s){
        Toast.makeText(mContext, s,Toast.LENGTH_LONG).show();
    }

    private void resize_image(File file, String nameFile, String path){

       try {
            int width = myapp.getConfigResolucao();

            File resizedImage = new Resizer(this.mContext)
                    .setTargetLength(width)
                    .setOutputFormat("JPEG")
                    .setOutputFilename(nameFile)
                    .setOutputDirPath(path)
                    .setSourceImage(file)
                    .getResizedFile();

        } catch (IOException e) {
            //Log.d("erro",e.getMessage());
            e.printStackTrace();
        }

    }

    private File file_from(Uri uri) throws IOException {
        InputStream inputStream = this.mContext.getContentResolver().openInputStream(uri);
        String fileName = getFileName(uri);
        String[] splitName = splitFileName(fileName);
        File tempFile = File.createTempFile(splitName[0], splitName[1]);

        tempFile = rename(tempFile, fileName);
        tempFile.deleteOnExit();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(tempFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (inputStream != null) {
            copy(inputStream, out);
            inputStream.close();
        }

        if (out != null) {
            out.close();
        }
        return tempFile;
    }

    private String[] splitFileName(String fileName) {
        String name = fileName;
        String extension = "";
        int i = fileName.lastIndexOf(".");
        if (i != -1) {
            name = fileName.substring(0, i);
            extension = fileName.substring(i);
        }

        return new String[]{name, extension};
    }

    private File rename(File file, String newName) {
        File newFile = new File(file.getParent(), newName);
        if (!newFile.equals(file)) {
            if (newFile.exists() && newFile.delete()) {
                // Log.d("FileUtil", "Delete old " + newName + " file");
            }
            if (file.renameTo(newFile)) {
                // Log.d("FileUtil", "Rename file to " + newName);
            }
        }
        return newFile;
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = this.mContext.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf(File.separator);
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


}
