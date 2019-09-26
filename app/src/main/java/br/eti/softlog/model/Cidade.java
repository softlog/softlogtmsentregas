package br.eti.softlog.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Paulo Sérgio Alves on 2018/03/13.
 */

@Entity (
        nameInDb = "cidades",
        indexes = {
                @Index(value = "nomeCidade ASC"),
                @Index(value = "codigoIbge ASC")
        }
)
public class Cidade {

    @Id
    private Long id;

    @Property(nameInDb = "nome_cidade")
    private String nomeCidade;

    @Property(nameInDb = "uf")
    private String uf;

    @Property(nameInDb = "codigo_ibge")
    private String codigoIbge;

@Generated(hash = 1725111507)
public Cidade(Long id, String nomeCidade, String uf, String codigoIbge) {
    this.id = id;
    this.nomeCidade = nomeCidade;
    this.uf = uf;
    this.codigoIbge = codigoIbge;
}

@Generated(hash = 1235207452)
public Cidade() {
}

public Long getId() {
    return this.id;
}

public void setId(Long id) {
    this.id = id;
}

public String getNomeCidade() {
    return this.nomeCidade;
}

public void setNomeCidade(String nomeCidade) {
    this.nomeCidade = nomeCidade;
}

public String getUf() {
    return this.uf;
}

public void setUf(String uf) {
    this.uf = uf;
}

public String getCodigoIbge() {
    return this.codigoIbge;
}

public void setCodigoIbge(String codigoIbge) {
    this.codigoIbge = codigoIbge;
}

}
