package br.eti.softlog.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import br.eti.softlog.model.Documento;
import br.eti.softlog.model.DocumentoCheck;
import br.eti.softlog.model.Entregas;
import br.eti.softlog.model.Ocorrencia;
import br.eti.softlog.model.OcorrenciaDao;
import br.eti.softlog.model.OcorrenciaDocumento;
import br.eti.softlog.softlogtmsentregas.EntregasApp;
import br.eti.softlog.softlogtmsentregas.Manager;

public class DocumentoEntregaViewModel extends AndroidViewModel {

    List<Documento> documentos;
    List<Ocorrencia> ocorrencias;
    Entregas entrega;
    Manager manager;
    EntregasApp app;
    boolean emAberto;

    public DocumentoEntregaViewModel(@NonNull Application application) {

        super(application);
        app = (EntregasApp) application;
        manager = new Manager(app);
        ocorrencias = app.getDaoSession().getOcorrenciaDao()
                .queryBuilder()
                .where(OcorrenciaDao.Properties.Ativo.eq(true))
                .orderAsc(OcorrenciaDao.Properties.Ocorrencia)
                .list();

        emAberto = false;

    }

    public void iniciarOcorrencia(String destinatarioId){

        if (!emAberto){
            this.entrega = manager.findEntregaByDataDestinatario(app.getDate(),destinatarioId);
            app.getDb().beginTransaction();

            for (Documento doc:this.entrega.getDocumentos()){
                OcorrenciaDocumento oco = new OcorrenciaDocumento();
                oco.setEntregaId(this.entrega.getId());
                oco.setDocumentoId(doc.getId());
                app.getDaoSession().insert(oco);
            }
            emAberto = true;
        }

    }

    public void finalizarOcorrencia(){
        try {
            app.getDb().setTransactionSuccessful();
        } finally {
            app.getDb().endTransaction();
        }
        emAberto = false;
    }

    public void cancelarOcorrencia(){
        app.getDb().endTransaction();
        emAberto = false;
    }

    public Entregas getEntregas(){
        return this.entrega;
    }

    public List<Documento> getDocumentos() {
        return  entrega.getDocumentos();
    }



    public List<Ocorrencia> getOcorrencias(){
        return this.ocorrencias;
    }

}
