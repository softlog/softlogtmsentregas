package br.eti.softlog.softlogtmsentregas;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import br.eti.softlog.model.Documento;
import br.eti.softlog.model.Entregas;
import br.eti.softlog.model.Pessoa;
import br.eti.softlog.utils.RecyclerViewClickListener;

import static androidx.core.content.ContextCompat.startActivity;

public class DataAdapterEntregas3 extends RecyclerView.Adapter<DataAdapterEntregas3.ViewHolder>  {

    private RecyclerViewClickListener mListener;
    private Manager manager;
    private final List<Entregas> entregas;
    private EntregasApp app;
    Context context;

    public DataAdapterEntregas3(Context context, List<Entregas> entregas, EntregasApp app,
                                RecyclerViewClickListener listener) {

        this.entregas = entregas;
        this.app = app;
        this.manager = new Manager(this.app);
        mListener = listener;
        this.context = context;

    }

    @NonNull
    @Override
    public DataAdapterEntregas3.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_view_entregas_3 , viewGroup, false);

        return new DataAdapterEntregas3.ViewHolder(view, mListener);


    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.editTemp.setVisibility(View.INVISIBLE);

        Entregas entrega = entregas.get(i);

        String numeroNfe;

//        numeroNfe = "NFe: " + doc.getNumeroNotaFiscal().toString().
//                replaceFirst("^0+(?!$)", "") + "-" +
//                doc.getSerie().toString().replaceFirst("^0+(?!$)", "");


        viewHolder.txtDestinatario.setText(entrega.getDestinatario().getNome().trim());
        if (entrega.getDestinatario().getTelefone() == null){
            viewHolder.txtTelefone.setText("Telefone não informado.");
        } else {
            viewHolder.txtTelefone.setText("Fone: " + entrega.getDestinatario().getTelefone());
        }

        viewHolder.txtEndereco.setText(entrega.getDestinatario().getEndereco() + " " +
                entrega.getDestinatario().getNumero());
        viewHolder.txtBairro.setText(entrega.getDestinatario().getBairro().trim());
        String cep = entrega.getDestinatario().getCep();

        /*
        try{
            viewHolder.txtOcorrencia.setText(doc.getOcorrencia().getId().toString() + " - " +
                    doc.getOcorrencia().getOcorrencia());
        } catch (Exception e){
            viewHolder.txtOcorrencia.setText("OCORRENCIA NAO ENCONTRADA");
        }
         */

        cep = cep.substring(0,5) + "-" + cep.substring(5,8);

        viewHolder.txtCep.setText(cep);
        viewHolder.txtCidade.setText(entrega.getDestinatario().getCidade().getNomeCidade().trim() + "-" +
                entrega.getDestinatario().getCidade().getUf().trim());


        //List<Documento> documentos = manager.findDocumentosByRomaneioDestinatario(app.getDate(),entrega.getDestinatario().getCnpjCpf());
        String listaNotasFiscais;
        listaNotasFiscais = "";
        for (Documento documento:entrega.getDocumentos()){
            listaNotasFiscais = listaNotasFiscais + documento.getNumeroNotaFiscal() + " ";
        }
        //listaNotasFiscais = listaNotasFiscais + ",";
        //listaNotasFiscais.replace(", ,","");

        viewHolder.txtQtdNotas.setText(String.valueOf(entrega.getDocumentos().size()));
        viewHolder.txtNotasFiscais.setText(listaNotasFiscais);

        String sDistancia;
        String sDuracao;

        /*
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
         */

        viewHolder.btnEntregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OcorrenciaStepActivity.class);
                intent.putExtra("destinatario_cnpj",entrega.getDestinatario().getCnpjCpf());
                intent.putExtra("id_ocorrencia_padrao", Long.valueOf(1));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return entregas.size();
    }

    public List<Entregas> getData() {
        return entregas;
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private RecyclerViewClickListener clickListener;


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
        TextView txtNotasFiscais;
        TextView txtOcorrencia;
        TextView txtQtdNotas;
        ImageButton btnEntregar;


        public ViewHolder(View view, RecyclerViewClickListener listener) {
            super(view);

            clickListener = listener;
            view.setOnClickListener(this);

            txtDestinatario = view.findViewById(R.id.entregaDestinatario);
            txtTelefone = view.findViewById(R.id.entregaTelefone);
            txtEndereco = view.findViewById(R.id.entregaEndereco);
            txtBairro = view.findViewById(R.id.entregaBairro);
            txtCep = view.findViewById(R.id.entregaCep);
            txtCidade = view.findViewById(R.id.entregaCidade);
            txtDistancia = view.findViewById(R.id.txtKm);
            txtDuracao = view.findViewById(R.id.txtDuracao);
            txtRemetente = view.findViewById(R.id.txtEmitente);
            txtNotasFiscais = view.findViewById(R.id.txt_notas_fiscais);
            txtQtdNotas = view.findViewById(R.id.txt_qtd_notas);
            editTemp = new EditText(view.getContext());
            btnEntregar = view.findViewById(R.id.btn_entrega);


        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getAdapterPosition());
        }
    }

}