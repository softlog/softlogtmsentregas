package br.eti.softlog.softlogtmsentregas;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import br.eti.softlog.adapters.DataAdapterImagemOcorrencia;
import br.eti.softlog.model.ImagemOcorrencia;
import br.eti.softlog.model.ImagemOcorrenciaDao;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ImagensOcorrencias2 extends AppCompatActivity {

    EntregasApp myapp;
    Manager manager;
    Long idDocumento;
    Long idImagem;
    ImagemOcorrencia img;


    @BindView(R.id.txt_nota_fiscal)
    TextView txtNFe ;

    @BindView(R.id.txt_ocorrencia)
    TextView txtOcorrencia;

    @BindView(R.id.txt_status)
    TextView txtStatus;

    @BindView(R.id.img_ocorrencia)
    ImageView imgOcorrencia;

    @BindView(R.id.btnAlterar)
    ImageButton btnSubstituir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagens_ocorrencias2);

        ButterKnife.bind(this);

        //ActivityUtils.finishAllActivitiesExceptNewest();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Canhotos");

        myapp = (EntregasApp) getApplicationContext();
        manager = new Manager(myapp);

        ButterKnife.bind(this);

        Intent intent = getIntent();

        idDocumento = intent.getLongExtra("id_documento",Long.valueOf(0));
        idImagem = intent.getLongExtra("id_imagem",Long.valueOf(0));

        btnSubstituir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        if (idImagem> 0) {
            img = myapp.getDaoSession()
                    .getImagemOcorrenciaDao()
                    .queryBuilder()
                    .where(ImagemOcorrenciaDao.Properties.Id.eq(idImagem))
                    .unique();
            initViews();
        } else {

            Intent intentCrop = CropImage.activity()
                    .setActivityTitle("Captura de Canhoto")
                    .setRequestedSize(800,0)
                    .getIntent(getApplicationContext());

            startActivityForResult(intentCrop, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);

        }

    }


    public void initViews(){
        String txtNfe = "NFe: " + img.getOcorrenciaDocumento().getDocumento().getNumeroNotaFiscal();
        String strOcorrencia = img.getOcorrenciaDocumento().getOcorrencia().getOcorrencia();

        txtNFe.setText(txtNfe);

        txtOcorrencia.setText(strOcorrencia);

        if (img.getSincronizado()){
            txtStatus.setText("Enviado para o sistema!");
            txtStatus.setTextColor(Color.BLUE);
        } else {
            txtStatus.setText("Não enviado para o sistema");
            txtStatus.setTextColor(Color.RED);
        }

        String fileImagem =  myapp.getApplicationContext().getFilesDir().toString() + "/" +
                img.getNomeArquivo();

        Picasso.get().load(new File(fileImagem)).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(imgOcorrencia);


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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
         */
    }
}
