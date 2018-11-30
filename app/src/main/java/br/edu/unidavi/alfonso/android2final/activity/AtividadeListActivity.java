package br.edu.unidavi.alfonso.android2final.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import br.edu.unidavi.alfonso.android2final.R;
import br.edu.unidavi.alfonso.android2final.entity.AtividadeViewModel;
import br.edu.unidavi.alfonso.android2final.entity.SessaoViewModel;

public class AtividadeListActivity extends AppCompatActivity {

    private AtividadeViewModel atvViewModel;
    private RecyclerView atividadeList = null;
    private FloatingActionButton btnNovo = null;

    private SessaoViewModel sesViewModel;
    private String usuario;

    private AtividadeAdapter adapter = new AtividadeAdapter(atividade -> {
        Intent intent = new Intent(getApplicationContext(), AtividadeDetalheActivity.class);
        intent.putExtra("id", atividade.getId());
        startActivity(intent);
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividade_list);

        sesViewModel = ViewModelProviders.of(this).get(SessaoViewModel.class);
        sesViewModel.sessaoLiveData.observe(this, sessao -> {
            this.usuario = sessao.getValor().trim();
            atvViewModel.fetchAtividades(usuario, false);
        });
        sesViewModel.findByChave("USUARIO_LOGADO");

        // Carrega o ViewModel
        atvViewModel = ViewModelProviders.of(this).get(AtividadeViewModel.class);
        atvViewModel.atividades.observe(this, atividades -> {
            if (atividades != null) {
                adapter.setup(atividades);
            }
        });

        atividadeList = findViewById(R.id.atividade_list);
        atividadeList.setLayoutManager(new LinearLayoutManager(this));
        atividadeList.setAdapter(adapter);

        btnNovo = findViewById(R.id.btnNovo);
        btnNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AtividadeNovaActivity.class));
            }
        });

//        LatLng ll = getLocation();
//        Toast.makeText(this, ll.toString(), Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        atvViewModel.fetchAtividades(usuario, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        //Carrega o arquivo de search_view_menu.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_view_menu, menu);

        //Pega o Componente.
        final SearchView mSearchView = (SearchView) menu.findItem(R.id.edSearch)
                .getActionView();
        //Define um texto de ajuda:
        mSearchView.setQueryHint("Pesquisa...");

        // exemplos de utilização:
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //mSearchView.clearFocus();
                atvViewModel.fetchByTitulo("%" + query + "%", usuario, false);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

//    public LatLng getLocation() {
//        Geocoder gc = new Geocoder(AtividadeListActivity.this);
//        List<Address> addressList;
//        LatLng ll = null;
//        try {
//            addressList = gc.getFromLocationName("Rua das Indústrias, 50 - Fundo Canoas, Rio do Sul, Santa Catarina, Brasil", 1);
//            ll = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return ll;
//    }
}
