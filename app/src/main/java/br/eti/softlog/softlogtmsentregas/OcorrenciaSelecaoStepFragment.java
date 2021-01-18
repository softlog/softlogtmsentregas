package br.eti.softlog.softlogtmsentregas;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import br.eti.softlog.model.Entregas;
import br.eti.softlog.model.Ocorrencia;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OcorrenciaSelecaoStepFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OcorrenciaSelecaoStepFragment extends Fragment implements BlockingStep {


    int position;
    Activity activity;
    String destinarioCnpj;
    List<Ocorrencia> ocorrencias;

    @BindView(R.id.spOcorrencia)
    SmartMaterialSpinner spinner;


    public static OcorrenciaSelecaoStepFragment newInstance(String param1, String param2) {
        OcorrenciaSelecaoStepFragment fragment = new OcorrenciaSelecaoStepFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment

        View view;
        view = inflater.inflate(R.layout.fragment_ocorrencia_selecao_step, container, false);
        ButterKnife.bind(this,view);

        activity = getActivity();
        Bundle b = this.getArguments();
        position = b.getInt("position_current",0);
        ocorrencias = ((OcorrenciaStepActivity)activity).mViewModel.getOcorrencias();
        initSpinnerOcorrencias();
        destinarioCnpj = ((OcorrenciaStepActivity)activity).destinatarioCnpj;

        return view;


    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

        if (spinner.getSelectedItemPosition()>-1){

            Ocorrencia ocorrencia = ocorrencias.get(spinner.getSelectedItemPosition());

            Entregas entrega = ((OcorrenciaStepActivity)activity).mViewModel
                    .getEntregas();

            entrega.setIdOcorrenciaPadrao(ocorrencia.getId());

        }

        callback.goToNextStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        if (spinner.getSelectedItemPosition() == -1)
            return new VerificationError("Informe uma ocorrência!");
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {
        Toast.makeText(((OcorrenciaStepActivity)activity).getApplicationContext(),error.getErrorMessage(),Toast.LENGTH_LONG).show();
    }

    private void initSpinnerOcorrencias() {



        Long idOcorrenciaPadrao = ((OcorrenciaStepActivity)activity).mViewModel
                .getEntregas()
                .getIdOcorrenciaPadrao();

        int indOcorrencia;
        indOcorrencia = -1;

        ((OcorrenciaStepActivity)activity).ocorrenciasList = new ArrayList<>();
        int i;
        i = 0;
        for (Ocorrencia ocorrencia:ocorrencias) {
            ((OcorrenciaStepActivity)activity).ocorrenciasList.add(ocorrencia.getOcorrencia().trim());
            if (ocorrencia.getId() == idOcorrenciaPadrao){
                indOcorrencia = i;
            }
            i++;
        }
        spinner.setItem(((OcorrenciaStepActivity)activity).ocorrenciasList);
        spinner.setSelection(indOcorrencia);
    }

}