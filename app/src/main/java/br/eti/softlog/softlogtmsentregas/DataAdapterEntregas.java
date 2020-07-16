package br.eti.softlog.softlogtmsentregas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import br.eti.softlog.adapters.DataAdapterImagemOcorrencia;
import br.eti.softlog.model.Documento;
import br.eti.softlog.model.ImagemOcorrencia;
import br.eti.softlog.utils.MaskEditUtil;
import br.eti.softlog.utils.RecyclerViewClickListener;
import br.eti.softlog.utils.Util;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

/**
 * Created by Paulo Sérgio Alves on 2018/03/21.
 */

public class DataAdapterEntregas extends RecyclerView.Adapter<DataAdapterEntregas.ViewHolder>  {

    private RecyclerViewClickListener mListener;

    private final List<Documento> documentos;
    private EntregasApp app;

    public DataAdapterEntregas(Context context, List<Documento> documentos,  EntregasApp app,
                               RecyclerViewClickListener listener) {
        this.documentos = documentos;
        this.app = app;
        mListener = listener;
    }

    @NonNull
    @Override
    public DataAdapterEntregas.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_view_entregas , viewGroup, false);

        return new DataAdapterEntregas.ViewHolder(view, mListener);


    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.editTemp.setVisibility(View.INVISIBLE);

        Documento doc = documentos.get(i);

        String numeroNfe;

        numeroNfe = "NFe: " + doc.getNumeroNotaFiscal().toString() + "-" +
                doc.getSerie().toString();
//        numeroNfe = "NFe: " + doc.getNumeroNotaFiscal().toString().
//                replaceFirst("^0+(?!$)", "") + "-" +
//                doc.getSerie().toString().replaceFirst("^0+(?!$)", "");

        viewHolder.txtNumero.setText(numeroNfe);
        viewHolder.txtNumero.setBackgroundColor(Color.LTGRAY);

        viewHolder.txtRemetente.setText(doc.getRemetente().getNome());
        viewHolder.txtDestinatario.setText(doc.getDestinatario().getNome().trim());
        viewHolder.txtTelefone.setText(doc.getDestinatario().getTelefone());
        viewHolder.txtEndereco.setText(doc.getDestinatario().getEndereco() + " " +
                doc.getDestinatario().getNumero());
        viewHolder.txtBairro.setText(doc.getDestinatario().getBairro().trim());
        String cep = doc.getDestinatario().getCep();

        viewHolder.txtRomaneio.setText("Romaneio: " + doc.getRomaneio().getNumeroRomaneio()
                        + " - " +
                Util.getDateFormatDMY(doc.getRomaneio().getDataSaida().substring(0,10)));

        viewHolder.txtOcorrencia.setText(doc.getOcorrencia().getId().toString() + " - " +
                doc.getOcorrencia().getOcorrencia());

        cep = cep.substring(0,5) + "-" + cep.substring(5,8);

        viewHolder.txtCep.setText(cep);
        viewHolder.txtCidade.setText(doc.getDestinatario().getCidade().getNomeCidade().trim() + "-" +
                doc.getDestinatario().getCidade().getUf().trim());

        String sDistancia;
        String sDuracao;

        if (doc.getDistance() == null) {
            sDistancia = "N/D";
        } else {
            DecimalFormat df = new DecimalFormat("#,###,###.00");
            double distancia = doc.getDistance()/1000;
            sDistancia = df.format(distancia);
        }

        viewHolder.txtDistancia.setText(sDistancia);

        if (doc.getTempoEstimado() == null) {
            sDuracao = "N/D";
        } else {
            Long totalSecs = Math.round(doc.getTempoEstimado());
            Long hours = totalSecs / 3600;
            Long minutes = (totalSecs % 3600) / 60;
            Long seconds = totalSecs % 60;

            sDuracao = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }

        viewHolder.txtDuracao.setText(sDuracao);


        try {
            if (doc.getOcorrencia().getPendencia() &&
                    (doc.getIdOcorrencia() < 300 || doc.getIdOcorrencia() > 399)){
                viewHolder.txtColor.setBackgroundColor(Color.RED);
            } else if (doc.getOcorrencia().getId() == 1 || doc.getOcorrencia().getId() == 2) {
                if (app.getModoDaltonico())
                    viewHolder.txtColor.setBackgroundColor(Color.BLUE);
                else
                    viewHolder.txtColor.setBackgroundColor(Color.GREEN);
            } else {
                viewHolder.txtColor.setBackgroundColor(Color.YELLOW);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return documentos.size();
    }

    public List<Documento> getData() {
        return documentos;
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private RecyclerViewClickListener clickListener;

        TextView txtNumero;
        TextView txtDestinatario;
        TextView txtTelefone;
        TextView txtEndereco;
        TextView txtBairro;
        TextView txtCep;
        TextView txtCidade;
        TextView txtColor;
        TextView txtDistancia;
        TextView txtDuracao;
        TextView txtRemetente;
        EditText editTemp;
        TextView txtRomaneio;
        TextView txtOcorrencia;

        public ViewHolder(View view, RecyclerViewClickListener listener) {
            super(view);

            clickListener = listener;
            view.setOnClickListener(this);

            txtNumero = view.findViewById(R.id.entregaNumero);
            txtDestinatario = view.findViewById(R.id.entregaDestinatario);
            txtTelefone = view.findViewById(R.id.entregaTelefone);
            txtEndereco = view.findViewById(R.id.entregaEndereco);
            txtBairro = view.findViewById(R.id.entregaBairro);
            txtCep = view.findViewById(R.id.entregaCep);
            txtCidade = view.findViewById(R.id.entregaCidade);
            txtColor = view.findViewById(R.id.txtColor);
            txtDistancia = view.findViewById(R.id.txtKm);
            txtDuracao = view.findViewById(R.id.txtDuracao);
            txtRemetente = view.findViewById(R.id.txtEmitente);
            txtRomaneio = view.findViewById(R.id.txt_romaneio);
            txtOcorrencia = view.findViewById(R.id.txt_ocorrencia);
            editTemp = new EditText(view.getContext());

        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getAdapterPosition());
        }
    }

}