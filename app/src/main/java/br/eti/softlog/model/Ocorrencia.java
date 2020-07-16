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

    @Property(nameInDb = "exige_recebedor")
    private boolean exigeRecebedor;

    @Property(nameInDb = "exige_documento")
    private boolean exigeDocumento;

    @Property(nameInDb = "exigeImagem")
    private boolean exigeImagem;

    @Generated(hash = 1029734719)
    public Ocorrencia(Long id, String ocorrencia, boolean pendencia, boolean ativo,
            boolean exigeRecebedor, boolean exigeDocumento, boolean exigeImagem) {
        this.id = id;
        this.ocorrencia = ocorrencia;
        this.pendencia = pendencia;
        this.ativo = ativo;
        this.exigeRecebedor = exigeRecebedor;
        this.exigeDocumento = exigeDocumento;
        this.exigeImagem = exigeImagem;
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

    public boolean getExigeRecebedor() {
        return this.exigeRecebedor;
    }

    public void setExigeRecebedor(boolean exigeRecebedor) {
        this.exigeRecebedor = exigeRecebedor;
    }

    public boolean getExigeDocumento() {
        return this.exigeDocumento;
    }

    public void setExigeDocumento(boolean exigeDocumento) {
        this.exigeDocumento = exigeDocumento;
    }

    public boolean getExigeImagem() {
        return this.exigeImagem;
    }

    public void setExigeImagem(boolean exigeImagem) {
        this.exigeImagem = exigeImagem;
    }

}
