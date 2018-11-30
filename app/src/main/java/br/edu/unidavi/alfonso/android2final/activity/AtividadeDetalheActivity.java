package br.edu.unidavi.alfonso.android2final.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import br.edu.unidavi.alfonso.android2final.R;
import br.edu.unidavi.alfonso.android2final.entity.Atividade;
import br.edu.unidavi.alfonso.android2final.entity.AtividadeViewModel;
import br.edu.unidavi.alfonso.android2final.entity.SessaoViewModel;

public class AtividadeDetalheActivity extends AppCompatActivity {

    private AtividadeViewModel atvViewModel;
    private SessaoViewModel sesViewModel;
    private String usuario = "";
    private long id = 0L;

    private Atividade atividade = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividade_detalhe);

        id = getIntent().getLongExtra("id", 0);

        TextView lblTitulo = findViewById(R.id.lblTitulo);
        TextView lblDescricao = findViewById(R.id.lblDescricao);
        TextView lblRua = findViewById(R.id.lblRua);
        TextView lblNumero = findViewById(R.id.lblNumero);
        TextView lblBairro= findViewById(R.id.lblBairro);
        TextView lblCidade = findViewById(R.id.lblCidade);
        TextView lblEstado = findViewById(R.id.lblEstado);

        sesViewModel = ViewModelProviders.of(this).get(SessaoViewModel.class);
        sesViewModel.sessaoLiveData.observe(this, sessao -> {
            if (sessao != null) {
                usuario = sessao.getValor().trim();
                atvViewModel.findById(id, usuario);
            } else {
                Toast.makeText(AtividadeDetalheActivity.this, "Sessão inválida", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        sesViewModel.findByChave("USUARIO_LOGADO");

        atvViewModel = ViewModelProviders.of(this).get(AtividadeViewModel.class);
        atvViewModel.atividadeLiveData.observe(this, atividade -> {
            if (atividade != null ) {
                this.atividade = atividade;
                setTitle(atividade.getTitulo());
                lblTitulo.setText(atividade.getTitulo());
                lblDescricao.setText(atividade.getDescricao());
                lblRua.setText(atividade.getRua());
                lblNumero.setText(String.valueOf(atividade.getNumero()));
                lblBairro.setText(atividade.getBairro());
                lblCidade.setText(atividade.getCidade());
                lblEstado.setText(atividade.getEstado());
            } else {
                Toast.makeText(AtividadeDetalheActivity.this, "Erro ao carregar os dados da atividade", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        atvViewModel.success.observe(this, success -> {
            if (success) {
                Toast.makeText(AtividadeDetalheActivity.this, "Atividade marcada como concluída", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(AtividadeDetalheActivity.this, "Erro ao gravar os dados da atividade", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        Button btnCancelar = findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btnMapa = findViewById(R.id.btnMapa);
        btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        //Carrega o arquivo de search_view_menu.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detalhe_actionbar_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_finalizar) {
            atividade.setStatus(true);
            atvViewModel.updateAtividade(atividade);
        }
        return super.onOptionsItemSelected(item);
    }

}
