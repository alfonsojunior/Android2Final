package br.edu.unidavi.alfonso.android2final.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "atividade")
public class Atividade {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String titulo;
    private String descricao;
    private Date data;
    private boolean status;
    private String rua;
    private int numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String usuario;

    @Ignore
    public Atividade(String titulo, String descricao, Date data, boolean status, String rua, int numero, String bairro, String cidade, String estado, String usuario) {
        this.id = 0L;
        this.titulo = titulo;
        this.descricao = descricao;
        this.data = data;
        this.status = status;
        this.rua = rua;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.usuario = usuario;
    }

    public Atividade(long id, String titulo, String descricao, Date data, boolean status, String rua, int numero, String bairro, String cidade, String estado, String usuario) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.data = data;
        this.status = status;
        this.rua = rua;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.usuario = usuario;
    }

    public long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public Date getData() {
        return data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getRua() {
        return rua;
    }

    public int getNumero() {
        return numero;
    }

    public String getBairro() {
        return bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }

    public String getUsuario() {
        return usuario;
    }
}
