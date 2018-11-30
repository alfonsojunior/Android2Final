package br.edu.unidavi.alfonso.android2final;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.edu.unidavi.alfonso.android2final.activity.AtividadeListActivity;
import br.edu.unidavi.alfonso.android2final.activity.RegistroActivity;
import br.edu.unidavi.alfonso.android2final.database.AppStore;
import br.edu.unidavi.alfonso.android2final.entity.Sessao;
import br.edu.unidavi.alfonso.android2final.entity.SessaoViewModel;
import br.edu.unidavi.alfonso.android2final.entity.Usuario;
import br.edu.unidavi.alfonso.android2final.entity.UsuarioViewModel;

public class MainActivity extends AppCompatActivity {

    private UsuarioViewModel usuViewModel;
    private SessaoViewModel sesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText edLogin = findViewById(R.id.edLogin);
        EditText edSenha = findViewById(R.id.edSenha);

        sesViewModel = ViewModelProviders.of(this).get(SessaoViewModel.class);
        sesViewModel.success.observe(this, success -> {
            if (success) {
                startActivity(new Intent(getApplicationContext(), AtividadeListActivity.class));
            }
        });

        // Carrega o ViewModel
        usuViewModel = ViewModelProviders.of(this).get(UsuarioViewModel.class);
        usuViewModel.usuarioLiveData.observe(this, usuario -> {
            if (usuario != null) {
                if ( edLogin.getText().toString().equals(usuario.getUsuario())
                        && edSenha.getText().toString().equals(usuario.getSenha()) ) {
                    sesViewModel.saveSessao(new Sessao("USUARIO_LOGADO", usuario.getUsuario()));
                } else {
                    Toast.makeText(MainActivity.this, "Usuário/Senha inválido", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Usuário/Senha inválido", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuViewModel.findUsuarioByUsuario(edLogin.getText().toString());
            }
        });

        Button btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegistroActivity.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppStore.destroyInstance();
    }

}
