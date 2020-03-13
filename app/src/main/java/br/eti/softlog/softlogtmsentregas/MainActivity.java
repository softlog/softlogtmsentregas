package br.eti.softlog.softlogtmsentregas;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blankj.utilcode.util.ActivityUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.JsonObject;
import com.idescout.sql.SqlScoutServer;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.eti.softlog.model.Documento;
import br.eti.softlog.model.DocumentoDao;
import br.eti.softlog.model.Pessoa;
import br.eti.softlog.model.Regiao;
import br.eti.softlog.utils.AppSingleton;
import br.eti.softlog.utils.Connectivity;
import br.eti.softlog.utils.Util;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.sentry.core.Sentry;

import static br.eti.softlog.utils.Util.getDateFormatYMD;

public class MainActivity extends AppCompatActivity implements OnLocationUpdatedListener {

    EntregasApp myapp;
    ListView lista_entregas;
    Manager manager;
    DataSync dataSync2;
    Connectivity connectivity;

    private FusedLocationProviderClient mFusedLocationClient;




    AdapterListViewEntregas adapter;
    List<Documento> documentos;
    List<Pessoa> entregas;

    ExtractRomaneioJson extractRomaneioJson;
    ExtractOcorrenciaJson extractOcorrenciaJson;

    ProgressBar progressBar;

    TextView txtDate;
    TextView txtUsuario;

    static final int DATE_DIALOG_ID = 0;

    private int mInterval = 5000; // 5 seconds by default, can be changed later
    private Handler mHandler;

    @OnClick(R.id.btnTodos)
    void onSubmitTodos() {

        Intent i = new Intent(getApplicationContext(), TakePhotoActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.btnConferidos)
    void onClickConferidos() {
        Intent i = new Intent(getApplicationContext(), MainActivityCrop.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ActivityUtils.finishAllActivitiesExceptNewest();

        //AndPermission.with(this);

        AndPermission.with(this)
                .permission(
                        Permission.WRITE_EXTERNAL_STORAGE,
                        Permission.READ_EXTERNAL_STORAGE
                )
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                    }
                }).onDenied(new Action() {
            @Override
            public void onAction(List<String> permissions) {
                //alert("Sem estas permissões o aplicativo não pode funcionar.");
                myapp.logout();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();

            }
        }).start();



        AndPermission.with(this)
                .permission(
                        Permission.CAMERA
                )
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                    }
                }).onDenied(new Action() {
            @Override
            public void onAction(List<String> permissions) {
                //alert("Sem estas permissões o aplicativo não pode funcionar.");
                myapp.logout();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();

            }
        }).start();

        AndPermission.with(this)
                .permission(
                        Permission.ACCESS_FINE_LOCATION,
                        Permission.ACCESS_COARSE_LOCATION
                )
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                    }
                }).onDenied(new Action() {
            @Override
            public void onAction(List<String> permissions) {
                //alert("Sem estas permissões o aplicativo não pode funcionar.");
                myapp.logout();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();

            }
        }).start();


        ButterKnife.bind(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        myapp = (EntregasApp) getApplicationContext();
        manager = new Manager(myapp);
        extractRomaneioJson = new ExtractRomaneioJson(getApplicationContext());
        extractOcorrenciaJson = new ExtractOcorrenciaJson(getApplicationContext());
        dataSync2 = new DataSync(getApplicationContext());
        connectivity = new Connectivity();

        progressBar = findViewById(R.id.pbMain);
        progressBar = findViewById(R.id.pbMain);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.GONE);
        // myapp.getPathDocs();

        documentos = manager.findDocumentoByDataRomaneio(myapp.getDate());

        lista_entregas = findViewById(R.id.lista_entrega);
        adapter = new AdapterListViewEntregas(documentos, this);
        lista_entregas.setAdapter(adapter);

        txtDate = findViewById(R.id.txtDate);


        Date data_corrente = myapp.getDate();
        txtDate.setText(getDateFormat(data_corrente));

        txtUsuario = findViewById(R.id.txtUsuario);
        txtUsuario.setText(myapp.getUsuario().getNome().toString());
        //loadOcorrencias();
        this.dataSync(data_corrente);

        Intent intentSource = getIntent();
        String flagServico = intentSource.getStringExtra("flagServico");


        //Inicializa os Servicos
        Intent it = new Intent(this.getApplicationContext(), ServiceMain.class);
        startService(it);

        Intent it2 = new Intent(this.getApplicationContext(),LocationService.class);
        startService(it2);


        //Intent it2 = new Intent(this.getApplicationContext(),ServiceTracking.class);
        //startService(it2);
        try {
            getDistanceDuration();
        } catch (Exception e) {

        }


        lista_entregas.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // pega o o item selecionado com os dados da pessoa
                Documento documento = (Documento) lista_entregas.getItemAtPosition(position);

                // cria a intent
                Intent intent = new Intent(getApplicationContext(), DocumentoActivity.class);
                // seta o parametro do medico a exibir os dados
                intent.putExtra("id_documento", documento.getId().toString());

                //  chama a Activity que mostra os detalhes
                startActivity(intent);
            }

        });

//        mHandler = new Handler();
//        startRepeatingTask();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);

        SearchView mSearchView = (SearchView) menu.findItem(R.id.localizar)
                .getActionView();
        mSearchView.setInputType(InputType.TYPE_CLASS_NUMBER);
        //
        mSearchView.setQueryHint("Digite o número do Protocolo");

        //lista_entregas = findViewById(R.id)
        // exemplos de utilização:
//        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                try {
//                    Long protocolo_id = Long.valueOf(query);
//                    protocolos = manager.findProtocoloSetorByIdProtocolo(protocolo_id);
//                    if (protocolos.size()==0){
//                        alert("Protocolo não encontrado");
//                    }
//                    reloadAllData();
//                } catch (Exception e) {
////                    Log.d("Erro ao converter",e.toString());
//                    alert("Favor digitar apenas números.");
//                }
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
////                Log.i("well", " this worked");
//                return false;
//            }
//        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.scanner) {
            Intent iScanner = new Intent(getApplicationContext(), BarCode2Activity.class);
            startActivity(iScanner);

        } else if (id == R.id.localizar) {

/*            try {
                dataSync.sendProtocolos();
            } catch (JSONException e) {
                e.printStackTrace();
            }*/


        } else if (id == R.id.atualizar) {

            documentos = manager.findDocumentoByDataRomaneio(myapp.getDate());
            reloadAllData();
            //loadOcorrencias();
            alert("Dados Atualizados.");

        } else if (id == R.id.sincronizar) {

            this.dataSync(myapp.getDate());
            dataSync2.sendOcorrencias();
            dataSync2.sendImagens();

        } else if (id == R.id.config) {
            Intent i = new Intent(getApplicationContext(), SettingsActivityMain.class);
            startActivity(i);
        } else if (id == R.id.data_corrente) {
            showDialog(DATE_DIALOG_ID);

        } else if (id == R.id.menu_mapa) {
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);

        } else if (id == R.id.exit) {
            myapp.logout();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);

    }

    private void alert(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }


    private void dataSync(Date data) {

        reloadAllData();

        if (!connectivity.isConnected(getApplicationContext())) {
            return;
        }

        boolean r = myapp.getConfigDownloadMobile();

        if (connectivity.isConnectedMobile(getApplicationContext())) {
            if (!myapp.getConfigDownloadMobile()) {
                return;
            }
        }

        alert("Sincronizando com o servidor!");
        String cData = Util.getDateFormatYMD(data);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        String codigo_acesso = String.valueOf(myapp.getUsuario().getCodigoAcesso());
        String cpf = myapp.getUsuario().getCpf();

        String url = "http://api.softlog.eti.br/api/softlog/romaneio2/" + codigo_acesso +
                "/" + cData + "/" + cpf;

        //url = "http://api.softlog.eti.br/api/softlog/protocolo/53/2018-02-01/0";
        //url = "http://api.softlog.eti.br/api/softlog/romaneio/81/2017-12-22/16286172840";

        //Log.d("Url",url);
        progressBar.setVisibility(View.VISIBLE);
        //Registro do usuario e criacao do banco de dados
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        extractRomaneioJson.extract(response);
                        reloadAllData();
                        progressBar.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //Log.d("ERRO",error.toString());
                if (error.networkResponse != null) {
                    reloadAllData();
                    progressBar.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        AppSingleton.getInstance(myapp.getApplicationContext()).addToRequestQueue(stringRequest, "Romaneios");
        //Log.d("Log","Processo Concluido!");
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        Calendar calendario = Calendar.getInstance();

        int ano = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, ano, mes,
                        dia);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {

                    Date data_corrente = getDate(year, monthOfYear, dayOfMonth);

                    String dataExtenso = getDateFormat(data_corrente);
                    txtDate.setText(dataExtenso);

                    myapp.setDate(data_corrente);

                    entregas = manager.findPessoasByDataRomaneio(myapp.getDate());

                    dataSync(data_corrente);

                    try {
                        getDistanceDuration();
                    } catch (Exception e ){

                    }

//                    reloadAllData();

//                    Log.d("Quantidade", String.valueOf(protocolos.size()));
                }
            };

    public static Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static String getDateFormat(Date data) {
        DateFormat formatador = DateFormat.getDateInstance(DateFormat.FULL, new Locale("pt", "BR"));
        String dataExtenso = formatador.format(data);
        return dataExtenso;
    }

    private void reloadAllData() {
        // get new modified random data
        // update data in our adapter
        documentos = manager.findDocumentoByDataRomaneio(myapp.getDate());

        adapter.getData().clear();
        adapter.getData().addAll(documentos);
        // fire the event
        adapter.notifyDataSetChanged();
    }

    private void loadOcorrencias() {


        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        String codigo_acesso = String.valueOf(myapp.getUsuario().getCodigoAcesso());

        String url = "http://api.softlog.eti.br/api/softlog/ocorrencia/" + codigo_acesso;

        //url = "http://api.softlog.eti.br/api/softlog/protocolo/53/2018-02-01/0";
        //url = "http://api.softlog.eti.br/api/softlog/romaneio/81/2017-12-22/16286172840";

        //Log.d("Url",url);

        //Registro do usuario e criacao do banco de dados
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        extractOcorrenciaJson.extract(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("ERRO",error.toString());

            }
        });
        AppSingleton.getInstance(myapp.getApplicationContext()).addToRequestQueue(stringRequest, "Ocorrencias");
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                alert("Tarefa periodica"); //this function can change value of mInterval.
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }


    public void getDistanceDuration() {



        if (!connectivity.isConnected(getApplicationContext())) {
            return;
        }

        if (connectivity.isConnectedMobile(getApplicationContext())){
            boolean usar_rede_movel = myapp.getConfigUploadOcorrenciaMobile();

            if (!usar_rede_movel){
                return;
            }
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Double latitude = location.getLatitude();
                            Double longitude = location.getLongitude();

                            final String sLatitude = String.valueOf(latitude);
                            final String sLongitude = String.valueOf(longitude);
                            final int tamanho = documentos.size();
                            final int ultimo = tamanho -1;

                            //Log.d("Tamanho",String.valueOf(tamanho));
                            for (int i=0;i < tamanho; i++) {
                                //Log.d("Posicao", String.valueOf(i));
                                Documento doc = documentos.get(i);
                                if (doc.getDestinatario().getLongitude() == null) {

                                    if (i == ultimo) {

                                    }
                                    continue;
                                }

                                String sLongitudeDestino = String.valueOf(doc.getDestinatario().getLongitude());
                                String sLatitudeDestino = String.valueOf(doc.getDestinatario().getLatitude());

                                String url = "http://router.project-osrm.org/route/v1/driving/"
                                        + sLongitude + ","
                                        + sLatitude + ";"
                                        + sLongitudeDestino + ","
                                        + sLatitudeDestino
                                        + "?overview=false";


                                if (i == ultimo) {
                                    Ion.with(getApplicationContext())
                                            .load(url)
                                            .asJsonObject()
                                            .setCallback(new FutureCallback<JsonObject>() {
                                                @Override
                                                public void onCompleted(Exception e, JsonObject result) {

                                                    //Log.d("Resposta", result.toString());
                                                    try {
                                                        JSONObject jObj = new JSONObject(result.toString());

                                                        JSONArray routes = jObj.getJSONArray("routes");

                                                        JSONObject route = routes.getJSONObject(0);

                                                        String sDuration = route.getString("duration");
                                                        String sDistance = route.getString("distance");

                                                        //Log.d("Gravando Posição", doc.getChaveNfe());
                                                        doc.setDistance(Double.valueOf(sDistance));
                                                        doc.setTempoEstimado(Double.valueOf(sDuration));

                                                        myapp.getDaoSession().update(doc);

                                                        //Log.d("Aviso", "Todos itens enviados para a fila");

                                                    } catch (JSONException e1) {
                                                        e1.printStackTrace();
                                                    }
                                                    // do stuff with the result or error
                                                }
                                            });
                                } else {
                                    Ion.with(getApplicationContext())
                                            .load(url)
                                            .asJsonObject()
                                            .setCallback(new FutureCallback<JsonObject>() {
                                                @Override
                                                public void onCompleted(Exception e, JsonObject result) {

                                                    //Log.d("Resposta", result.toString());
                                                    try {
                                                        Log.d("Ponto","Parada");
                                                        JSONObject jObj = new JSONObject(result.toString());

                                                        JSONArray routes = jObj.getJSONArray("routes");

                                                        JSONObject route = routes.getJSONObject(0);

                                                        String sDuration = route.getString("duration");
                                                        String sDistance = route.getString("distance");

                                                        //Log.d("Gravando Chave", doc.getChaveNfe());
                                                        doc.setDistance(Double.valueOf(sDistance));
                                                        doc.setTempoEstimado(Double.valueOf(sDuration));

                                                        myapp.getDaoSession().update(doc);

                                                    } catch (JSONException e1) {
                                                        e1.printStackTrace();
                                                        doc.setDistance(Double.valueOf("0.00"));
                                                        doc.setTempoEstimado(Double.valueOf("0.00"));
                                                    }
                                                    // do stuff with the result or error
                                                }
                                            });
                                }
                            }

                        }
                    }
                });

    }


    @Override
    public void onLocationUpdated(Location location) {

    }
}
