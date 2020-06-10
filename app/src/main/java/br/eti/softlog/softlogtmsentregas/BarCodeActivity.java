package br.eti.softlog.softlogtmsentregas;

import androidx.appcompat.app.AppCompatActivity;
import br.eti.softlog.model.Documento;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

public class BarCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    EntregasApp app;
    Manager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);
        List<BarcodeFormat> list = new ArrayList<BarcodeFormat>();
        list.add(BarcodeFormat.CODE_128);
        mScannerView.setFormats(list);
        mScannerView.setAutoFocus(true);

        app = (EntregasApp)getApplicationContext();
        manager = new Manager(app);
    }

    @Override
    public void handleResult(Result rawResult) {
        String chaveNfe = rawResult.getText();
        if (chaveNfe.length()==44){
            Documento documento = manager.findDocumentoByChaveNFeAberta(chaveNfe);
            if (documento != null){
                Intent i = new Intent(getApplicationContext(),DocumentoActivity.class);
                i.putExtra("id_documento",documento.getId().toString());
                startActivity(i);
                finish();
            }

        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

}
