package com.appestado.countandsave;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by bginer on 17/02/2016.
 */
public class Tool_FechaHora {

    private static final String formatoFechaAMD = "yyyy-MM-dd";
    private static final String formatoFechaDMA = "dd-MM-yyyy";
    private static final String formatoHora = "HH:mm:ss";

    public static String getFechaDMA(){
        return new SimpleDateFormat(formatoFechaDMA).format(Calendar.getInstance().getTime());
    }

    public static String getFechaDMA(String fechaAMD){
        return new SimpleDateFormat(formatoFechaDMA).format(fechaAMD);
    }

    public static String getFechaAMD(){
        return new SimpleDateFormat(formatoFechaAMD).format(Calendar.getInstance().getTime());
    }

    public static String getFechaAMD(String fechaDMA){
        return new SimpleDateFormat(formatoFechaAMD).format(fechaDMA);
    }

    public static String getHora(){
        return new SimpleDateFormat(formatoHora).format(Calendar.getInstance().getTime());
    }

    public static String formateaDia(int dia){
        if(dia>9){
            return ""+dia;
        }else{
            return "0"+dia;
        }
    }

    public static String formateaMes(int mes){
        if(mes>9){
            return ""+mes;
        }else{
            return "0"+mes;
        }
    }

}

