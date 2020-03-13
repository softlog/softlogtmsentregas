package br.eti.softlog.softlogtmsentregas;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import br.eti.softlog.model.Documento;
import br.eti.softlog.utils.MaskEditUtil;
import br.eti.softlog.utils.Util;

/**
 * Created by Paulo Sérgio Alves on 2018/03/21.
 */

public class AdapterListViewEntregas extends BaseAdapter {

    private final List<Documento> documentos;
    private final Activity act;

    public AdapterListViewEntregas(List<Documento> documentos, Activity act) {
        this.documentos = documentos;
        this.act = act;
    }

    @Override
    public int getCount() {
        return documentos.size();
    }

    @Override
    public Object getItem(int position) {
        return documentos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = act.getLayoutInflater().inflate(R.layout.list_view_entregas,parent,false);

        TextView txtNumero = view.findViewById(R.id.entregaNumero);
        TextView txtDestinatario = view.findViewById(R.id.entregaDestinatario);
        TextView txtTelefone = view.findViewById(R.id.entregaTelefone);
        TextView txtEndereco = view.findViewById(R.id.entregaEndereco);
        TextView txtBairro = view.findViewById(R.id.entregaBairro);
        TextView txtCep = view.findViewById(R.id.entregaCep);
        TextView txtCidade = view.findViewById(R.id.entregaCidade);
        TextView txtColor = view.findViewById(R.id.txtColor);
        TextView txtDistancia = view.findViewById(R.id.txtKm);
        TextView txtDuracao = view.findViewById(R.id.txtDuracao);

        TextView txtRemetente = view.findViewById(R.id.txtEmitente);

        EditText editTemp = new EditText(view.getContext());

        editTemp.setVisibility(View.INVISIBLE);

        Documento doc = documentos.get(position);

        String numeroNfe;

        numeroNfe = "NFe: " + doc.getNumeroNotaFiscal().toString() + "-" +
                doc.getSerie().toString();
//        numeroNfe = "NFe: " + doc.getNumeroNotaFiscal().toString().
//                replaceFirst("^0+(?!$)", "") + "-" +
//                doc.getSerie().toString().replaceFirst("^0+(?!$)", "");

        txtNumero.setText(numeroNfe);
        txtNumero.setBackgroundColor(Color.LTGRAY);

        txtRemetente.setText(doc.getRemetente().getNome());
        txtDestinatario.setText(doc.getDestinatario().getNome().trim());
        txtTelefone.setText(doc.getDestinatario().getTelefone());
        txtEndereco.setText(doc.getDestinatario().getEndereco() + " " +
                            doc.getDestinatario().getNumero());
        txtBairro.setText(doc.getDestinatario().getBairro().trim());
        String cep = doc.getDestinatario().getCep();

        cep = cep.substring(0,5) + "-" + cep.substring(5,8);

        txtCep.setText(cep);
        txtCidade.setText(doc.getDestinatario().getCidade().getNomeCidade().trim() + "-" +
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

        txtDistancia.setText(sDistancia);

        if (doc.getTempoEstimado() == null) {
            sDuracao = "N/D";
        } else {
            Long totalSecs = Math.round(doc.getTempoEstimado());
            Long hours = totalSecs / 3600;
            Long minutes = (totalSecs % 3600) / 60;
            Long seconds = totalSecs % 60;

            sDuracao = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }

        txtDuracao.setText(sDuracao);


        try {
            if (doc.getOcorrencia().getPendencia()){
                txtColor.setBackgroundColor(Color.RED);
            } else if (doc.getOcorrencia().getId() > 0) {
                txtColor.setBackgroundColor(Color.GREEN);
            } else {
                txtColor.setBackgroundColor(Color.YELLOW);
            }
        } catch (Exception e) {

        }


        return view;
    }

    public List<Documento> getData() {
        return documentos;
    }




}