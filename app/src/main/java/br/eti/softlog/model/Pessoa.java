package br.eti.softlog.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;

import java.util.List;

/**
 * Created by Paulo Sergio Alves on 2018/03/13.
 */
@Entity(
        nameInDb = "pessoas",
        indexes = {
                @Index(value = "cnpjCpf ASC")
        }
)
public class Pessoa {

    @Id
    private Long id;

    @Property(nameInDb = "tipo_pessoa")
    private int tipoPessoa;

    @Property(nameInDb = "nome")
    private String nome;

    @Property(nameInDb="cnpj_cpf")
    private String cnpjCpf;

    @Property(nameInDb="endereco")
    private String endereco;

    @Property(nameInDb = "numero")
    private String numero;

    @Property(nameInDb = "bairro")
    private String bairro;

    @Property(nameInDb = "id_cidade")
    private Long idCidade;

    @ToOne(joinProperty = "idCidade")
    private Cidade cidade;

    @Property(nameInDb = "cep")
    private String cep;

    @Property(nameInDb = "telefone")
    private String telefone;

    @Property(nameInDb = "whatsapp")
    private String whatsapp;

    @Property(nameInDb = "id_regiao")
    private Long idRegiao;

    @Property(nameInDb = "latitude")
    private String latitude;

    @Property(nameInDb = "longitude")
    private String longitude;

    @ToMany(referencedJoinProperty = "destinatarioCnpj")
    private List<Documento> documentos;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1409126797)
    private transient PessoaDao myDao;

    @Generated(hash = 869691244)
    private transient Long cidade__resolvedKey;

@Generated(hash = 392129186)
public Pessoa(Long id, int tipoPessoa, String nome, String cnpjCpf,
        String endereco, String numero, String bairro, Long idCidade,
        String cep, String telefone, String whatsapp, Long idRegiao,
        String latitude, String longitude) {
    this.id = id;
    this.tipoPessoa = tipoPessoa;
    this.nome = nome;
    this.cnpjCpf = cnpjCpf;
    this.endereco = endereco;
    this.numero = numero;
    this.bairro = bairro;
    this.idCidade = idCidade;
    this.cep = cep;
    this.telefone = telefone;
    this.whatsapp = whatsapp;
    this.idRegiao = idRegiao;
    this.latitude = latitude;
    this.longitude = longitude;
}



@Generated(hash = 722086707)
public Pessoa() {
}

public Long getId() {
    return this.id;
}

public void setId(Long id) {
    this.id = id;
}

public int getTipoPessoa() {
    return this.tipoPessoa;
}

public void setTipoPessoa(int tipoPessoa) {
    this.tipoPessoa = tipoPessoa;
}

public String getNome() {
    return this.nome;
}

public void setNome(String nome) {
    this.nome = nome;
}

public String getCnpjCpf() {
    return this.cnpjCpf;
}

public void setCnpjCpf(String cnpjCpf) {
    this.cnpjCpf = cnpjCpf;
}

public String getEndereco() {
    return this.endereco;
}

public void setEndereco(String endereco) {
    this.endereco = endereco;
}

public String getNumero() {
    return this.numero;
}

public void setNumero(String numero) {
    this.numero = numero;
}

public String getBairro() {
    return this.bairro;
}

public void setBairro(String bairro) {
    this.bairro = bairro;
}

public Long getIdCidade() {
    return this.idCidade;
}

public void setIdCidade(Long idCidade) {
    this.idCidade = idCidade;
}

public String getCep() {
    return this.cep;
}

public void setCep(String cep) {
    this.cep = cep;
}

public String getTelefone() {
    return this.telefone;
}

public void setTelefone(String telefone) {
    if (telefone=="null")
        this.telefone = null;
    else
        this.telefone = telefone;
}

public String getWhatsapp() {
    return this.whatsapp;
}

public void setWhatsapp(String whatsapp) {
    this.whatsapp = whatsapp;
}

public Long getIdRegiao() {
    return this.idRegiao;
}

public void setIdRegiao(Long idRegiao) {
    this.idRegiao = idRegiao;
}

public String getLatitude() {
    return this.latitude;
}

public void setLatitude(String latitude) {
    if (latitude=="null")
        this.latitude = null;
    else
        this.latitude = latitude;
}

public String getLongitude() {
    return this.longitude;
}

public void setLongitude(String longitude) {
    if (longitude=="null")
        this.longitude = null;
    else
        this.longitude = longitude;
}



/** To-one relationship, resolved on first access. */
@Generated(hash = 1849902531)
public Cidade getCidade() {
    Long __key = this.idCidade;
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
@Generated(hash = 473709177)
public void setCidade(Cidade cidade) {
    synchronized (this) {
        this.cidade = cidade;
        idCidade = cidade == null ? null : cidade.getId();
        cidade__resolvedKey = idCidade;
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



/**
 * To-many relationship, resolved on first access (and after reset).
 * Changes to to-many relations are not persisted, make changes to the target entity.
 */
@Generated(hash = 1451555788)
public List<Documento> getDocumentos() {
    if (documentos == null) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        DocumentoDao targetDao = daoSession.getDocumentoDao();
        List<Documento> documentosNew = targetDao._queryPessoa_Documentos(id);
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



/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 403681025)
public void __setDaoSession(DaoSession daoSession) {
    this.daoSession = daoSession;
    myDao = daoSession != null ? daoSession.getPessoaDao() : null;
}

}
