package br.eti.softlog.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Paulo Sérgio Alves on 2018/03/13.
 */

@Entity(nameInDb = "ocorrencias")
public class Ocorrencia {

    @Id
    private Long id;

    @Property(nameInDb = "ocorrencia")
    private String ocorrencia;

    @Property(nameInDb = "pendencia")
    private boolean pendencia;

    @Property(nameInDb = "ativo")
    private boolean ativo;

    @Generated(hash = 716966030)
    public Ocorrencia(Long id, String ocorrencia, boolean pendencia,
            boolean ativo) {
        this.id = id;
        this.ocorrencia = ocorrencia;
        this.pendencia = pendencia;
        this.ativo = ativo;
    }

    @Generated(hash = 1820280585)
    public Ocorrencia() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOcorrencia() {
        return this.ocorrencia;
    }

    public void setOcorrencia(String ocorrencia) {
        this.ocorrencia = ocorrencia;
    }

    public boolean getPendencia() {
        return this.pendencia;
    }

    public void setPendencia(boolean pendencia) {
        this.pendencia = pendencia;
    }


    @Override
    public String toString() {
        return String.format("%03d", id) + " - " + ocorrencia.toUpperCase();
    }

    public boolean getAtivo() {
        return this.ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

}
