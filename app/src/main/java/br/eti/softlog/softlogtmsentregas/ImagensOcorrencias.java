package br.eti.softlog.softlogtmsentregas;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.blankj.utilcode.util.ActivityUtils;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import br.eti.softlog.adapters.DataAdapterImagemOcorrencia;
import br.eti.softlog.model.ImagemOcorrencia;
import butterknife.ButterKnife;

public class ImagensOcorrencias extends AppCompatActivity {

    EntregasApp myapp;
    Manager manager;
    Long idDocumento;
    List<ImagemOcorrencia> imagensOcorrenciasList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagens_ocorrencias);

        ActivityUtils.finishAllActivitiesExceptNewest();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Canhotos");

        myapp = (EntregasApp) getApplicationContext();
        manager = new Manager(myapp);

        ButterKnife.bind(this);

        Intent intent = getIntent();

        idDocumento = intent.getLongExtra("id_documento",Long.valueOf(0));

        initViews();


    }

    private void initViews(){
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        imagensOcorrenciasList = manager.findImagensOcorrenciasByNFe(idDocumento);

        DataAdapterImagemOcorrencia adapter = new DataAdapterImagemOcorrencia(getApplicationContext(),
                myapp, imagensOcorrenciasList);

        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_imagens, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent i = new Intent(getApplicationContext(),DocumentoActivity.class);

        i.putExtra("id_documento",String.valueOf(idDocumento));

        startActivity(i);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        onSupportNavigateUp();
    }
}
