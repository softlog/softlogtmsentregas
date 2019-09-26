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
 * Created by Paulo Sérgio Alves on 2018/03/13.
 */

@Entity (
        nameInDb = "romaneios",
        indexes = {
                @Index(value="numeroRomaneio ASC")
        }
)
public class Romaneio {

    @Id
    private Long id;

    @Property(nameInDb = "numero_romaneio")
    private String numeroRomaneio;

    @Property(nameInDb = "data_romaneio")
    private String dataRomaneio;

    @Property(nameInDb = "data_saida")
    private String dataSaida;

    @Property(nameInDb = "data_chegada")
    private String dataChegada;

    @Property(nameInDb = "veiculo")
    private Long veiculoId;

    @ToOne(joinProperty = "veiculoId")
    private Veiculo veiculo;

    @Property(nameInDb = "motorista_cpf")
    private Long motoristaCpf;

    @ToOne(joinProperty = "motoristaCpf")
    private Pessoa motorista;

    @Property(nameInDb = "redespachador_cnpj")
    private Long redespachadorCnpj;

    @ToOne(joinProperty = "redespachadorCnpj")
    private Pessoa redespachador;

    @Property(nameInDb = "cidade_origem_id")
    private Long cidadeOrigemId;

    @ToOne(joinProperty = "cidadeOrigemId")
    private Cidade cidade;

    @Property(nameInDb = "regiao_origem_id")
    private Long regiaoId;

    @ToOne(joinProperty = "regiaoId")
    private Regiao regiao;

    @Property(nameInDb = "status")
    private boolean status;

    @ToMany(referencedJoinProperty = "romaneioId")
    @OrderBy("id ASC")
    private List<Documento> documentos;

    @Property(nameInDb = "data_expedicao")
    private String dataExpedicao;

    /** Used to resolve relations */
@Generated(hash = 2040040024)
private transient DaoSession daoSession;

/** Used for active entity operations. */
@Generated(hash = 2084097363)
private transient RomaneioDao myDao;


@Generated(hash = 850484254)
public Romaneio() {
}

@Generated(hash = 667881728)
public Romaneio(Long id, String numeroRomaneio, String dataRomaneio, String dataSaida,
        String dataChegada, Long veiculoId, Long motoristaCpf, Long redespachadorCnpj,
        Long cidadeOrigemId, Long regiaoId, boolean status, String dataExpedicao) {
    this.id = id;
    this.numeroRomaneio = numeroRomaneio;
    this.dataRomaneio = dataRomaneio;
    this.dataSaida = dataSaida;
    this.dataChegada = dataChegada;
    this.veiculoId = veiculoId;
    this.motoristaCpf = motoristaCpf;
    this.redespachadorCnpj = redespachadorCnpj;
    this.cidadeOrigemId = cidadeOrigemId;
    this.regiaoId = regiaoId;
    this.status = status;
    this.dataExpedicao = dataExpedicao;
}

public Long getId() {
    return this.id;
}

public void setId(Long id) {
    this.id = id;
}

public String getNumeroRomaneio() {
    return this.numeroRomaneio;
}

public void setNumeroRomaneio(String numeroRomaneio) {
    this.numeroRomaneio = numeroRomaneio;
}

public String getDataSaida() {
    return this.dataSaida;
}

public void setDataSaida(String dataSaida) {
    if (dataSaida=="null")
        this.dataSaida = null;
    else
        this.dataSaida = dataSaida;
}

public String getDataChegada() {
    return this.dataChegada;
}

public void setDataChegada(String dataChegada) {
    if (dataChegada=="null")
        this.dataChegada = null;
    else
        this.dataChegada = dataChegada;
}

public Long getVeiculoId() {
    return this.veiculoId;
}

public void setVeiculoId(Long veiculoId) {
    this.veiculoId = veiculoId;
}

public Long getMotoristaCpf() {
    return this.motoristaCpf;
}

public void setMotoristaCpf(Long motoristaCpf) {
    this.motoristaCpf = motoristaCpf;
}

public Long getRedespachadorCnpj() {
    return this.redespachadorCnpj;
}

public void setRedespachadorCnpj(Long redespachadorCnpj) {
    this.redespachadorCnpj = redespachadorCnpj;
}

public Long getCidadeOrigemId() {
    return this.cidadeOrigemId;
}

public void setCidadeOrigemId(Long cidadeOrigemId) {
    this.cidadeOrigemId = cidadeOrigemId;
}

public Long getRegiaoId() {
    return this.regiaoId;
}

public void setRegiaoId(Long regiaoId) {
    this.regiaoId = regiaoId;
}

public boolean getStatus() {
    return this.status;
}

public void setStatus(boolean status) {
    this.status = status;
}

@Generated(hash = 1445715105)
private transient Long veiculo__resolvedKey;

/** To-one relationship, resolved on first access. */
@Generated(hash = 711281188)
public Veiculo getVeiculo() {
    Long __key = this.veiculoId;
    if (veiculo__resolvedKey == null || !veiculo__resolvedKey.equals(__key)) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        VeiculoDao targetDao = daoSession.getVeiculoDao();
        Veiculo veiculoNew = targetDao.load(__key);
        synchronized (this) {
            veiculo = veiculoNew;
            veiculo__resolvedKey = __key;
        }
    }
    return veiculo;
}

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 2110402675)
public void setVeiculo(Veiculo veiculo) {
    synchronized (this) {
        this.veiculo = veiculo;
        veiculoId = veiculo == null ? null : veiculo.getId();
        veiculo__resolvedKey = veiculoId;
    }
}

@Generated(hash = 1996371168)
private transient Long motorista__resolvedKey;

/** To-one relationship, resolved on first access. */
@Generated(hash = 263252723)
public Pessoa getMotorista() {
    Long __key = this.motoristaCpf;
    if (motorista__resolvedKey == null
            || !motorista__resolvedKey.equals(__key)) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        PessoaDao targetDao = daoSession.getPessoaDao();
        Pessoa motoristaNew = targetDao.load(__key);
        synchronized (this) {
            motorista = motoristaNew;
            motorista__resolvedKey = __key;
        }
    }
    return motorista;
}

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 762500851)
public void setMotorista(Pessoa motorista) {
    synchronized (this) {
        this.motorista = motorista;
        motoristaCpf = motorista == null ? null : motorista.getId();
        motorista__resolvedKey = motoristaCpf;
    }
}

@Generated(hash = 2072855708)
private transient Long redespachador__resolvedKey;

/** To-one relationship, resolved on first access. */
@Generated(hash = 1822256261)
public Pessoa getRedespachador() {
    Long __key = this.redespachadorCnpj;
    if (redespachador__resolvedKey == null
            || !redespachador__resolvedKey.equals(__key)) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        PessoaDao targetDao = daoSession.getPessoaDao();
        Pessoa redespachadorNew = targetDao.load(__key);
        synchronized (this) {
            redespachador = redespachadorNew;
            redespachador__resolvedKey = __key;
        }
    }
    return redespachador;
}

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 1605230888)
public void setRedespachador(Pessoa redespachador) {
    synchronized (this) {
        this.redespachador = redespachador;
        redespachadorCnpj = redespachador == null ? null
                : redespachador.getId();
        redespachador__resolvedKey = redespachadorCnpj;
    }
}

@Generated(hash = 869691244)
private transient Long cidade__resolvedKey;

/** To-one relationship, resolved on first access. */
@Generated(hash = 964371912)
public Cidade getCidade() {
    Long __key = this.cidadeOrigemId;
    if (cidade__resolvedKey == null || !cidade__resolvedKey.equals(__key)) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        CidadeDao targetDao = daoSession.getCidadeDao();
        Cidade cidadeNew = targetDao.load(__key);
        synchronized (this) {
            cidade = cidadeNew;
            cidade__resolvedKey = __key;
        }
    }
    return cidade;
}

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 238447130)
public void setCidade(Cidade cidade) {
    synchronized (this) {
        this.cidade = cidade;
        cidadeOrigemId = cidade == null ? null : cidade.getId();
        cidade__resolvedKey = cidadeOrigemId;
    }
}

@Generated(hash = 101808153)
private transient Long regiao__resolvedKey;

/** To-one relationship, resolved on first access. */
@Generated(hash = 962445062)
public Regiao getRegiao() {
    Long __key = this.regiaoId;
    if (regiao__resolvedKey == null || !regiao__resolvedKey.equals(__key)) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        RegiaoDao targetDao = daoSession.getRegiaoDao();
        Regiao regiaoNew = targetDao.load(__key);
        synchronized (this) {
            regiao = regiaoNew;
            regiao__resolvedKey = __key;
        }
    }
    return regiao;
}

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 1920134820)
public void setRegiao(Regiao regiao) {
    synchronized (this) {
        this.regiao = regiao;
        regiaoId = regiao == null ? null : regiao.getId();
        regiao__resolvedKey = regiaoId;
    }
}

/**
 * To-many relationship, resolved on first access (and after reset).
 * Changes to to-many relations are not persisted, make changes to the target entity.
 */
@Generated(hash = 1259842208)
public List<Documento> getDocumentos() {
    if (documentos == null) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        DocumentoDao targetDao = daoSession.getDocumentoDao();
        List<Documento> documentosNew = targetDao._queryRomaneio_Documentos(id);
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

public String getDataRomaneio() {
    return this.dataRomaneio;
}

public void setDataRomaneio(String dataRomaneio) {
    if (dataRomaneio=="null")
        this.dataRomaneio = null;
    else
        this.dataRomaneio = dataRomaneio;
}

public String getDataExpedicao() {
    return this.dataExpedicao;
}

public void setDataExpedicao(String dataExpedicao) {
    this.dataExpedicao = dataExpedicao;
}

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 25089770)
public void __setDaoSession(DaoSession daoSession) {
    this.daoSession = daoSession;
    myDao = daoSession != null ? daoSession.getRomaneioDao() : null;
}

}

