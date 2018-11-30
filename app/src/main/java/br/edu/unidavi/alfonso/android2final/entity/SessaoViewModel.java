package br.edu.unidavi.alfonso.android2final.entity;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.List;

import br.edu.unidavi.alfonso.android2final.database.AppStore;

public class SessaoViewModel extends AndroidViewModel {

    public final MutableLiveData<List<Sessao>> sessoes = new MutableLiveData<>();
    public final MutableLiveData<Sessao> sessaoLiveData = new MutableLiveData<>();
    public final MutableLiveData<Boolean> success = new MutableLiveData<>();

    public SessaoViewModel(@NonNull Application application) {
        super(application);
    }

    public void fetchSessoes() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                List<Sessao> ses = AppStore.getInstance(getApplication())
                        .getSessaoDAO().fetchSessoes();
                sessoes.postValue(ses);
                return null;
            }

        }.execute();
    }

    public void findByChave(final String chave) {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                Sessao sessao = AppStore.getInstance(getApplication())
                        .getSessaoDAO().fetchByChave(chave);
                sessaoLiveData.postValue(sessao);
                return null;
            }
        }.execute();

    }

    public void deleteSessao(final Sessao sessao) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                AppStore.getInstance(getApplication())
                        .getSessaoDAO()
                        .delete(sessao);
                success.postValue(true);
                return null;
            }
        }.execute();

    }

    public void updateSessao(final Sessao sessao) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                AppStore.getInstance(getApplication())
                        .getSessaoDAO()
                        .update(sessao);
                success.postValue(true);
                return null;
            }
        }.execute();

    }

    public void saveSessao(final Sessao sessao) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                AppStore.getInstance(getApplication())
                        .getSessaoDAO()
                        .insert(sessao);
                success.postValue(true);
                return null;
            }
        }.execute();

    }

}
