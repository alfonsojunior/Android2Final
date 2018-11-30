package br.edu.unidavi.alfonso.android2final.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import br.edu.unidavi.alfonso.android2final.entity.Atividade;
import br.edu.unidavi.alfonso.android2final.entity.AtividadeDAO;
import br.edu.unidavi.alfonso.android2final.entity.Sessao;
import br.edu.unidavi.alfonso.android2final.entity.SessaoDAO;
import br.edu.unidavi.alfonso.android2final.entity.Usuario;
import br.edu.unidavi.alfonso.android2final.entity.UsuarioDAO;

@Database(entities = {Usuario.class, Atividade.class, Sessao.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class AppStore extends RoomDatabase {

    public abstract UsuarioDAO getUsuarioDAO();
    public abstract AtividadeDAO getAtividadeDAO();
    public abstract SessaoDAO getSessaoDAO();

    private static AppStore instance = null;

    public static AppStore getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context,
                    AppStore.class,
                    "Android2Final.db")
                    //.allowMainThreadQueries() // Executar sem ser async
                    //.fallbackToDestructiveMigration() // Usado para destruir o banco e recriar
                    //.addMigrations(Migrations.FROM_1_TO_2)
                    .build();
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }

}
