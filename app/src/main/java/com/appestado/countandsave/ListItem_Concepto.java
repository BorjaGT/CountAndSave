package com.appestado.countandsave;

/**
 * Created by borja_000 on 29/08/2015.
 */
public class ListItem_Concepto {
    public String color;
    public String concepto;
    public float valor;
    public float porcentaje;
    public boolean habilitado;
    //public Button boton;

    public ListItem_Concepto(){
        this.color = "#00FFFFFF";
        this.concepto = "";
        this.valor = 0;
        this.porcentaje = 0;
        this.habilitado = true;
        //this.boton = null;
    }

    public ListItem_Concepto(String col, String con, float val, float por){
        this.color = col;
        this.concepto = con;
        this.valor = val;
        this.porcentaje = por;
        this.habilitado = true;
        //this.boton = null;
    }

}
