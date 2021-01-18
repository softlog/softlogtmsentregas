package br.eti.softlog.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * Created by Paulo Sergio Alves on 2018/03/13.
 */

@Entity(
            nameInDb = "documentos",
            indexes = {
                    @Index(value = "dataEmissao ASC"),
                    @Index(value = "dataExpedicao ASC"),
                    @Index(value = "numeroNotaFiscal ASC"),
                    @Index(value = "remetenteCnpj ASC"),
                    @Index(value = "destinatarioCnpj ASC")
            }
        )
public class Documento {

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "id_nota_fiscal_imp")
    private Long idNotaFiscalImp;

    @Property(nameInDb = "data_emissao")
    private String dataEmissao;

    @Property(nameInDb = "data_expedicao")
    private String dataExpedicao;

    @Property(nameInDb = "chave_nfe")
    private String chaveNfe;

    @Property(nameInDb = "serie")
    private String serie;

    @Property(nameInDb = "numero_nota_fiscal")
    private String numeroNotaFiscal;

    @Property(nameInDb = "remetente_cnpj")
    private Long remetenteCnpj;

    @ToOne(joinProperty = "remetenteCnpj")
    private Pessoa remetente;

    @Property(nameInDb = "destintario_cnpj")
    private Long destinatarioCnpj;

    @ToOne(joinProperty = "destinatarioCnpj")
    private Pessoa destinatario;

    @Property(nameInDb = "romaneio_id")
    private Long romaneioId;

    @ToOne(joinProperty = "romaneioId")
    private Romaneio romaneio;

    @ToMany(referencedJoinProperty = "documentoId")
    @OrderBy("id ASC")
    private List<OcorrenciaDocumento> ocorrenciaDocumentos;

    @Property(nameInDb = "valor")
    private Double valor;

    @Property(nameInDb = "peso")
    private Double peso;

    @Property(nameInDb = "volumes")
    private Double volumes;

    @Property(nameInDb = "id_ocorrencia")
    private Long idOcorrencia;

    @ToOne(joinProperty = "idOcorrencia")
    private Ocorrencia ocorrencia;

    @Property(nameInDb = "data_ocorrencia")
    private String dataOcorrencia;

    @Property(nameInDb = "id_conhecimento_notas_fiscais")
    private Long idConhecimentoNotasFiscais;

    @Property(nameInDb = "id_conhecimento")
    private Long idConhecimento;

    @Property(nameInDb = "distance")
    private Double distance;

    @Property(nameInDb = "tempo_estimado")
    private Double tempoEstimado;

    @Property(nameInDb = "cep")
    private String cep;

    @Property(nameInDb = "selected")
    private boolean Selected;

    @Property(nameInDb = "entrega_id")
    private Long entregaId;

    @ToOne(joinProperty = "entregaId")
    Entregas entrega;

    @Property(nameInDb = "tem_canhoto")
    boolean temCanhoto;



/** Used to resolve relations */
@Generated(hash = 2040040024)
private transient DaoSession daoSession;

/** Used for active entity operations. */
@Generated(hash = 177588185)
private transient DocumentoDao myDao;

@Generated(hash = 1847628385)
public Documento(Long id, Long idNotaFiscalImp, String dataEmissao, String dataExpedicao,
        String chaveNfe, String serie, String numeroNotaFiscal, Long remetenteCnpj,
        Long destinatarioCnpj, Long romaneioId, Double valor, Double peso, Double volumes,
        Long idOcorrencia, String dataOcorrencia, Long idConhecimentoNotasFiscais,
        Long idConhecimento, Double distance, Double tempoEstimado, String cep,
        boolean Selected, Long entregaId, boolean temCanhoto) {
    this.id = id;
    this.idNotaFiscalImp = idNotaFiscalImp;
    this.dataEmissao = dataEmissao;
    this.dataExpedicao = dataExpedicao;
    this.chaveNfe = chaveNfe;
    this.serie = serie;
    this.numeroNotaFiscal = numeroNotaFiscal;
    this.remetenteCnpj = remetenteCnpj;
    this.destinatarioCnpj = destinatarioCnpj;
    this.romaneioId = romaneioId;
    this.valor = valor;
    this.peso = peso;
    this.volumes = volumes;
    this.idOcorrencia = idOcorrencia;
    this.dataOcorrencia = dataOcorrencia;
    this.idConhecimentoNotasFiscais = idConhecimentoNotasFiscais;
    this.idConhecimento = idConhecimento;
    this.distance = distance;
    this.tempoEstimado = tempoEstimado;
    this.cep = cep;
    this.Selected = Selected;
    this.entregaId = entregaId;
    this.temCanhoto = temCanhoto;
}

@Generated(hash = 2067782857)
public Documento() {
}

public Long getId() {
    return this.id;
}

public void setId(Long id) {
    this.id = id;
}

public Long getIdNotaFiscalImp() {
    return this.idNotaFiscalImp;
}

public void setIdNotaFiscalImp(Long idNotaFiscalImp) {
    this.idNotaFiscalImp = idNotaFiscalImp;
}

public String getDataEmissao() {
    return this.dataEmissao;
}

public void setDataEmissao(String dataEmissao) {
    this.dataEmissao = dataEmissao;
}

public String getDataExpedicao() {
    return this.dataExpedicao;
}

public void setDataExpedicao(String dataExpedicao) {
    this.dataExpedicao = dataExpedicao;
}

public String getChaveNfe() {
    return this.chaveNfe;
}

public void setChaveNfe(String chaveNfe) {
    this.chaveNfe = chaveNfe;
}

public String getSerie() {
    return this.serie;
}

public void setSerie(String serie) {
    this.serie = serie;
}

public String getNumeroNotaFiscal() {
    return this.numeroNotaFiscal;
}

public void setNumeroNotaFiscal(String numeroNotaFiscal) {
    this.numeroNotaFiscal = numeroNotaFiscal;
}

public Long getRemetenteCnpj() {
    return this.remetenteCnpj;
}

public void setRemetenteCnpj(Long remetenteCnpj) {
    this.remetenteCnpj = remetenteCnpj;
}

public Long getDestinatarioCnpj() {
    return this.destinatarioCnpj;
}

public void setDestinatarioCnpj(Long destinatarioCnpj) {
    this.destinatarioCnpj = destinatarioCnpj;
}

public Long getRomaneioId() {
    return this.romaneioId;
}

public void setRomaneioId(Long romaneioId) {
    this.romaneioId = romaneioId;
}

public Double getValor() {
    return this.valor;
}

public void setValor(Double valor) {
    this.valor = valor;
}

public Double getPeso() {
    return this.peso;
}

public void setPeso(Double peso) {
    this.peso = peso;
}

public Double getVolumes() {
    return this.volumes;
}

public void setVolumes(Double volumes) {
    this.volumes = volumes;
}

public Long getIdOcorrencia() {
    return this.idOcorrencia;
}

public void setIdOcorrencia(Long idOcorrencia) {
    this.idOcorrencia = idOcorrencia;
}

public String getDataOcorrencia() {
    return this.dataOcorrencia;
}

public void setDataOcorrencia(String dataOcorrencia) {
    this.dataOcorrencia = dataOcorrencia;
}

public Long getIdConhecimentoNotasFiscais() {
    return this.idConhecimentoNotasFiscais;
}

public void setIdConhecimentoNotasFiscais(Long idConhecimentoNotasFiscais) {
    this.idConhecimentoNotasFiscais = idConhecimentoNotasFiscais;
}

public Long getIdConhecimento() {
    return this.idConhecimento;
}

public void setIdConhecimento(Long idConhecimento) {
    this.idConhecimento = idConhecimento;
}

public Double getDistance() {
    return this.distance;
}

public void setDistance(Double distance) {
    this.distance = distance;
}

public Double getTempoEstimado() {
    return this.tempoEstimado;
}

public void setTempoEstimado(Double tempoEstimado) {
    this.tempoEstimado = tempoEstimado;
}

public String getCep() {
    return this.cep;
}

public void setCep(String cep) {
    this.cep = cep;
}

@Generated(hash = 603090513)
private transient Long remetente__resolvedKey;

/** To-one relationship, resolved on first access. */
@Generated(hash = 931312748)
public Pessoa getRemetente() {
    Long __key = this.remetenteCnpj;
    if (remetente__resolvedKey == null
            || !remetente__resolvedKey.equals(__key)) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        PessoaDao targetDao = daoSession.getPessoaDao();
        Pessoa remetenteNew = targetDao.load(__key);
        synchronized (this) {
            remetente = remetenteNew;
            remetente__resolvedKey = __key;
        }
    }
    return remetente;
}

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 652398806)
public void setRemetente(Pessoa remetente) {
    synchronized (this) {
        this.remetente = remetente;
        remetenteCnpj = remetente == null ? null : remetente.getId();
        remetente__resolvedKey = remetenteCnpj;
    }
}

@Generated(hash = 695539817)
private transient Long destinatario__resolvedKey;

/** To-one relationship, resolved on first access. */
@Generated(hash = 1805247917)
public Pessoa getDestinatario() {
    Long __key = this.destinatarioCnpj;
    if (destinatario__resolvedKey == null
            || !destinatario__resolvedKey.equals(__key)) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        PessoaDao targetDao = daoSession.getPessoaDao();
        Pessoa destinatarioNew = targetDao.load(__key);
        synchronized (this) {
            destinatario = destinatarioNew;
            destinatario__resolvedKey = __key;
        }
    }
    return destinatario;
}

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 752801012)
public void setDestinatario(Pessoa destinatario) {
    synchronized (this) {
        this.destinatario = destinatario;
        destinatarioCnpj = destinatario == null ? null : destinatario.getId();
        destinatario__resolvedKey = destinatarioCnpj;
    }
}

@Generated(hash = 1771332099)
private transient Long romaneio__resolvedKey;

/** To-one relationship, resolved on first access. */
@Generated(hash = 929123856)
public Romaneio getRomaneio() {
    Long __key = this.romaneioId;
    if (romaneio__resolvedKey == null || !romaneio__resolvedKey.equals(__key)) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        RomaneioDao targetDao = daoSession.getRomaneioDao();
        Romaneio romaneioNew = targetDao.load(__key);
        synchronized (this) {
            romaneio = romaneioNew;
            romaneio__resolvedKey = __key;
        }
    }
    return romaneio;
}

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 1752136909)
public void setRomaneio(Romaneio romaneio) {
    synchronized (this) {
        this.romaneio = romaneio;
        romaneioId = romaneio == null ? null : romaneio.getId();
        romaneio__resolvedKey = romaneioId;
    }
}

@Generated(hash = 1723205787)
private transient Long ocorrencia__resolvedKey;

@Generated(hash = 613319668)
private transient Long entrega__resolvedKey;

/** To-one relationship, resolved on first access. */
@Generated(hash = 887810511)
public Ocorrencia getOcorrencia() {
    Long __key = this.idOcorrencia;
    if (ocorrencia__resolvedKey == null
            || !ocorrencia__resolvedKey.equals(__key)) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        OcorrenciaDao targetDao = daoSession.getOcorrenciaDao();
        Ocorrencia ocorrenciaNew = targetDao.load(__key);
        synchronized (this) {
            ocorrencia = ocorrenciaNew;
            ocorrencia__resolvedKey = __key;
        }
    }
    return ocorrencia;
}

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 1535261583)
public void setOcorrencia(Ocorrencia ocorrencia) {
    synchronized (this) {
        this.ocorrencia = ocorrencia;
        idOcorrencia = ocorrencia == null ? null : ocorrencia.getId();
        ocorrencia__resolvedKey = idOcorrencia;
    }
}

/**
 * To-many relationship, resolved on first access (and after reset).
 * Changes to to-many relations are not persisted, make changes to the target entity.
 */
@Generated(hash = 842980957)
public List<OcorrenciaDocumento> getOcorrenciaDocumentos() {
    if (ocorrenciaDocumentos == null) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        OcorrenciaDocumentoDao targetDao = daoSession
                .getOcorrenciaDocumentoDao();
        List<OcorrenciaDocumento> ocorrenciaDocumentosNew = targetDao
                ._queryDocumento_OcorrenciaDocumentos(id);
        synchronized (this) {
            if (ocorrenciaDocumentos == null) {
                ocorrenciaDocumentos = ocorrenciaDocumentosNew;
            }
        }
    }
    return ocorrenciaDocumentos;
}

/** Resets a to-many relationship, making the next get call to query for a fresh result. */
@Generated(hash = 1546227233)
public synchronized void resetOcorrenciaDocumentos() {
    ocorrenciaDocumentos = null;
}

/**
 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
 * Entity must attached to an entity context.
 */
@Generated(hash = 128553479)
public void delete() {
    if (myDao == null) {
        throw new DaoException("Entity is detached from DAO context");
    }
    myDao.delete(this);
}

/**
 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
 * Entity must attached to an entity context.
 */
@Generated(hash = 1942392019)
public void refresh() {
    if (myDao == null) {
        throw new DaoException("Entity is detached from DAO context");
    }
    myDao.refresh(this);
}

/**
 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
 * Entity must attached to an entity context.
 */
@Generated(hash = 713229351)
public void update() {
    if (myDao == null) {
        throw new DaoException("Entity is detached from DAO context");
    }
    myDao.update(this);
}

public boolean getSelected() {
    return this.Selected;
}

public void setSelected(boolean Selected) {
    this.Selected = Selected;
}

public Long getEntregaId() {
    return this.entregaId;
}

public void setEntregaId(Long entregaId) {
    this.entregaId = entregaId;
}

/** To-one relationship, resolved on first access. */
@Generated(hash = 1185742007)
public Entregas getEntrega() {
    Long __key = this.entregaId;
    if (entrega__resolvedKey == null || !entrega__resolvedKey.equals(__key)) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        EntregasDao targetDao = daoSession.getEntregasDao();
        Entregas entregaNew = targetDao.load(__key);
        synchronized (this) {
            entrega = entregaNew;
            entrega__resolvedKey = __key;
        }
    }
    return entrega;
}

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 959071249)
public void setEntrega(Entregas entrega) {
    synchronized (this) {
        this.entrega = entrega;
        entregaId = entrega == null ? null : entrega.getId();
        entrega__resolvedKey = entregaId;
    }
}

public boolean getTemCanhoto() {
    return this.temCanhoto;
}

public void setTemCanhoto(boolean temCanhoto) {
    this.temCanhoto = temCanhoto;
}

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 745708422)
public void __setDaoSession(DaoSession daoSession) {
    this.daoSession = daoSession;
    myDao = daoSession != null ? daoSession.getDocumentoDao() : null;
}



}
