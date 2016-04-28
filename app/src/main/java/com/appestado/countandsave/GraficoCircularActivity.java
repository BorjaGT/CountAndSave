package com.appestado.countandsave;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


/**
 * Modificado el 25/09/2015.
 */
public class GraficoCircularActivity extends FragmentActivity {

    /**
    * FINALES
    * */
    final int diametroGrafico = 500;

    /**
    * VARIABLES
    **/
    VariablesGlobales vg;
    EditText etxInicio;
    EditText etxFin;
    //Button btn_recalculaGrafico;

    ListView lista;
    ArrayAdapter_Leyenda adaptador;
    GraficoCircular_Thread gct;
    GraficoCircular_SurfaceView gcsv;
    SurfaceView surface;
    boolean flagDatePickerInicio = false;
    boolean firstTime = true;
    Checkea_Thread ct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.grafico_circular_layout);

        vg = VariablesGlobales.getInstance();
        vg.FA_graficosActivity = this;

        surface = (SurfaceView) findViewById(R.id.surfaceGrafico);
        surface.setZOrderOnTop(true);

        //Seteamos la fecha actual en el textview y el listener
        etxInicio = (EditText)findViewById(R.id.etxInicio);
        Bundle args = new Bundle();
        fechaActual(args);
        etxInicio.setText("01" + "/" + args.getString("month") + "/" + args.getString("year"));
        etxInicio.setShadowLayer(1, 2, 2, Color.LTGRAY);
        etxInicio.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                flagDatePickerInicio = true;
                showDatePicker();
            }
        });

        etxFin = (EditText)findViewById(R.id.etxFin);
        args = new Bundle();
        fechaActual(args);
        etxFin.setText(args.getString("day") + "/" + args.getString("month") + "/" + args.getString("year"));
        etxFin.setShadowLayer(1, 2, 2, Color.LTGRAY);
        etxFin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                flagDatePickerInicio = false;
                showDatePicker();
            }
        });

        //btn_recalculaGrafico = (Button)findViewById(R.id.btn_calcula);
        /*btn_recalculaGrafico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarDatos();
            }
        });*/


        //Abrimos la base de datos 'DbDatos'
        vg.datosdbh = DatosSQLiteHelper.getInstance(this);

        //Inicializamos la listaLeyenda
        vg.listaLeyenda = new ArrayList<ListItem_Concepto>();

        //Instancia del ListView
        lista = (ListView)findViewById(R.id.lv_leyenda);

        //Inicializar el adaptador con la fuente de datos
        adaptador = new ArrayAdapter_Leyenda(this, vg.listaLeyenda);

        //Relacionando la lista con el adaptador
        lista.setAdapter(adaptador);

        //Inicializamos el mapa de objetos ViewHolder
        vg.mapa_ViewHolder = new HashMap<Integer,ViewHolder>();

        //Inicializamos el Gráfico Circular
        gcsv = new GraficoCircular_SurfaceView(this);

        consultaBBDD();
    }

    public void consultaBBDD(){

        Log.d("Debug", "consultaBBDD()");

        String ini, ini_dia, ini_mes, ini_ano;
        String fin, fin_dia, fin_mes, fin_ano;
        char j,c;

        //leer la fecha Inicio y la fecha Final
        ini = etxInicio.getText().toString();
        ini_dia = "";
        ini_mes = "";
        ini_ano = "";
        j='d';
        for(int i=0; i<ini.length(); ++i){
            c = ini.charAt(i);
            if(c == '/'){
                if(j == 'd') j = 'm';
                else j = 'y';
            }else {
                switch (j) {
                    case 'd':
                        ini_dia += ini.charAt(i);
                        break;
                    case 'm':
                        ini_mes += ini.charAt(i);
                        break;

                    case 'y':
                        ini_ano += ini.charAt(i);
                        break;
                }
            }
        }

        fin = etxFin.getText().toString();
        fin_dia = "";
        fin_mes = "";
        fin_ano = "";
        j='d';
        for(int i=0; i<fin.length(); ++i){
            c = fin.charAt(i);
            if(c == '/'){
                if(j == 'd') j = 'm';
                else j = 'y';
            }else {
                switch (j) {
                    case 'd':
                        fin_dia += fin.charAt(i);
                        break;
                    case 'm':
                        fin_mes += fin.charAt(i);
                        break;

                    case 'y':
                        fin_ano += fin.charAt(i);
                        break;
                }
            }
        }

        Log.d("FECHAS: ",ini_ano + "-" + ini_mes + "-" + ini_dia+" // "+ fin_ano + "-" + fin_mes + "-" + fin_dia);

        //buscar que conceptos diferentes hay entre el rango de fechas proporcionado
        vg.listaConceptosDiferents = vg.datosdbh.qry_getConceptosEntreFechas(ini_ano + "-" + ini_mes + "-" + ini_dia, fin_ano + "-" + fin_mes + "-" + fin_dia);
        Log.d("Debug", "+++ ListaConceptos: " + vg.listaConceptosDiferents.size());

        //buscar los datos en la base de datos
        vg.listaGastos = vg.datosdbh.qry_getDatosEntreFechas(ini_ano + "-" + ini_mes + "-" + ini_dia, fin_ano + "-" + fin_mes + "-" + fin_dia);
        Log.d("Debug", "+++ ListaGastos: " + vg.listaGastos.size());

        //buscar los colores que están en uso
        vg.listaColores = vg.datosdbh.qry_getColoresEnUso();
        Log.d("Debug", "+++ ListaColores: " + vg.listaColores.size());

        //actualizar los elementos de la leyenda
        //setListaLeyenda();

        //ct = new Checkea_Thread();
        //ct.start();
        generarLista();
    }


    private void generarLista() {

        Log.d("Debug", "generarLista()");
        //Calcular la suma total
        vg.gastoTotal = 0;
        for (ItemDatos id : vg.listaGastos) {
            vg.gastoTotal += id.getValor();
        }
        Log.d("Debug", "GASTO: " + vg.gastoTotal);

        vg.listaLeyenda.clear();
        //Reiteramos nConceptos veces
        for(int i=0; i<vg.listaConceptosDiferents.size(); ++i){
            //entramos la linea de leyenda
            vg.listaLeyenda.add(new ListItem_Concepto());
        }
        adaptador.notifyDataSetChanged();
        if(!firstTime){
            gcsv.updateSurface();
        }

        firstTime = false;
    }






    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            if(flagDatePickerInicio) {
                etxInicio.setText(Tool_FechaHora.formateaDia(dayOfMonth) + "/" + Tool_FechaHora.formateaMes(monthOfYear) + "/" + year);
            }else{
                etxFin.setText(Tool_FechaHora.formateaDia(dayOfMonth) + "/" + Tool_FechaHora.formateaMes(monthOfYear) + "/" + year);
            }
        }
    };

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Bundle args = new Bundle();
        fechaActual(args);

        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                int month = monthOfYear+1;
                if(flagDatePickerInicio) {
                    etxInicio.setText(dayOfMonth + "/" + month + "/" + year);
                }else{
                    etxFin.setText(dayOfMonth + "/" + month + "/" + year);
                }
                flagDatePickerInicio = false;

                /*
                * AQUI ACTUALIZAMOS LOS DATOS DEL GRAFICO Y DE LA LEYENDA !!!
                * */
                Log.d("Debug", "llamamos a consultaBBDD()");
                consultaBBDD();
            }
        });
        date.show(getSupportFragmentManager(), "Date Picker");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public void fechaActual(Bundle bdl){
        Calendar calender = Calendar.getInstance();
        bdl.putString("year", ""+calender.get(Calendar.YEAR));

        int mes = calender.get(Calendar.MONTH)+1;
        if(mes>9){
            bdl.putString("month", ""+mes);
        }else{
            bdl.putString("month", "0"+mes);
        }

        int dia = calender.get(Calendar.DAY_OF_MONTH);
        if(dia>9){
            bdl.putString("day", ""+dia);
        }else{
            bdl.putString("day", "0"+dia);
        }
    }



    /**
     * CLASE GRAFICOCIRCULAR_SURFACEVIEW
     */
    private class GraficoCircular_SurfaceView extends SurfaceView implements SurfaceHolder.Callback{

        public SurfaceHolder holder;
        public VariablesGlobales varglob;

        public GraficoCircular_SurfaceView(Context context) {
            super(context);
            holder = surface.getHolder();
            holder.setFormat(PixelFormat.TRANSLUCENT);
            holder.addCallback(this);
            varglob = vg;
        }

        public void updateSurface(){
            Log.d("Debug", "updateSurface");
            (gct = new GraficoCircular_Thread(holder,varglob)).start();
        }

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            Log.d("Debug", "*** surfaceCreated");
            (gct = new GraficoCircular_Thread(holder,varglob)).start();
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            Log.d("Debug", "*** surfaceChanged");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            Log.d("Debug", "*** surfaceDestroyed");
            boolean retry = true;
            gct.setRunning(false);
            while (retry) {
                try {
                    gct.join();
                    retry = false;
                } catch (InterruptedException e) {
                }
            }
        }
    }











    /**
     *  CLASE GRAFICOCIRCULAR_THREAD
     * */
    private class GraficoCircular_Thread extends Thread {

        VariablesGlobales vg;
        private SurfaceHolder holder;
        private boolean running = true;
        int i = 0;
        float total;
        int concepto_id;
        String color_Concepto;
        RectF rectF;
        float inicioPorcion;
        float porcentajeConcepto;
        float sumatorio;
        Canvas canvas;

        public GraficoCircular_Thread(SurfaceHolder holder, VariablesGlobales varglob) {
            this.holder = holder;
            vg = varglob;
        }

        @Override
        public void run() {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);

            Paint paintLinea = new Paint();
            paintLinea.setAntiAlias(true);
            paintLinea.setColor(Color.BLACK);
            paintLinea.setStyle(Paint.Style.STROKE);
            paintLinea.setStrokeWidth(3);

            //Calcular la suma total
            total = 0;
            for (ItemDatos id : vg.listaGastos) {
                if(id.seleccionado) {
                    total += id.getValor();
                }
            }

            inicioPorcion = -90;
            canvas = null;

            //Bloqueamos el canvas
            do{
                canvas = holder.lockCanvas();
            }while(canvas==null);

            int tamX = canvas.getWidth();
            int tamY = canvas.getHeight();

            //Borramos el fondo
            //canvas.drawColor(Color.TRANSPARENT);
            paint.setColor(Color.WHITE);
            canvas.drawRect(0,0,tamX,tamY,paint);
            holder.unlockCanvasAndPost(canvas);

            do {
                canvas = holder.lockCanvas();
            } while (canvas == null);

            int iniX = (tamX - diametroGrafico) / 2;
            int iniY = (tamY - diametroGrafico) / 2;
            int finX = (tamX + diametroGrafico) / 2;
            int finY = (tamY + diametroGrafico) / 2;

            rectF = new RectF(iniX, iniY, finX, finY);

            //Reiteramos nConceptos veces
            for(int i=0; i<vg.listaConceptosDiferents.size(); ++i){

                //Leemos el identificador del concepto que toca en la posición i
                concepto_id = vg.listaConceptosDiferents.get(i).concepto_id;

                //Calculamos el porcentaje del concepto en base al total
                sumatorio = 0;
                for (ItemDatos id : vg.listaGastos) {
                    if(id.getConcepto() == concepto_id) {
                        sumatorio += id.getValor();
                    }
                }
                porcentajeConcepto = Math.round((sumatorio * 360) / total);

                //Consultamos el color que le pertenece a ese concepto
                color_Concepto = vg.datosdbh.qry_getColorByConcepto(concepto_id);
                Log.d("Debug","COLOR_CONCEPTO: "+color_Concepto);
                paint.setColor(Color.parseColor(color_Concepto));
                canvas.drawArc(rectF, inicioPorcion, porcentajeConcepto, true, paint);

                //dibujamos la linea exterior
                canvas.drawArc(rectF, inicioPorcion, porcentajeConcepto, true, paintLinea);

                inicioPorcion += porcentajeConcepto;
            }
            holder.unlockCanvasAndPost(canvas);
            Log.d("Debug", "*** Fin del run");

        }

        public void setRunning(boolean b) {
            running = b;
        }
    }

    private class Checkea_Thread extends Thread {

        VariablesGlobales vg;

        public Checkea_Thread(VariablesGlobales vg) {
            Log.d("Debug", "*** gt.new");
            this.vg = vg;
        }

        @Override
        public void run() {
            Log.d("Debug", "*** gt.start()");

                    Log.d("Debug", "*** gct.start()");
                    gct = new GraficoCircular_Thread(gcsv.holder, vg);
                    gct.start();

        }
    }
}
