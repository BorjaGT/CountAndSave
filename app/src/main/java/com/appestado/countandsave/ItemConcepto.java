package com.appestado.countandsave;

/**
 * Created by bginer on 17/02/2016.
 */
public class ItemConcepto {

    public int concepto_id;
    public String concepto_nombre;
    public int concepto_color;
    //public boolean activado;

    public ItemConcepto(int id, String nombre, int color){
        concepto_id = id;
        concepto_nombre = nombre;
        concepto_color = color;
        //activado = true;
    }
}
