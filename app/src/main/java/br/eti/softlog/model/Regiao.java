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
@Entity (nameInDb = "regiao")
public class Regiao {

    @Id
    private Long id;

    @Property(nameInDb = "regiao")
    private String regiao;

    @Property(nameInDb = "tipo_regiao")
    private String tipoRegiao;

    @Property(nameInDb = "regiao_principal_id")
    private Long regiaoPrincipalId;

    @ToOne(joinProperty = "regiaoPrincipalId")
    private Regiao regiaoPrincipal;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 532909860)
    private transient RegiaoDao myDao;

    @Generated(hash = 1562996922)
    public Regiao(Long id, String regiao, String tipoRegiao,
            Long regiaoPrincipalId) {
        this.id = id;
        this.regiao = regiao;
        this.tipoRegiao = tipoRegiao;
        this.regiaoPrincipalId = regiaoPrincipalId;
    }

    @Generated(hash = 343401069)
    public Regiao() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegiao() {
        return this.regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    public String getTipoRegiao() {
        return this.tipoRegiao;
    }

    public void setTipoRegiao(String tipoRegiao) {
        this.tipoRegiao = tipoRegiao;
    }

    public Long getRegiaoPrincipalId() {
        return this.regiaoPrincipalId;
    }

    public void setRegiaoPrincipalId(Long regiaoPrincipalId) {
        this.regiaoPrincipalId = regiaoPrincipalId;
    }

    @Generated(hash = 1692527297)
    private transient Long regiaoPrincipal__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1299649641)
    public Regiao getRegiaoPrincipal() {
        Long __key = this.regiaoPrincipalId;
        if (regiaoPrincipal__resolvedKey == null
                || !regiaoPrincipal__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RegiaoDao targetDao = daoSession.getRegiaoDao();
            Regiao regiaoPrincipalNew = targetDao.load(__key);
            synchronized (this) {
                regiaoPrincipal = regiaoPrincipalNew;
                regiaoPrincipal__resolvedKey = __key;
            }
        }
        return regiaoPrincipal;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1878944313)
    public void setRegiaoPrincipal(Regiao regiaoPrincipal) {
        synchronized (this) {
            this.regiaoPrincipal = regiaoPrincipal;
            regiaoPrincipalId = regiaoPrincipal == null ? null
                    : regiaoPrincipal.getId();
            regiaoPrincipal__resolvedKey = regiaoPrincipalId;
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
    @Generated(hash = 29553303)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRegiaoDao() : null;
    }

}
