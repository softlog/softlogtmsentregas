package br.eti.softlog.adapters;

import android.content.Context;
import android.os.Bundle;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import br.eti.softlog.softlogtmsentregas.OcorrenciaCanhotoStepFragment;
import br.eti.softlog.softlogtmsentregas.OcorrenciaDataStepFragment;
import br.eti.softlog.softlogtmsentregas.OcorrenciaGeralStepFragment;
import br.eti.softlog.softlogtmsentregas.OcorrenciaRecebedorStepFragment;
import br.eti.softlog.softlogtmsentregas.OcorrenciaSelecaoStepFragment;

public class OcorrenciaStepAdapter  extends AbstractFragmentStepAdapter {

    public OcorrenciaStepAdapter(@NonNull FragmentManager fm, @NonNull Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {

        Bundle b = new Bundle();

        b.putInt("position_current", position);


        if (position==0){
            final OcorrenciaSelecaoStepFragment stepSelecao;
            stepSelecao = new OcorrenciaSelecaoStepFragment();
            stepSelecao.setArguments(b);
            return stepSelecao;
        }

        if (position==1){
            final OcorrenciaDataStepFragment stepData;
            stepData = new OcorrenciaDataStepFragment();
            stepData.setArguments(b);
            return stepData;
        }

        if (position==2){
            final OcorrenciaRecebedorStepFragment stepRecebedor;
            stepRecebedor = new OcorrenciaRecebedorStepFragment();
            stepRecebedor.setArguments(b);
            return stepRecebedor;

        }

        if (position==3){
            final OcorrenciaCanhotoStepFragment stepCanhoto;
            stepCanhoto = new OcorrenciaCanhotoStepFragment();
            stepCanhoto.setArguments(b);
            return stepCanhoto;

        }

        if (position==4){
            final OcorrenciaDataStepFragment stepData;
            stepData = new OcorrenciaDataStepFragment();
            stepData.setArguments(b);
            return stepData;
        }


        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        //Override this method to set Step title for the Tabs, not necessary for other stepper types

        String title;
        String labelNext;

        switch (position){
            case 0:
                title = "Ocorrência";
                labelNext = "Próximo";
                break;
            case 1:
                title = "Data/Hora";
                labelNext = "Próximo";
                break;
            case 2:
                title = "Recebedor";
                labelNext = "Próximo";
                break;
            case 3:
                title = "Canhotos";
                labelNext = "Fim";
                break;

            case 4:
                title = "Observação";
                labelNext = "Fim";
                break;

            default:
                title = "Próximo";
                labelNext = "Próximo";
        }

        return new StepViewModel.Builder(context)
                .setTitle(title)
                .setEndButtonLabel(labelNext)
                .create();

    }

}
