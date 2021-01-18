package br.eti.softlog.softlogtmsentregas;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import br.eti.softlog.Fragments.TimePickerFragment;
import br.eti.softlog.model.Entregas;
import br.eti.softlog.model.Ocorrencia;
import br.eti.softlog.utils.Util;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import im.delight.android.location.SimpleLocation;
import io.blackbox_vision.datetimepickeredittext.view.DatePickerInputEditText;
import io.blackbox_vision.datetimepickeredittext.view.TimePickerInputEditText;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OcorrenciaDataStepFragment extends Fragment implements BlockingStep {


    int hour;
    int minute;

    Long idOcorrencia;
    Long idDocumento;
    boolean capturaImagem;

    Double latitude;
    Double longitude;

    EntregasApp app;
    Manager manager;

    Activity activity;

    @BindView(R.id.txtOcorrencia)
    TextView txtOcorrencia;

    @BindView(R.id.editData)
    DatePickerInputEditText editData;

    @BindView(R.id.editHora)
    TimePickerInputEditText editHora;

    public OcorrenciaDataStepFragment() {
        // Required empty public constructor
    }


    public static OcorrenciaDataStepFragment newInstance(String param1, String param2) {
        OcorrenciaDataStepFragment fragment = new OcorrenciaDataStepFragment();
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
        View view = inflater.inflate(R.layout.fragment_ocorrencia_data_step, container, false);

        ButterKnife.bind(this, view);
        activity = getActivity();


        manager = new Manager(((OcorrenciaStepActivity)activity).myapp);

        Entregas entrega = ((OcorrenciaStepActivity)activity).mViewModel.getEntregas();

        Date dData = new Date();
        String cDataHora;
        cDataHora = Util.getDateFormat(Util.getDateTimeFormatYMD(dData));

        if (entrega.getDataOcorrencia() == null){
            editData.setText(cDataHora.substring(0,10));
            editHora.setText(cDataHora.substring(11,16));
        } else {
            cDataHora = Util.getDateFormat2(entrega.getDataOcorrencia() + " " + entrega.getHoraOcorrencia());

            editData.setText(cDataHora.substring(0, 10));
            editHora.setText(cDataHora.substring(11, 16));
        }

       return view;
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

        String cData;
        String cHora;

        String dataOco;
        String horaOco;
        dataOco = editData.getText().toString();
        horaOco = editHora.getText().toString();

        cData = null;
        if (!dataOco.isEmpty()){
            Date dData;

            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            try {
                dData = formato.parse(dataOco);
                formato.applyPattern("yyyy-MM-dd");
                cData = formato.format(dData);
                ((OcorrenciaStepActivity)activity).mViewModel.getEntregas().setDataOcorrencia(cData);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        if (!horaOco.isEmpty()){
            cHora = horaOco;
            ((OcorrenciaStepActivity)activity).mViewModel.getEntregas().setHoraOcorrencia(cHora);
        } else {
            cHora = null;
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
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}