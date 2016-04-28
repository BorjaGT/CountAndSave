package com.appestado.countandsave;

import android.support.v4.app.FragmentActivity;

import java.util.List;
import java.util.Map;

/**
 * Created by bginer on 17/02/2016.
 *
 * Esta clase implementa el patrón Singleton.
 */
public class VariablesGlobales {

    private static VariablesGlobales INSTANCE = null;

    DatosSQLiteHelper datosdbh;
    public List<ItemDatos> listaGastos;
    public List<ItemConcepto> listaConceptos;
    public List<ItemConcepto> listaConceptosDiferents;
    public int id_concepto_seleccionado;
    public List<ItemColor> listaColores;
    public List<ListItem_Concepto> listaLeyenda;
    public FragmentActivity FA_graficosActivity;
    public Map<Integer,ViewHolder> mapa_ViewHolder;
    public float gastoTotal;

    // El constructor privado no permite que se genere un constructor por defecto
    // para poder implementar el patrón Singleton
    private VariablesGlobales() {
    }

    // creador sincronizado para protegerse de posibles problemas multi-hilo
    // otra prueba para evitar instanciación múltiple
    private synchronized static void createInstance() {
        if (INSTANCE == null) {
            INSTANCE = new VariablesGlobales();
        }
    }
    public static VariablesGlobales getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }
}

