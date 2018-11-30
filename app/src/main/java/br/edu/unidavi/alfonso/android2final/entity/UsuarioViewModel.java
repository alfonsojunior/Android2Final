package br.edu.unidavi.alfonso.android2final.entity;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.List;

import br.edu.unidavi.alfonso.android2final.database.AppStore;

public class UsuarioViewModel extends AndroidViewModel {

    public final MutableLiveData<List<Usuario>> usuarios = new MutableLiveData<>();
    public final MutableLiveData<Usuario> usuarioLiveData = new MutableLiveData<>();
    public final MutableLiveData<Boolean> success = new MutableLiveData<>();

    public UsuarioViewModel(@NonNull Application application) {
        super(application);
    }

    public void fetchUsuarios() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                List<Usuario> usus = AppStore.getInstance(getApplication())
                        .getUsuarioDAO().fetchUsuarios();
                usuarios.postValue(usus);
                return null;
            }

        }.execute();
    }

    public void findUsuarioByUsuario(final String usuario) {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                Usuario usuario1 = AppStore.getInstance(getApplication())
                        .getUsuarioDAO().fetchByUsuario(usuario);
                usuarioLiveData.postValue(usuario1);
                return null;
            }
        }.execute();

    }

    public void deleteUsuario(final Usuario usuario) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                AppStore.getInstance(getApplication())
                        .getUsuarioDAO()
                        .delete(usuario);
                success.postValue(true);
                return null;
            }
        }.execute();

    }

    public void updateUsuario(final Usuario usuario) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                AppStore.getInstance(getApplication())
                        .getUsuarioDAO()
                        .update(usuario);
                success.postValue(true);
                return null;
            }
        }.execute();

    }

    public void saveUsuario(final Usuario usuario) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                AppStore.getInstance(getApplication())
                        .getUsuarioDAO()
                        .insert(usuario);
                success.postValue(true);
                return null;
            }
        }.execute();

    }

}
