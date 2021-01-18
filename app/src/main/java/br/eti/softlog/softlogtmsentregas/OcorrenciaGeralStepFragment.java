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

import java.text.SimpleDateFormat;
import java.util.Date;

public class OcorrenciaGeralStepFragment extends Fragment implements BlockingStep {

    static final int DATE_DIALOG_ID = 0;
    static final int TIME_DIALOG_ID = 1;


    int hour;
    int minute;

    Long idOcorrencia;
    Long idDocumento;
    boolean capturaImagem;

    Double latitude;
    Double longitude;


    EntregasApp app;
    Manager manager;
    Util util;
    Activity activity;

    @BindView(R.id.txtOcorrencia)
    TextView txtOcorrencia;

    @BindView(R.id.editData)
    DatePickerInputEditText editData;

    @BindView(R.id.editHora)
    TimePickerInputEditText editHora;

    @BindView(R.id.editNomeRecebedor)
    AppCompatEditText editNomeRecebedor;

    @BindView(R.id.editNumeroDocumento)
    AppCompatEditText editNumeroDocumento;

    @BindView(R.id.editObservacao)
    EditText editObservacao;

    public OcorrenciaGeralStepFragment() {
        // Required empty public constructor
    }


    public static OcorrenciaGeralStepFragment newInstance(String param1, String param2) {
        OcorrenciaGeralStepFragment fragment = new OcorrenciaGeralStepFragment();
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
        View view = inflater.inflate(R.layout.fragment_ocorrencia_geral_step, container, false);

        ButterKnife.bind(view);

        activity = getActivity();

        util = new Util();
        manager = new Manager(((OcorrenciaStepActivity)activity).myapp);

        //Intent inCall = getIntent();
        //idOcorrencia = inCall.getLongExtra("id_ocorrencia",1);
        //idDocumento = inCall.getLongExtra("id_documento",0);

        /*
        Ocorrencia oco = manager.findOcorrenciaById(((OcorrenciaStepActivity)activity).idOcorrenciaPadrao);

        txtOcorrencia.setText(oco.getOcorrencia().toString());



        Date dataAtual = new Date();
        editData.setText(Util.getDateFormatDMY(Util.getDateFormatYMD(dataAtual)));
        editData.setEnabled(false);

        String hora = new SimpleDateFormat("HH:mm").format(dataAtual);
        editHora.setText(hora);
        editHora.setEnabled(false);

        editObservacao.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editObservacao.setRawInputType(InputType.TYPE_CLASS_TEXT);

        */


        return view;
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
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