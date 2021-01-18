package br.eti.softlog.softlogtmsentregas;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.eti.softlog.model.Veiculo;

/**
 * Created by Paulo Sérgio Alves on 2018/03/23.
 */

public class ExtractRomaneioJson {

    EntregasApp app;
    Manager manager;
    private Context mContext;

    public ExtractRomaneioJson (Context context){
        mContext = context;
        app = (EntregasApp) context.getApplicationContext();
        manager = new Manager(app);
    }

    public void extract(String response) {
        JSONObject jObj = null;
        try {
            jObj = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        extract(jObj);
    }

    public void extract(JSONObject jObj){

        try {
            //Log.d("Response",response);
            //Log.d("Extraindo Dados","Romaneios");
            //Gravando Cidades
            JSONArray cidades = jObj.getJSONArray("cidades");

            app.getDb().beginTransaction();
            for(int i = 0;i<cidades.length();i++){
                JSONObject cidade = cidades.getJSONObject(i);
                Long idCidade = cidade.getLong("id_cidade");
                String nomeCidade = cidade.getString("nome_cidade");
                String uf = cidade.getString("uf");
                String cod_ibge = cidade.getString("cod_ibge");
                manager.addCidade(idCidade,nomeCidade,uf,cod_ibge);
            }


            //Gravando Pessoas
            JSONArray pessoas = jObj.getJSONArray("pessoas");

            for(int i = 0;i<pessoas.length(); i++){
                JSONObject pessoa = pessoas.getJSONObject(i);

                String cnpjCpf = pessoa.getString("cnpj_cpf");
                String nome = pessoa.getString("nome");
                String endereco = pessoa.getString("endereco");
                String numero = pessoa.getString("numero");
                String bairro = pessoa.getString("bairro");
                Long idCidade = pessoa.getLong("id_cidade");
                String cep = pessoa.getString("cep");
                String telefone = pessoa.getString("telefone");
                String latitude = pessoa.getString("latitude");
                String longitude = pessoa.getString("longitude");
                Long id = Long.valueOf(cnpjCpf);
                int tipoPessoa;
                if (cnpjCpf.length()==14)
                    tipoPessoa = 2;
                else
                    tipoPessoa = 1;

                Log.d("Pessoa",nome);
                manager.addPessoa(id,tipoPessoa,nome,cnpjCpf,endereco,numero,
                        bairro, idCidade,cep,telefone,null,
                        null,latitude,longitude);
            }

            //Gravando Veiculos
            JSONArray veiculos = jObj.getJSONArray("veiculo");

            for(int i = 0; i<veiculos.length();i++) {

                JSONObject veiculo = veiculos.getJSONObject(i);

                String placa = veiculo.getString("placa_veiculo");
                String descricao = veiculo.getString("descricao");

                manager.addVeiculo(placa,descricao);
            }

            //Gravando Ocorrencias
            JSONArray ocorrencias = jObj.getJSONArray("ocorrencias");

            for (int i = 0; i <ocorrencias.length(); i++ ){
                JSONObject ocorrencia = ocorrencias.getJSONObject(i);

                //Log.d("Ocorrencia",ocorrencia.toString());
                Long idOcorrencia = ocorrencia.getLong("id_ocorrencia");
                String ocorrenciaDesc = ocorrencia.getString("ocorrencia");
                int pendencia = ocorrencia.getInt("pendencia");
                int ativo = ocorrencia.getInt("aplicativo_mobile");
                int exigeRecebedor = ocorrencia.getInt("exige_recebedor");
                int exigeDocumento = ocorrencia.getInt("exige_documento");
                int exigeImagem = ocorrencia.getInt("exige_imagem");

                boolean bPendencia;

                if (pendencia == 1)
                    bPendencia = true;
                else
                    bPendencia = false;

                boolean bAtivo;

                if (ativo == 1)
                    bAtivo = true;
                else
                    bAtivo = false;

                boolean bExigeRecebedor;

                if (exigeRecebedor == 1)
                    bExigeRecebedor = true;
                else
                    bExigeRecebedor = false;

                boolean bExigeDocumento;

                if (exigeDocumento == 1)
                    bExigeDocumento = true;
                else
                    bExigeDocumento = false;

                boolean bExigeImagem;

                if (exigeImagem == 1)
                    bExigeImagem = true;
                else
                    bExigeImagem = false;

                manager.addOcorrencia(idOcorrencia,ocorrenciaDesc,bPendencia, bAtivo,
                        bExigeRecebedor, bExigeRecebedor, bExigeImagem);

            }
            //Grava as entregas
            JSONArray entregas = jObj.getJSONArray("entregas");
            for(int i = 0; i < entregas.length();i++){
                JSONObject entrega = entregas.getJSONObject(i);
                Long idEntrega =  entrega.getLong("id_entrega");
                Log.d("Id Entrega",String.valueOf(idEntrega));
                Long destinatarioId = Long.valueOf(entrega.getLong("destinatario_cnpj"));
                String dataExpedicao = entrega.getString("data_expedicao");
                int statusI = entrega.getInt("status");
                int temPendenciaI = entrega.getInt("tem_pendencia");
                String latitude = entrega.getString("latitude");
                String longitude = entrega.getString("longitude");
                int ordemEntrega = entrega.getInt("ordem_entrega");

                boolean status;
                if (statusI==1)
                    status = true;
                else
                    status = false;

                boolean temPendencia;
                if (temPendenciaI==1)
                    temPendencia = true;
                else
                    temPendencia = false;

                manager.addEntregas(idEntrega,destinatarioId,dataExpedicao,status,latitude,longitude,ordemEntrega,temPendencia);

            }

            //Gravando Romaneios
            JSONArray romaneios = jObj.getJSONArray("romaneios");
            for(int i = 0; i<romaneios.length();i++){

                JSONObject romaneio = romaneios.getJSONObject(i);

                Long idRomaneio = romaneio.getLong("id_romaneio");
                String numeroRomaneio = romaneio.getString("numero_romaneio");
                String dataRomaneio = romaneio.getString("data_romaneio");
                String dataSaida = romaneio.getString("data_saida");
                String dataChegada = romaneio.getString("data_chegada");
                String dataExpedicaoRomaneio = romaneio.getString("data_expedicao");
                String placaVeiculo = romaneio.getString("placa_veiculo");
                String motoristaCpf = romaneio.getString("motorista_cpf");
                String redespachadorCnpj = romaneio.getString("redespachador_cpf");
                Long cidadeOrigemId = romaneio.getLong("id_origem");

                Long longRedespachadorCnpj;
                if (redespachadorCnpj == "null")
                    longRedespachadorCnpj = null;
                else
                    longRedespachadorCnpj = Long.valueOf(redespachadorCnpj);

                Veiculo veiculoRomaneio = manager.findVeiculoByPlaca(placaVeiculo);
                Long idVeiculo;
                if (veiculoRomaneio == null)
                    idVeiculo = null;
                else
                    idVeiculo = veiculoRomaneio.getId();

                manager.addRomaneio(idRomaneio,numeroRomaneio,dataRomaneio,dataSaida,
                        dataChegada,idVeiculo,Long.valueOf(motoristaCpf),
                        longRedespachadorCnpj,cidadeOrigemId,
                        null,false, dataExpedicaoRomaneio);

                //Gravando Documentos
                JSONArray documentos = romaneio.getJSONArray("documentos");

                for(int j = 0; j<documentos.length();j++){
                    JSONObject documento = documentos.getJSONObject(j);
                    Long idNotaFiscalImp;
                    try {
                        idNotaFiscalImp = documento.getLong("id_nota_fiscal_imp");
                    } catch (JSONException e) {
                        idNotaFiscalImp = null;
                    }

                    if (j > 47) {
                        Log.d("Tag","Investigando");
                    }

                    String dataEmissao = documento.getString("data_emissao");
                    String dataExpedicao = documento.getString("data_expedicao");
                    String chaveNfe = documento.getString("chave_nfe");
                    String serie = documento.getString("serie");
                    String numeroNotaFiscal = documento.getString("numero_nota_fiscal");
                    Log.d("NFe",chaveNfe);
                    Long remetenteCnpj = documento.getLong("remetente_cnpj");
                    Long destinatarioCnpj = documento.getLong("destinatario_cnpj");
                    Long romaneioId = documento.getLong("id_romaneio");
                    Double valor = documento.getDouble("valor");
                    Double peso = documento.getDouble("peso");
                    Double volumes = documento.getDouble("volume");
                    Long idOcorrencia = documento.getLong("id_ocorrencia");
                    String dataOcorrencia1 = documento.getString("data_ocorrencia");
                    String cep = documento.getString("cep");
                    Long idEntrega = documento.getLong("id_entrega");
                    String dataOcorrencia;

                    Long idConhecimentoNotasFiscais;
                    Long idConhecimento;

                    try {
                        idConhecimentoNotasFiscais = documento.getLong("id_conhecimento_notas_fiscais");
                    } catch (JSONException e) {
                        idConhecimentoNotasFiscais = null;
                    }

                    try {
                        idConhecimento = documento.getLong("id_conhecimento");
                    } catch (JSONException e) {
                        idConhecimento = null;
                    }

                    if (dataOcorrencia1=="null"){
                        dataOcorrencia = null;
                    } else {
                        dataOcorrencia = dataOcorrencia1;
                    }

                    manager.addDocumento(idNotaFiscalImp,dataEmissao,dataExpedicao,
                            chaveNfe,serie,numeroNotaFiscal,remetenteCnpj,
                            destinatarioCnpj,romaneioId,valor,peso,volumes,
                            idOcorrencia,dataOcorrencia, idConhecimentoNotasFiscais,
                            idConhecimento, 0.0, 0.0, cep, idEntrega);

                }
            }
            app.getDb().setTransactionSuccessful();
        } catch (JSONException e1) {
            e1.printStackTrace();
            Log.d("Importacao Romaneio", e1.getMessage());
        } finally {
            app.getDb().endTransaction();
        }
    }

}
