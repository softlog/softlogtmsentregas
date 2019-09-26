package br.eti.softlog.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Paulo Sérgio Alves on 2018/03/13.
 */

@Entity(
        nameInDb = "veiculos",
        indexes = {
                @Index(value = "placaVeiculo ASC")
        }

)
public class Veiculo {

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "placa_veiculo")
    private String placaVeiculo;

    @Property(nameInDb = "descricao")
    private String descricao;

@Generated(hash = 663356760)
public Veiculo(Long id, String placaVeiculo, String descricao) {
    this.id = id;
    this.placaVeiculo = placaVeiculo;
    this.descricao = descricao;
}

@Generated(hash = 1230213743)
public Veiculo() {
}

public Long getId() {
    return this.id;
}

public void setId(Long id) {
    this.id = id;
}

public String getPlacaVeiculo() {
    return this.placaVeiculo;
}

public void setPlacaVeiculo(String placaVeiculo) {
    this.placaVeiculo = placaVeiculo;
}

public String getDescricao() {
    return this.descricao;
}

public void setDescricao(String descricao) {
    this.descricao = descricao;
}
}
