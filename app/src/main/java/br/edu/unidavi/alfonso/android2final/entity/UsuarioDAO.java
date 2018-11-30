package br.edu.unidavi.alfonso.android2final.entity;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface UsuarioDAO {

    @Query("SELECT * FROM usuario")
    public List<Usuario> fetchUsuarios();

    @Query("SELECT * FROM usuario WHERE usuario = :usu")
    public Usuario fetchByUsuario(String usu);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insert(Usuario usuario);

    @Update
    public void update(Usuario usuario);

    @Delete
    public void delete(Usuario usuario);

}
