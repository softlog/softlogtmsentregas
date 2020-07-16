package br.eti.softlog.softlogtmsentregas;

import androidx.fragment.app.FragmentActivity;
import br.eti.softlog.model.Documento;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapasEntrega2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    //private GoogleMap mMap;
    Double latitude;
    Double longitude;
    boolean multi;
    EntregasApp myapp;
    Manager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapas_entrega2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Intent i = getIntent();

        latitude = i.getDoubleExtra("latitude",0);
        longitude = i.getDoubleExtra("longitude",0);

        if (latitude.equals(0.0))
            multi = true;
        else
            multi = false;


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

        // Add a marker in Sydney and move the camera
        if (!multi) {
            LatLng localEntrega = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(localEntrega).title("Local de Entrega"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(localEntrega, 14));
        } else {
            myapp = (EntregasApp)getApplicationContext();
            manager = new Manager(myapp);
            LatLng primeiraEntrega;

            primeiraEntrega = new LatLng(0,0);

            int i = 0;
            List<Documento> documentos = manager.findDocumentoByDataRomaneio(myapp.getDate());

            for (Documento documento : documentos  ){
                LatLng localEntrega;
                if (documento.getDestinatario().getLatitude()!=null){
                    i = i + 1;
                    latitude = Double.valueOf(documento.getDestinatario().getLatitude());
                    longitude = Double.valueOf(documento.getDestinatario().getLongitude());

                    localEntrega = new LatLng(latitude, longitude);

                    if(i==1){
                        primeiraEntrega = new LatLng(latitude,longitude);
                    }

                    mMap.addMarker(new MarkerOptions().position(localEntrega)
                            .title(documento.getDestinatario().getNome()));
                }
            }

            mMap.setMyLocationEnabled(true);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(primeiraEntrega, 10));

            // Check if we were successful in obtaining the map.
            /*
            if (mMap != null) {
                mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location arg0) {
                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.truck);
                        mMap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title("Minha Localização").icon(icon));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(arg0.getLatitude(), arg0.getLongitude()), 6));
                    }
                });


            }

             */

        }

    }
}
