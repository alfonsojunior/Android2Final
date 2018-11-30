package br.edu.unidavi.alfonso.android2final.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import br.edu.unidavi.alfonso.android2final.R;
import br.edu.unidavi.alfonso.android2final.entity.AtividadeViewModel;
import br.edu.unidavi.alfonso.android2final.entity.SessaoViewModel;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double lat = 0.0;
    private double lng = 0.0;
    private long id = 0;
    private String marcador = "";
    private String endereco = "";

    private AtividadeViewModel atvViewModel;
    private SessaoViewModel sesViewModel;
    private String usuario = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        id = getIntent().getLongExtra("id", 0);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        sesViewModel = ViewModelProviders.of(this).get(SessaoViewModel.class);
        sesViewModel.sessaoLiveData.observe(this, sessao -> {
            if (sessao != null) {
                usuario = sessao.getValor().trim();
                atvViewModel.findById(id, usuario);
            } else {
                Toast.makeText(this, "Sessão inválida", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        sesViewModel.findByChave("USUARIO_LOGADO");

        atvViewModel = ViewModelProviders.of(this).get(AtividadeViewModel.class);
        atvViewModel.atividadeLiveData.observe(this, atividade -> {
            if (atividade != null) {
                marcador = atividade.getTitulo();

                endereco = atividade.getRua();
                if (atividade.getNumero() > 0) {
                    endereco += ", " + String.valueOf(atividade.getNumero());
                }
                if (!atividade.getBairro().equals("")) {
                    endereco += " - " + atividade.getBairro();
                }
                if (!atividade.getCidade().equals("")) {
                    endereco += ", " + atividade.getCidade();
                }
                if (!atividade.getEstado().equals("")) {
                    endereco += ", " + atividade.getEstado();
                }
                endereco += ", Brasil";

                LatLng ll = getLocation();
                lat = ll.latitude;
                lng = ll.longitude;
                addMarker();

            } else {
                finish();
            }
        });

        if (!hasLocationPermission()) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1000);
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Não posso abrir o mapa sem essa permissão!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }

        }

    }

    boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public LatLng getLocation() {
        Geocoder gc = new Geocoder(this);
        List<Address> addressList;
        LatLng ll = null;
        try {
            addressList = gc.getFromLocationName(endereco, 1);
            ll = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ll;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        new AlertDialog.Builder(this)
                                .setTitle("Não posso abrir o mapa sem essa permissão!")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })
                                .show();
                    } else {
                        LatLng ll = getLocation();
                        lat = ll.latitude;
                        lng = ll.longitude;
                        addMarker();
                    }
                }
            }
        }
    }

    private void addMarker() {
        LatLng ponto = new LatLng(lat,lng);
        MarkerOptions marker = new MarkerOptions()
                .draggable(true)
                .position(ponto)
                .icon(BitmapDescriptorFactory.defaultMarker())
                .title(marcador);
        mMap.addMarker(marker);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ponto));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ponto,17f));
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}
