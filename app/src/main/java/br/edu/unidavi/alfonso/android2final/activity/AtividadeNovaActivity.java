package br.edu.unidavi.alfonso.android2final.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.edu.unidavi.alfonso.android2final.R;
import br.edu.unidavi.alfonso.android2final.entity.Atividade;
import br.edu.unidavi.alfonso.android2final.entity.AtividadeViewModel;
import br.edu.unidavi.alfonso.android2final.entity.SessaoViewModel;

public class AtividadeNovaActivity extends AppCompatActivity {

    private AtividadeViewModel atvViewModel;
    private SessaoViewModel sesViewModel;
    private String usuario;

    private EditText edRua;
    private EditText edNumero;
    private EditText edBairro;
    private EditText edCidade;
    private EditText edEstado;

    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividade_nova);

        setTitle(R.string.nova_atividade);

        sesViewModel = ViewModelProviders.of(this).get(SessaoViewModel.class);
        sesViewModel.sessaoLiveData.observe(this, sessao -> {
            usuario = sessao.getValor().trim();
        });
        sesViewModel.findByChave("USUARIO_LOGADO");

        // Carrega o ViewModel
        atvViewModel = ViewModelProviders.of(this).get(AtividadeViewModel.class);
        atvViewModel.success.observe(this, success -> {
            if (success) {
                Toast.makeText(AtividadeNovaActivity.this, "Atividade salva com sucesso", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(AtividadeNovaActivity.this, "Erro ao salvar a atividade", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        EditText edTitulo = findViewById(R.id.edTitulo);
        EditText edDescricao = findViewById(R.id.edDescricao);
        edRua = findViewById(R.id.edRua);
        edNumero = findViewById(R.id.edNumero);
        edBairro = findViewById(R.id.edBairro);
        edCidade = findViewById(R.id.edCidade);
        edEstado = findViewById(R.id.edEstado);

        Button btnCancelar = findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btnConfirmar = findViewById(R.id.btnConfirmar);
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edNumero.getText().toString().equals("")) {
                    edNumero.setText("0");
                }
                atvViewModel.saveAtividade(
                        new Atividade(
                                edTitulo.getText().toString(),
                                edDescricao.getText().toString(),
                                new Date(),
                                false,
                                edRua.getText().toString(),
                                Integer.parseInt(edNumero.getText().toString()),
                                edBairro.getText().toString(),
                                edCidade.getText().toString(),
                                edEstado.getText().toString(),
                                usuario
                        )
                );
            }
        });

        if (!hasLocationPermission() || !hasCoarseLocationPermission()) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        1000);
            }

        } else {
            carregarEndereco();
        }
    }

    boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    boolean hasCoarseLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION) || permissions[i].equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        new AlertDialog.Builder(this)
                                .setTitle("Se você permitir a utilização da localização, o sistema poderá carregar se último endereço conhecido nessa tela!")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })
                                .show();
                    } else {
                        carregarEndereco();
                    }
                }
            }
        }
    }

    private void carregarEndereco() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {

                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Geocoder geocoder;
                            List<Address> addresses;
                            geocoder = new Geocoder(AtividadeNovaActivity.this, Locale.getDefault());
                            try {
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                edRua.setText(addresses.get(0).getThoroughfare());
                                if (addresses.get(0).getFeatureName() != null)
                                    if (!addresses.get(0).getFeatureName().equals(""))
                                        edNumero.setText(addresses.get(0).getFeatureName());
                                if (addresses.get(0).getSubLocality() != null)
                                    if (!addresses.get(0).getSubLocality().equals(""))
                                        edBairro.setText(addresses.get(0).getSubLocality());
                                edCidade.setText(addresses.get(0).getLocality());
                                edEstado.setText(addresses.get(0).getAdminArea());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    });

            return;
        }

    }
}
