package br.edu.unidavi.alfonso.android2final.entity;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface SessaoDAO {

    @Query("SELECT * FROM sessao")
    public List<Sessao> fetchSessoes();

    @Query("SELECT * FROM sessao WHERE chave = :chave")
    public Sessao fetchByChave(String chave);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(Sessao sessao);

    @Update
    public void update(Sessao sessao);

    @Delete
    public void delete(Sessao sessao);

}
