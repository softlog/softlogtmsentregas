package br.eti.softlog.softlogtmsentregas;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Credentials;
import android.net.Uri;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
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
import br.eti.softlog.model.OcorrenciaDocumentoDao;
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
    CognitoCachingCredentialsProvider credentialsProvider;

    public DataSync(Context context) {
        mContext = context;
        myapp = (EntregasApp) context.getApplicationContext();
        manager = new Manager(myapp);
        util = new Util();
        connectivity = new Connectivity();


        credentialsProvider = new CognitoCachingCredentialsProvider(
                mContext,
                "us-east-1:1dfdb86d-ce11-4a0c-b93d-2246d23be812",
                //ID do grupo de identidades
                Regions.US_EAST_1 // Região
        );

    }

    public void getRomaneios() {
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
        String uuid = myapp.getUsuario().getUuid();


        String url = "http://api.softlog.eti.br/api/softlog/romaneio_v4/" + codigo_acesso +
                "/" + data_expedicao + "/" + uuid + "/" + manager.getUltimaAlteracaoRomaneio(data_expedicao);

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

            }
        });

        AppSingleton.getInstance(myapp.getApplicationContext()).addToRequestQueue(stringRequest, "Login");
        //Log.d("Log","Processo Concluido!");
    }


    public void sendOcorrencias() {


        //Log.d(TAG,"Log file path is: " + LogManager.getInstance().getFilePath());

        /*
        if (!connectivity.isConnected(mContext)) {
            util.appendLog("Sem conexao com a internet.",myapp.getFileLog());
            //LogUtil.i(TAG, "Log file path is: " + LogManager.getInstance().getFilePath());
            return;
        }
        */

        if (false){
            return ;
        }
        if (connectivity.isConnectedMobile(mContext)) {
            boolean usar_rede_movel = myapp.getConfigUploadOcorrenciaMobile();

            if (!usar_rede_movel) {
                util.appendLog("Upload de Ocorrências", "Configurado para não fazer upload de ocorrencias.", myapp.getFileLog());
                //LogUtil.i(TAG, "Log file path is: " + LogManager.getInstance().getFilePath());
                return;
            }
        }

        /*
        OcorrenciaDocumento ocorrenciaDocumento;
        ocorrenciaDocumento = myapp.getDaoSession().getOcorrenciaDocumentoDao().queryBuilder()
                .where(OcorrenciaDocumentoDao.Properties.Id.eq(1209))
                .unique();
        ocorrenciaDocumento.setSincronizado(false);
        myapp.getDaoSession().update(ocorrenciaDocumento);
         */

        final List<OcorrenciaDocumento> ocorrencias = manager.findOcorrenciaNaoSincronizada();

        if (ocorrencias.size() > 0) {
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

                    util.appendLog("Envio Ocorrências", e.getMessage(), myapp.getFileLog());
                    e.printStackTrace();

                }

                jaOcorrencias.put(joOcorrencia);

            }

            JSONObject json = new JSONObject();
            try {
                json.put("ocorrencias", jaOcorrencias);
            } catch (JSONException e) {
                util.appendLog("Envio Ocorrências", e.getMessage(), myapp.getFileLog());
            }

            String strJson = json.toString();

            //Log.d("Json", json.toString());

            final String codigo_acesso = String.valueOf(myapp.getUsuario().getCodigoAcesso());
            String url = "http://api.softlog.eti.br/api/softlog/ocorrencia_v3";


            //Registro do usuario e criacao do banco de dados
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("Ocorrências",response);

                                JSONArray resultado;
                                JSONObject jResultado = new JSONObject(response);
                                resultado = jResultado.getJSONArray("ocorrencias");
                                myapp.getDb().beginTransaction();
                                for (int i = 0; i < resultado.length(); i++) {

                                    JSONObject aOcorrencia = resultado.getJSONObject(i);
                                    Long id = aOcorrencia.getLong("id_aplicativo");
                                    Long idServidor = aOcorrencia.getLong("id_servidor");

                                    OcorrenciaDocumento oco = myapp.getDaoSession()
                                            .getOcorrenciaDocumentoDao()
                                            .queryBuilder()
                                            .where(OcorrenciaDocumentoDao.Properties.Id.eq(id))
                                            .unique();

                                    oco.setSincronizado(true);
                                    oco.setIdServidor(idServidor);
                                    Date date = new Date();
                                    oco.setDataSincronizacao(util.getDateTimeFormatYMD(date));

                                    util.appendLog("Envio de ocorrências",
                                            "Ocorrência Doc.N.: " + oco.getDocumento().getChaveNfe() + " enviada", myapp.getFileLog());

                                    myapp.getDaoSession().update(oco);


                                }
                                myapp.getDb().setTransactionSuccessful();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (Exception e){
                                e.printStackTrace();
                            } finally {
                                myapp.getDb().endTransaction();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put("ocorrencias", jaOcorrencias.toString());
                    parameters.put("codigo_acesso", codigo_acesso);
                    return parameters;
                }

            };

            AppSingleton.getInstance(myapp.getApplicationContext()).addToRequestQueue(stringRequest, "Ocorrencias");
        }

    }


    public void sendImagens() {

        //if (!connectivity.isConnected(mContext)) {
        //    util.appendLog("Upload Imagem", "Sem conexão.",myapp.getFileLog());
        //    return;
        //}
        if (false){
            return ;
        }

        if (connectivity.isConnectedMobile(mContext)) {
            boolean usar_rede_movel = myapp.getConfigUploadImageMobile();
            if (!usar_rede_movel) {
                util.appendLog("Upload de imagem.", "Configurado para não fazer upload de ocorrencias.", myapp.getFileLog());
                return;
            }
        }

        final List<ImagemOcorrencia> imagens = manager.findImagensNaoSincronizada();

        if (imagens.size() > 0) {
            final JSONArray jaImagens = new JSONArray();

            for (int i = 0; i < imagens.size(); i++) {

                JSONObject joImagem = new JSONObject();
                ImagemOcorrencia image = imagens.get(i);
                if (image.getNomeArquivo() == null) {
                    continue;
                }

                String encoded;
                try {

                    String path = myapp.getApplicationContext().getFilesDir().toString();

                    //File root = Environment.getExternalStorageDirectory();
                    //ImageView IV = (ImageView) findViewById(R.id."image view")// ;

                    String fileImagem = image.getNomeArquivo();

                    AmazonS3 s3 = new AmazonS3Client(credentialsProvider);

                    TransferUtility transferUtility = new TransferUtility(s3, mContext);
                    s3.setRegion(Region.getRegion(Regions.SA_EAST_1));

                    //s3.setRegion(Region.getRegion(Regions.SA_EAST_1));

                    String key = "imagens/" + String.valueOf(myapp.getUsuario().getCodigoAcesso()) + "/" + fileImagem;
                    Log.d("Upload:", key);

                    final TransferObserver observer = transferUtility.upload(
                            "sconfirmei",
                            key,
                            new File(path + '/' + fileImagem),
                            CannedAccessControlList.PublicRead
                    );

                    observer.setTransferListener(new TransferListener() {
                        @Override
                        public void onStateChanged(int id, TransferState state) {
                            if (state.equals(TransferState.COMPLETED)) {

                                image.setSincronizado(true);
                                myapp.getDaoSession().update(image);
                                //Log.d("Imagem","Enviada com sucesso");

                            } else if (state.equals(TransferState.FAILED)) {
                                util.appendLog("Upload Imagem", "Falha no envio da imagem", myapp.getFileLog());
                                //Log.d("Imagem","Falha no envio");
                            }
                        }

                        @Override
                        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

                        }

                        @Override
                        public void onError(int id, Exception ex) {
                            //Log.d("Ocorreu um erro", ex.getMessage());

                            /*
                            FirebaseCrash.log("Banco Dados: " + String.valueOf(myapp.getUsuario().getCodigoAcesso()));
                            FirebaseCrash.log("Imagem: " + String.valueOf(fileImagem));
                            FirebaseCrash.report(new Exception("Erro Upload Imagem " +  ex.getMessage()));
                            FirebaseCrash.log("Usuario: " + myapp.getUsuario().getCpf());
                             */


                        }
                    });

                } catch (Exception e) {
                    /*
                    FirebaseCrash.log("Banco Dados: " + String.valueOf(myapp.getUsuario().getCodigoAcesso()));
                    FirebaseCrash.report(new Exception("Erro Upload Imagem " +  e.getMessage()));
                    FirebaseCrash.log("Usuario: " + myapp.getUsuario().getCpf());
                    util.appendLog("Upload Imagem", e.getMessage(),myapp.getFileLog());
                     */
                    continue;
                }
            }
        }
    }


    public void sendTracking() {
        if (1==1)
            return ;

        if (!connectivity.isConnected(mContext)) {
            return;
        }

        final List<TrackingGps> trackingGpss = manager.findTrackingGpsNaoSincronizado();

        if (trackingGpss.size() > 0) {
            final JSONArray jaTracking = new JSONArray();

            for (int i = 0; i < trackingGpss.size(); i++) {
                JSONObject joTracking = new JSONObject();
                TrackingGps trackingGps = trackingGpss.get(i);

                try {
                    joTracking.put("motorista_cpf", trackingGps.getMotoristaCpf().toString());
                    joTracking.put("data_localizacao", trackingGps.getDataLocalizacao().toString());
                    joTracking.put("latitude", trackingGps.getLatitude().doubleValue());
                    joTracking.put("longitude", trackingGps.getLongitude().doubleValue());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                jaTracking.put(joTracking);
            }

            JSONObject json = new JSONObject();
            try {
                json.put("tracking", jaTracking);
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
                            for (int i = 0; i < trackingGpss.size(); i++) {
                                TrackingGps trackingGps = trackingGpss.get(i);
                                myapp.getDaoSession().delete(trackingGps);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    //Log.d("ERRO",error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put("tracking", jaTracking.toString());
                    parameters.put("codigo_acesso", codigo_acesso);
                    return parameters;
                }

            };
            AppSingleton.getInstance(myapp.getApplicationContext()).addToRequestQueue(stringRequest, "Tracking");
        }
    }

    private void alert(String s) {
        Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
    }

    private void resize_image(File file, String nameFile, String path) {

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
