package br.edu.unidavi.alfonso.android2final.entity;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface AtividadeDAO {

    @Query("SELECT * FROM atividade WHERE usuario = :usuario AND status = :status")
    public List<Atividade> fetchAtividades(String usuario, boolean status);

    @Query("SELECT * FROM atividade WHERE titulo like :titulo AND usuario = :usuario AND status = :status")
    public List<Atividade> fetchByTitulo(String titulo, String usuario, boolean status);

    @Query("SELECT * FROM atividade WHERE id = :id AND usuario = :usuario")
    public Atividade fetchById(long id, String usuario);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insert(Atividade atividade);

    @Update
    public void update(Atividade atividade);

    @Delete
    public void delete(Atividade atividade);
}
