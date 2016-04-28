package com.appestado.countandsave;

/**
 * Created by Borja on 09/08/2015.
 */

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DatePickerFragment extends DialogFragment {
    OnDateSetListener ondateSet;
    public int year, month, day;


    public DatePickerFragment() {
    }

    public void setCallBack(DatePickerDialog.OnDateSetListener ondate) {
        ondateSet = ondate;
    }


    @Override
    public void setArguments(Bundle args) {
        //super.setArguments(args);
        year = args.getInt("year");
        month = args.getInt("month");
        day = args.getInt("day");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String fecha = Tool_FechaHora.getFechaDMA();
        String f = fecha.substring(1,2);
        int day = Integer.parseInt(fecha.substring(0, 2));
        int month = Integer.parseInt(fecha.substring(3, 5));
        int year = Integer.parseInt(fecha.substring(6));

        return new DatePickerDialog(getActivity(), ondateSet, year, month-1, day);
    }
}