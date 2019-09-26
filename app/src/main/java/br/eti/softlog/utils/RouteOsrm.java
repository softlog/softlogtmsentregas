package br.eti.softlog.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.List;

import br.eti.softlog.model.Documento;
import br.eti.softlog.softlogtmsentregas.EntregasApp;

public class RouteOsrm {

    Context mContext;
    String url = "http://router.project-osrm.org/route/v1/driving/-49.3049,-16.7501;-49.2337097,-16.6337985?geometries=geojson&steps=true";


    public RouteOsrm(Context context){
        mContext =  context;
    }

    public String getRoute(double latitudeOrigem, double longitudeOrigem,
                                  double latitudeDestino, double longitudeDestino){

        String sLongitudeOrigem = String.valueOf(longitudeOrigem);
        String sLatitudeOrigem = String.valueOf(latitudeOrigem);
        String sLongitudeDestino = String.valueOf(longitudeDestino);
        String sLatitudeDestino = String.valueOf(latitudeDestino);


        String url = "http://router.project-osrm.org/route/v1/driving/"
                + sLongitudeOrigem + ","
                + sLatitudeOrigem + ","
                + sLongitudeDestino + ";"
                + sLatitudeDestino
                + "?geometries=geojson&steps=true";

        //url = "http://api.softlog.eti.br/api/softlog/protocolo/53/2018-02-01/0";
        //url = "http://api.softlog.eti.br/api/softlog/romaneio/81/2017-12-22/16286172840";

        //Log.d("Url",url);

        //Registro do usuario e criacao do banco de dados
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //Log.d("ERRO",error.toString());

                if (error.networkResponse.statusCode==404){
                    //Log.i("Erro","Sem protocolos");
                } else {
                    //Log.i("Erro","Ocorreu um erro");
                }
            }
        });

        AppSingleton.getInstance(EntregasApp.getInstance().getApplicationContext())
                .addToRequestQueue(stringRequest,"Osrm");

        return "";
    }

}
