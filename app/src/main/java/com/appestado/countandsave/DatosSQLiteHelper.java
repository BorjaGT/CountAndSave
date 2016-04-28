package com.appestado.countandsave;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Debug;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bginer on 17/02/2016.
 *
 * Esta clase implementa el patrón Singleton.
 */
public class DatosSQLiteHelper extends SQLiteOpenHelper {

    private static DatosSQLiteHelper INSTANCE = null;

    // Versión de la base de datos
    private static final int DATABASE_VERSION = 1;

    // Nombre de la base de datos
    private static final String DATABASE_NAME = "DBCounterSave";

    private static SQLiteDatabase db;

    //Sentencias SQL para crear las tablas
    String SQL_CREATE_GASTOS = "CREATE TABLE TbGastos (Gasto_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "Gasto_valor REAL," +
            "Gasto_concepto INTEGER," +
            "Gasto_fecha TEXT)";
    String SQL_CREATE_CONCEPTOS = "CREATE TABLE TbConceptos (Concepto_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "Concepto_nombre TEXT," +
            "Concepto_color INTEGER)";
    String SQL_CREATE_COLORES = "CREATE TABLE TbColores (Color_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "Color_valor TEXT," +
            "Color_enUso BOOLEAN)";


    // El constructor privado no permite que se genere un constructor por defecto
    // para poder implementar el patrón Singleton
    private DatosSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // creador sincronizado para protegerse de posibles problemas multi-hilo
    // otra prueba para evitar instanciación múltiple
    private synchronized static void createInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DatosSQLiteHelper(context);
        }
    }
    public static DatosSQLiteHelper getInstance(Context context) {
        if (INSTANCE == null) {
            createInstance(context);
            db = DatosSQLiteHelper.getInstance(context).getReadableDatabase();
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase sqldb) {

        db = sqldb;

        //Se ejecuta la sentencia SQL de creación de las tablas
        db.execSQL(SQL_CREATE_GASTOS);
        db.execSQL(SQL_CREATE_CONCEPTOS);
        db.execSQL(SQL_CREATE_COLORES);

        //Se rellenan los datos por defecto en caso que la base de datos esté vacía
        Cursor cur = db.rawQuery("SELECT COUNT(*) FROM TbColores", null);
        if (cur != null) {
            cur.moveToFirst();                       // Always one row returned.
            if (cur.getInt (0) == 0) {               // Zero count means empty table.
                db.execSQL("INSERT INTO TbColores (Color_id, Color_valor, Color_enUso) VALUES ('1','#FFEEE8AA',1)");
                db.execSQL("INSERT INTO TbColores (Color_id, Color_valor, Color_enUso) VALUES ('2','#FF90EE90',1)");
                db.execSQL("INSERT INTO TbColores (Color_id, Color_valor, Color_enUso) VALUES ('3','#FFEE82EE',1)");
                db.execSQL("INSERT INTO TbColores (Color_id, Color_valor, Color_enUso) VALUES ('4','#FFFF69B4',1)");
                db.execSQL("INSERT INTO TbColores (Color_id, Color_valor, Color_enUso) VALUES ('5','#FFFFA500',1)");
                db.execSQL("INSERT INTO TbColores (Color_id, Color_valor, Color_enUso) VALUES ('6','#FF9ACD32',1)");
                //db.execSQL("INSERT INTO TbColores (Color_id, Color_valor, Color_enUso) VALUES ('7','#FF87CEEB',1)");
                insertarColor("#FF87CEEB");
                db.execSQL("INSERT INTO TbColores (Color_valor, Color_enUso) VALUES ('#FFBC8F8F',0)");
                db.execSQL("INSERT INTO TbColores (Color_valor, Color_enUso) VALUES ('#FFFFA07A',0)");
                db.execSQL("INSERT INTO TbColores (Color_valor, Color_enUso) VALUES ('#FF4682B4',0)");
            }
        }

        cur = db.rawQuery("SELECT COUNT(*) FROM TbConceptos", null);
        if (cur != null) {
            cur.moveToFirst();                       // Always one row returned.
            if (cur.getInt (0) == 0) {               // Zero count means empty table.
                db.execSQL("INSERT INTO TbConceptos (Concepto_nombre, Concepto_color) VALUES ('Varios','1')");
                db.execSQL("INSERT INTO TbConceptos (Concepto_nombre, Concepto_color) VALUES ('Comestibles','2')");
                db.execSQL("INSERT INTO TbConceptos (Concepto_nombre, Concepto_color) VALUES ('Ropa','3')");
                db.execSQL("INSERT INTO TbConceptos (Concepto_nombre, Concepto_color) VALUES ('Hogar','4')");
                db.execSQL("INSERT INTO TbConceptos (Concepto_nombre, Concepto_color) VALUES ('Restaurante','5')");
                //db.execSQL("INSERT INTO TbConceptos (Concepto_nombre, Concepto_color) VALUES ('Ocio','6')");
                //db.execSQL("INSERT INTO TbConceptos (Concepto_nombre, Concepto_color) VALUES ('Turismo','7')");
                insertarConcepto("Ocio",6);
                insertarConcepto("Turismo",7);
            }
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqldb, int oldVersion, int newVersion) {
        //Se elimina la versión anterior de las tablas
        db.execSQL("DROP TABLE IF EXISTS TbGastos");
        db.execSQL("DROP TABLE IF EXISTS TbConceptos");
        db.execSQL("DROP TABLE IF EXISTS TbColores");

        // create new tables
        onCreate(db);
    }

    /**
     *
     * @param valor
     * @param idConcepto
     * @return
     */
    public void insertarGasto(float valor, int idConcepto){

        //Consulta para conseguir la id del concepto
        //Cursor cur = db.rawQuery("SELECT Concepto_id FROM TbConceptos WHERE Concepto_nombre=='" + concepto + "'", null);
        //cur.moveToFirst();                       // Always one row returned.
        //int idConcepto = cur.getInt (0);

        //Contenedor de valores
        ContentValues values = new ContentValues();

        //Seteando valores
        values.put("Gasto_valor", valor);
        values.put("Gasto_concepto", idConcepto);
        values.put("Gasto_fecha", Tool_FechaHora.getFechaAMD());

        //Insertando en la base de datos
        db.insert("TbGastos", null, values);

    }

    public void insertarConcepto(String nombre, int color_id){
        //SQLiteDatabase db = this.getReadableDatabase();

        //Cntenedor de valores
        ContentValues values = new ContentValues();

        //Seteando valores
        values.put("Concepto_nombre",nombre);
        values.put("Concepto_color",color_id);

        //Insertando en la base de datos
        db.insert("TbConceptos", null, values);

    }


    public void insertarColor(String valor){

        //Cntenedor de valores
        ContentValues values = new ContentValues();

        //Seteando valores
        values.put("Color_valor",valor);
        values.put("Color_enUso",1);

        //Insertando en la base de datos
        db.insert("TbColores", null, values);

    }

    /**
     * Devuelve una lista con los gastos entre dos fechas proporcionadas.
     *
     * @param fecha_ini
     * @param fecha_fin
     * @return
     */
    public List<ItemDatos> qry_getDatosEntreFechas(String fecha_ini, String fecha_fin) {
        List<ItemDatos> lista = new ArrayList<ItemDatos>();

        String selectQuery = "SELECT * FROM TbGastos WHERE Gasto_fecha > '"+fecha_ini+"' AND Gasto_fecha <= '"+fecha_fin+"'";

        //SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                ItemDatos id = new ItemDatos();
                id.setFecha(c.getString((c.getColumnIndex("Gasto_fecha"))));
                id.setValor((c.getFloat(c.getColumnIndex("Gasto_valor"))));
                id.setConcepto(c.getInt(c.getColumnIndex("Gasto_concepto")));

                // adding to list
                lista.add(id);
            } while (c.moveToNext());
        }

        return lista;
    }

    /**
     * Devuelve una lista con los gastos entre dos fechas proporcionadas.
     *
     * @param fecha_ini
     * @param fecha_fin
     * @return
     */
    public List<ItemConcepto> qry_getConceptosEntreFechas(String fecha_ini, String fecha_fin) {
        List<ItemConcepto> lista = new ArrayList<ItemConcepto>();
        String selectQuery = "SELECT DISTINCT Gasto_concepto FROM TbGastos WHERE Gasto_fecha > '"+fecha_ini+"' AND Gasto_fecha <= '"+fecha_fin+"'";

        //SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                // adding to list
                int id = c.getInt(c.getColumnIndex("Gasto_concepto"));
                lista.add(new ItemConcepto(id, qry_getConceptoById(id), 0));
            } while (c.moveToNext());
        }

        return lista;
    }

    public List<ItemColor> qry_getColoresEnUso() {
        List<ItemColor> lista = new ArrayList<ItemColor>();
        ItemColor item;
        String selectQuery = "SELECT Color_id, Color_valor FROM TbColores WHERE Color_enUso == '1'";

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                item = new ItemColor();
                item.setId(c.getInt((c.getColumnIndex("Color_id"))));
                item.setValor(c.getString(c.getColumnIndex("Color_valor")));

                // adding to list
                lista.add(item);
            } while (c.moveToNext());
        }
        return lista;
    }

    public String qry_getColorByConcepto(int concepto_id){
        String color;
        String selectQuery = "SELECT Color_valor FROM TbColores WHERE Color_id == (SELECT Concepto_color FROM TbConceptos WHERE Concepto_id == '"+concepto_id+"')";

        Cursor c = db.rawQuery(selectQuery, null);

        c.moveToFirst();
        color = c.getString(c.getColumnIndex("Color_valor"));

        Log.d("Debug","*** COLOR: "+color);

        return color;
    }

    public String qry_getConceptoById(int concepto_id){
        String concepto;
        String selectQuery = "SELECT Concepto_nombre FROM TbConceptos WHERE Concepto_id == '"+concepto_id+"'";

        Cursor c = db.rawQuery(selectQuery, null);

        c.moveToFirst();
        concepto = c.getString(c.getColumnIndex("Concepto_nombre"));

        return concepto;
    }

    /*
    * DEVUELVE UNA LISTA DE LOS DIFERENTES CONCEPTOS QUE HAY EN LA BBDD
    * */
    public List<ItemConcepto> qry_getListaConceptos() {
        List<ItemConcepto> lista = new ArrayList<ItemConcepto>();
        String selectQuery = "SELECT * FROM TbConceptos";

        //SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        Log.d("Debug","COUNT: "+c.getCount());
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                // adding to list
                lista.add(new ItemConcepto(c.getInt(0), c.getString(1), c.getInt(2)));
                Log.d("Debug", c.getString(1));
            } while (c.moveToNext());
        }

        return lista;
    }
}

