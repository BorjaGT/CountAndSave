package com.appestado.countandsave;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /*
    @Bind(R.id.button_1)Button button1;
    @Bind(R.id.button_2)Button button2;
    @Bind(R.id.button_3)Button button3;
    @Bind(R.id.button_sum)Button buttonSum;
    @Bind(R.id.button_4)Button button4;
    @Bind(R.id.button_5)Button button5;
    @Bind(R.id.button_6)Button button6;
    @Bind(R.id.button_res)Button buttonRes;
    @Bind(R.id.button_7)Button button7;
    @Bind(R.id.button_8)Button button8;
    @Bind(R.id.button_9)Button button9;
    @Bind(R.id.button_mul)Button buttonMul;
    @Bind(R.id.button_0)Button button0;
    @Bind(R.id.button_punto)Button buttonPunto;
    @Bind(R.id.button_igual)Button buttonIgual;
    @Bind(R.id.button_div)Button buttonDiv;
    */
    @Bind(R.id.toolbar)Toolbar toolbar;
    @Bind(R.id.pantalla_teclado)TextView pantalla_teclado;
    @Bind(R.id.txv_concepto)TextView txv_concepto;
    @Bind(R.id.fab)FloatingActionButton fab;
    @Bind(R.id.drawer_layout)DrawerLayout drawer_layout;

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private VariablesGlobales VG;

    private final String regex_Z = "^([0-9]{1,4})$";
    private final String regex_R = "^([0-9]{1,4}(\\.[0-9]{1,2}){0,1})$";
    private String regex_Roperador = "^([0-9]{1,4}(\\.[0-9]{1,2}){0,1}[\\+\\-\\*\\/])$";
    private String regex_Roperador0 = "^([0-9]{1,4}(\\.[0-9]{1,2}){0,1}[\\+\\-\\*\\/]0)$";
    private String regex_Roperador0Punto = "^([0-9]{1,4}(\\.[0-9]{1,2}){0,1}[\\+\\-\\*\\/]0\\.)$";
    private String regex_Roperador0Punto0 = "^([0-9]{1,4}(\\.[0-9]{1,2}){0,1}[\\+\\-\\*\\/]0\\.0)$";
    private String regex_RoperadorZ = "^([0-9]{1,4}(\\.[0-9]{1,2}){0,1}[\\+\\-\\*\\/]([0-9]{1,4}))$";
    private String regex_RoperadorR = "^([0-9]{1,4}(\\.[0-9]{1,2}){0,1}[\\+\\-\\*\\/]([0-9]{1,4}(\\.[0-9]{1,2}){0,1}))$";

    //Comportamiento de los botones del teclado
    @OnClick({R.id.button_0,
            R.id.button_1,
            R.id.button_2,
            R.id.button_3,
            R.id.button_4,
            R.id.button_5,
            R.id.button_6,
            R.id.button_7,
            R.id.button_8,
            R.id.button_9,
            R.id.button_sum,
            R.id.button_res,
            R.id.button_mul,
            R.id.button_div,
            R.id.button_igual,
            R.id.button_punto,
            R.id.fab})
    public void botonClick(View v) {
        String valorActualPantalla = pantalla_teclado.getText().toString();

        switch (v.getId()){
            case R.id.button_1:
                valorActualPantalla += "1";
                if(Pattern.matches(regex_R, valorActualPantalla) || Pattern.matches(regex_RoperadorR,valorActualPantalla)){
                    pantalla_teclado.setText(valorActualPantalla);
                }
                break;
            case R.id.button_2:
                valorActualPantalla += "2";
                if(Pattern.matches(regex_R,valorActualPantalla) || Pattern.matches(regex_RoperadorR,valorActualPantalla)){
                    pantalla_teclado.setText(valorActualPantalla);
                }
                break;
            case R.id.button_3:
                valorActualPantalla += "3";
                if(Pattern.matches(regex_R,valorActualPantalla) || Pattern.matches(regex_RoperadorR,valorActualPantalla)){
                    pantalla_teclado.setText(valorActualPantalla);
                }
                break;
            case R.id.button_4:
                valorActualPantalla += "4";
                if(Pattern.matches(regex_R,valorActualPantalla) || Pattern.matches(regex_RoperadorR,valorActualPantalla)){
                    pantalla_teclado.setText(valorActualPantalla);
                }
                break;
            case R.id.button_5:
                valorActualPantalla += "5";
                if(Pattern.matches(regex_R,valorActualPantalla) || Pattern.matches(regex_RoperadorR,valorActualPantalla)){
                    pantalla_teclado.setText(valorActualPantalla);
                }
                break;
            case R.id.button_6:
                valorActualPantalla += "6";
                if(Pattern.matches(regex_R,valorActualPantalla) || Pattern.matches(regex_RoperadorR,valorActualPantalla)){
                    pantalla_teclado.setText(valorActualPantalla);
                }
                break;
            case R.id.button_7:
                valorActualPantalla += "7";
                if(Pattern.matches(regex_R,valorActualPantalla) || Pattern.matches(regex_RoperadorR,valorActualPantalla)){
                    pantalla_teclado.setText(valorActualPantalla);
                }
                break;
            case R.id.button_8:
                valorActualPantalla += "8";
                if(Pattern.matches(regex_R,valorActualPantalla) || Pattern.matches(regex_RoperadorR,valorActualPantalla)){
                    pantalla_teclado.setText(valorActualPantalla);
                }
                break;
            case R.id.button_9:
                valorActualPantalla += "9";
                if(Pattern.matches(regex_R,valorActualPantalla) || Pattern.matches(regex_RoperadorR,valorActualPantalla)){
                    pantalla_teclado.setText(valorActualPantalla);
                }
                break;
            case R.id.button_0:
                valorActualPantalla += "0";
                if(valorActualPantalla.length() == 1 || Pattern.matches(regex_Roperador0,valorActualPantalla)){
                    pantalla_teclado.append("0.");
                }else if(Pattern.matches(regex_R,valorActualPantalla)
                        || Pattern.matches(regex_RoperadorZ,valorActualPantalla)
                        || Pattern.matches(regex_Roperador0Punto0, valorActualPantalla)){
                    pantalla_teclado.setText(valorActualPantalla);
                }
                break;
            case R.id.button_punto:
                if(valorActualPantalla.length() == 0 || Pattern.matches(regex_Roperador, valorActualPantalla)){
                    pantalla_teclado.append("0.");
                }else if(Pattern.matches(regex_Z,valorActualPantalla)|| Pattern.matches(regex_RoperadorZ,valorActualPantalla)){
                    pantalla_teclado.append(".");
                }
                break;

            case R.id.button_sum:
                //comprobar con expresión regular si al pulsar el simbolo + ya hay
                //introducido un valor real válido
                valorActualPantalla += "+";
                if(Pattern.matches(regex_Roperador,valorActualPantalla)){
                    pantalla_teclado.setText(valorActualPantalla);
                }
                break;

            case R.id.button_div:
                //comprobar con expresión regular si al pulsar el simbolo / ya hay
                //introducido un valor real válido
                valorActualPantalla += "/";
                if(Pattern.matches(regex_Roperador,valorActualPantalla)){
                    pantalla_teclado.setText(valorActualPantalla);
                }
                break;

            case R.id.button_mul:
                //comprobar con expresión regular si al pulsar el simbolo * ya hay
                //introducido un valor real válido
                valorActualPantalla += "*";
                if(Pattern.matches(regex_Roperador,valorActualPantalla)){
                    pantalla_teclado.setText(valorActualPantalla);
                }
                break;

            case R.id.button_res:
                //comprobar con expresión regular si al pulsar el simbolo - ya hay
                //introducido un valor real válido
                valorActualPantalla += "-";
                if(Pattern.matches(regex_Roperador,valorActualPantalla)){
                    pantalla_teclado.setText(valorActualPantalla);
                }
                break;

            case R.id.button_igual:
                //comprobar con RegExs que sea una expresión matemática válida
                //o sea, dos valores reales unidos por un simbolo
                if(Pattern.matches(regex_RoperadorR,pantalla_teclado.getText())){
                    //primero buscamos el operador y guardamos su posición
                    char op;
                    int pos = 0;
                    do{
                        ++pos;
                        op = valorActualPantalla.charAt(pos);
                    }while(op != '+' && op != '-' && op != '*' && op != '/');

                    BigDecimal valor1 = new BigDecimal(valorActualPantalla.substring(0, pos)).setScale(2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal valor2 = new BigDecimal(valorActualPantalla.substring(pos+1,valorActualPantalla.length())).setScale(2,BigDecimal.ROUND_HALF_UP);
                    switch (op){
                        case '+':
                            pantalla_teclado.setText((valor1.add(valor2)).toString());
                            break;
                        case '-':
                            pantalla_teclado.setText((valor1.subtract(valor2)).toString());
                            break;
                        case '*':
                            pantalla_teclado.setText((valor1.multiply(valor2)).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
                            break;
                        case '/':
                            pantalla_teclado.setText((valor1.divide(valor2,BigDecimal.ROUND_HALF_UP)).toString());
                            break;
                    }
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String valor = pantalla_teclado.getText().toString();
                SQLiteDatabase dbw = VG.datosdbh.getWritableDatabase();
                String concepto = txv_concepto.getText().toString();

                //Si hay algún valor != 0 y hemos abierto correctamente la base de datos
                if((valor.compareTo("")==0)) {
                    pantalla_teclado.setText("");
                }else if(concepto.compareTo("")==0){
                    Snackbar.make(drawer_layout,R.string.aviso_concepto, Snackbar.LENGTH_LONG).show();
                }else{
                    if ((Pattern.matches(regex_R,valor) || Pattern.matches(regex_Z, valor)) && (Float.valueOf(valor) != 0) && dbw != null) {
                        //Insertamos los datos en la tabla Usuarios
                        VG.datosdbh.insertarGasto(Float.parseFloat(pantalla_teclado.getText().toString()),VG.id_concepto_seleccionado);

                        //Borramos la pantalla del teclado y ponemos a cero
                        pantalla_teclado.setText("");

                        //Actualizamos el widget
                        /*Intent intent = new Intent(this.getActivity(),MiWidget.class);
                        intent.setAction(ACTION_actualizarWidget);
                        int[] ids = {R.xml.miwidget_wprovider};
                        //AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);
                        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
                        this.getActivity().sendBroadcast(intent);

                        //Finalizamos el Activity
                        this.getActivity().finish();*/
                    }
                }
            }
        });

        mNavigationDrawerFragment = (NavigationDrawerFragment)getFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, drawer_layout);

        VG = VariablesGlobales.getInstance();

        //Abrimos la base de datos 'DbDatos'
        VG.datosdbh = DatosSQLiteHelper.getInstance(this);

        //Llenamos la tabla de Conceptos de las Variables Globales para su posterior consulta
        VG.listaConceptos = VG.datosdbh.qry_getListaConceptos();

        //Añadimos los conceptos al Drawer
        for (ItemConcepto ic : VG.listaConceptos) {
            mNavigationDrawerFragment.addItem(ic.concepto_nombre);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        VG = VariablesGlobales.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        try {
            txv_concepto.setText(VG.listaConceptos.get(position).concepto_nombre);
            VG.id_concepto_seleccionado = VG.listaConceptos.get(position).concepto_id;
        } catch (Exception e) {
            Log.d("Debug", e.getMessage());
        }
    }

}

