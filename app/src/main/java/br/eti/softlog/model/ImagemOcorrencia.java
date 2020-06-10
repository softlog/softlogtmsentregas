package br.eti.softlog.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * Created by Paulo Sérgio Alves on 2018/03/13.
 */
@Entity(
        nameInDb = "imagens_ocorrencia"
)
public class ImagemOcorrencia {

    @Id
    private Long id;

    @Property(nameInDb = "ocorrencia_documento_id")
    private Long ocorrenciaDocumentoId;

    @ToOne(joinProperty = "ocorrenciaDocumentoId")
    private OcorrenciaDocumento ocorrenciaDocumento;

    @Property(nameInDb = "nome_arquivo")
    private String nomeArquivo;

    @Property(nameInDb = "sincronizado")
    private boolean sincronizado;


/** Used to resolve relations */
@Generated(hash = 2040040024)
private transient DaoSession daoSession;

/** Used for active entity operations. */
@Generated(hash = 2013186850)
private transient ImagemOcorrenciaDao myDao;

@Generated(hash = 827563250)
public ImagemOcorrencia(Long id, Long ocorrenciaDocumentoId, String nomeArquivo,
        boolean sincronizado) {
    this.id = id;
    this.ocorrenciaDocumentoId = ocorrenciaDocumentoId;
    this.nomeArquivo = nomeArquivo;
    this.sincronizado = sincronizado;
}

@Generated(hash = 502202036)
public ImagemOcorrencia() {
}

public Long getId() {
    return this.id;
}

public void setId(Long id) {
    this.id = id;
}

public Long getOcorrenciaDocumentoId() {
    return this.ocorrenciaDocumentoId;
}

public void setOcorrenciaDocumentoId(Long ocorrenciaDocumentoId) {
    this.ocorrenciaDocumentoId = ocorrenciaDocumentoId;
}

public String getNomeArquivo() {
    return this.nomeArquivo;
}

public void setNomeArquivo(String nomeArquivo) {
    this.nomeArquivo = nomeArquivo;
}

public boolean getSincronizado() {
    return this.sincronizado;
}

public void setSincronizado(boolean sincronizado) {
    this.sincronizado = sincronizado;
}

@Generated(hash = 19025123)
private transient Long ocorrenciaDocumento__resolvedKey;

/** To-one relationship, resolved on first access. */
@Generated(hash = 302832484)
public OcorrenciaDocumento getOcorrenciaDocumento() {
    Long __key = this.ocorrenciaDocumentoId;
    if (ocorrenciaDocumento__resolvedKey == null
            || !ocorrenciaDocumento__resolvedKey.equals(__key)) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        OcorrenciaDocumentoDao targetDao = daoSession
                .getOcorrenciaDocumentoDao();
        OcorrenciaDocumento ocorrenciaDocumentoNew = targetDao.load(__key);
        synchronized (this) {
            ocorrenciaDocumento = ocorrenciaDocumentoNew;
            ocorrenciaDocumento__resolvedKey = __key;
        }
    }
    return ocorrenciaDocumento;
}

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 909549512)
public void setOcorrenciaDocumento(OcorrenciaDocumento ocorrenciaDocumento) {
    synchronized (this) {
        this.ocorrenciaDocumento = ocorrenciaDocumento;
        ocorrenciaDocumentoId = ocorrenciaDocumento == null ? null
                : ocorrenciaDocumento.getId();
        ocorrenciaDocumento__resolvedKey = ocorrenciaDocumentoId;
    }
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

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 1891814012)
public void __setDaoSession(DaoSession daoSession) {
    this.daoSession = daoSession;
    myDao = daoSession != null ? daoSession.getImagemOcorrenciaDao() : null;
}

}
