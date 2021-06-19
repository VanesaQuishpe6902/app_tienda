package com.utc.applogeo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/*
autor: Vanesa Quishpe
creado: 12/05/2021
modificado: 09/06/2021
descripcion: Gestionar la bdd de la app
*/

public class BaseDatos extends SQLiteOpenHelper {
    //definiendo el nombre de la bdd
    private static final String nombreBdd = "bdd_usuarios_noveno_a";
    // definiendo la version de la bdd
    private static final int versionBdd = 3;
    //definiendo la estructura de la tabla usuario
    private static final String tablaUsuario = "create table usuario(id_usu integer primary key autoincrement, apellido_usu text, nombre_usu text," +
            " email_usu text, password_usu text)";
    //definiendo la estructura de la tabla CLIENTE
    private static final String tablaCliente = "create table cliente(id_cli integer primary key autoincrement," +
            "cedula_cli text, apellido_cli text, nombre_cli text, telefono_cli text, direccion_cli text);";
    //definiendo la estructura de la tabla PRODUCTO
    private static final String tablaProducto = "create table producto(id_pro integer primary key autoincrement," +
            "nombre_pro text, precio_pro DECIMAL (6,2), iva_pro INTEGER , stock_pro INTEGER, fechacaducidad_pro DATETIME);";

    //CONSTRUCTOR
    public BaseDatos(Context contexto) {
        super(contexto, nombreBdd, null, versionBdd);
    }

    ////PROCESO 1: Metodo que ejecuta automaticamente al consstruir la clase BaseDatos
    @Override
    public void onCreate(SQLiteDatabase db) {
        //ejecutando el query ddl para crear la tabla usuario con sus atributos
        db.execSQL(tablaUsuario);
        //ejecutando el query ddl para crear la tabla Cliente con sus atributos
        db.execSQL(tablaCliente);
        //ejecutando el query ddl para crear la tabla Producto con sus atributos
        db.execSQL(tablaProducto);
    }

    //PROCESO 2: Metodo que se ejecuta automaticamente cuando se detectan cambios en la versino con sus atributos
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Eliminacion de la version anterior de la tabla usuario
        db.execSQL("DROP TABLE IF EXISTS usuario");
        //Ejecucion del codigo para crear la tabla usuario con su nueva estructura
        db.execSQL(tablaUsuario);
        // Eliminacion de la version anterior de la tabla Cliente
        db.execSQL("DROP TABLE IF EXISTS cliente");
        //Ejecucion del codigo para crear la tabla Cliente con su nueva estructura
        db.execSQL(tablaCliente);
        // Eliminacion de la version anterior de la tabla Producto
        db.execSQL("DROP TABLE IF EXISTS producto");
        //Ejecucion del codigo para crear la tabla Producto con su nueva estructura
        db.execSQL(tablaProducto);
    }

    ///PROCESO 3: Metodo para insertar datos, retorna tru cuando inserta y false cuando hay error
    public boolean agregarUsuario(String apellido, String nombre, String email, String password) {
        SQLiteDatabase miBDD = getWritableDatabase(); //Llamando a la base de datos en el objeto miBDD
        if (miBDD != null)//validando que la base de datos exista no sea nula
        {
            miBDD.execSQL("insert into usuario (apellido_usu, nombre_usu, email_usu, password_usu) " +
                    "values ('" + apellido + "','" + nombre + "','" + email + "','" + password + "')");
            miBDD.close();
            return true;
        }
        return false; //retorno cuando no exista la bdd
    }

    //PROCESO 4: metodo para consultar por email y password
    public Cursor obtenerUsuarioporEmailPassword(String email, String password) {
        SQLiteDatabase miBDD = getWritableDatabase(); //Llamando a la base de datos
        Cursor usuario = miBDD.rawQuery("select * from usuario where " +
                "email_usu='" + email + "' and password_usu='" + password + "';", null); //Realizando la consulta y almacenando los resultados en el objeto usuario
        if (usuario.moveToFirst()) {//verificando que el objeto usuario tenga resultados
            return usuario; //retornamos datos encontrados
        } else {
            //Nose encuentra el usuario ..Porque no eexiste el email y congtrase{a
            return null;
        }

    }

    ///PROCESO 5: Metodo para insertar datos de clientes dentro de la BDD
    public boolean agregarCliente(String cedula, String apellido, String nombre, String telefono, String direccion)
    {
        SQLiteDatabase miBDD = getWritableDatabase(); //objeto para manejar la bdd
        if (miBDD != null)//validando que la base de datos exista no sea nula
        {
            miBDD.execSQL("insert into cliente(cedula_cli, apellido_cli, nombre_cli,telefono_cli, direccion_cli) " +
                    "values  ('" + cedula + "','" + apellido + "','" + nombre + "','" + telefono + "','" + direccion+ "')");
            miBDD.close();
            return true;
        }
        return false; //retorno cuando no exista la bdd
    }
    //PROCESO 6: metodo para consultar cliente existente en la BDD
    public Cursor obtenerClientes() {
        SQLiteDatabase miBDD = getWritableDatabase(); //Llamando a la base de datos
        Cursor clientes = miBDD.rawQuery("select * from cliente;", null);
        if (clientes.moveToFirst()) {//verificando que el objeto usuario tenga resultados
            return clientes; //retornar el cursor que contiene el listado de cliente
        } else {
            //Nose encuentra el usuario ..Porque no eexiste el email y congtrase{a
            return null;
        }

    }

    ///PROCESO 7: Metodo para insertar datos de productos dentro de la BDD
    public boolean agregarProducto(String nombre, double precio, int iva, int stock, String fechaCaducidad)
    {
        SQLiteDatabase miBDD = getWritableDatabase(); //objeto para manejar la bdd
        if (miBDD != null)//validando que la base de datos exista no sea nula
        {
            miBDD.execSQL("insert into producto(nombre_pro, precio_pro, iva_pro,stock_pro, fechacaducidad_pro) " +
                    "values  ('" + nombre + "','" + precio + "','" + iva + "','" + stock + "','" + fechaCaducidad+ "')");
            miBDD.close();
            return true;
        }
        return false; //retorno cuando no exista la bdd
    }
    //PROCESO 8: metodo para consultar producto existente en la BDD
    public Cursor obtenerProducto() {
        SQLiteDatabase miBDD = getWritableDatabase(); //Llamando a la base de datos
        Cursor productos = miBDD.rawQuery("select * from producto;", null);
        if (productos.moveToFirst()) {//verificando que el objeto producto tenga resultados
            return productos; //retornar el cursor que contiene el listado de producto
        } else {
            //Nose encuentra el producto
            return null;
        }

    }

    ///Metodo para actualizar el registro del cliente
    public boolean acualizarCliente(String cedula, String apellido, String nombre, String telefono, String direccion, String id)
    {
        SQLiteDatabase miBdd = getWritableDatabase(); //objeto para manejar la base de datos
        if(miBdd!= null)
        {
            miBdd.execSQL("update cliente set cedula_cli='"+cedula+"', " +
                    "apellido_cli='"+apellido+"', nombre_cli='"+nombre+"', " +
                    "telefono_cli='"+telefono+"',direccion_cli='"+direccion+"' where id_cli="+id);
        miBdd.close(); //cerrando la conexion con la bdd
        return true; //regresando verdadero ya que el proceso d actualizacion fue exitosa
        }
        return false; //retorna falso cuando no existe la bdd
    }
    //Metodo para eliminar un registro de clientes
    public boolean eliminarCliente (String id)
    {
        SQLiteDatabase miBdd = getWritableDatabase(); //objeto para manejar la bdd
        if(miBdd!= null) //validando que la bdd exista
        {
            miBdd.execSQL("delete from cliente where id_cli="+id); //ejecutar el query de eliminacion
            miBdd.close(); //cerrando la conexion con la bdd
            return true; //regresando verdadero ya que el proceso d actualizacion fue exitosa
        }
        return false; //retorna falso cuando no existe la bdd
    }
}
