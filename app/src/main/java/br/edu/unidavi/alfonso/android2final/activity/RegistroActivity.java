package br.edu.unidavi.alfonso.android2final.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.edu.unidavi.alfonso.android2final.R;
import br.edu.unidavi.alfonso.android2final.entity.AtividadeViewModel;
import br.edu.unidavi.alfonso.android2final.entity.Usuario;
import br.edu.unidavi.alfonso.android2final.entity.UsuarioViewModel;

public class RegistroActivity extends AppCompatActivity {

    private UsuarioViewModel usuViewModel;
    private Usuario usuario;

    EditText edUsuario;
    EditText edSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        this.setTitle(R.string.registro_usuario);

        // Carrega o ViewModel
        usuViewModel = ViewModelProviders.of(this).get(UsuarioViewModel.class);
        usuViewModel.success.observe(this, success -> {
            if (Boolean.TRUE.equals(success)) {
                finish();
            } else {
                Toast.makeText(RegistroActivity.this, "Erro ao salvar o usuário", Toast.LENGTH_SHORT).show();
            }
        });

        edUsuario = findViewById(R.id.edUsuario);
        edSenha = findViewById(R.id.edSenha);

        Button btnConfirmar = findViewById(R.id.btnConfirmar);
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuViewModel.saveUsuario(new Usuario(edUsuario.getText().toString(), edSenha.getText().toString()));
            }
        });

        Button btnCancelar = findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
