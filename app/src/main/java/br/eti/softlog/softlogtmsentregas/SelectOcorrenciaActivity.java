package br.eti.softlog.softlogtmsentregas;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.List;

import br.eti.softlog.model.Ocorrencia;
import br.eti.softlog.model.OcorrenciaDao;

public class SelectOcorrenciaActivity extends AppCompatActivity {

    EntregasApp app;

    List<Ocorrencia> ocorrencias;
    Long idDocumento;
    Long idOcorrencia;
    ArrayAdapter<Ocorrencia> adapter;
    ListView lista_oco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_ocorrencia);

        Intent i = getIntent();

        idDocumento = i.getLongExtra("id_documento",0);

        app = (EntregasApp)getApplicationContext();

        if (app.getFiltroOcorrencias())
            ocorrencias = app.getDaoSession().getOcorrenciaDao().
                    queryBuilder()
                    .where(OcorrenciaDao.Properties.Ativo.eq(1))
                    .orderAsc(OcorrenciaDao.Properties.Id).list();
        else
            ocorrencias = app.getDaoSession().getOcorrenciaDao()
                    .queryBuilder()
                    .orderAsc(OcorrenciaDao.Properties.Id).list();


        adapter = new ArrayAdapter<Ocorrencia>(this,
                android.R.layout.simple_list_item_1,ocorrencias);

        lista_oco = findViewById(R.id.lista_ocorrencia);

        lista_oco.setAdapter(adapter);
        lista_oco.setPadding(16,8,16,8);


        lista_oco.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // pega o o item selecionado com os dados da pessoa
                Ocorrencia ocorrencia = (Ocorrencia) lista_oco.getItemAtPosition(position);

                // cria a intent
                Intent intent = new Intent(getApplicationContext(), RegistroOcorrenciasActivity.class);
                // seta o parametro do medico a exibir os dados
                intent.putExtra("id_ocorrencia",ocorrencia.getId());
                intent.putExtra("id_documento",idDocumento);

                //  chama a Activity que mostra os detalhes
                startActivity(intent);
            }

        });

    }

    private void alert(String s){
        Toast.makeText(this, s,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select_ocorrencia, menu);


        SearchView mSearchView = (SearchView) menu.findItem(R.id.localizar)
                .getActionView();
        mSearchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        //
        mSearchView.setQueryHint("Digite para começar a pesquisar");

        // exemplos de utilização:
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                ocorrencias = app.getDaoSession().getOcorrenciaDao().queryBuilder().where(
                        OcorrenciaDao.Properties.Ocorrencia.like("%" + query + "%")
                ).orderAsc(OcorrenciaDao.Properties.Id).list();

                if (ocorrencias.size() == 0) {
                    alert("Não foi possível encontrar nenhuma ocorrência");
                    app.getDaoSession().getOcorrenciaDao().queryBuilder()
                            .orderAsc(OcorrenciaDao.Properties.Id).list();
                }
                adapter.clear();
                adapter.addAll(ocorrencias);
                // fire the event
                adapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//
//                if (newText.length() > 4) {
//
//                    ocorrencias = app.getDaoSession().getOcorrenciaDao().queryBuilder().where(
//                            OcorrenciaDao.Properties.Ocorrencia.like(newText)
//                    ).orderAsc(OcorrenciaDao.Properties.Id).list();
//
//                    if (ocorrencias.size() == 0) {
//                        return false;
//                    }
//                    adapter.clear();
//                    adapter.addAll(ocorrencias);
//                    // fire the event
//                    adapter.notifyDataSetChanged();
//                }
                return false;
            }

        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();



//        if (id == R.id.menu_continuar) {
//
//            Intent i = new Intent(getApplicationContext(),RegistroOcorrenciasActivity.class);
//
//            i.putExtra("id_ocorrencia",idOcorrencia);
//            i.putExtra("id_documento", idDocumento);
//
//
//            startActivity(i);
//
//
//        }
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
