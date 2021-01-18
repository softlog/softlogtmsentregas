package br.eti.softlog.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.ToOne;

@Entity(
        nameInDb = "entregas",
        indexes = {
                @Index(value = "dataExpedicao ASC"),
                @Index(value = "destinatarioId ASC")
        }
)
public class Entregas {

    @Id(autoincrement = false)
    Long id;

    @Property(nameInDb = "destinatario_id")
    Long destinatarioId;

    @ToOne(joinProperty = "destinatarioId")
    Pessoa destinatario;

    @Property(nameInDb = "data_expedicao")
    String dataExpedicao;

    @Property(nameInDb = "status")
    Boolean status;

    @Property(nameInDb = "latitude")
    String latitude;

    @Property(nameInDb = "longitude")
    String longitude;

    @Property(nameInDb = "ordem_entrega")
    int ordemEntrega;

    @Property(nameInDb = "tem_pendencia")
    boolean temPendencia;

    @ToMany(referencedJoinProperty = "entregaId")
    List<Documento> documentos;

    @Property(nameInDb = "id_ocorrencia_padrao")
    Long idOcorrenciaPadrao;

    @Property(nameInDb = "data_ocorrencia")
    private String dataOcorrencia;

    @Property(nameInDb = "hora_ocorrencia")
    private String horaOcorrencia;

    @Property(nameInDb = "documento_recebedor")
    private String documentoRecebedor;

    @Property(nameInDb = "nome_recebedor")
    private String nomeRecebedor;

    @Property(nameInDb = "observacoes")
    private String observacoes;


/** Used to resolve relations */
@Generated(hash = 2040040024)
private transient DaoSession daoSession;

/** Used for active entity operations. */
@Generated(hash = 797490925)
private transient EntregasDao myDao;

@Generated(hash = 768035919)
public Entregas(Long id, Long destinatarioId, String dataExpedicao, Boolean status,
        String latitude, String longitude, int ordemEntrega, boolean temPendencia,
        Long idOcorrenciaPadrao, String dataOcorrencia, String horaOcorrencia,
        String documentoRecebedor, String nomeRecebedor, String observacoes) {
    this.id = id;
    this.destinatarioId = destinatarioId;
    this.dataExpedicao = dataExpedicao;
    this.status = status;
    this.latitude = latitude;
    this.longitude = longitude;
    this.ordemEntrega = ordemEntrega;
    this.temPendencia = temPendencia;
    this.idOcorrenciaPadrao = idOcorrenciaPadrao;
    this.dataOcorrencia = dataOcorrencia;
    this.horaOcorrencia = horaOcorrencia;
    this.documentoRecebedor = documentoRecebedor;
    this.nomeRecebedor = nomeRecebedor;
    this.observacoes = observacoes;
}

@Generated(hash = 1046415521)
public Entregas() {
}

public Long getId() {
    return this.id;
}

public void setId(Long id) {
    this.id = id;
}

public Long getDestinatarioId() {
    return this.destinatarioId;
}

public void setDestinatarioId(Long destinatarioId) {
    this.destinatarioId = destinatarioId;
}

public String getDataExpedicao() {
    return this.dataExpedicao;
}

public void setDataExpedicao(String dataExpedicao) {
    this.dataExpedicao = dataExpedicao;
}

public Boolean getStatus() {
    return this.status;
}

public void setStatus(Boolean status) {
    this.status = status;
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

public int getOrdemEntrega() {
    return this.ordemEntrega;
}

public void setOrdemEntrega(int ordemEntrega) {
    this.ordemEntrega = ordemEntrega;
}

public boolean getTemPendencia() {
    return this.temPendencia;
}

public void setTemPendencia(boolean temPendencia) {
    this.temPendencia = temPendencia;
}

@Generated(hash = 695539817)
private transient Long destinatario__resolvedKey;

/** To-one relationship, resolved on first access. */
@Generated(hash = 663158508)
public Pessoa getDestinatario() {
    Long __key = this.destinatarioId;
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
@Generated(hash = 1714984551)
public void setDestinatario(Pessoa destinatario) {
    synchronized (this) {
        this.destinatario = destinatario;
        destinatarioId = destinatario == null ? null : destinatario.getId();
        destinatario__resolvedKey = destinatarioId;
    }
}

/**
 * To-many relationship, resolved on first access (and after reset).
 * Changes to to-many relations are not persisted, make changes to the target entity.
 */
@Generated(hash = 1512943786)
public List<Documento> getDocumentos() {
    if (documentos == null) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        DocumentoDao targetDao = daoSession.getDocumentoDao();
        List<Documento> documentosNew = targetDao._queryEntregas_Documentos(id);
        synchronized (this) {
            if (documentos == null) {
                documentos = documentosNew;
            }
        }
    }
    return documentos;
}

/** Resets a to-many relationship, making the next get call to query for a fresh result. */
@Generated(hash = 95461526)
public synchronized void resetDocumentos() {
    documentos = null;
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

public Long getIdOcorrenciaPadrao() {
    return this.idOcorrenciaPadrao;
}

public void setIdOcorrenciaPadrao(Long idOcorrenciaPadrao) {
    this.idOcorrenciaPadrao = idOcorrenciaPadrao;
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

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 1641098349)
public void __setDaoSession(DaoSession daoSession) {
    this.daoSession = daoSession;
    myDao = daoSession != null ? daoSession.getEntregasDao() : null;
}


}
