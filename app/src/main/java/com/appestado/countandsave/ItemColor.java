package com.appestado.countandsave;

/**
 * Created by bginer on 17/02/2016.
 */
public class ItemColor {
    int Color_id;
    String Color_valor;

    // constructors
    public ItemColor() {
    }

    public ItemColor(int id, String valor) {
        this.Color_id = id;
        this.Color_valor = valor;
    }

    // setters
    public void setId(int id) { this.Color_id = id; }

    public void setValor(String valor) {
        this.Color_valor = valor;
    }


    // getters
    public int getFecha() {
        return this.Color_id;
    }

    public String getValor() { return this.Color_valor; }

}
