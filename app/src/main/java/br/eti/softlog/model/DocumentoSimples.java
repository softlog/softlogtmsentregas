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
        nameInDb = "documentos_simples",
        indexes = {
                @Index(value = "dataEmissao ASC"),
                @Index(value = "dataExpedicao ASC"),
                @Index(value = "numeroNotaFiscal ASC"),
                @Index(value = "remetenteCnpj ASC")
        }
)
public class DocumentoSimples {

    @Id(autoincrement = true)
    private Long id;

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

    @Property(nameInDb = "documento_id")
    private Long documentoId;

    @ToOne(joinProperty = "documentoId")
    private Documento documento;

    @Property(nameInDb = "id_ocorrencia")
    private Long idOcorrencia;

    @ToOne(joinProperty = "idOcorrencia")
    private Ocorrencia ocorrencia;

    @Property(nameInDb = "data_ocorrencia")
    private String dataOcorrencia;

/** Used to resolve relations */
@Generated(hash = 2040040024)
private transient DaoSession daoSession;

/** Used for active entity operations. */
@Generated(hash = 1898879136)
private transient DocumentoSimplesDao myDao;

@Generated(hash = 2097989974)
public DocumentoSimples(Long id, String dataEmissao, String dataExpedicao,
        String chaveNfe, String serie, String numeroNotaFiscal,
        Long remetenteCnpj, Long documentoId, Long idOcorrencia,
        String dataOcorrencia) {
    this.id = id;
    this.dataEmissao = dataEmissao;
    this.dataExpedicao = dataExpedicao;
    this.chaveNfe = chaveNfe;
    this.serie = serie;
    this.numeroNotaFiscal = numeroNotaFiscal;
    this.remetenteCnpj = remetenteCnpj;
    this.documentoId = documentoId;
    this.idOcorrencia = idOcorrencia;
    this.dataOcorrencia = dataOcorrencia;
}

@Generated(hash = 1609357342)
public DocumentoSimples() {
}

public Long getId() {
    return this.id;
}

public void setId(Long id) {
    this.id = id;
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

public Long getDocumentoId() {
    return this.documentoId;
}

public void setDocumentoId(Long documentoId) {
    this.documentoId = documentoId;
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
@Generated(hash = 1706461705)
public void __setDaoSession(DaoSession daoSession) {
    this.daoSession = daoSession;
    myDao = daoSession != null ? daoSession.getDocumentoSimplesDao() : null;
}


}