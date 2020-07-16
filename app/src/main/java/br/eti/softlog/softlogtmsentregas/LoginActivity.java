package br.eti.softlog.softlogtmsentregas;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport;
import com.google.firebase.crashlytics.FirebaseCrashlytics;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import br.eti.softlog.model.Usuario;
import br.eti.softlog.utils.AppSingleton;
import br.eti.softlog.utils.MaskEditUtil;
import br.eti.softlog.utils.Util;


public class LoginActivity extends AppCompatActivity {

    EditText editCodigoAcesso;
    EditText editUsuario;
    EditText editSenha;
    TextView txtVersao;
    Button btnLogin;
    EntregasApp myapp;
    Manager manager;
    Usuario usuario;
    FirebaseCrashlytics crashlytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        editCodigoAcesso = (EditText) findViewById(R.id.editCodigoAcesso);
        editUsuario = (EditText) findViewById(R.id.editUsuario);
        editUsuario.addTextChangedListener(MaskEditUtil.mask(editUsuario, MaskEditUtil.FORMAT_CPF));
        editSenha = (EditText) findViewById(R.id.editSenha);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtVersao = findViewById(R.id.txt_versao);

        myapp = (EntregasApp) getApplicationContext();
        manager = new Manager(myapp);

        crashlytics = FirebaseCrashlytics.getInstance();

        try {
            String versionName = getPackageManager()
                    .getPackageInfo(getPackageName(), 0).versionName;

            txtVersao.setText("Versão: " + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        //Validacao do Usuario
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editCodigoAcesso.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(),
                            R.string.valid_codigo_acesso,
                            Toast.LENGTH_LONG).show();
                } else if (editUsuario.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(),
                            R.string.valid_usuario,
                            Toast.LENGTH_LONG).show();
                } else if (editSenha.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(),
                            R.string.valid_senha,
                            Toast.LENGTH_LONG).show();
                } else {

                    final String lc_usuario;
                    final String lc_senha;
                    final String lc_codigo_acesso;
                    final String lc_uuid;

                    lc_usuario = editUsuario.getText().toString()
                            .replace(".", "")
                            .replace("-", "");

                    lc_senha = editSenha.getText().toString();
                    lc_codigo_acesso = editCodigoAcesso.getText().toString();

                    final String nome_bd = lc_usuario + "_" + lc_codigo_acesso + ".db";

                    //Verifica se tem banco de dados para o usuario e codigo de acesso
                    boolean acessa_api = true;
                    //if (myapp.doesDatabaseExist(getApplicationContext(), nome_bd)) {
                      //  myapp.setBD(nome_bd);
                    //}
                    myapp.setBD(nome_bd);
                    usuario = manager.findUsuarioByLogin(lc_usuario);
                    if (usuario != null) {
                        if (usuario.getUuid() != null) {
                            if (usuario.getUuid().isEmpty()){
                                usuario.setUuid(myapp.getUUID());
                                myapp.getDaoSession().update(usuario);
                                lc_uuid = usuario.getUuid();
                            } else {
                                lc_uuid = usuario.getUuid();
                            }
                        } else {
                            usuario.setUuid(myapp.getUUID());
                            myapp.getDaoSession().update(usuario);
                            lc_uuid = usuario.getUuid();
                        }
                    } else {
                        lc_uuid = myapp.getUUID();
                    }

                    if (acessa_api) {
                        //Se nao tiver, acessa api para verificar se existe usuario registrado
                        // Request a string response from the provided URL.

                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                        final String url = "http://api.softlog.eti.br/api/softlog/usuario2";


                        //Registro do usuario e criacao do banco de dados
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        //Log
                                        if (usuario == null) {
                                            try {
                                                JSONObject jObj = new JSONObject(response);

                                                Long id_usuario = jObj.getLong("id_usuario");
                                                String cpf = jObj.getString("cnpj_cpf");
                                                String login = jObj.getString("login_name");
                                                String nome = jObj.getString("nome_usuario");
                                                String email = jObj.getString("email");

                                                //Log.d("Senha Informada",lc_senha.trim());
                                                //Log.d("Senha",senha.trim());
                                                myapp.setBD(nome_bd);

                                                final Manager manager = new Manager(myapp);

                                                int ln_codigo_acesso = Integer.parseInt(lc_codigo_acesso);

                                                //Insere na tabela de usuarios do aplicativo.
                                                usuario = new Usuario(id_usuario, nome, cpf,
                                                        login, "", email, null, null,
                                                        ln_codigo_acesso, null, true,
                                                        true,
                                                        lc_uuid);
                                                try {
                                                    myapp.getDaoSession().getUsuarioDao()
                                                            .insert(usuario);
                                                } catch (Exception e) {
                                                    return;
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        //Seta dados globais na application
                                        myapp.setUsuario(usuario);
                                        myapp.setConfigLogin(lc_usuario);
                                        myapp.setConfigDb(nome_bd);
                                        myapp.setConfigStatus(true);

                                        Intent intent = new Intent(LoginActivity.this,
                                                MainActivity.class);

                                        startActivity(intent);

                                        finish();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {



                                        /*

                                        FirebaseCrash.log("Login" + lc_usuario);
                                        FirebaseCrash.log("UUID " +lc_uuid);
                                        FirebaseCrash.log("Codigo Acesso " + lc_codigo_acesso);
                                        FirebaseCrash.report(new Exception(error.getMessage()));

                                         */

                                        //Log.d("ERRO",error.toString());
                                        if (error.networkResponse == null) {

                                                Toast.makeText(getApplicationContext(),
                                                    "Favor verificar sua conexão.",
                                                    Toast.LENGTH_LONG).show();

                                            Toast.makeText(getApplicationContext(),
                                                    error.getMessage(),
                                                    Toast.LENGTH_LONG).show();

                                            Util.appendLog("Login",error.getMessage(),myapp.getFileLog());

                                        } else {
                                            if (error.networkResponse.statusCode == 404) {

                                                Toast.makeText(getApplicationContext(),
                                                        "Usuário Inexistente ou Senha Incorreta.",
                                                        Toast.LENGTH_LONG).show();

                                            } else {

                                                Toast.makeText(getApplicationContext(),
                                                        R.string.erro_acesso,
                                                        Toast.LENGTH_LONG).show();

                                                Util.appendLog("Login",error.getMessage(),myapp.getFileLog());

                                            }
                                        }
                                        crashlytics.setCustomKey("Login", lc_usuario);
                                        crashlytics.setCustomKey("UUID", lc_uuid);
                                        crashlytics.setCustomKey("Codigo Acesso", lc_codigo_acesso);
                                        crashlytics.setCustomKey("Erro Login", error.getMessage());

                                        /*
                                        crashlytics.log(error.getMessage());

                                        crashlytics.setCrashlyticsCollectionEnabled(true);
                                        try{
                                            throw new RuntimeException(error.getMessage());
                                        } catch (Exception e){
                                            FirebaseCrashlytics.getInstance().recordException(e);
                                        }

                                         */
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() throws
                                    AuthFailureError {
                                Map<String, String> parameters = new HashMap<String, String>();
                                parameters.put("login", lc_usuario);
                                parameters.put("uuid", lc_uuid);
                                parameters.put("senha", lc_senha);
                                parameters.put("codigo_acesso", lc_codigo_acesso);

                                return parameters;
                            }
                        };

                        AppSingleton.getInstance(
                                getApplicationContext())
                                .addToRequestQueue(stringRequest, "Login");
                        //Log.d("Log","Processo Concluido!");
                    }
                }
            }
        });
    }

}
