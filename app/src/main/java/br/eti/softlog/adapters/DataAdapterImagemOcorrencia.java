package br.eti.softlog.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import br.eti.softlog.model.ImagemOcorrencia;
import br.eti.softlog.softlogtmsentregas.EntregasApp;
import br.eti.softlog.softlogtmsentregas.MainActivityCrop;
import br.eti.softlog.softlogtmsentregas.R;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

public class DataAdapterImagemOcorrencia extends RecyclerView.Adapter<DataAdapterImagemOcorrencia.ViewHolder> {

    private Context context;
    private List<ImagemOcorrencia> imagensOcorrencias;
    private EntregasApp app;

    public DataAdapterImagemOcorrencia(Context context, EntregasApp app, List<ImagemOcorrencia> imagensOcorrencias) {
        this.context = context;
        this.imagensOcorrencias = imagensOcorrencias;
        this.app = app;
    }

    @NonNull
    @Override
    public DataAdapterImagemOcorrencia.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_layout_imagens_ocorrencias, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ImagemOcorrencia img = imagensOcorrencias.get(i);
        String txtNfe = "NFe: " + img.getOcorrenciaDocumento().getDocumento().getNumeroNotaFiscal();
        String txtOcorrencia = img.getOcorrenciaDocumento().getOcorrencia().getOcorrencia();

        viewHolder.txtNFe.setText(txtNfe);
        viewHolder.txtOcorrencia.setText(txtOcorrencia);

        if (img.getSincronizado()){
            viewHolder.txtStatus.setText("Enviado para o sistema!");
            viewHolder.txtStatus.setTextColor(Color.BLUE);
        } else {
            viewHolder.txtStatus.setText("Não enviado para o sistema");
            viewHolder.txtStatus.setTextColor(Color.RED);
        }

        String fileImagem =  app.getApplicationContext().getFilesDir().toString() + "/" +
                img.getNomeArquivo();

        Picasso.get().load(new File(fileImagem)).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(viewHolder.imgOcorrencia);



       viewHolder.imgOcorrencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                File file = new File(fileImagem);
                Uri photoURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider",file);
                intent.setDataAndType(photoURI, "image/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);

                 */
            }
        });

       viewHolder.btnSubstituir.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i = new Intent(context, MainActivityCrop.class);


               i.putExtra("id_ocorrencia", Long.valueOf(1));
               i.putExtra("id_documento", img.getOcorrenciaDocumento().getDocumentoId());
               i.putExtra("data_ocorrencia",img.getOcorrenciaDocumento().getDataOcorrencia());
               i.putExtra("hora_ocorrencia", img.getOcorrenciaDocumento().getHoraOcorrencia());
               i.putExtra("nome_recebedor", img.getOcorrenciaDocumento().getNomeRecebedor());
               i.putExtra("doc_recebedor", img.getOcorrenciaDocumento().getDocumentoRecebedor());
               i.putExtra("observacao", img.getOcorrenciaDocumento().getObservacoes());
               i.putExtra("latitude",img.getOcorrenciaDocumento().getLatitude());
               i.putExtra("longitude",img.getOcorrenciaDocumento().getLongitude());
               i.putExtra("id_imagem",img.getId());


               startActivity(i);
               ((Activity)v.getContext()).finish();
           }
       });

    }

    @Override
    public int getItemCount() {
        return imagensOcorrencias.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtNFe;
        TextView txtOcorrencia;
        TextView txtStatus;
        ImageView imgOcorrencia;
        Button btnSubstituir;

        public ViewHolder(View view) {
            super(view);
            txtNFe = (TextView)view.findViewById(R.id.txt_nota_fiscal);
            txtOcorrencia = (TextView)view.findViewById(R.id.txt_ocorrencia);
            txtStatus = (TextView)view.findViewById(R.id.txt_status);
            imgOcorrencia = (ImageView)view.findViewById(R.id.img_ocorrencia);
            btnSubstituir = view.findViewById(R.id.btnAlterar);


        }


    }

}
