package br.eti.softlog.softlogtmsentregas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import br.eti.softlog.adapters.DataAdapterDocumento;
import br.eti.softlog.model.Documento;
import br.eti.softlog.model.DocumentoCheck;
import br.eti.softlog.viewmodel.DocumentoEntregaViewModel;
import br.eti.softlog.viewmodel.PrincipalViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DocumentosEntregaActivity extends AppCompatActivity {

    List<Documento> documentos;
    List<DocumentoCheck> documentoCheck;
    Long idDestinatario;

    EntregasApp app;
    Manager manager;
    DataAdapterDocumento adapter;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.btn_iniciar)
    Button btnIniciar;

    DocumentoEntregaViewModel mViewModel;
    String destinatarioCnpj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documentos_entrega);

        Intent intent = getIntent();

        destinatarioCnpj = intent.getStringExtra("destinatario_cnpj");

        ButterKnife.bind(this);

        mViewModel = ViewModelProviders.of(this).get(DocumentoEntregaViewModel.class);

        adapter = new DataAdapterDocumento(getApplicationContext(),
                (EntregasApp) getApplicationContext(),
                mViewModel.getDocumentos());

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }
}
