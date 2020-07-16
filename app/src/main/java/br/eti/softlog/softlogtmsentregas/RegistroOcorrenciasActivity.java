package br.eti.softlog.softlogtmsentregas;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.eti.softlog.Fragments.TimePickerFragment;
import br.eti.softlog.model.Documento;
import br.eti.softlog.model.Ocorrencia;
import br.eti.softlog.model.OcorrenciaDocumento;
import br.eti.softlog.utils.Util;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import im.delight.android.location.SimpleLocation;


public class RegistroOcorrenciasActivity extends AppCompatActivity {

    static final int DATE_DIALOG_ID = 0;
    static final int TIME_DIALOG_ID = 1;


    int hour;
    int minute;

    Long idOcorrencia;
    Long idDocumento;
    boolean capturaImagem;

    Double latitude;
    Double longitude;
    private SimpleLocation location;

    EntregasApp app;
    Manager manager;
    Util util;

    @BindView(R.id.txtOcorrencia)
    TextView txtOcorrencia;

    @BindView(R.id.editData)
    EditText editData;

    @BindView(R.id.editHora)
    EditText editHora;

    @BindView(R.id.editNomeRecebedor)
    EditText editNomeRecebedor;

    @BindView(R.id.editNumeroDocumento)
    EditText editNumeroDocumento;

    @BindView(R.id.editObservacao)
    EditText editObservacao;

    @OnClick(R.id.imgBtnDate) void btnDataClick(){
        showDialog(DATE_DIALOG_ID);
    }

    @OnClick(R.id.imgBtnHora) void btnHoraClick(){
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(),"TimePicker");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_ocorrencias);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Registro Entrega");

        app = (EntregasApp)getApplicationContext();
        util = new Util();

        manager = new Manager(app);

        // construct a new instance of SimpleLocation
        location = new SimpleLocation(this);
        // if we can't access the location yet
        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(this);
        }

        Intent inCall = getIntent();
        idOcorrencia = inCall.getLongExtra("id_ocorrencia",1);
        idDocumento = inCall.getLongExtra("id_documento",0);


        Ocorrencia oco = manager.findOcorrenciaById(idOcorrencia);

        txtOcorrencia.setText(oco.getOcorrencia().toString());

        Date dataAtual = new Date();
        editData.setText(Util.getDateFormatDMY(Util.getDateFormatYMD(dataAtual)));
        editData.setEnabled(false);

        String hora = new SimpleDateFormat("HH:mm").format(dataAtual);
        editHora.setText(hora);
        editHora.setEnabled(false);

        editObservacao.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editObservacao.setRawInputType(InputType.TYPE_CLASS_TEXT);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ocorrencia, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        final String dataOcorrenciaTemp = editData.getText().toString();

        final String dataOcorrencia;
        dataOcorrencia = dataOcorrenciaTemp.substring(6) + "-" +
                dataOcorrenciaTemp.substring(3,5) + "-" +
                dataOcorrenciaTemp.substring(0,2);

        final String horaOcorrencia = editHora.getText().toString();
        final String nomeRecebedor = editNomeRecebedor.getText().toString();
        final String docRecebedor = editNumeroDocumento.getText().toString();
        final String observacao = editObservacao.getText().toString();

        try{
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            location.endUpdates();
        } catch (Error e) {

        }

        if (id == R.id.menu_continuar) {



            if (idOcorrencia == 1 && app.getConfigCanhotoObrigatorio()) {
                Intent i = new Intent(getApplicationContext(), MainActivityCrop.class);


                i.putExtra("id_ocorrencia", Long.valueOf(1));
                i.putExtra("id_documento", idDocumento);
                i.putExtra("data_ocorrencia", dataOcorrencia);
                i.putExtra("hora_ocorrencia", horaOcorrencia);
                i.putExtra("nome_recebedor", nomeRecebedor);
                i.putExtra("doc_recebedor", docRecebedor);
                i.putExtra("observacao", observacao);
                i.putExtra("latitude",latitude);
                i.putExtra("longitude",longitude);

                startActivity(i);
                finish();

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("Confirmar");
                builder.setMessage("Deseja anexar alguma imagem?");

                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplicationContext(), MainActivityCrop.class);


                        i.putExtra("id_ocorrencia", idOcorrencia);
                        i.putExtra("id_documento", idDocumento);
                        i.putExtra("data_ocorrencia", dataOcorrencia);
                        i.putExtra("hora_ocorrencia", horaOcorrencia);
                        i.putExtra("nome_recebedor", nomeRecebedor);
                        i.putExtra("doc_recebedor", docRecebedor);
                        i.putExtra("observacao", observacao);
                        i.putExtra("latitude",latitude);
                        i.putExtra("longitude",longitude);

                        startActivity(i);
                        dialog.dismiss();
                        finish();

                        // Do nothing but close the dialog
                    }
                });

                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.

                        }



                        EntregasApp app = (EntregasApp)getApplicationContext();
                        Manager manager = new Manager(app);

                        Documento documento = manager.findDocumentoById(idDocumento);

                        documento.setIdOcorrencia(idOcorrencia);
                        documento.setDataOcorrencia(dataOcorrencia + " " + horaOcorrencia);
                        app.getDaoSession().update(documento);

                        OcorrenciaDocumento oco = manager.addOcorrenciaDocumento(documento.getId(),idOcorrencia,dataOcorrencia,
                                horaOcorrencia,null,docRecebedor,nomeRecebedor,observacao,
                                String.valueOf(latitude),String.valueOf(longitude));


                        Toast.makeText(getApplicationContext(),"Ocorrência de Entrega gravada com sucesso!",Toast.LENGTH_LONG).show();

                        Intent mainIntent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(mainIntent);
                        finish();
                        // Do nothing
                        dialog.dismiss();



                    }
                });

                AlertDialog alert = builder.create();
                alert.show();


            }

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        Calendar calendario = Calendar.getInstance();

        int ano = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, ano, mes,
                        dia);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {

                    Date data_corrente = Util.getDate(year,monthOfYear,dayOfMonth);

                    editData.setText(Util.getDateFormatDMY(Util.getDateFormatYMD(data_corrente)));

                }
    };

    @Override
    public boolean onSupportNavigateUp() {
        Intent i = new Intent(getApplicationContext(), DocumentoActivity.class);

        i.putExtra("id_documento", String.valueOf(idDocumento));

        startActivity(i);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        onSupportNavigateUp();
    }

}
