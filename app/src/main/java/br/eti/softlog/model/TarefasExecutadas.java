package br.eti.softlog.model;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.ToOne;

@Entity(nameInDb = "tarefas_executadas")
public class TarefasExecutadas {

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "data_tarefa")
    private String dataTarefa;

    @Property(nameInDb = "destinatario_id")
    private Long destintarioId;

    @Property(nameInDb = "tipo_tarefa")
    private int tipoTarefa;

    @Property(nameInDb = "data_hora_inicio")
    private String dataHoraInicioViagem;

    @Property(nameInDb = "latitude_inicio")
    private String latitudeInicio;

    @Property(nameInDb = "longitude_inicio")
    private String longitudeInicio;

    @Property(nameInDb = "data_hora_check_in")
    private String dataHoraCheckIn;

    @Property(nameInDb = "latitude_check_in")
    private String latitudeCheckIn;

    @Property(nameInDb = "longitude_check_in")
    private String longitudeCheckIn;

    @Property(nameInDb = "data_hora_check_out")
    private String dataHoraCheckOut;

    @Property(nameInDb = "latitude_check_out")
    private String latitudeCheckOut;

    @Property(nameInDb = "longitude_check_out")
    private String longitudeCheckOut;

    @ToMany(referencedJoinProperty = "tarefaExecutadaId")
    private List<OcorrenciaDocumento> ocorrenciaDocumentos;

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

    @Property(nameInDb = "distance")
    private Double distance;

    @Property(nameInDb = "duracao")
    private Double duracao;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 349024766)
    private transient TarefasExecutadasDao myDao;

    @Generated(hash = 869691244)
    private transient Long cidade__resolvedKey;

    @Generated(hash = 1642958377)
    public TarefasExecutadas(Long id, String dataTarefa, Long destintarioId, int tipoTarefa,
            String dataHoraInicioViagem, String latitudeInicio, String longitudeInicio,
            String dataHoraCheckIn, String latitudeCheckIn, String longitudeCheckIn,
            String dataHoraCheckOut, String latitudeCheckOut, String longitudeCheckOut, String endereco,
            String numero, String bairro, Long idCidade, String cep, String telefone, String whatsapp,
            Long idRegiao, Double distance, Double duracao) {
        this.id = id;
        this.dataTarefa = dataTarefa;
        this.destintarioId = destintarioId;
        this.tipoTarefa = tipoTarefa;
        this.dataHoraInicioViagem = dataHoraInicioViagem;
        this.latitudeInicio = latitudeInicio;
        this.longitudeInicio = longitudeInicio;
        this.dataHoraCheckIn = dataHoraCheckIn;
        this.latitudeCheckIn = latitudeCheckIn;
        this.longitudeCheckIn = longitudeCheckIn;
        this.dataHoraCheckOut = dataHoraCheckOut;
        this.latitudeCheckOut = latitudeCheckOut;
        this.longitudeCheckOut = longitudeCheckOut;
        this.endereco = endereco;
        this.numero = numero;
        this.bairro = bairro;
        this.idCidade = idCidade;
        this.cep = cep;
        this.telefone = telefone;
        this.whatsapp = whatsapp;
        this.idRegiao = idRegiao;
        this.distance = distance;
        this.duracao = duracao;
    }

    @Generated(hash = 1880987081)
    public TarefasExecutadas() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTipoTarefa() {
        return this.tipoTarefa;
    }

    public void setTipoTarefa(int tipoTarefa) {
        this.tipoTarefa = tipoTarefa;
    }

    public String getDataHoraCheckIn() {
        return this.dataHoraCheckIn;
    }

    public void setDataHoraCheckIn(String dataHoraCheckIn) {
        this.dataHoraCheckIn = dataHoraCheckIn;
    }

    public String getLatitudeCheckIn() {
        return this.latitudeCheckIn;
    }

    public void setLatitudeCheckIn(String latitudeCheckIn) {
        this.latitudeCheckIn = latitudeCheckIn;
    }

    public String getLongitudeCheckIn() {
        return this.longitudeCheckIn;
    }

    public void setLongitudeCheckIn(String longitudeCheckIn) {
        this.longitudeCheckIn = longitudeCheckIn;
    }

    public String getDataHoraCheckOut() {
        return this.dataHoraCheckOut;
    }

    public void setDataHoraCheckOut(String dataHoraCheckOut) {
        this.dataHoraCheckOut = dataHoraCheckOut;
    }

    public String getLatitudeCheckOut() {
        return this.latitudeCheckOut;
    }

    public void setLatitudeCheckOut(String latitudeCheckOut) {
        this.latitudeCheckOut = latitudeCheckOut;
    }

    public String getLongitudeCheckOut() {
        return this.longitudeCheckOut;
    }

    public void setLongitudeCheckOut(String longitudeCheckOut) {
        this.longitudeCheckOut = longitudeCheckOut;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1560877412)
    public List<OcorrenciaDocumento> getOcorrenciaDocumentos() {
        if (ocorrenciaDocumentos == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            OcorrenciaDocumentoDao targetDao = daoSession
                    .getOcorrenciaDocumentoDao();
            List<OcorrenciaDocumento> ocorrenciaDocumentosNew = targetDao
                    ._queryTarefasExecutadas_OcorrenciaDocumentos(id);
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

    public String getDataHoraInicioViagem() {
        return this.dataHoraInicioViagem;
    }

    public void setDataHoraInicioViagem(String dataHoraInicioViagem) {
        this.dataHoraInicioViagem = dataHoraInicioViagem;
    }

    public String getLatitudeInicio() {
        return this.latitudeInicio;
    }

    public void setLatitudeInicio(String latitudeInicio) {
        this.latitudeInicio = latitudeInicio;
    }

    public String getLongitudeInicio() {
        return this.longitudeInicio;
    }

    public void setLongitudeInicio(String longitudeInicio) {
        this.longitudeInicio = longitudeInicio;
    }

    public String getDataTarefa() {
        return this.dataTarefa;
    }

    public void setDataTarefa(String dataTarefa) {
        this.dataTarefa = dataTarefa;
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

    public Long getDestintarioId() {
        return this.destintarioId;
    }

    public void setDestintarioId(Long destintarioId) {
        this.destintarioId = destintarioId;
    }

    public Double getDistance() {
        return this.distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getDuracao() {
        return this.duracao;
    }

    public void setDuracao(Double duracao) {
        this.duracao = duracao;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2068180465)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTarefasExecutadasDao() : null;
    }


}
