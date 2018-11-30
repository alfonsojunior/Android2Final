package br.edu.unidavi.alfonso.android2final.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "usuario")
public class Usuario {

    @NonNull
    @PrimaryKey
    private final String usuario;
    private final String senha;

    public Usuario(String usuario, String senha) {

        this.usuario = usuario;
        this.senha = senha;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getSenha() {
        return senha;
    }

}
