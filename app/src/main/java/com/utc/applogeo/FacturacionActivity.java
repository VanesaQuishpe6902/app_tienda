package com.utc.applogeo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class FacturacionActivity extends AppCompatActivity {
    String idCli, idVenta;
    Cursor infoCliente, infoVenta;
    BaseDatos bdd;
    TextView txtCedulaFacturacion, txtNombresFacturacion, txtTelefonoFacturacion, txtIdVenta, txtFechaVenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facturacion);
        txtIdVenta = (TextView) findViewById(R.id.txtIdVenta);
        txtFechaVenta = (TextView) findViewById(R.id.txtFechaVenta);
        txtCedulaFacturacion = (TextView) findViewById(R.id.txtCedulaFacturacion);
        txtNombresFacturacion = (TextView) findViewById(R.id.txtNombresFacturacion);
        txtTelefonoFacturacion = (TextView) findViewById(R.id.txtBuscarProductoFacturacion);
        bdd = new BaseDatos(getApplicationContext());
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
            txtNombresFacturacion.setText(infoCliente.getString(3) + " " + infoCliente.getString(2));
            txtTelefonoFacturacion.setText(infoCliente.getString(4));

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
}