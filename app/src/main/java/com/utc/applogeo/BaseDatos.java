package com.utc.applogeo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/*
autor: Vanesa Quishpe, Vaca Alex, Angel Tapia
creado: 12/05/2021
modificado: 19/06/2021
descripcion: Gestionar la bdd de la app
*/

public class BaseDatos extends SQLiteOpenHelper {
    //definiendo el nombre de la bdd
    private static final String nombreBdd = "bdd_usuarios_noveno_a";
    // definiendo la version de la bdd
    private static final int versionBdd = 1;
    //definiendo la estructura de la tabla usuario
    private static final String tablaUsuario = "create table usuario(id_usu integer primary key autoincrement, apellido_usu text, nombre_usu text," +
            " email_usu text, password_usu text)";
    //definiendo la estructura de la tabla CLIENTE
    private static final String tablaCliente = "create table cliente(id_cli integer primary key autoincrement," +
            "cedula_cli text, apellido_cli text, nombre_cli text, telefono_cli text, direccion_cli text);";
    //definiendo la estructura de la tabla PRODUCTO
    private static final String tablaProducto = "create table producto(id_pro integer primary key autoincrement," +
            "nombre_pro text, precio_pro DECIMAL (6,2), iva_pro INTEGER , stock_pro INTEGER, fechacaducidad_pro DATETIME);";
    // V E N T A
    private static final String tablaVenta = "CREATE TABLE venta(" +
            "id_vent INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "f_vent DATETIME, " +
            "subtotal_vent DECIMAL (6,2)," +
            "iva_vent DECIMAL(6,2), " +
            "total_vent DECIMAL(6,2), " +
            "estado_vent INTEGER, " +
            "fk_id_cli INTEGER, " +
            "FOREIGN KEY (fk_id_cli) REFERENCES cliente (id_cli)" +
            ");";
    // D E T A L L E
    private static final String tablaDetalle = "CREATE TABLE detalle(" +
            "id_det INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "fk_id_venta INTEGER, " +
            "fk_id_pro INTEGER, " +
            "cantidad_det INTEGER, " +
            "FOREIGN KEY (fk_id_venta) REFERENCES venta (id_vent), " +
            "FOREIGN KEY (fk_id_pro) REFERENCES producto (id_pro) " +
            ");";
    // SEMILLA
    private static final String ingresarConsumidorFinal = "INSERT INTO cliente (cedula_cli, apellido_cli, nombre_cli, telefono_cli,direccion_cli) " +
            "VALUES ('1111111111','Consumidor','Final','02222222','Ninguno')";

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
        //ejecutando el query ddl para crear la tabla Ventas con sus atributos
        db.execSQL(tablaVenta);
        //ejecutando el query ddl para crear la tabla Detalle con sus atributos
        db.execSQL(tablaDetalle);
        // Ejecutar Semilla
        db.execSQL(ingresarConsumidorFinal);
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
        // Eliminacion de la version anterior de la tabla Venta
        db.execSQL("DROP TABLE IF EXISTS venta");
        //Ejecucion del codigo para crear la tabla Producto con su nueva estructura
        db.execSQL(tablaVenta);
        db.execSQL("DROP TABLE IF EXISTS detalle");
        //Ejecucion del codigo para crear la tabla Producto con su nueva estructura
        db.execSQL(tablaDetalle);
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
    public boolean agregarCliente(String cedula, String apellido, String nombre, String telefono, String direccion) {
        SQLiteDatabase miBDD = getWritableDatabase(); //objeto para manejar la bdd
        if (miBDD != null)//validando que la base de datos exista no sea nula
        {
            miBDD.execSQL("insert into cliente(cedula_cli, apellido_cli, nombre_cli,telefono_cli, direccion_cli) " +
                    "values  ('" + cedula + "','" + apellido + "','" + nombre + "','" + telefono + "','" + direccion + "')");
            miBDD.close();
            return true;
        }
        return false; //retorno cuando no exista la bdd
    }

    //PROCESO 6: metodo para consultar cliente existente en la BDD
    public Cursor obtenerClientes() {
        SQLiteDatabase miBDD = getWritableDatabase(); //Llamando a la base de datos
        Cursor clientes = miBDD.rawQuery("select * from cliente WHERE id_cli > 1;", null);
        if (clientes.moveToFirst()) {//verificando que el objeto usuario tenga resultados
            return clientes; //retornar el cursor que contiene el listado de cliente
        } else {
            //Nose encuentra el usuario ..Porque no eexiste el email y congtrase{a
            return null;
        }

    }

    //PROCESO 6: metodo para consultar cliente existente en la BDD
    public Cursor consultarDatosClientes(int id) {
        SQLiteDatabase miBDD = getWritableDatabase(); //Llamando a la base de datos
        Cursor clientes = miBDD.rawQuery("select * from cliente where id_cli = " + id + ";", null);
        if (clientes.moveToFirst()) {//verificando que el objeto usuario tenga resultados
            return clientes; //retornar el cursor que contiene el listado de cliente
        } else {
            //Nose encuentra el usuario ..Porque no eexiste el email y congtrase{a
            return null;
        }

    }

    //PROCESO 6: metodo para consultar cliente existente en la BDD
    public Cursor consultarDatosClientesPorNombre(String ci) {
        SQLiteDatabase miBDD = getWritableDatabase(); //Llamando a la base de datos
        Cursor clientes = miBDD.rawQuery("select * from cliente where cedula_cli = '" + ci + "';", null);
        if (clientes.moveToFirst()) {//verificando que el objeto usuario tenga resultados
            return clientes; //retornar el cursor que contiene el listado de cliente
        } else {
            //Nose encuentra el usuario ..Porque no eexiste el email y congtrase{a
            return null;
        }

    }

    ///PROCESO 7: Metodo para insertar datos de productos dentro de la BDD
    public boolean agregarProducto(String nombre, double precio, int iva, int stock, String fechaCaducidad) {
        SQLiteDatabase miBDD = getWritableDatabase(); //objeto para manejar la bdd
        if (miBDD != null)//validando que la base de datos exista no sea nula
        {
            miBDD.execSQL("insert into producto(nombre_pro, precio_pro, iva_pro,stock_pro, fechacaducidad_pro) " +
                    "values  ('" + nombre + "','" + precio + "','" + iva + "','" + stock + "','" + fechaCaducidad + "')");
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

    public Cursor obtenerProductoId(int id) {
        SQLiteDatabase miBDD = getWritableDatabase(); //Llamando a la base de datos
        Cursor productos = miBDD.rawQuery("select * from producto WHERE id_pro = '" + id + "';", null);
        if (productos.moveToFirst()) {//verificando que el objeto producto tenga resultados
            return productos; //retornar el cursor que contiene el listado de producto
        } else {
            //Nose encuentra el producto
            return null;
        }

    }

    public Cursor obtenerProductoNombre(String datos) {
        SQLiteDatabase miBDD = getWritableDatabase(); //Llamando a la base de datos
        Cursor productos = miBDD.rawQuery("select * from producto " +
                "WHERE nombre_pro LIKE '%" + datos + "%';", null);
        if (productos.moveToFirst()) {//verificando que el objeto producto tenga resultados
            return productos; //retornar el cursor que contiene el listado de producto
        } else {
            //Nose encuentra el producto
            return null;
        }

    }

    public boolean editarProducto(int id, String nombre, double precioP, int iva, int stockP, String fechaCaducidadP) {
        String sql = "UPDATE producto SET " +
                "nombre_pro = '" + nombre + "', " +
                "precio_pro = " + precioP + ", " +
                "iva_pro = " + iva + ", " +
                "stock_pro = " + stockP + "," +
                "fechacaducidad_pro = '" + fechaCaducidadP + "' " +
                "WHERE id_pro = '" + id + "';";
        SQLiteDatabase miBdd = getWritableDatabase();
        if (miBdd != null) {
            miBdd.execSQL(sql);
            miBdd.close(); //cerrando la conexion con la bdd
            return true; //regresando verdadero ya que el proceso d actualizacion fue exitosa
        }
        return false; //retorna falso cuando no existe la bdd
    }

    //Metodo para eliminar un registro de producto
    public boolean eliminarProducto(String id) {
        SQLiteDatabase miBdd = getWritableDatabase(); //objeto para manejar la bdd
        if (miBdd != null) //validando que la bdd exista
        {
            miBdd.execSQL("DELETE FROM producto WHERE id_pro= '" + id + "';"); //ejecutar el query de eliminacion
            miBdd.close(); //cerrando la conexion con la bdd
            return true; //regresando verdadero ya que el proceso d actualizacion fue exitosa
        }
        return false; //retorna falso cuando no existe la bdd
    }

    ///Metodo para actualizar el registro del cliente
    public boolean acualizarCliente(String cedula, String apellido, String nombre, String telefono, String direccion, String id) {
        SQLiteDatabase miBdd = getWritableDatabase(); //objeto para manejar la base de datos
        if (miBdd != null) {
            miBdd.execSQL("update cliente set cedula_cli='" + cedula + "', " +
                    "apellido_cli='" + apellido + "', nombre_cli='" + nombre + "', " +
                    "telefono_cli='" + telefono + "',direccion_cli='" + direccion + "' where id_cli=" + id);
            miBdd.close(); //cerrando la conexion con la bdd
            return true; //regresando verdadero ya que el proceso d actualizacion fue exitosa
        }
        return false; //retorna falso cuando no existe la bdd
    }

    //Metodo para eliminar un registro de clientes
    public boolean eliminarCliente(String id) {
        SQLiteDatabase miBdd = getWritableDatabase(); //objeto para manejar la bdd
        if (miBdd != null) //validando que la bdd exista
        {
            miBdd.execSQL("delete from cliente where id_cli=" + id); //ejecutar el query de eliminacion
            miBdd.close(); //cerrando la conexion con la bdd
            return true; //regresando verdadero ya que el proceso d actualizacion fue exitosa
        }
        return false; //retorna falso cuando no existe la bdd
    }


    //  G E S T I O N    V E N T A S
    /*
     * Metodos
     * Create => Registrar venta (registrarVenta)
     * Read => Listar Ventas (listarVentas)
     * Update =>Ingresar nuevos valores (actualizarVenta)
     *          Anular venta (anularVentas)
     * Delete => [Sin usar]
     * */

    // C R E A T E
    public boolean registrarVenta(String fecha, String datosCliente) {
        SQLiteDatabase miBDD = getWritableDatabase(); //objeto para manejar la bdd
        String sql = "INSERT INTO venta(f_vent, subtotal_vent, iva_vent, total_vent, estado_vent, fk_id_cli) " +
                "VALUES ('" + fecha + "',0 ,0 ,0 ,0 ,'" + datosCliente + "')";
        if (miBDD != null)//validando que la base de datos exista no sea nula
        {
            miBDD.execSQL(sql);
            miBDD.close();
            return true;
        }
        return false; //retorno cuando no exista la bdd
    }

    // R E A D
    public Cursor listarVentas() {
        SQLiteDatabase miBDD = getWritableDatabase(); //Llamando a la base de datos
        String sql = "select * from venta " +
                "INNER JOIN cliente " +
                "ON venta.fk_id_cli = cliente.id_cli " +
                "ORDER BY id_vent DESC;";
        Cursor ventas = miBDD.rawQuery(sql, null);
        if (ventas.moveToFirst()) {//verificando que el objeto ventas tenga resultados
            return ventas; //retornar el cursor que contiene el listado de ventas
        } else {
            //Nose encuentra ventas
            return null;
        }

    }

    public Cursor buscarVenta(String id) {
        SQLiteDatabase miBDD = getWritableDatabase(); //Llamando a la base de datos
        String sql = "select * from venta WHERE id_vent = '" + id + "';";
        Cursor ventas = miBDD.rawQuery(sql, null);
        if (ventas.moveToFirst()) {//verificando que el objeto ventas tenga resultados
            return ventas; //retornar el cursor que contiene el listado de ventas
        } else {
            //Nose encuentra ventas
            return null;
        }

    }

    public Cursor buscarVentasFecha(String fecha) {
        SQLiteDatabase miBDD = getWritableDatabase(); //Llamando a la base de datos
        String sql = "select * from venta WHERE f_vent = '" + fecha + "';";
        Cursor ventas = miBDD.rawQuery(sql, null);
        if (ventas.moveToFirst()) {//verificando que el objeto ventas tenga resultados
            return ventas; //retornar el cursor que contiene el listado de ventas
        } else {
            //Nose encuentra ventas
            return null;
        }

    }

    // U P D A T E
    public boolean cambiarEstadoVenta(String id, int estado) {
        String sql;
        SQLiteDatabase miBdd = getWritableDatabase();
        if (miBdd != null) {
            sql = "UPDATE venta SET " +
                    "estado_vent = " + estado + " " + // 0 En curso, 1 Finaliado
                    "WHERE id_vent = '" + id + "';";

            miBdd.execSQL(sql);
            miBdd.close();
        }
        return false;
    }

    public boolean eliminarVenta(String id) {
        String sql = "";
        SQLiteDatabase miBdd = getWritableDatabase();
        if (miBdd != null) {
            sql = "DELETE FROM venta WHERE id_vent = '" + id + "';";
            miBdd.execSQL(sql);
            miBdd.close();
            return true;
        }
        return false;
    }

    //  G E S T I O N    D E T A L L E S
    /*
     * Metodos
     * Create => Registrar detalle (registrarDetalle(String id_venta, id_producto, int cantidad))
     * Read => Listar Detalles de la Venta (listarDetalle(String id_venta))
     * Delete => Quitar producto de la venta (quitarProducto(String id))
     * */
    //C R E A T E
    // Metodo para agregar un registro al detalle
    public boolean registrarProductoDetalle(int id_venta, int id_prod, int cantidad) {
        SQLiteDatabase miBdd = getWritableDatabase();
        // Actualizar la cantidad del producto

        if (miBdd != null) {
            // Actualizar cantidad de producto
            restarProductos(id_prod, cantidad);
            // Ingresar detalle
            String sql = "INSERT INTO detalle (fk_id_venta, fk_id_pro, cantidad_det) " +
                    "VALUES ('" + id_venta + "','" + id_prod + "'," + cantidad + ");";
            miBdd.execSQL(sql);
            // Actualizamos valores totales de la venta
            calcularTotalesVenta(id_venta);
            // Cerrar Conexion
            miBdd.close();
            return true;
        }
        return false;
    }

    // R E A D
    public Cursor listarDetalle(String id_venta) {
        SQLiteDatabase miBdd = getReadableDatabase();
        String sql = "SELECT * FROM detalle " +
                "INNER JOIN producto " +
                "ON detalle.fk_id_pro = producto.id_pro " +
                "WHERE fk_id_venta = '" + id_venta + "';";
        Cursor detalle = miBdd.rawQuery(sql, null);
        if (detalle.moveToFirst()) {//verificando que el objeto ventas tenga resultados
            return detalle; //retornar el cursor que contiene el listado de ventas
        } else {
            //Nose encuentra datos
            return null;
        }
    }

    public Cursor buscarDetalle(int id) {
        SQLiteDatabase miBdd = getReadableDatabase();
        String sql = "SELECT * FROM detalle " +
                "WHERE id_det = '" + id + "';";
        Cursor detalle = miBdd.rawQuery(sql, null);
        if (detalle.moveToFirst()) {//verificando que el objeto ventas tenga resultados
            return detalle; //retornar el cursor que contiene el listado de ventas
        } else {
            //Nose encuentra ventas
            return null;
        }
    }

    // D E L E T E
    // Metodo para quitar un registro del detalle
    public boolean quitarProductoDetalle(int id) {
        SQLiteDatabase miBdd = getWritableDatabase(); //objeto para manejar la bdd
        if (miBdd != null) //validando que la bdd exista
        {
            // Obtener informacion del producto a traves del detalle
            Cursor infoProducto = buscarDetalle(id);
            // Obtener id de la venta
            int idVent = Integer.parseInt(infoProducto.getString(1));
            // Obtener id del producto
            int idProd = Integer.parseInt(infoProducto.getString(2));
            // Obtener cantidad vendida
            int cantProdVendida = Integer.parseInt(infoProducto.getString(3));
            // Regresar producto
            regresarProducto(idProd, cantProdVendida);
            // Elimina registro
            String sql = "DELETE FROM detalle WHERE id_det = '" + id + "';";
            miBdd.execSQL(sql);
            // Actualizamos valores totales de la venta
            calcularTotalesVenta(idVent);
            miBdd.close(); //cerrando la conexion con la bdd
            return true; //regresando verdadero ya que el proceso de actualizacion fue exitosa
        }
        return false; //retorna falso cuando no existe la bdd
    }

    // SUBMETODOS
    // Submetodo que ayudará a restar el stock del producto
    public void restarProductos(int id, int cantidad) {
        int nuevaCantidad = 0;
        SQLiteDatabase miBdd = getWritableDatabase();
        if (miBdd != null) {
            // Obtener informacion del producto
            Cursor producto = obtenerProductoId(id);
            // Calcular nueva cantidad
            nuevaCantidad = Integer.parseInt(producto.getString(4)) - cantidad;
            // Actualizar cantidad del producto
            String sqlActualizarProducto = "UPDATE producto " +
                    "SET stock_pro = " + nuevaCantidad + " " +
                    "WHERE id_pro = '" + id + "';";
            miBdd.execSQL(sqlActualizarProducto);
        }
    }

    // Submetodo que ayudará a regresar el producti al stock
    public void regresarProducto(int id, int cantidad) {
        int nuevaCantidad = 0;
        SQLiteDatabase miBdd = getWritableDatabase();
        if (miBdd != null) {
            // Obtener informacion del producto
            Cursor producto = obtenerProductoId(id);
            // Calcular nueva cantidad
            nuevaCantidad = Integer.parseInt(producto.getString(4)) + cantidad;
            // Actualizar cantidad del producto
            String sqlActualizarProducto = "UPDATE producto " +
                    "SET stock_pro = " + nuevaCantidad + " " +
                    "WHERE id_pro = '" + id + "';";
            miBdd.execSQL(sqlActualizarProducto);

        }
    }

    public Cursor listarVentas(int id_venta) {
        SQLiteDatabase miBDD = getReadableDatabase();
        String sqlListaProducto = "SELECT * FROM detalle WHERE fk_id_venta = '" + id_venta + "';";
        Cursor listaVentas = miBDD.rawQuery(sqlListaProducto, null);
        if (listaVentas.moveToFirst()) {
            return listaVentas;
        } else {
            return null;
        }

    }

    // Submetodo que ayudará a calcular el subtotal, iva y el total de la venta
    public void calcularTotalesVenta(int id_venta) {
        double subtotal = 0, iva = 0, total = 0;
        SQLiteDatabase miBdd = getWritableDatabase();
        if (miBdd != null) {
            // Obtener lista de productos
            Cursor listaProductos = listarVentas(id_venta);
            if (listaProductos != null) {
                do {
                    // Obtener informacion de la venta
                    Cursor infoProducto = obtenerProductoId(Integer.parseInt(listaProductos.getString(2)));
                    // Calcular subtotal
                    double valor = Double.parseDouble(infoProducto.getString(2)) * Integer.parseInt(listaProductos.getString(3));
                    subtotal = subtotal + valor;

                    // Actualizar informacion de venta
                } while (listaProductos.moveToNext());
            }
            // Calcular IVA
            iva = subtotal * 0.12;
            // Calcular Total
            total = subtotal + iva;
            // Guardo en la base de datos
            String sqlActualizarVenta = "UPDATE venta SET " +
                    "subtotal_vent = " + subtotal + ", " +
                    "iva_vent = " + iva + ", " +
                    "total_vent = " + total + " " +
                    "WHERE id_vent = '" + id_venta + "';";
            miBdd.execSQL(sqlActualizarVenta);
        }
    }


}
