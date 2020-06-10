package br.eti.softlog.softlogtmsentregas;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.maps.model.LatLng;

import br.eti.softlog.utils.Util;

import java.util.Formatter;
import java.util.Locale;

import br.eti.softlog.model.Documento;


public class DocumentoActivity extends AppCompatActivity {

    EntregasApp app;
    Manager manager;
    Util util;
    Formatter formatter;
    Documento doc;

    Long idDocumento;

    TextView txtChave;
    TextView txtDadosGerais;
    TextView txtNumero;
    TextView txtEmissao;
    TextView txtExpedicao;
    TextView txtExpedicaoRomaneio;
    TextView txtValor;
    TextView txtPeso;
    TextView txtVolume;
    TextView txtDadosRemetente;
    TextView txtRemetente;
    TextView txtOrigem;
    TextView txtDadosDestinatario;
    TextView txtDestinatarioNome;
    TextView txtEndereco;
    TextView txtBairro;
    TextView txtCep;
    TextView txtCidade;
    TextView txtTelefone;
    TextView txtOcorrencia;
    TextView txtDataOcorrencia;
    TextView txtRecebidoPor;
    TextView txtRomaneio;
    TextView txtDataExpedicaoRomaneio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documento);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Documento");

        app = (EntregasApp)getApplicationContext();
        manager = new Manager(app);
        util = new Util();
        Locale ptBr = new Locale("pt", "BR");
        formatter = new Formatter(ptBr);


        txtChave = findViewById(R.id.entregaChave);
        txtNumero = findViewById(R.id.entregaNumero);
        txtEmissao = findViewById(R.id.entregaEmissao);
        txtExpedicao = findViewById(R.id.entregaExpedicao);
        txtValor = findViewById(R.id.entregaValor);
        txtPeso = findViewById(R.id.entregaPeso);
        txtVolume = findViewById(R.id.entregaVolumes);
        txtRemetente = findViewById(R.id.entregaRemetente);
        txtOrigem = findViewById(R.id.entregaOrigem);
        txtDestinatarioNome = findViewById(R.id.entregaDestinatario);
        txtEndereco = findViewById(R.id.entregaEndereco);
        txtBairro = findViewById(R.id.entregaBairro);
        txtCep = findViewById(R.id.entregaCep);
        txtCidade = findViewById(R.id.entregaCidade);
        txtTelefone = findViewById(R.id.entregaTelefone);
        txtDadosGerais = findViewById(R.id.textDadosGerais);
        txtDadosRemetente = findViewById(R.id.textRemetente);
        txtDadosDestinatario = findViewById(R.id.textDestinatario);
        txtRomaneio = findViewById(R.id.txt_romaneio);
        txtDataExpedicaoRomaneio = findViewById(R.id.txt_data_expedicao_romaneio);

        txtOcorrencia = findViewById(R.id.txtOcorrencia);
        txtDataOcorrencia = findViewById(R.id.txt_data_ocorrencia);

        Intent intent = getIntent();
        String sIdDocumento = intent.getStringExtra("id_documento");
        idDocumento = Long.valueOf(sIdDocumento);
        doc = manager.findDocumentoById(idDocumento);


//        txtDadosGerais.setBackgroundColor(Color.BLUE);
//        txtDadosGerais.setTextColor(Color.WHITE);
//        txtDadosDestinatario.setBackgroundColor(Color.BLUE);
//        txtDadosDestinatario.setTextColor(Color.WHITE);
//        txtDadosRemetente.setBackgroundColor(Color.BLUE);
//        txtDadosRemetente.setTextColor(Color.WHITE);

        txtChave.setText(doc.getChaveNfe());

        txtNumero.setText(doc.getNumeroNotaFiscal().toString().trim() + "-" +
                doc.getSerie().toString().trim());

        txtEmissao.setText(util.getDateFormatDMY(doc.getDataEmissao()));
        txtExpedicao.setText(util.getDateFormatDMY(doc.getDataExpedicao()));

        txtValor.setText(doc.getValor().toString());
        txtPeso.setText(doc.getPeso().toString());
        txtVolume.setText(doc.getVolumes().toString());

        txtRemetente.setText(doc.getRemetente().getNome() + " - " + doc.getRemetente().getCnpjCpf());
        txtOrigem.setText(doc.getRemetente().getCidade().getNomeCidade().trim() + "-" +
                doc.getRemetente().getCidade().getUf().trim());

        txtDestinatarioNome.setText(doc.getDestinatario().getNome().trim());
        txtTelefone.setText(doc.getDestinatario().getTelefone());
        txtEndereco.setText(doc.getDestinatario().getEndereco() + " " +
                doc.getDestinatario().getNumero());
        txtBairro.setText(doc.getDestinatario().getBairro().trim());
        String cep = doc.getDestinatario().getCep();

        cep = cep.substring(0,5) + "-" + cep.substring(5,8);

        txtCep.setText(cep);
        txtCidade.setText(doc.getDestinatario().getCidade().getNomeCidade().trim() + "-" +
                doc.getDestinatario().getCidade().getUf().trim());

        if (doc.getDataOcorrencia() == null) {
            txtDataOcorrencia.setText(util.getDateFormatDMY(doc.getRomaneio().getDataRomaneio()));
        }
        else {
            txtDataOcorrencia.setText(util.getDateFormatDMY(doc.getDataOcorrencia()));
        }

        txtOcorrencia.setText(doc.getOcorrencia().toString());

        txtRomaneio.setText(doc.getRomaneio().getNumeroRomaneio().toString());
        txtDataExpedicaoRomaneio.setText(util.getDateFormatDMY(doc.getRomaneio().getDataExpedicao()));


//        LatLng posicaoInicial = new LatLng(latitude,longitude);
//        LatLng posicaiFinal = new LatLng(latitude,longitude);
//
//        double distance = SphericalUtil.computeDistanceBetween(posicaoInicial, posicaiFinal);
//        Log.i("LOG","A Distancia é = "+formatNumber(distance));
//
//        private String formatNumber(double distance) {
//            String unit = "m";
//            if (distance  1000) {
//                distance /= 1000;
//                unit = "km";
//            }
//
//            return String.format("%4.3f%s", distance, unit);
//        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_documento, menu);

        if (!doc.getOcorrencia().getPendencia() && doc.getOcorrencia().getId()>0){
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(false);
        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();

        if (id == R.id.menu_entregue) {

            Intent i = new Intent(getApplicationContext(),RegistroOcorrenciasActivity.class);

            i.putExtra("id_ocorrencia",Long.valueOf(1));
            i.putExtra("id_documento",idDocumento);

            startActivity(i);

        } else if (id == R.id.menu_pendencia){
            Intent i = new Intent(getApplicationContext(), SelectOcorrenciaActivity.class);

            i.putExtra("id_documento", idDocumento);

            startActivity(i);

        } else if (id == R.id.menu_ocorrencias) {
            //Intent i = new Intent(getApplicationContext(),OcorrenciasListActivity.class);
            //i.putExtra("id_documento",idDocumento);
            //startActivity(i);
            //finish();
        } else if (id == R.id.menu_canhotos) {
            Intent i = new Intent(getApplicationContext(),ImagensOcorrencias.class);
            i.putExtra("id_documento",idDocumento);
            startActivity(i);
            finish();
        }


        else if (id == R.id.menu_waze){

            if (doc.getDestinatario().getLatitude() != null) {
                Double latitude = Double.valueOf(doc.getDestinatario().getLatitude());
                Double longitude = Double.valueOf(doc.getDestinatario().getLongitude());
                String uri = "https://waze.com/ul?ll=" + doc.getDestinatario().getLatitude() + "," +
                        doc.getDestinatario().getLongitude()+ "&navigate=yes";
                startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
            }
        }
        else if (id == R.id.menu_maps){

            if (doc.getDestinatario().getLatitude() != null) {
                Double latitude = Double.valueOf(doc.getDestinatario().getLatitude());
                Double longitude = Double.valueOf(doc.getDestinatario().getLongitude());

                Uri gmmIntentUri = Uri.parse("google.navigation:q="+ doc.getDestinatario().getLatitude() + ","
                        + doc.getDestinatario().getLongitude());

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        }
        else if (id == R.id.menu_mapa){
            Intent i = new Intent(getApplicationContext(),MapsActivity.class);

            if (doc.getDestinatario().getLatitude() != null) {
                Double latitude = Double.valueOf(doc.getDestinatario().getLatitude());
                Double longitude = Double.valueOf(doc.getDestinatario().getLongitude());
                i.putExtra("latitude", latitude);
                i.putExtra("longitude", longitude);
            }
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);

    }

    private void alert(String s){
        Toast.makeText(this, s,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent i = new Intent(getApplicationContext(),MainActivity.class);

        i.putExtra("id_documento",idDocumento);

        startActivity(i);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        onSupportNavigateUp();
    }
}
