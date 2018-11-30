package br.edu.unidavi.alfonso.android2final.entity;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.List;

import br.edu.unidavi.alfonso.android2final.database.AppStore;

public class AtividadeViewModel extends AndroidViewModel {

    public final MutableLiveData<List<Atividade>> atividades = new MutableLiveData<>();
    public final MutableLiveData<Atividade> atividadeLiveData = new MutableLiveData<>();
    public final MutableLiveData<Boolean> success = new MutableLiveData<>();

    public AtividadeViewModel(@NonNull Application application) {
        super(application);
    }

    public void fetchAtividades(String usuario, boolean status) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                atividades.postValue(
                        AppStore.getInstance(getApplication())
                                .getAtividadeDAO().fetchAtividades(usuario, status) );
                return null;
            }

        }.execute();
    }

    public void fetchByTitulo(String titulo, String usuario, boolean status) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                atividades.postValue(
                        AppStore.getInstance(getApplication())
                                .getAtividadeDAO().fetchByTitulo(titulo, usuario, status) );
                return null;
            }

        }.execute();
    }

    public void findById(final long id, String usuario) {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                Atividade atividade = AppStore.getInstance(getApplication())
                        .getAtividadeDAO().fetchById(id, usuario);
                atividadeLiveData.postValue(atividade);
                return null;
            }
        }.execute();

    }

    public void deleteAtividade(final Atividade atividade) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                AppStore.getInstance(getApplication())
                        .getAtividadeDAO()
                        .delete(atividade);
                success.postValue(true);
                return null;
            }
        }.execute();

    }

    public void updateAtividade(final Atividade atividade) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                AppStore.getInstance(getApplication())
                        .getAtividadeDAO()
                        .update(atividade);
                success.postValue(true);
                return null;
            }
        }.execute();

    }

    public void saveAtividade(final Atividade atividade) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                AppStore.getInstance(getApplication())
                        .getAtividadeDAO()
                        .insert(atividade);
                success.postValue(true);
                return null;
            }
        }.execute();

    }

}
