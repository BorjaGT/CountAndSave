package com.appestado.countandsave;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

/**
 * Created by borja_000 on 29/08/2015.
 */
public class ArrayAdapter_Leyenda extends ArrayAdapter<ListItem_Concepto> {

    VariablesGlobales vg;
    ViewHolder viewholder;

    public ArrayAdapter_Leyenda(Context context, List<ListItem_Concepto> objects) {
        super(context, 0, objects);
        vg = VariablesGlobales.getInstance();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        View listItemView = convertView;


            if (vg.mapa_ViewHolder.get(position) == null) {
                //Obteniendo una instancia del inflater
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                //inflando con leyenda_list_item.xml
                listItemView = inflater.inflate(R.layout.leyenda_list_item, null);

                viewholder = new ViewHolder();
                viewholder.btn_color = (ImageButton) listItemView.findViewById(R.id.btn_seleccion);
                viewholder.txv_concepto = (TextView) listItemView.findViewById(R.id.txv_concepto);
                viewholder.txv_valor = (TextView) listItemView.findViewById(R.id.txv_valor);
                viewholder.txv_porcentaje = (TextView) listItemView.findViewById(R.id.txv_porcentaje);
                viewholder.barra_progreso = (ProgressBar) listItemView.findViewById(R.id.progressBar);

                vg.mapa_ViewHolder.put(position, viewholder);

            } else {
                viewholder = vg.mapa_ViewHolder.get(position);
            }

        //Obteniendo instancia de la ListItem_Concepto en la posición actual
        ListItem_Concepto lic = vg.listaLeyenda.get(position);

        if(lic.habilitado){
            viewholder.btn_color.setBackgroundColor(Color.parseColor(lic.color));
            viewholder.btn_color.setImageDrawable(getContext().getDrawable(R.drawable.ic_green_tick));
        }else{
            viewholder.btn_color.setBackgroundColor(Color.parseColor(lic.color));
            viewholder.btn_color.setImageResource(R.drawable.ic_cross);
        }

        viewholder.btn_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListItem_Concepto lic = getItem(position);
                if (lic.habilitado) {
                    lic.habilitado = false;
                    notifyDataSetChanged();
                } else {
                    lic.habilitado = true;
                    notifyDataSetChanged();
                }
            }
        });

        new UpdateProgress(position, viewholder).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        //Devolver al ListView la fila creada
        return listItemView;

    }


    private class UpdateProgress extends AsyncTask<Void, Integer, Void> {

        ViewHolder holder;
        int progreso;
        int posicion;
        Integer[] s = new Integer[2];
        int concepto_id;
        String color_Concepto;
        String nombre_Concepto;
        float porcentajeConcepto;
        float sumatorio;
        DecimalFormatSymbols otherSymbols;
        DecimalFormat df;
        //VariablesGlobales vg;

        public UpdateProgress(int pos, ViewHolder vh) {
            this.posicion = pos;
            this.holder = vh;

            otherSymbols = new DecimalFormatSymbols();
            otherSymbols.setDecimalSeparator('.');
            df = new DecimalFormat("#.##",otherSymbols);
        }


        @Override
        protected void onPreExecute() {
            //Leemos el identificador del concepto que toca en la posición i
            concepto_id = vg.listaConceptosDiferents.get(posicion).concepto_id;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            holder.barra_progreso.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            progreso = 0;
            sumatorio = 0;
            //Calculamos el porcentaje del concepto en base al total

            float pasoPorcentaje = 100/vg.listaGastos.size();
            for (ItemDatos id : vg.listaGastos) {
                if(id.getConcepto() == concepto_id) {
                    sumatorio += id.getValor();
                }
                progreso += pasoPorcentaje;
                publishProgress(progreso);
            }

            //Consultamos el color que le pertenece a ese concepto
            color_Concepto = vg.datosdbh.qry_getColorByConcepto(concepto_id);

            //Consultamos el nombre que le pertenece a ese concepto
            nombre_Concepto = vg.datosdbh.qry_getConceptoById(concepto_id);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            holder.btn_color.setVisibility(View.VISIBLE);

            holder.txv_concepto.setText(nombre_Concepto);
            holder.txv_concepto.setTextColor(Color.parseColor(color_Concepto));
            holder.txv_concepto.setShadowLayer(1, 2, 2, Color.BLACK);
            holder.txv_concepto.setVisibility(View.VISIBLE);

            holder.txv_valor.setText(String.valueOf(sumatorio) + "€");
            holder.txv_valor.setShadowLayer(1, 2, 2, Color.LTGRAY);
            holder.txv_valor.setVisibility(View.VISIBLE);

            holder.txv_porcentaje.setText(Float.parseFloat(df.format((sumatorio * 100) / vg.gastoTotal)) + "%");
            holder.txv_porcentaje.setShadowLayer(1, 2, 2, Color.LTGRAY);
            holder.txv_porcentaje.setVisibility(View.VISIBLE);

            holder.barra_progreso.setVisibility(View.INVISIBLE);
        }
    }
}
