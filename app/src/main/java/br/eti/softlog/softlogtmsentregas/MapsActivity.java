package br.eti.softlog.softlogtmsentregas;


import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

public class MapsActivity extends FragmentActivity {

    //private GoogleMap mMap;
    Double latitude;
    Double longitude;
    boolean multi;
    EntregasApp myapp;
    Manager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        /*
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

         */

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
    /*
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

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(primeiraEntrega, 14));
        }

    }

     */
}
