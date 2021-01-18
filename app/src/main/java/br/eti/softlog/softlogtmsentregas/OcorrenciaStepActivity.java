package br.eti.softlog.softlogtmsentregas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import br.eti.softlog.adapters.OcorrenciaStepAdapter;
import br.eti.softlog.model.Documento;
import br.eti.softlog.model.Entregas;
import br.eti.softlog.model.Ocorrencia;
import br.eti.softlog.viewmodel.DocumentoEntregaViewModel;
import butterknife.ButterKnife;
import im.delight.android.location.SimpleLocation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.pixplicity.easyprefs.library.Prefs;
import com.stepstone.stepper.StepperLayout;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.List;

public class OcorrenciaStepActivity extends AppCompatActivity {

    private StepperLayout mStepperLayout;

    EntregasApp myapp;
    Manager manager;

    private SimpleLocation location;

    OcorrenciaStepAdapter ocorrenciaStepAdapter;

    DocumentoEntregaViewModel mViewModel;
    String destinatarioCnpj;


    List<String> ocorrenciasList;
    Long idOcorrenciaPadrao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocorrencia_step);

        ButterKnife.bind(this);

        mViewModel = ViewModelProviders.of(this).get(DocumentoEntregaViewModel.class);
        myapp = (EntregasApp)getApplicationContext();
        manager = new Manager(myapp);

        Intent intent = getIntent();

        destinatarioCnpj = intent.getStringExtra("destinatario_cnpj");
        idOcorrenciaPadrao = intent.getLongExtra("id_ocorrencia_padrao",0);
        mViewModel.iniciarOcorrencia(destinatarioCnpj);

        //documentos.mViewModel.getDocumentos(destinatarioCnpj);
        //ocorrencias = mViewModel.getOcorrencias();


        mStepperLayout = findViewById(R.id.stepperLayout);
        ocorrenciaStepAdapter = new OcorrenciaStepAdapter(getSupportFragmentManager(),this);
        mStepperLayout.setAdapter(ocorrenciaStepAdapter);

        if (idOcorrenciaPadrao == 1) {

            mStepperLayout.setCurrentStepPosition(1);
            mViewModel.getEntregas().setIdOcorrenciaPadrao(Long.valueOf(1));

        }



        // construct a new instance of SimpleLocation
        location = new SimpleLocation(this);
        // if we can't access the location yet
        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(this);
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Registro Ocorrências Entrega");


    }

    @Override
    public boolean onSupportNavigateUp() {

        //Intent i = new Intent(getApplicationContext(),PrincipalDraweActivity.class);
        //startActivity(i);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        onSupportNavigateUp();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


            if (resultCode == RESULT_OK) {

                Documento documento = mViewModel.getDocumentos().get(requestCode);
                /*
                        Uri resultUri = result.getUri();
                        Imagem.TipoImagem tipoImagem;
                        tipoImagem = Imagem.TipoImagem.ODOMETRO;

                        if (tipo.equals("maquina")){
                            tipoImagem = Imagem.TipoImagem.HORIMETRO;
                        }

                        ((AtividadeStepActivity)activity).manager.addImagem(activity.getApplicationContext(),
                                resultUri,atividade,null,tipoImagem);

                        path = pathRoot + "/" + atividade.getImagens().get(0).getNomeArquivo();

                        Picasso.get().load(new File(path)).networkPolicy(NetworkPolicy.NO_CACHE)
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .into(img);

                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Exception error = result.getError();
                    }
                 */

        }
    }
}