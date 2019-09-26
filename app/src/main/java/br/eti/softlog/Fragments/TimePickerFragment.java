package br.eti.softlog.Fragments;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.TextView;
import android.app.DialogFragment;
import android.app.Dialog;
import java.util.Calendar;
import android.widget.TimePicker;

import br.eti.softlog.softlogtmsentregas.R;
/**
 * A simple {@link Fragment} subclass.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current time as the default values for the time picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        //Create and return a new instance of TimePickerDialog
        return new TimePickerDialog(getActivity(),this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    //onTimeSet() callback method
    public void onTimeSet(TimePicker view, int hourOfDay, int minute){

        //Do something with the user chosen time
        //Get reference of host activity (XML Layout File) TextView widget
        EditText et = (EditText) getActivity().findViewById(R.id.editHora);

        //Display the user changed time on TextView
        String cHora;
        String cMinuto;

        if (hourOfDay<10)
            cHora = '0'+ String.valueOf(hourOfDay);
        else
            cHora = String.valueOf(hourOfDay);

        if (minute<10)
            cMinuto = '0' + String.valueOf(minute);
        else
            cMinuto = String.valueOf(minute);

        et.setText(cHora + ":" + cMinuto);
    }
}