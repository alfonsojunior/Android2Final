package br.edu.unidavi.alfonso.android2final.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "sessao")
public class Sessao {

    @PrimaryKey
    @NonNull
    private String chave;
    private String valor;

    public Sessao(String chave, String valor) {
        this.chave = chave;
        this.valor = valor;
    }

    public String getChave() {
        return chave;
    }

    public String getValor() {
        return valor;
    }
}
