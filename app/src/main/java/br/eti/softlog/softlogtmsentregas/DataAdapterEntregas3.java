package br.eti.softlog.softlogtmsentregas;


import android.content.Context;
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
import br.eti.softlog.model.Pessoa;
import br.eti.softlog.utils.RecyclerViewClickListener;

public class DataAdapterEntregas3 extends RecyclerView.Adapter<DataAdapterEntregas3.ViewHolder>  {

    private RecyclerViewClickListener mListener;

    private final List<Pessoa> entregas;
    private EntregasApp app;

    public DataAdapterEntregas3(Context context, List<Pessoa> entregas, EntregasApp app,
                                RecyclerViewClickListener listener) {

        this.entregas = entregas;
        this.app = app;
        mListener = listener;

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

        Pessoa entrega = entregas.get(i);

        String numeroNfe;

//        numeroNfe = "NFe: " + doc.getNumeroNotaFiscal().toString().
//                replaceFirst("^0+(?!$)", "") + "-" +
//                doc.getSerie().toString().replaceFirst("^0+(?!$)", "");


        viewHolder.txtDestinatario.setText(entrega.getNome().trim());
        viewHolder.txtTelefone.setText(entrega.getTelefone());
        viewHolder.txtEndereco.setText(entrega.getEndereco() + " " +
                entrega.getNumero());
        viewHolder.txtBairro.setText(entrega.getBairro().trim());
        String cep = entrega.getCep();

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
        viewHolder.txtCidade.setText(entrega.getCidade().getNomeCidade().trim() + "-" +
                entrega.getCidade().getUf().trim());

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
                Log.d("Adapter","Olá");
            }
        });
    }

    @Override
    public int getItemCount() {
        return entregas.size();
    }

    public List<Pessoa> getData() {
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
        TextView txtRomaneio;
        TextView txtOcorrencia;
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
            txtColor = view.findViewById(R.id.txtColor);
            txtDistancia = view.findViewById(R.id.txtKm);
            txtDuracao = view.findViewById(R.id.txtDuracao);
            txtRemetente = view.findViewById(R.id.txtEmitente);
            editTemp = new EditText(view.getContext());
            btnEntregar = view.findViewById(R.id.btn_entrega);

        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getAdapterPosition());
        }
    }

}