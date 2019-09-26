package br.eti.softlog.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "tracking_gps")
public class TrackingGps
{

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "placa_veiculo")
    private String placaVeiculo;

    @Property(nameInDb = "motorista_cpf")
    private String motoristaCpf;

    @Property(nameInDb = "data_localizacao")
    private String dataLocalizacao;

    @Property(nameInDb = "latitude")
    private Double latitude;

    @Property(nameInDb = "longitude")
    private Double longitude;

    @Property(nameInDb = "sincronizado")
    private boolean sincrozinado;

    @Generated(hash = 1388469668)
    public TrackingGps(Long id, String placaVeiculo, String motoristaCpf,
            String dataLocalizacao, Double latitude, Double longitude,
            boolean sincrozinado) {
        this.id = id;
        this.placaVeiculo = placaVeiculo;
        this.motoristaCpf = motoristaCpf;
        this.dataLocalizacao = dataLocalizacao;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sincrozinado = sincrozinado;
    }

    @Generated(hash = 1818333536)
    public TrackingGps() {
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

    public String getMotoristaCpf() {
        return this.motoristaCpf;
    }

    public void setMotoristaCpf(String motoristaCpf) {
        this.motoristaCpf = motoristaCpf;
    }

    public String getDataLocalizacao() {
        return this.dataLocalizacao;
    }

    public void setDataLocalizacao(String dataLocalizacao) {
        this.dataLocalizacao = dataLocalizacao;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public boolean getSincrozinado() {
        return this.sincrozinado;
    }

    public void setSincrozinado(boolean sincrozinado) {
        this.sincrozinado = sincrozinado;
    }

}
