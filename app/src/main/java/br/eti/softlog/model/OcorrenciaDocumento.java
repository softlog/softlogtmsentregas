package br.eti.softlog.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * Created by Paulo Sérgio Alves on 2018/03/13.
 */

@Entity(
        nameInDb = "ocorrencias_documento",
        indexes = {
                @Index(value = "dataOcorrencia ASC"),
                @Index(value = "dataRegistro ASC")
        }
)
public class OcorrenciaDocumento {

    @Id
    private Long id;

    @Property(nameInDb = "documento_id")
    private Long documentoId;

    @ToOne(joinProperty = "documentoId")
    private Documento documento;

    @Property(nameInDb = "codigo_ocorrencia")
    private Long codigoOcorrencia;

    @ToOne(joinProperty = "codigoOcorrencia")
    private Ocorrencia ocorrencia;

    @Property(nameInDb = "data_ocorrencia")
    private String dataOcorrencia;

    @Property(nameInDb = "hora_ocorrencia")
    private String horaOcorrencia;

    @Property(nameInDb = "data_registro")
    private String dataRegistro;

    @Property(nameInDb = "documento_recebedor")
    private String documentoRecebedor;

    @Property(nameInDb = "nome_recebedor")
    private String nomeRecebedor;

    @Property(nameInDb = "observacoes")
    private String observacoes;

    @Property(nameInDb = "latitude")
    private String latitude;

    @Property(nameInDb = "longitude")
    private String longitude;

    @Property(nameInDb = "sincronizado")
    private boolean sincronizado;

    @Property(nameInDb = "data_sincronizacao")
    private String dataSincronizacao;

    @Property(nameInDb = "tarefa_executada_id")
    private Long tarefaExecutadaId;

    @Property(nameInDb = "id_servidor")
    private Long idServidor;

    @ToOne(joinProperty = "tarefaExecutadaId")
    private TarefasExecutadas tarefaExecutada;

    @ToMany(referencedJoinProperty = "ocorrenciaDocumentoId")
    private List<ImagemOcorrencia> imagemOcorrencias;

    @Property(nameInDb = "finalizado")
    boolean finalizado;

    @Property(nameInDb = "entrega_id")
    Long entregaId;

    @Property(nameInDb = "tem_canhoto")
    boolean temCanhoto;

/** Used to resolve relations */
@Generated(hash = 2040040024)
private transient DaoSession daoSession;

/** Used for active entity operations. */
@Generated(hash = 1805366801)
private transient OcorrenciaDocumentoDao myDao;

@Generated(hash = 1223682688)
public OcorrenciaDocumento(Long id, Long documentoId, Long codigoOcorrencia,
        String dataOcorrencia, String horaOcorrencia, String dataRegistro,
        String documentoRecebedor, String nomeRecebedor, String observacoes,
        String latitude, String longitude, boolean sincronizado, String dataSincronizacao,
        Long tarefaExecutadaId, Long idServidor, boolean finalizado, Long entregaId,
        boolean temCanhoto) {
    this.id = id;
    this.documentoId = documentoId;
    this.codigoOcorrencia = codigoOcorrencia;
    this.dataOcorrencia = dataOcorrencia;
    this.horaOcorrencia = horaOcorrencia;
    this.dataRegistro = dataRegistro;
    this.documentoRecebedor = documentoRecebedor;
    this.nomeRecebedor = nomeRecebedor;
    this.observacoes = observacoes;
    this.latitude = latitude;
    this.longitude = longitude;
    this.sincronizado = sincronizado;
    this.dataSincronizacao = dataSincronizacao;
    this.tarefaExecutadaId = tarefaExecutadaId;
    this.idServidor = idServidor;
    this.finalizado = finalizado;
    this.entregaId = entregaId;
    this.temCanhoto = temCanhoto;
}

@Generated(hash = 1712609508)
public OcorrenciaDocumento() {
}

public Long getId() {
    return this.id;
}

public void setId(Long id) {
    this.id = id;
}

public Long getDocumentoId() {
    return this.documentoId;
}

public void setDocumentoId(Long documentoId) {
    this.documentoId = documentoId;
}

public Long getCodigoOcorrencia() {
    return this.codigoOcorrencia;
}

public void setCodigoOcorrencia(Long codigoOcorrencia) {
    this.codigoOcorrencia = codigoOcorrencia;
}

public String getDataOcorrencia() {
    return this.dataOcorrencia;
}

public void setDataOcorrencia(String dataOcorrencia) {
    this.dataOcorrencia = dataOcorrencia;
}

public String getHoraOcorrencia() {
    return this.horaOcorrencia;
}

public void setHoraOcorrencia(String horaOcorrencia) {
    this.horaOcorrencia = horaOcorrencia;
}

public String getDataRegistro() {
    return this.dataRegistro;
}

public void setDataRegistro(String dataRegistro) {
    this.dataRegistro = dataRegistro;
}

public String getDocumentoRecebedor() {
    return this.documentoRecebedor;
}

public void setDocumentoRecebedor(String documentoRecebedor) {
    this.documentoRecebedor = documentoRecebedor;
}

public String getNomeRecebedor() {
    return this.nomeRecebedor;
}

public void setNomeRecebedor(String nomeRecebedor) {
    this.nomeRecebedor = nomeRecebedor;
}

public String getObservacoes() {
    return this.observacoes;
}

public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
}

public String getLatitude() {
    return this.latitude;
}

public void setLatitude(String latitude) {
    this.latitude = latitude;
}

public String getLongitude() {
    return this.longitude;
}

public void setLongitude(String longitude) {
    this.longitude = longitude;
}

public boolean getSincronizado() {
    return this.sincronizado;
}

public void setSincronizado(boolean sincronizado) {
    this.sincronizado = sincronizado;
}

public String getDataSincronizacao() {
    return this.dataSincronizacao;
}

public void setDataSincronizacao(String dataSincronizacao) {
    this.dataSincronizacao = dataSincronizacao;
}

@Generated(hash = 202080066)
private transient Long documento__resolvedKey;

/** To-one relationship, resolved on first access. */
@Generated(hash = 1042367595)
public Documento getDocumento() {
    Long __key = this.documentoId;
    if (documento__resolvedKey == null
            || !documento__resolvedKey.equals(__key)) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        DocumentoDao targetDao = daoSession.getDocumentoDao();
        Documento documentoNew = targetDao.load(__key);
        synchronized (this) {
            documento = documentoNew;
            documento__resolvedKey = __key;
        }
    }
    return documento;
}

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 1070169441)
public void setDocumento(Documento documento) {
    synchronized (this) {
        this.documento = documento;
        documentoId = documento == null ? null : documento.getId();
        documento__resolvedKey = documentoId;
    }
}

@Generated(hash = 1723205787)
private transient Long ocorrencia__resolvedKey;

@Generated(hash = 421127464)
private transient Long tarefaExecutada__resolvedKey;

/** To-one relationship, resolved on first access. */
@Generated(hash = 1206448991)
public Ocorrencia getOcorrencia() {
    Long __key = this.codigoOcorrencia;
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
@Generated(hash = 2094494111)
public void setOcorrencia(Ocorrencia ocorrencia) {
    synchronized (this) {
        this.ocorrencia = ocorrencia;
        codigoOcorrencia = ocorrencia == null ? null : ocorrencia.getId();
        ocorrencia__resolvedKey = codigoOcorrencia;
    }
}

/**
 * To-many relationship, resolved on first access (and after reset).
 * Changes to to-many relations are not persisted, make changes to the target entity.
 */
@Generated(hash = 1379637793)
public List<ImagemOcorrencia> getImagemOcorrencias() {
    if (imagemOcorrencias == null) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        ImagemOcorrenciaDao targetDao = daoSession.getImagemOcorrenciaDao();
        List<ImagemOcorrencia> imagemOcorrenciasNew = targetDao
                ._queryOcorrenciaDocumento_ImagemOcorrencias(id);
        synchronized (this) {
            if (imagemOcorrencias == null) {
                imagemOcorrencias = imagemOcorrenciasNew;
            }
        }
    }
    return imagemOcorrencias;
}

/** Resets a to-many relationship, making the next get call to query for a fresh result. */
@Generated(hash = 1786532268)
public synchronized void resetImagemOcorrencias() {
    imagemOcorrencias = null;
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

public Long getTarefaExecutadaId() {
    return this.tarefaExecutadaId;
}

public void setTarefaExecutadaId(Long tarefaExecutadaId) {
    this.tarefaExecutadaId = tarefaExecutadaId;
}

/** To-one relationship, resolved on first access. */
@Generated(hash = 77611701)
public TarefasExecutadas getTarefaExecutada() {
    Long __key = this.tarefaExecutadaId;
    if (tarefaExecutada__resolvedKey == null
            || !tarefaExecutada__resolvedKey.equals(__key)) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        TarefasExecutadasDao targetDao = daoSession.getTarefasExecutadasDao();
        TarefasExecutadas tarefaExecutadaNew = targetDao.load(__key);
        synchronized (this) {
            tarefaExecutada = tarefaExecutadaNew;
            tarefaExecutada__resolvedKey = __key;
        }
    }
    return tarefaExecutada;
}

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 1943213675)
public void setTarefaExecutada(TarefasExecutadas tarefaExecutada) {
    synchronized (this) {
        this.tarefaExecutada = tarefaExecutada;
        tarefaExecutadaId = tarefaExecutada == null ? null : tarefaExecutada.getId();
        tarefaExecutada__resolvedKey = tarefaExecutadaId;
    }
}

public Long getIdServidor() {
    return this.idServidor;
}

public void setIdServidor(Long idServidor) {
    this.idServidor = idServidor;
}

public boolean getFinalizado() {
    return this.finalizado;
}

public void setFinalizado(boolean finalizado) {
    this.finalizado = finalizado;
}

public Long getEntregaId() {
    return this.entregaId;
}

public void setEntregaId(Long entregaId) {
    this.entregaId = entregaId;
}

public boolean getTemCanhoto() {
    return this.temCanhoto;
}

public void setTemCanhoto(boolean temCanhoto) {
    this.temCanhoto = temCanhoto;
}

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 709124051)
public void __setDaoSession(DaoSession daoSession) {
    this.daoSession = daoSession;
    myDao = daoSession != null ? daoSession.getOcorrenciaDocumentoDao() : null;
}

}
