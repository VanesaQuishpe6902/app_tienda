package com.utc.applogeo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

/*
autor: Vanesa Quishpe, Alex Vaca, Tapia Agel
creado: 21/06/2021
modificado: 21/06/2021
descripcion: Actividad para gestionar los factura dentro la base de datos SQLite
*/


public class FacturacionActivity extends AppCompatActivity {
    // Formatear valores double
    String pattern = "##.##";
    DecimalFormat decimalFormat = new DecimalFormat(pattern);
    Button btnAgregarProductoFacturacion, btnFinalizarVenta, btnAnularVenta;
    String idCli, idVenta;
    Cursor infoCliente, infoVenta, infoDescripcionVenta;
    BaseDatos bdd;
    TextView txtCedulaFacturacion, txtNombresFacturacion, txtTelefonoFacturacion, txtIdVenta, txtFechaVenta;
    ListView lstDescripcionVenta;
    ArrayList<String> listaDecripcionVenta = new ArrayList<>();
    // Totales
    TextView txtSubtotalFacturacion, txtIvaFacturacion, txtTotalFacturacion;
    int idEliminar = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facturacion);
        // Mapear
        txtIdVenta = (TextView) findViewById(R.id.txtIdVenta);
        txtFechaVenta = (TextView) findViewById(R.id.txtFechaVenta);
        txtCedulaFacturacion = (TextView) findViewById(R.id.txtCedulaFacturacion);
        txtNombresFacturacion = (TextView) findViewById(R.id.txtNombresFacturacion);
        txtTelefonoFacturacion = (TextView) findViewById(R.id.txtBuscarProductoFacturacion);
        txtSubtotalFacturacion = (TextView) findViewById(R.id.txtSubtotalFacturacion);
        txtIvaFacturacion = (TextView) findViewById(R.id.txtIvaFacturacion);
        txtTotalFacturacion = (TextView) findViewById(R.id.txtTotalFacturacion);
        btnAgregarProductoFacturacion = (Button) findViewById(R.id.btnAgregarProductoFacturacion);
        btnFinalizarVenta = (Button) findViewById(R.id.btnFinalizarVenta);
        btnAnularVenta = (Button) findViewById(R.id.btnAnularVenta);
        bdd = new BaseDatos(getApplicationContext());
        lstDescripcionVenta = (ListView) findViewById(R.id.lstDescripcionVenta);
        // Traer datos extras
        Bundle parametrosExtra = getIntent().getExtras();
        if (parametrosExtra != null) {
            try {
                //Intente realizar estas lineas de codigo
                idCli = parametrosExtra.getString("id_cli");
                idVenta = parametrosExtra.getString("id_venta");
                infoVenta = bdd.buscarVenta(idVenta);
                infoCliente = bdd.consultarDatosClientes(Integer.parseInt(idCli));


            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), "Error al procesar la solicitud" + ex.toString(), Toast.LENGTH_LONG).show();

            }
            txtIdVenta.setText(idVenta);
            txtFechaVenta.setText(infoVenta.getString(1));
            txtCedulaFacturacion.setText(infoCliente.getString(1));
            txtNombresFacturacion.setText(infoCliente.getString(2) + " " + infoCliente.getString(3));
            txtTelefonoFacturacion.setText(infoCliente.getString(4));

        }
        // Cargar lista descripcion
        obtenerDescripcion();

        // Metodo para comprobar estado de la venta
        estadoVenta();
    }

    private void estadoVenta() {
        infoVenta = bdd.buscarVenta(idVenta);
        int estadoVenta = Integer.parseInt(infoVenta.getString(5));
        if (estadoVenta == 0) {
            Toast.makeText(this, "Estado venta: En proceso", Toast.LENGTH_SHORT).show();
//            btnAgregarProductoFacturacion.setText("En proceso");
            // metodo para eliminar
            borrarProductoDescripcion();

            btnAnularVenta.setVisibility(View.GONE);
            btnFinalizarVenta.setVisibility(View.VISIBLE);
        } else if (estadoVenta == 1) {
            Toast.makeText(this, "Estado venta: Finalizada", Toast.LENGTH_SHORT).show();
            btnAgregarProductoFacturacion.setEnabled(false);
            btnAgregarProductoFacturacion.setText("Venta Finalizada");
            btnFinalizarVenta.setVisibility(View.GONE);
            btnAnularVenta.setVisibility(View.VISIBLE);
            btnAgregarProductoFacturacion.setTextColor(Color.parseColor("#D6D6D6"));
            // Quitar la posibilidad de borrar
            lstDescripcionVenta.setOnItemLongClickListener(null);

        }
    }

    private void borrarProductoDescripcion() {
        lstDescripcionVenta.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                infoDescripcionVenta.moveToPosition(position);
                idEliminar = Integer.parseInt(infoDescripcionVenta.getString(0));
//                Toast.makeText(FacturacionActivity.this, "ID: " + idEliminar, Toast.LENGTH_SHORT).show();
                confirmarEliminarProductoDetalle(idEliminar);
                return false;
            }
        });
    }

    public void confirmarEliminarProductoDetalle(int id) {
        // Recuperar datos de la vista
        AlertDialog.Builder estructuraConfirmacion = new AlertDialog.Builder(this)
                .setTitle("Eliminar producto")
                .setMessage("Esta seguro de quitar el producto de la factura?")
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bdd.quitarProductoDetalle(id);
                        obtenerDescripcion();
                        // Toast.makeText(getApplicationContext(), "Eliminado con éxito! " + id, Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getApplicationContext(), "Se canceló la acción", Toast.LENGTH_SHORT).show();
                    }
                }).setCancelable(true);
        // Mostrar Cuadro de dialogo
        AlertDialog cuadroDialogo = estructuraConfirmacion.create();
        cuadroDialogo.show();
    }

    private void obtenerDescripcion() {
        // Resultados
        infoVenta = bdd.buscarVenta(idVenta);
        Double subTotal = Double.parseDouble(infoVenta.getString(2)),
                iva = Double.parseDouble(infoVenta.getString(3)),
                total = Double.parseDouble(infoVenta.getString(4));

        txtSubtotalFacturacion.setText(decimalFormat.format(subTotal));
        txtIvaFacturacion.setText(decimalFormat.format(iva));
        txtTotalFacturacion.setText(decimalFormat.format(total));
        //
        listaDecripcionVenta.clear();
        // Llamar metodo desde la base de datos
        infoDescripcionVenta = bdd.listarDetalle(idVenta);
        if (infoDescripcionVenta != null) {
            do {
                String cantidad = infoDescripcionVenta.getString(3),
                        descripcion = infoDescripcionVenta.getString(5),
                        preUni = infoDescripcionVenta.getString(6);

                Double preTotal = Integer.parseInt(cantidad) * Double.parseDouble(preUni);

                /*for (int i = 0; i < infoDescripcionVenta.getColumnCount(); i++) {
                    System.out.println("COL: " + i + " Datos: " + infoDescripcionVenta.getColumnName(i));
                }*/
                listaDecripcionVenta.add(cantidad + " | " + descripcion + " | " + preUni + " | " + decimalFormat.format(preTotal));
                ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaDecripcionVenta);
                lstDescripcionVenta.setAdapter(adapter);
            } while (infoDescripcionVenta.moveToNext());
        } else {
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaDecripcionVenta);
            lstDescripcionVenta.setAdapter(adapter);
//            Toast.makeText(this, "Sin datos", Toast.LENGTH_SHORT).show();
        }
    }

    //Metodo para volver al la ventana de facturacion
    public void volverVentanaTipoFacturacion(View vista) {
        finish();//cerrando ventana de nueva venta
        //creando un objeto para manejar la ventana de ventas
        Intent ventanaTipoFacturacion = new Intent(getApplicationContext(), VentasActivity.class);
        startActivity(ventanaTipoFacturacion);//solicitando que se abra la ventana de gestion clientes

    }

    //Metodo para abrir la pantalla de agregar productos a la factura
    public void abrirPantallaAgregarProductos(View vista) {
        Intent pantallaAgregarProductosFactura = new Intent(getApplicationContext(), AgregarProductoFactura.class);//Creando un Intent para invocar a Cliente Activity
        pantallaAgregarProductosFactura.putExtra("id_cli", idCli);
        pantallaAgregarProductosFactura.putExtra("id_venta", idVenta);
        startActivity(pantallaAgregarProductosFactura); //Iniciando la pantalla Clientes

    }

    public void finalizarVenta(View vista) {
        AlertDialog.Builder estructuraConfirmacion = new AlertDialog.Builder(this)
                .setTitle("Finalizar Venta")
                .setMessage("Esta seguro de finalizar la venta? No podra agregar más productos.")
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bdd.cambiarEstadoVenta(idVenta, 1);
                        estadoVenta();
                        // Toast.makeText(getApplicationContext(), "Venta finalizada con éxito! " + id, Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getApplicationContext(), "Se canceló la acción", Toast.LENGTH_SHORT).show();
                    }
                }).setCancelable(true);
        // Mostrar Cuadro de dialogo
        AlertDialog cuadroDialogo = estructuraConfirmacion.create();
        cuadroDialogo.show();

    }

    public void anularVenta(View vista) {
        AlertDialog.Builder estructuraConfirmacion = new AlertDialog.Builder(this)
                .setTitle("Anular Venta")
                .setMessage("Esta seguro de anular la venta? Todos los productos seran reingresados.")
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Logica ir regresando los productos
                        // Capturar lista de productos en detalle
                        infoDescripcionVenta = bdd.listarDetalle(idVenta);
                        if (infoDescripcionVenta != null) {

                            do {
                                // Recorrer la lista eliminando cada producto
                                int idAnularDetalle = Integer.parseInt(infoDescripcionVenta.getString(0));
                                // System.out.println("Se quito el producto: " + idAnularDetalle);
                                bdd.quitarProductoDetalle(idAnularDetalle);
                            } while (infoDescripcionVenta.moveToNext());  // Llegar hasta el final de la lista
                        }
                        // Eliminar el registro de la base de datos
                        bdd.eliminarVenta(idVenta);
                        // System.out.println("Se quito la venta: " + idVenta);
                        volverVentanaTipoFacturacion(null);
                        Toast.makeText(getApplicationContext(), "Venta anulada con éxito! ", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getApplicationContext(), "Se canceló la acción", Toast.LENGTH_SHORT).show();
                    }
                }).setCancelable(true);
        // Mostrar Cuadro de dialogo
        AlertDialog cuadroDialogo = estructuraConfirmacion.create();
        cuadroDialogo.show();
    }

}