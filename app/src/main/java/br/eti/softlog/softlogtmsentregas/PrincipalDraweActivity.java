package br.eti.softlog.softlogtmsentregas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import br.eti.softlog.model.Documento;
import br.eti.softlog.model.DocumentoDao;
import br.eti.softlog.model.ImagemOcorrencia;
import br.eti.softlog.model.OcorrenciaDocumento;
import br.eti.softlog.model.Pessoa;
import br.eti.softlog.utils.AppSingleton;
import br.eti.softlog.utils.Connectivity;
import br.eti.softlog.utils.RecyclerViewClickListener;
import br.eti.softlog.utils.Util;
import br.eti.softlog.viewmodel.PrincipalViewModel;
import br.eti.softlog.worker.DocumentosWorker;
import butterknife.ButterKnife;
import de.cketti.fileprovider.PublicFileProvider;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.SearchView;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PrincipalDraweActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener{

    static final int DATE_DIALOG_ID = 0;

    PrincipalViewModel mViewModel;

    FloatingActionButton btnData;
    FloatingActionButton btnAtualizar;

    EntregasApp myapp;
    Manager manager;
    DataSync dataSync2;
    Connectivity connectivity;
    Context context;
    Date dateSync;
    Util util;
    private FusedLocationProviderClient mFusedLocationClient;

    public DataAdapterEntregas2 adapterView;
    List<Documento> documentos;
    List<Pessoa> entregas;

    ExtractRomaneioJson extractRomaneioJson;
    ExtractOcorrenciaJson extractOcorrenciaJson;

    ProgressBar pbAtualizar;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    ViewPager pager;
    TabLayout mTabLayout;
    TabItem firstItem,secondItem,thirdItem;
    PrincipalPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_drawe);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Entregas");
        context = this.getApplicationContext();

        mViewModel = ViewModelProviders.of(this).get(PrincipalViewModel.class);
        myapp = (EntregasApp) getApplicationContext();
        manager = new Manager(myapp);
        util = new Util();

        setPermissao();

        Intent inCall = getIntent();
        extractRomaneioJson = new ExtractRomaneioJson(getApplicationContext());
        extractOcorrenciaJson = new ExtractOcorrenciaJson(getApplicationContext());
        dataSync2 = new DataSync(getApplicationContext());
        connectivity = new Connectivity();
        ActivityUtils.finishAllActivitiesExceptNewest();


        ButterKnife.bind(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        pager = findViewById(R.id.viewpager);
        mTabLayout = findViewById(R.id.tablayout);

        firstItem = findViewById(R.id.firstItem);
        secondItem = findViewById(R.id.secondItem);
        thirdItem = findViewById(R.id.thirditem);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        btnData = findViewById(R.id.fb_data);
        btnAtualizar = findViewById(R.id.fbAtualizar);
        btnAtualizar.hide();
        pbAtualizar = findViewById(R.id.pb_atualizar);


        pbAtualizar.setVisibility(View.INVISIBLE);
        pbAtualizar.setVisibility(View.GONE);

        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        adapter = new PrincipalPagerAdapter(getSupportFragmentManager(), mTabLayout.getTabCount());
        pager.setAdapter(adapter);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        RecyclerViewClickListener listener = (view, position) -> {

            Documento documento = (Documento) documentos.get(position);

            // cria a intent
            Intent intent = new Intent(getApplicationContext(), DocumentoActivity.class);

            // seta o parametro do medico a exibir os dados
            intent.putExtra("id_documento", documento.getId().toString());

            //  chama a Activity que mostra os detalhes
            startActivity(intent);
            finish();

        };

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        //Operacoes do Dia
        mViewModel.loadDocumentos(myapp.getDate());
        documentos = mViewModel.getDocumentos();
        adapterView = new DataAdapterEntregas2(getApplicationContext(), documentos, myapp, listener);
        Date data_corrente = myapp.getDate();

        mViewModel.getDocumentosLiveData().observe(this,documentoListUpdateObserver);

        //adapterView = new DataAdapterEntregas(getApplicationContext(), documentos, myapp, listener);
        this.dataSync(data_corrente);

        Intent intentSource = getIntent();
        String flagServico = intentSource.getStringExtra("flagServico");

        //Inicializa os Servicos
        Intent it = new Intent(this.getApplicationContext(), ServiceMain.class);
        startService(it);

        btnData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });


        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadAllData();
                btnAtualizar.hide();
            }
        });


        mViewModel.sendOcorrencias();

        mViewModel.getOutputWorkInfo().observe(this, listOfWorkInfo -> {

            // If there are no matching work info, do nothing
            if (listOfWorkInfo == null || listOfWorkInfo.isEmpty()) {
                return;
            }

            // We only care about the first output status.
            // Every continuation has only one worker tagged TAG_SYNC_DATA
            WorkInfo workInfo = listOfWorkInfo.get(0);
            Log.d("WorkManager", "WorkState: " + workInfo.getState());
            if (workInfo.getState() == WorkInfo.State.ENQUEUED) {
                //TODO: Notificar Usuario;
            } else {
                //TODO:
            }
        });

    }

    Observer<List<Documento>> documentoListUpdateObserver = new Observer<List<Documento>>() {
        @Override
        public void onChanged(List<Documento> documentosList) {

            Log.d("WorkManager","Atualizando lista! " + String.valueOf(mViewModel.getDocumentos().size()));

            adapterView.getData().clear();
            adapterView.getData().addAll(mViewModel.getDocumentos());
            // fire the event
            adapterView.notifyDataSetChanged();

        }
    };



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if(item.getItemId() == R.id.menuTab){
            Toast.makeText(this, "Btn is clicked.", Toast.LENGTH_SHORT).show();
        }
        return false;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal_2, menu);

        SearchView mSearchView = (SearchView) menu.findItem(R.id.localizar)
                .getActionView();
        mSearchView.setInputType(InputType.TYPE_CLASS_NUMBER);
        //
        mSearchView.setQueryHint("Digite o número da Nota Fiscal");

        //lista_entregas = findViewById(R.id)
        // exemplos de utilização:
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                try {


                    String data = Util.getDateFormatYMD(myapp.getDate());

                    QueryBuilder queryBuilder = myapp.getDaoSession().getDocumentoDao().queryBuilder()
                            .where(DocumentoDao.Properties.NumeroNotaFiscal.like("%" + query.trim() + "%"));


                    //queryBuilder.join(DocumentoDao.Properties.RomaneioId, Romaneio.class).
                    //        where(RomaneioDao.Properties.DataExpedicao.eq(data));

                    documentos  = queryBuilder.orderAsc(DocumentoDao.Properties.RomaneioId).list();

                    if (documentos.size()==0){
                        alert("Documento não encontrado");
                    }
                    /*
                    adapter.getData().clear();
                    adapter.getData().addAll(documentos);
                    // fire the event
                    adapter.notifyDataSetChanged();

                     */


                } catch (Exception e) {
                    Log.d("Erro ao converter",e.toString());
                }


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.i("well", " this worked");
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.scanner) {
            Intent iScanner = new Intent(getApplicationContext(),  BarCode2Activity.class);
            startActivity(iScanner);

        } else if (id == R.id.localizar) {

/*            try {
                dataSync.sendProtocolos();
            } catch (JSONException e) {
                e.printStackTrace();
          }*/



        } else if (id == R.id.atualizar) {


            //documentos = manager.findDocumentoByDataRomaneio(myapp.getDate());
            reloadAllData();
            //loadOcorrencias();
            alert("Dados Atualizados.");

        } else if (id == R.id.sincronizar) {
            /*
            this.dataSync(myapp.getDate());
            dataSync2.sendOcorrencias();
            dataSync2.sendImagens();

             */

        } else if (id == R.id.reenvio_imagens) {

            for (Documento documento:documentos){
                for (OcorrenciaDocumento oco:documento.getOcorrenciaDocumentos()){
                    for (ImagemOcorrencia img:oco.getImagemOcorrencias()){
                        img.setSincronizado(false);
                        myapp.getDaoSession().update(img);
                    }
                }
            }
        } else if (id == R.id.reenvio_ocorrencia) {

            for (Documento documento:documentos){
                for (OcorrenciaDocumento oco:documento.getOcorrenciaDocumentos()){
                    oco.setSincronizado(false);
                    myapp.getDaoSession().update(oco);
                }
            }

            dataSync2.sendOcorrencias();
            dataSync2.sendImagens();

        } else if (id == R.id.send_log) {


            //File logPath = new File(this.getApplicationContext().getFilesDir(), "log_sconfirmei");
            String nameLog =  myapp.getNameDb().replace(".db","") + "_log_sconfirmei.txt";
            File fileLog = new File(this.getApplicationContext().getFilesDir(), nameLog);
            Uri contentUri = PublicFileProvider.getUriForFile(getApplicationContext(),
                    "com.mydomain.publicfileprovider", fileLog );



            ///mnt/sdcard/sconfirmei/05137082864_114._log_sconfirmei.txt
            final Intent shareIntent = new Intent(Intent.ACTION_SEND);

            shareIntent.setType("text/*");

            //final File file = new File(myapp.getFileLog());

            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);

            startActivity(Intent.createChooser(shareIntent, "Compartilhar arquivo usando..."));

        } else if (id == R.id.send_db) {


            //File logPath = new File(this.getApplicationContext().getFilesDir(), "log_sconfirmei");
            String nameDb =  myapp.getNameDb();
            myapp.backupBD(getApplicationContext(),nameDb);

            String dirBackup = Environment.getExternalStorageDirectory() + "/sconfirmei";
            File fileDb = new File(dirBackup, nameDb);

            Uri contentUri = PublicFileProvider.getUriForFile(getApplicationContext(),
                    "com.mydomain.publicfileprovider", fileDb);
            ///mnt/sdcard/sconfirmei/05137082864_114._log_sconfirmei.txt
            final Intent shareIntent = new Intent(Intent.ACTION_SEND);

            shareIntent.setType("application/x-sqlite3");

            //final File file = new File(myapp.getFileLog());

            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);

            startActivity(Intent.createChooser(shareIntent, "Compartilhar banco de dados usando..."));


        } else if (id == R.id.config) {
            Intent i = new Intent(getApplicationContext(), SettingsActivityMain.class);
            startActivity(i);
        } else if (id == R.id.data_corrente) {
            showDialog(DATE_DIALOG_ID);

        } else if (id == R.id.menu_mapa) {

            Intent intent = new Intent(getApplicationContext(), MapasEntrega2.class);
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

        dateSync = data;

        Data inputData = new Data.Builder()
                .putString("data",Util.getDateFormatYMD(dateSync))
                .build();

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        pbAtualizar.setVisibility(View.VISIBLE);

        OneTimeWorkRequest otwRequest =
                new OneTimeWorkRequest.Builder(DocumentosWorker.class)
                        .setInputData(inputData)
                        .setConstraints(constraints).build();
        WorkManager.getInstance().enqueue(otwRequest);

        WorkManager.getInstance(getApplicationContext()).getWorkInfoByIdLiveData(otwRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {
                        Log.d("WorkManager",workInfo.getState().toString());
                        if (workInfo != null && workInfo.getState() == WorkInfo.State.FAILED) {

                            pbAtualizar.setVisibility(View.INVISIBLE);
                            pbAtualizar.setVisibility(View.GONE);
                        }

                        if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                            pbAtualizar.setVisibility(View.INVISIBLE);
                            pbAtualizar.setVisibility(View.GONE);
                            reloadAllData();
                            //btnAtualizar.show();
                        }
                    }
                });

    }



    private void reloadAllData() {
        // get new modified random data
        // update data in our adapter
        Log.d("WorkManager",myapp.getDate().toString());
        mViewModel.loadDocumentos(myapp.getDate());


        try {
            //getDistanceDuration();
        } catch (Exception e) {
            Log.d("erro", e.getMessage());
        }


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
                    //txtDate.setText(dataExtenso);

                    myapp.setDate(data_corrente);

                    entregas = manager.findPessoasByDataRomaneio(myapp.getDate());

                    dataSync(data_corrente);

                    try {
                    //    getDistanceDuration();
                    } catch (Exception e ){

                    }

//                  reloadAllData();

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

    public void setPermissao(){
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
    }



}
