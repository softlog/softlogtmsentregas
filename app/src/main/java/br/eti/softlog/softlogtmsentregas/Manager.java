package br.eti.softlog.softlogtmsentregas;

import android.content.Context;
import android.util.Log;

import com.pixplicity.easyprefs.library.Prefs;

import org.greenrobot.greendao.query.Join;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.Date;
import java.util.List;

import br.eti.softlog.model.Cidade;
import br.eti.softlog.model.CidadeDao;
import br.eti.softlog.model.Documento;
import br.eti.softlog.model.DocumentoDao;
import br.eti.softlog.model.Entregas;
import br.eti.softlog.model.EntregasDao;
import br.eti.softlog.model.ImagemOcorrencia;
import br.eti.softlog.model.ImagemOcorrenciaDao;
import br.eti.softlog.model.Ocorrencia;
import br.eti.softlog.model.OcorrenciaDao;
import br.eti.softlog.model.OcorrenciaDocumento;
import br.eti.softlog.model.OcorrenciaDocumentoDao;
import br.eti.softlog.model.Pessoa;
import br.eti.softlog.model.PessoaDao;
import br.eti.softlog.model.Regiao;
import br.eti.softlog.model.Romaneio;
import br.eti.softlog.model.RomaneioDao;
import br.eti.softlog.model.TrackingGps;
import br.eti.softlog.model.TrackingGpsDao;
import br.eti.softlog.model.Usuario;
import br.eti.softlog.model.UsuarioDao;
import br.eti.softlog.model.Veiculo;
import br.eti.softlog.model.VeiculoDao;
import br.eti.softlog.utils.Util;

/**
 * Created by Paulo Sérgio Alves on 2018/03/15.
 */

public class Manager {

    private EntregasApp app ;
    Util util;

    private Query<Ocorrencia> ocorrenciaQry;

    public Manager(EntregasApp myapp){
        app = myapp;
        util = new Util();

        try{
            ocorrenciaQry = app.getDaoSession().getOcorrenciaDao().queryBuilder()
                    .where(OcorrenciaDao.Properties.Id.eq(-1))
                    .limit(1)
                    .build();
        } catch (Exception e){

        }


    }

    //Adiciona um usuario, caso ele
    public Usuario addUsuario(String nome,
                              String cpf,
                              String login,
                              String senha,
                              String email) {

        Usuario usuario = findUsuarioByLogin(login);

        if (usuario == null) {
            usuario.setNome(nome);
            usuario.setCpf(cpf);
            usuario.setLogin(login);
            usuario.setSenha(senha);
            usuario.setEmail(email);
            app.getDaoSession().getUsuarioDao().insert(usuario);
        }

        return usuario;
    }

    public Usuario findUsuarioByLogin(String cpf) {
        QueryBuilder query = app.getDaoSession().getUsuarioDao().queryBuilder();
        return (Usuario) query.where(UsuarioDao.Properties.Cpf.eq(cpf)).unique();
    }

    public boolean hasUsuario() {
        long qt = app.getDaoSession().getUsuarioDao().queryBuilder().count();
        if (qt==0) {
            return false;
        } else {
            return true;
        }
    }

    public Cidade findCidadeById(Long id){
        return app.getDaoSession().getCidadeDao().queryBuilder().where(CidadeDao.Properties.Id.eq(id)).unique();
    }

    public Cidade addCidade(Long id, String nome_cidade, String uf, String codigo_ibge){

        Cidade cidade = findCidadeById(id);
        if (cidade == null) {
            cidade = new Cidade(id, nome_cidade,uf, codigo_ibge);
            app.getDaoSession().insert(cidade);
        }

        return cidade;
    }

    public Ocorrencia findOcorrenciaById(Long id){

        ocorrenciaQry.setParameter(0,id);
        Ocorrencia ocorrencia = ocorrenciaQry.unique();

        return ocorrencia;
    }

    public Ocorrencia addOcorrencia(Long id, String ocorrencia_desc, boolean pendencia, boolean ativo,
                                    boolean exigeRecebedor, boolean exigeDocumento, boolean exigeImagem){

        Ocorrencia ocorrencia = findOcorrenciaById(id);
        if (ocorrencia == null) {
            ocorrencia = new Ocorrencia(id,ocorrencia_desc,pendencia,ativo,false, false, false);
            app.getDaoSession().insert(ocorrencia);
        } else {
            ocorrencia.setOcorrencia(ocorrencia_desc);
            ocorrencia.setPendencia(pendencia);
            ocorrencia.setAtivo(ativo);
            ocorrencia.setExigeRecebedor(ocorrencia.getExigeRecebedor());
            ocorrencia.setExigeDocumento(ocorrencia.getExigeDocumento());
            ocorrencia.setExigeImagem(ocorrencia.getExigeImagem());
            app.getDaoSession().update(ocorrencia);
        }

        return ocorrencia;
    }



    public Pessoa findPessoaById(Long id){
        return app.getDaoSession().getPessoaDao().queryBuilder().where(PessoaDao.Properties.Id.eq(id)).unique();
    }

    public List<Pessoa> findPessoasByDataRomaneio(Date data_expedicao, boolean finalizada) {

        String data = Util.getDateFormatYMD(data_expedicao);

        QueryBuilder qryPessoas = app.getDaoSession().getPessoaDao().queryBuilder();


        /*
        Join joinDocumento = qryPessoas.join(Documento.class, DocumentoDao.Properties.DestinatarioCnpj);

        Join joinRomaneio = qryPessoas.join(joinDocumento,DocumentoDao.Properties.RomaneioId,
                Romaneio.class,RomaneioDao.Properties.Id);
        joinRomaneio.where(RomaneioDao.Properties.DataExpedicao.eq(data));
         */

        qryPessoas.LOG_SQL = true;
        qryPessoas.LOG_VALUES = true;

        if (finalizada){
            qryPessoas.where( new WhereCondition.StringCondition("cnpj_cpf IN " +
                    "(SELECT destintario_cnpj " +
                    "FROM documentos " +
                    "JOIN romaneios " +
                    "ON documentos.romaneio_id = romaneios._id " +
                    "WHERE (id_ocorrencia > 0 AND (id_ocorrencia < 300 OR id_ocorrencia > 399)) " +
                    "AND romaneios  .data_expedicao = '" + Util.getDateFormatYMD(data_expedicao) + "')"));
        } else {
            qryPessoas.where( new WhereCondition.StringCondition("cnpj_cpf IN " +
                    "(SELECT destintario_cnpj " +
                    "FROM documentos " +
                    "JOIN romaneios " +
                    "ON documentos.romaneio_id = romaneios._id " +
                    "WHERE (id_ocorrencia = 0 OR (id_ocorrencia >= 300 OR id_ocorrencia <= 399)) " +
                    "AND romaneios.data_expedicao = '" + Util.getDateFormatYMD(data_expedicao) + "')"));
        }

        List<Pessoa> pessoas = qryPessoas.list();
//       List<Documento> documentos = query.orderRaw("cep ASC").list();

        //qryPessoas.LOG_SQL = false;
        //qryPessoas.LOG_VALUES = false;

        return pessoas;
    }

    public List<Documento> findDocumentosByRomaneioDestinatario(Date data_expedicao, String destinatarioCnpj){

        String data = Util.getDateFormatYMD(data_expedicao);
        QueryBuilder query = app.getDaoSession().getDocumentoDao().queryBuilder();
        query.where(DocumentoDao.Properties.DestinatarioCnpj.eq(destinatarioCnpj));

//      query.LOG_SQL = true;
//      query.LOG_VALUES = true;

        query.join(DocumentoDao.Properties.RomaneioId,Romaneio.class).
                where(RomaneioDao.Properties.DataExpedicao.eq(data));

        //Join pessoas = query.join(DocumentoDao.Properties.DestinatarioCnpj,Pessoa.class);

//        query.join(DocumentoDao.Properties.DestinatarioCnpj,Pessoa.class);
//        Join joinRomaneios = query.join(DocumentoDao.Properties.RomaneioId, Romaneio.class);
//        joinRomaneios.where(RomaneioDao.Properties.DataExpedicao.eq(data));

        List<Documento> documentos;
        documentos = query.orderAsc(DocumentoDao.Properties.NumeroNotaFiscal).list();
//       List<Documento> documentos = query.orderRaw("cep ASC").list();

        return documentos;


    }

    public List<Documento> findDocumentosByRomaneioDestinatarioPendente(Date data_expedicao, String destinatarioCnpj){

        String data = Util.getDateFormatYMD(data_expedicao);
        QueryBuilder query = app.getDaoSession().getDocumentoDao().queryBuilder();
        query.where(DocumentoDao.Properties.DestinatarioCnpj.eq(destinatarioCnpj));

//      query.LOG_SQL = true;
//      query.LOG_VALUES = true;

        query.join(DocumentoDao.Properties.RomaneioId,Romaneio.class).
                where(RomaneioDao.Properties.DataExpedicao.eq(data));

        //Join pessoas = query.join(DocumentoDao.Properties.DestinatarioCnpj,Pessoa.class);

//        query.join(DocumentoDao.Properties.DestinatarioCnpj,Pessoa.class);
//        Join joinRomaneios = query.join(DocumentoDao.Properties.RomaneioId, Romaneio.class);
//        joinRomaneios.where(RomaneioDao.Properties.DataExpedicao.eq(data));

        List<Documento> documentos;
        documentos = query.orderAsc(DocumentoDao.Properties.NumeroNotaFiscal).list();
//       List<Documento> documentos = query.orderRaw("cep ASC").list();
        return documentos;


    }

    public List<Pessoa> findPessoasByDataRomaneio(Date data_expedicao){

        String data = Util.getDateFormatYMD(data_expedicao);

        QueryBuilder qryPessoas = app.getDaoSession().getPessoaDao().queryBuilder();

        Join joinDocumento = qryPessoas.join(Documento.class, DocumentoDao.Properties.DestinatarioCnpj);

        Join joinRomaneio = qryPessoas.join(joinDocumento,DocumentoDao.Properties.RomaneioId,
                Romaneio.class,RomaneioDao.Properties.Id);

        //qryPessoas.LOG_SQL = false;
        //qryPessoas.LOG_VALUES = false;

        joinRomaneio.where(RomaneioDao.Properties.DataExpedicao.eq(data));

        List<Pessoa> pessoas = qryPessoas.list();
//       List<Documento> documentos = query.orderRaw("cep ASC").list();

        //qryPessoas.LOG_SQL = false;
        //qryPessoas.LOG_VALUES = false;

        return pessoas;
    }

    public List<ImagemOcorrencia> findImagensOcorrenciasByNFe(Long idDocumento){



        QueryBuilder qry1 = app.getDaoSession().getImagemOcorrenciaDao().queryBuilder();

        Join joinOcorrencias = qry1.join(ImagemOcorrenciaDao.Properties.OcorrenciaDocumentoId,OcorrenciaDocumento.class);

        Join joinDocumento = qry1.join(joinOcorrencias,OcorrenciaDocumentoDao.Properties.DocumentoId,
                Documento.class,DocumentoDao.Properties.Id);

        //qryPessoas.LOG_SQL = false;
        //qryPessoas.LOG_VALUES = false;

        joinDocumento.where(DocumentoDao.Properties.Id.eq(idDocumento));

         return qry1.orderAsc(ImagemOcorrenciaDao.Properties.Id).list();


    }

    public Pessoa addPessoa(Long id, int tipoPessoa, String nome, String cnpjCpf,
                            String endereco, String numero, String bairro, Long idCidade,
                            String cep, String telefone, String whatsapp, Long idRegiao,
                            String latitude, String longitude){

        Pessoa pessoa = findPessoaById(id);
        if (pessoa == null) {
            pessoa = new Pessoa(id, tipoPessoa, nome, cnpjCpf, endereco, numero, bairro,
                    idCidade,cep,telefone,whatsapp, idRegiao,latitude, longitude);
            pessoa.setLongitude(longitude);
            pessoa.setLatitude(latitude);
            pessoa.setTelefone(telefone);

            app.getDaoSession().insert(pessoa);

        } else {

            if (!pessoa.getEndereco().equals(endereco)) {
                Log.d("Add Pessoa","Atualizando endereco");
                pessoa.setEndereco(endereco);
                pessoa.setBairro(bairro);
                pessoa.setNumero(numero);
                pessoa.setIdCidade(idCidade);
                pessoa.setIdRegiao(idRegiao);
                pessoa.setCep(cep);
                pessoa.setLatitude(latitude);
                pessoa.setLongitude(longitude);
                pessoa.setTelefone(telefone);
                pessoa.setWhatsapp(whatsapp);
                app.getDaoSession().update(pessoa);
            } else if (pessoa.getLatitude() != latitude) {
                pessoa.setLatitude(latitude);
                pessoa.setLongitude(longitude);
                app.getDaoSession().update(pessoa);
            }

        }

        return pessoa;
    }

    public Veiculo findVeiculoByPlaca(String placa){
        return app.getDaoSession().getVeiculoDao().queryBuilder().
                where(VeiculoDao.Properties.PlacaVeiculo.eq(placa)).unique();
    }

    public Veiculo addVeiculo(String placa, String descricao){

        Veiculo veiculo = findVeiculoByPlaca(placa);
        if (veiculo == null) {
            veiculo = new Veiculo(null,placa,descricao);
            app.getDaoSession().insert(veiculo);
        }

        return veiculo;
    }


    public Romaneio findRomaneioById(Long id){

        return app.getDaoSession().getRomaneioDao().queryBuilder().
                where(RomaneioDao.Properties.Id.eq(id)).unique();
    }

    public List<Entregas> findEntregasByDataStatus(Date dataExpedicao, boolean status){

        String data = Util.getDateFormatYMD(dataExpedicao);

        QueryBuilder<Entregas> qryEntregas = app.getDaoSession().getEntregasDao().queryBuilder()
                .where(EntregasDao.Properties.DataExpedicao.eq(data))
                .where(EntregasDao.Properties.Status.eq(status));

        //qryEntregas.LOG_SQL = true;
        //qryEntregas.LOG_VALUES = true;

        return qryEntregas.list();
    }

    public Entregas findEntregaByDataDestinatario(Date dataExpedicao, String destinatarioId){

        String data = Util.getDateFormatYMD(dataExpedicao);

        return app.getDaoSession().getEntregasDao().queryBuilder()
                .where(EntregasDao.Properties.DataExpedicao.eq(data))
                .where(EntregasDao.Properties.DestinatarioId.eq(destinatarioId))
                .limit(1)
                .unique();


    }

    public Entregas findEntregaById(Long id){

        return app.getDaoSession().getEntregasDao().queryBuilder()
                .where(EntregasDao.Properties.Id.eq(id)).unique();
    }

    public Entregas addEntregas(Long id, Long destinatarioId, String dataExpedicao,
                                Boolean status, String latitude, String longitude, int ordemEntrega,
                                boolean temPendencia){

        Entregas entrega = findEntregaById(id);

        if (entrega==null){
            entrega = new Entregas();
            entrega.setId(id);
            entrega.setDestinatarioId(destinatarioId);
            entrega.setDataExpedicao(dataExpedicao);
            entrega.setStatus(status);
            entrega.setLatitude(latitude);
            entrega.setLongitude(longitude);
            entrega.setOrdemEntrega(ordemEntrega);
            entrega.setTemPendencia(temPendencia);

            app.getDaoSession().insert(entrega);
        }

        return entrega;
    }

    public Romaneio addRomaneio(Long id, String numeroRomaneio, String dataRomaneio,
                                String dataSaida, String dataChegada, Long veiculoId, Long motoristaCpf,
                                Long redespachadorCnpj, Long cidadeOrigemId, Long regiaoId,
                                boolean status, String dataExpedicao) {

        Romaneio romaneio = findRomaneioById(id);

        if (romaneio == null) {

            romaneio = new Romaneio();
            romaneio.setId(id);
            romaneio.setNumeroRomaneio(numeroRomaneio);
            romaneio.setDataRomaneio(dataRomaneio);
            romaneio.setDataSaida(dataSaida);
            romaneio.setDataChegada(dataChegada);
            romaneio.setVeiculoId(veiculoId);
            romaneio.setMotoristaCpf(motoristaCpf);
            romaneio.setRedespachadorCnpj(redespachadorCnpj);
            romaneio.setCidadeOrigemId(cidadeOrigemId);
            romaneio.setRegiaoId(regiaoId);
            romaneio.setDataExpedicao(dataExpedicao);
            app.getDaoSession().insert(romaneio);
        } else {
            romaneio.setDataRomaneio(dataRomaneio);
            app.getDaoSession().update(romaneio);
        }
        return romaneio;
    }

    public Documento findDocumentoByIdNotaFiscalImpIdRomaneio(Long idNotaFiscalImp, Long idRomaneio){

        if(idNotaFiscalImp != null) {
            return app.getDaoSession().getDocumentoDao().queryBuilder().
                    where(DocumentoDao.Properties.IdNotaFiscalImp.eq(idNotaFiscalImp),
                            DocumentoDao.Properties.RomaneioId.eq(idRomaneio)).unique();
        } else {

            return null;
        }
    }


    public Documento findDocumentoByIdConhecimentoNotaFiscalImpIdRomaneio(Long idConhecimentoNotaFiscal, Long idRomaneio){

        if(idConhecimentoNotaFiscal != null) {
            return app.getDaoSession().getDocumentoDao().queryBuilder().
                    where(DocumentoDao.Properties.IdConhecimentoNotasFiscais.eq(idConhecimentoNotaFiscal),
                            DocumentoDao.Properties.RomaneioId.eq(idRomaneio)).unique();
        } else {
            return null;
        }
    }

    public int getQuantidadeOcorrencias(Date dataExpedicao){

        String data = Util.getDateFormatYMD(dataExpedicao);
        QueryBuilder query = app.getDaoSession().getDocumentoDao().queryBuilder()
                .where(DocumentoDao.Properties.IdOcorrencia.gt(0))
                .whereOr(DocumentoDao.Properties.IdOcorrencia.lt(300),
                         DocumentoDao.Properties.IdOcorrencia.gt(399));

//        query.LOG_SQL = true;
//        query.LOG_VALUES = true;

        query.join(DocumentoDao.Properties.RomaneioId,Romaneio.class).
                where(RomaneioDao.Properties.DataExpedicao.eq(data));

        //Join pessoas = query.join(DocumentoDao.Properties.DestinatarioCnpj,Pessoa.class);

//        query.join(DocumentoDao.Properties.DestinatarioCnpj,Pessoa.class);
//        Join joinRomaneios = query.join(DocumentoDao.Properties.RomaneioId, Romaneio.class);
//        joinRomaneios.where(RomaneioDao.Properties.DataExpedicao.eq(data));

        List<Documento> documentos = query.orderAsc(DocumentoDao.Properties.Distance).list();
        return documentos.size();
    }

    public int getQuantidadeDocumentos(Date dataExpedicao){

        String data = Util.getDateFormatYMD(dataExpedicao);
        QueryBuilder query = app.getDaoSession().getDocumentoDao().queryBuilder();
//        query.LOG_SQL = true;
//        query.LOG_VALUES = true;

        query.join(DocumentoDao.Properties.RomaneioId,Romaneio.class).
                where(RomaneioDao.Properties.DataExpedicao.eq(data));


        //Join pessoas = query.join(DocumentoDao.Properties.DestinatarioCnpj,Pessoa.class);

//        query.join(DocumentoDao.Properties.DestinatarioCnpj,Pessoa.class);
//        Join joinRomaneios = query.join(DocumentoDao.Properties.RomaneioId, Romaneio.class);
//        joinRomaneios.where(RomaneioDao.Properties.DataExpedicao.eq(data));

        List<Documento> documentos = query.orderAsc(DocumentoDao.Properties.Distance).list();
        return documentos.size();
    }

    public Documento findDocumentoById(Long id){
        return app.getDaoSession().getDocumentoDao().queryBuilder().
                where(DocumentoDao.Properties.Id.eq(id)).unique();
    }

    public Documento findDocumentoByChaveNFeAberta(String chaveNfe){
        return app.getDaoSession().getDocumentoDao().queryBuilder().
                where(DocumentoDao.Properties.ChaveNfe.eq(chaveNfe))
                .orderDesc(DocumentoDao.Properties.Id)
                .unique();
    }

    public Documento findDocumentoByChaveNFeDataRomaneio(String chaveNfe, Date dataExpedicao){


        String data = Util.getDateFormatYMD(dataExpedicao);
        Documento documento = app.getDaoSession().getDocumentoDao()
                .queryBuilder()
                .where(DocumentoDao.Properties.ChaveNfe.eq(chaveNfe))
                .orderDesc(DocumentoDao.Properties.Id).limit(1).unique();

//      query.LOG_SQL = true;
//      query.LOG_VALUES = true;
        //query.join(DocumentoDao.Properties.RomaneioId,Romaneio.class).
        //        where(RomaneioDao.Properties.DataExpedicao.eq(data));
        //Join pessoas = query.join(DocumentoDao.Properties.DestinatarioCnpj,Pessoa.class);
//        query.join(DocumentoDao.Properties.DestinatarioCnpj,Pessoa.class);
//        Join joinRomaneios = query.join(DocumentoDao.Properties.RomaneioId, Romaneio.class);
//        joinRomaneios.where(RomaneioDao.Properties.DataExpedicao.eq(data));

        return documento;

    }


    public Documento addDocumento(Long idNotaFiscalImp, String dataEmissao, String dataExpedicao,
                                  String chaveNfe, String serie, String numeroNotaFiscal, Long remetenteCnpj,
                                  Long destinatarioCnpj, Long romaneioId, Double valor, Double peso,
                                  Double volumes, Long idOcorrencia, String dataOcorrencia,
                                  Long idConhecimentoNotasFiscais, Long idConhecimento,
                                  Double distance, Double tempoEstimado, String cep, Long idEntrega) {


        Documento documento = new Documento();

        if (idConhecimentoNotasFiscais == null){
            documento = findDocumentoByIdNotaFiscalImpIdRomaneio(idNotaFiscalImp,romaneioId);
        } else {
            documento = findDocumentoByIdConhecimentoNotaFiscalImpIdRomaneio(idConhecimentoNotasFiscais,romaneioId);
        }

        if (documento==null){
            documento = new Documento(null,idNotaFiscalImp,dataEmissao,dataExpedicao,chaveNfe,
                     serie,numeroNotaFiscal,remetenteCnpj,destinatarioCnpj,romaneioId,
                         valor,peso,volumes,idOcorrencia,dataOcorrencia, idConhecimentoNotasFiscais,
                    idConhecimento, distance, tempoEstimado, cep, false, idEntrega, false);
            app.getDaoSession().insert(documento);
        } else {

            if (documento.getCep() == null){
                documento.setCep(cep);
                app.getDaoSession().update(documento);
            }


            //if (app.getModoConsulta()){
            //    documento.setIdOcorrencia(idOcorrencia);
            //    documento.setDataOcorrencia(dataOcorrencia);
            //    app.getDaoSession().update(documento);
            //}

            //Se documento foi incluido num romaneio mais recente, apaga documento atual
            // e inclui um novo
            /*
            if (romaneioId>documento.getRomaneioId()){
                app.getDaoSession().delete(documento);
                documento = new Documento(null,idNotaFiscalImp,dataEmissao,dataExpedicao,chaveNfe,
                        serie,numeroNotaFiscal,remetenteCnpj,destinatarioCnpj,romaneioId,
                        valor,peso,volumes,idOcorrencia,dataOcorrencia, idConhecimentoNotasFiscais,
                        idConhecimento, distance, tempoEstimado);

                app.getDaoSession().insert(documento);
            }
             */

        }

        return documento;
    }

    public String getUltimaAlteracaoRomaneio(String data){
        String ultimaAlteracao;

        if (app.getModoConsulta()){
            ultimaAlteracao = data + " 00:00:00";
        } else {
            Romaneio romaneio = app.getDaoSession().getRomaneioDao().queryBuilder()
                    .where(RomaneioDao.Properties.DataExpedicao.eq(data))
                    .orderDesc(RomaneioDao.Properties.DataRomaneio).limit(1).unique();
            if (romaneio==null){
                ultimaAlteracao = data + " 00:00:00";
            } else {
                ultimaAlteracao = romaneio.getDataRomaneio();
            }
        }


        return ultimaAlteracao;
    }

    public List<Documento> findDocumentoByDataRomaneio(Date data_expedicao){

        String data = Util.getDateFormatYMD(data_expedicao);
        QueryBuilder query = app.getDaoSession().getDocumentoDao().queryBuilder();
//        query.LOG_SQL = true;
//        query.LOG_VALUES = true;

        query.join(DocumentoDao.Properties.RomaneioId,Romaneio.class).
                where(RomaneioDao.Properties.DataExpedicao.eq(data));

        //Join pessoas = query.join(DocumentoDao.Properties.DestinatarioCnpj,Pessoa.class);

//        query.join(DocumentoDao.Properties.DestinatarioCnpj,Pessoa.class);
//        Join joinRomaneios = query.join(DocumentoDao.Properties.RomaneioId, Romaneio.class);
//        joinRomaneios.where(RomaneioDao.Properties.DataExpedicao.eq(data));

        List<Documento> documentos;

        if (Prefs.getBoolean("ordernar_distancia",true)){
             documentos = query.orderAsc(DocumentoDao.Properties.Distance).list();
        } else {
            documentos = query.orderAsc(DocumentoDao.Properties.Cep).list();
        }

//       List<Documento> documentos = query.orderRaw("cep ASC").list();


        return documentos;
    }


    public OcorrenciaDocumento addOcorrenciaDocumento(Long documentoId, Long codigoOcorrencia, String dataOcorrencia,
                              String horaOcorrencia, String dataRegistro, String documentoRecebedor,
                              String nomeRecebedor, String observacoes, String latitude,
                              String longitude) {

        OcorrenciaDocumento oco = new OcorrenciaDocumento();

        Date dataAtual = new Date();

        String cDataAtual = util.getDateTimeFormatYMD(dataAtual);

        oco.setDocumentoId(documentoId);
        oco.setCodigoOcorrencia(codigoOcorrencia);
        oco.setDataOcorrencia(dataOcorrencia);
        oco.setHoraOcorrencia(horaOcorrencia);
        oco.setDataRegistro(cDataAtual);
        oco.setNomeRecebedor(nomeRecebedor);
        oco.setDocumentoRecebedor(documentoRecebedor);
        oco.setLatitude(latitude);
        oco.setLongitude(longitude);

        app.getDaoSession().insert(oco);

        return oco;
    }

    public List<OcorrenciaDocumento> findOcorrenciaNaoSincronizada(){
        return app.getDaoSession().getOcorrenciaDocumentoDao().queryBuilder()
                .where(OcorrenciaDocumentoDao.Properties.Sincronizado.eq(0))
                .where(OcorrenciaDocumentoDao.Properties.Finalizado.eq(true))
                .orderAsc(OcorrenciaDocumentoDao.Properties.Id).list();
    }

    public List<ImagemOcorrencia> findImagensNaoSincronizada(){
        return app.getDaoSession().getImagemOcorrenciaDao().queryBuilder().where(
                ImagemOcorrenciaDao.Properties.Sincronizado.eq(0)
        ).orderAsc(ImagemOcorrenciaDao.Properties.Id).list();
    }

    public List<TrackingGps> findTrackingGpsNaoSincronizado(){
        return app.getDaoSession().getTrackingGpsDao().queryBuilder().where(
                TrackingGpsDao.Properties.Sincrozinado.eq(0)
        ).orderAsc(TrackingGpsDao.Properties.Id).list();
    }
}
