package br.eti.softlog.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Paulo Sergio Alves on 2018/03/12.
 */

@Entity(nameInDb = "usuarios")
public class Usuario {

    @Id()
    private Long id;

    @Property(nameInDb = "nome")
    private String nome;

    @Property(nameInDb = "cpf")
    private String cpf;

    @Property(nameInDb = "login")
    private String login;

    @Property(nameInDb = "senha")
    private String senha;

    @Property(nameInDb = "email")
    private String email;

    @Property(nameInDb = "created_at")
    private String createdAt;

    @Property(nameInDb = "updated_at")
    private String updatedAt;

    @Property(nameInDb = "codigo_acesso")
    private int codigoAcesso;

    @Generated(hash = 1388773144)
    public Usuario(Long id, String nome, String cpf, String login, String senha,
            String email, String createdAt, String updatedAt, int codigoAcesso) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.login = login;
        this.senha = senha;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.codigoAcesso = codigoAcesso;
    }

    @Generated(hash = 562950751)
    public Usuario() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return this.cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return this.senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getCodigoAcesso() {
        return this.codigoAcesso;
    }

    public void setCodigoAcesso(int codigoAcesso) {
        this.codigoAcesso = codigoAcesso;
    }

    public boolean validUsuario(String senha_informada) {

        if (senha_informada.trim().equals(this.senha.trim())) {
            return true;
        } else {
            return false;
        }
    }

}