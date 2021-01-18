package br.eti.softlog.softlogtmsentregas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.lifecycle.Observer;
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
import br.eti.softlog.model.Entregas;
import br.eti.softlog.model.ImagemOcorrencia;
import br.eti.softlog.model.OcorrenciaDocumento;
import br.eti.softlog.model.Pessoa;
import br.eti.softlog.utils.RecyclerViewClickListener;
import br.eti.softlog.utils.Util;
import br.eti.softlog.viewmodel.PrincipalViewModel;
import br.eti.softlog.worker.DocumentosWorker;
import butterknife.ButterKnife;
import de.cketti.fileprovider.PublicFileProvider;

import android.app.DatePickerDialog;
import android.app.Dialog;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PrincipalDraweActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener{

    static final int DATE_DIALOG_ID = 0;

    PrincipalViewModel mViewModel;
    FloatingActionButton btnData;
    FloatingActionButton btnAtualizar;

    EntregasApp myapp;

    public DataAdapterEntregas3 adapterView;
    public DataAdapterEntregas3 adapterView2;

    List<Entregas> entregas;
    List<Entregas> entregues;

    ProgressBar pbAtualizar;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    ViewPager pager;
    TabLayout mTabLayout;
    TabItem firstItem, secondItem, thirdItem;
    PrincipalPagerAdapter adapter;
    TextView txtDate;
    TextView txtUsuario;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_drawe);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Entregas");


        mViewModel = ViewModelProviders.of(this).get(PrincipalViewModel.class);
        myapp = (EntregasApp) getApplicationContext();

        setPermissao();
        ActivityUtils.finishAllActivitiesExceptNewest();

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
        txtDate = findViewById(R.id.txt_date);

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

            /*
            mViewModel.get

            // cria a intent
            Intent intent = new Intent(getApplicationContext(), DocumentoActivity.class);

            // seta o parametro do medico a exibir os dados
            intent.putExtra("id_destinatario", entrega.getId().toString());

            //  chama a Activity que mostra os detalhes
            startActivity(intent);
            finish();
             */

        };

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        //Operacoes do Dia
        mViewModel.loadDocumentos(myapp.getDate());
        entregas = mViewModel.getEntregas();
        entregues = mViewModel.getEntregues();

        adapterView = new DataAdapterEntregas3(getApplicationContext(), mViewModel.getEntregas(), myapp, listener);
        mViewModel.getEntregasLiveData().observe(this,entregaListUpdateObserver);

        adapterView2 = new DataAdapterEntregas3(getApplicationContext(), mViewModel.getEntregues(), myapp, listener);
        mViewModel.getEntreguesLiveData().observe(this,entregueListUpdateObserver);

        this.dataSync();

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
                mViewModel.loadDocumentos(myapp.getDate());
                btnAtualizar.hide();
            }
        });

        //txtUsuario.setText(myapp.getUsuario().getNome());
        txtDate.setText(Util.getDateFormat(myapp.getDate()));
        //mViewModel.sendOcorrencias();

        /*
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
         */

    }

    Observer<List<Pessoa>> entregaListUpdateObserver = new Observer<List<Pessoa>>() {
        @Override
        public void onChanged(List<Pessoa> entregaList) {

            Log.d("WorkManager","Atualizando lista! " + String.valueOf(mViewModel.getEntregas().size()));

            adapterView.getData().clear();
            adapterView.getData().addAll(mViewModel.getEntregas());
            adapterView.notifyDataSetChanged();

        }
    };

    Observer<List<Pessoa>> entregueListUpdateObserver = new Observer<List<Pessoa>>() {
        @Override
        public void onChanged(List<Pessoa> entregaList) {

            Log.d("WorkManager","Atualizando lista! " + String.valueOf(mViewModel.getEntregues().size()));

            adapterView2.getData().clear();
            adapterView2.getData().addAll(mViewModel.getEntregues());
            adapterView2.notifyDataSetChanged();
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


                    /*
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
           /*
           try {
                dataSync.sendProtocolos();
            } catch (JSONException e) {
                e.printStackTrace();
          }*/
        } else if (id == R.id.atualizar) {
            mViewModel.loadDocumentos(myapp.getDate());
            alert("Dados Atualizados.");
        } else if (id == R.id.sincronizar) {
            /*
            this.dataSync(myapp.getDate());
            dataSync2.sendOcorrencias();
            dataSync2.sendImagens();

             */

        } else if (id == R.id.reenvio_imagens) {

            /*
            for (Documento documento:documentos){
                for (OcorrenciaDocumento oco:documento.getOcorrenciaDocumentos()){
                    for (ImagemOcorrencia img:oco.getImagemOcorrencias()){
                        img.setSincronizado(false);
                        myapp.getDaoSession().update(img);
                    }
                }
            }

             */
        } else if (id == R.id.reenvio_ocorrencia) {
            /*
            for (Documento documento:documentos){
                for (OcorrenciaDocumento oco:documento.getOcorrenciaDocumentos()){
                    oco.setSincronizado(false);
                    myapp.getDaoSession().update(oco);
                }
            }

             */

            //dataSync2.sendOcorrencias();
            //dataSync2.sendImagens();

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


    private void dataSync() {

        Log.d("WorkManager DataSync",myapp.getDate().toString());
        mViewModel.loadDocumentos(myapp.getDate());

        Data inputData = new Data.Builder()
                .putString("data",Util.getDateFormatYMD(myapp.getDate()))
                .build();

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();



        OneTimeWorkRequest otwRequest =
                new OneTimeWorkRequest.Builder(DocumentosWorker.class)
                        .setInputData(inputData)
                        .setConstraints(constraints).build();
        WorkManager.getInstance().enqueue(otwRequest);

        WorkManager.getInstance(getApplicationContext()).getWorkInfoByIdLiveData(otwRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {
                        pbAtualizar.setVisibility(View.VISIBLE);
                        Log.d("WorkManager",workInfo.getState().toString());
                        if (workInfo != null && workInfo.getState() == WorkInfo.State.FAILED) {

                            pbAtualizar.setVisibility(View.INVISIBLE);
                            pbAtualizar.setVisibility(View.GONE);

                        }

                        if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                            pbAtualizar.setVisibility(View.INVISIBLE);
                            pbAtualizar.setVisibility(View.GONE);
                            Log.d("WorkManager",myapp.getDate().toString());
                            mViewModel.loadDocumentos(myapp.getDate());
                        }
                    }
                });

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


                    Date data_corrente = Util.getDate(year, monthOfYear, dayOfMonth);
                    myapp.setDate(data_corrente);
                    txtDate.setText(Util.getDateFormat(myapp.getDate()));
                    dataSync();

                }
            };




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
