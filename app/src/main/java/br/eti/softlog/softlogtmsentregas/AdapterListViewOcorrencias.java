package br.eti.softlog.softlogtmsentregas;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import br.eti.softlog.model.Documento;
import br.eti.softlog.model.OcorrenciaDocumento;

public class AdapterListViewOcorrencias extends BaseAdapter {

    private final List<OcorrenciaDocumento> ocorrenciaDocumentos;
    private final Activity act;

    public AdapterListViewOcorrencias(List<OcorrenciaDocumento> ocorrenciaDocumentos, Activity act) {
        this.ocorrenciaDocumentos = ocorrenciaDocumentos;
        this.act = act;
    }

    @Override
    public int getCount() {
        return ocorrenciaDocumentos.size();
    }

    @Override
    public Object getItem(int position) {
        return ocorrenciaDocumentos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = act.getLayoutInflater().inflate(R.layout.list_view_ocorrencias,parent,false);

//        TextView txtNumero = view.findViewById(R.id.entregaNumero);
//        TextView txtDestinatario = view.findViewById(R.id.entregaDestinatario);
//        TextView txtTelefone = view.findViewById(R.id.entregaTelefone);
//        TextView txtEndereco = view.findViewById(R.id.entregaEndereco);
//        TextView txtBairro = view.findViewById(R.id.entregaEndereco);
//        TextView txtCep = view.findViewById(R.id.entregaCep);
//        TextView txtCidade = view.findViewById(R.id.entregaCidade);
//        TextView txtColor = view.findViewById(R.id.txtColor);
//
//        EditText editTemp = new EditText(view.getContext());
//
//        editTemp.setVisibility(View.INVISIBLE);
//
//        Documento doc = documentos.get(position);
//
//        String numeroNfe;
//
//        numeroNfe = "NFe: " + doc.getNumeroNotaFiscal().toString() + "-" +
//                doc.getSerie().toString();
////        numeroNfe = "NFe: " + doc.getNumeroNotaFiscal().toString().
////                replaceFirst("^0+(?!$)", "") + "-" +
////                doc.getSerie().toString().replaceFirst("^0+(?!$)", "");
//
//        txtNumero.setText(numeroNfe);
//        txtNumero.setBackgroundColor(Color.LTGRAY);
//
//        txtDestinatario.setText(doc.getDestinatario().getNome().trim());
//        txtTelefone.setText(doc.getDestinatario().getTelefone());
//        txtEndereco.setText(doc.getDestinatario().getEndereco() + " " +
//                doc.getDestinatario().getNumero());
//        txtBairro.setText(doc.getDestinatario().getBairro().trim());
//        String cep = doc.getDestinatario().getCep();
//
//        cep = cep.substring(0,5) + "-" + cep.substring(5,8);
//
//        txtCep.setText(cep);
//        txtCidade.setText(doc.getDestinatario().getCidade().getNomeCidade().trim() + "-" +
//                doc.getDestinatario().getCidade().getUf().trim());
//
//
//
//
//        if (doc.getOcorrencia().getPendencia()){
//            txtColor.setBackgroundColor(Color.RED);
//        } else if (doc.getOcorrencia().getId() > 0) {
//            txtColor.setBackgroundColor(Color.GREEN);
//        } else {
//            txtColor.setBackgroundColor(Color.YELLOW);
//        }

        return view;
    }

    public List<OcorrenciaDocumento> getData() {
        return ocorrenciaDocumentos;
    }

}
