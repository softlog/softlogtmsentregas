package br.eti.softlog.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.theartofdev.edmodo.cropper.CropImage;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import br.eti.softlog.model.Documento;
import br.eti.softlog.softlogtmsentregas.EntregasApp;
import br.eti.softlog.softlogtmsentregas.R;

public class DataAdapterDocumento extends RecyclerView.Adapter<DataAdapterDocumento.ViewHolder> {

    private LayoutInflater inflater;
    List<Documento> documentos;
    private Context ctx;
    EntregasApp app;

    public DataAdapterDocumento(Context context, EntregasApp app, List<Documento> documentos) {
        ctx = context;
        this.documentos = documentos;
        this.app = app;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.nota_fiscal_item, viewGroup, false);

        ViewHolder holder = new ViewHolder(view);

        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txtNf.setText(documentos.get(position).getNumeroNotaFiscal());

        holder.btnAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CropImage.activity()
                        .setActivityTitle("Captura de Canhoto")
                        .setRequestedSize(800,0)
                        .getIntent(ctx);
                ((Activity) ctx).startActivityForResult(intent, position);
            }
        });

        int qtCanhoto;

    }

    @Override
    public int getItemCount() {
        return documentos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView txtNf;
        protected TextView status;
        protected ImageButton btnAddImg;

        public ViewHolder(View itemView) {

            super(itemView);

            txtNf = (TextView) itemView.findViewById(R.id.txt_nf);
            status = itemView.findViewById(R.id.status);
            btnAddImg = itemView.findViewById(R.id.img_add_foto);

            //tvNotaFiscal = (TextView) itemView.findViewById(R.id.txt_nota_fiscal);
        }

    }

}
