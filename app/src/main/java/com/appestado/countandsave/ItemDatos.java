package com.appestado.countandsave;

/**
 * Created by bginer on 17/02/2016.
 */
public class ItemDatos {
    String fecha;
    float valor;
    int concepto;
    boolean seleccionado;

    // constructors
    public ItemDatos() {
        this.seleccionado = true; //Cierto por defecto
    }

    public ItemDatos(String fecha, float valor, int concepto) {
        this.fecha = fecha;
        this.valor = valor;
        this.concepto = concepto;
        this.seleccionado = true; //Cierto por defecto
    }

    // setters
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public void setConcepto(int concepto) {
        this.concepto = concepto;
    }


    // getters
    public String getFecha() {
        return this.fecha;
    }

    public float getValor() {
        return this.valor;
    }

    public int getConcepto() {
        return this.concepto;
    }

    public boolean getSeleccionado(){return this.seleccionado;}
}

