package com.utc.applogeo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class FacturacionActivity extends AppCompatActivity {
    String id;
    Cursor infoCliente;
    BaseDatos bdd;
    TextView txtCedulaFacturacion, txtNombresFacturacion, txtTelefonoFacturacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facturacion);
        txtCedulaFacturacion = (TextView) findViewById(R.id.txtCedulaFacturacion);
        txtNombresFacturacion = (TextView) findViewById(R.id.txtNombresFacturacion);
        txtTelefonoFacturacion = (TextView) findViewById(R.id.txtTelefonoFacturacion);
        bdd = new BaseDatos(getApplicationContext());
        Bundle parametrosExtra = getIntent().getExtras();
        if (parametrosExtra != null) {
            try {
                //Intente realizar estas lineas de codigo
                id = parametrosExtra.getString("id_cli");
                infoCliente = bdd.consultarDatosClientes(Integer.parseInt(id));


            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), "Error al procesar la solicitud" + ex.toString(), Toast.LENGTH_LONG).show();

            }
            txtCedulaFacturacion.setText(infoCliente.getString(1));
            txtNombresFacturacion.setText(infoCliente.getString(3)+" "+infoCliente.getString(2));
            txtTelefonoFacturacion.setText(infoCliente.getString(4));

        }
    }
    //Metodo para volver al la ventana de facturacion
    public void volverVentanaTipoFacturacion(View vista) {
        finish();//cerrando ventana de nueva venta
        //creando un objeto para manejar la ventana de ventas
        Intent ventanaTipoFacturacion = new Intent(getApplicationContext(), MetodoVentaActivity.class);
        startActivity(ventanaTipoFacturacion);//solicitando que se abra la ventana de gestion clientes

    }
    //Metodo para abrir la pantalla de agregar productos a la factura
    public void abrirPantallaAgregarProductos(View vista)
    {
        Intent pantallaAgregarProductosFactura = new Intent(getApplicationContext(), AgregarProductoFactura.class);//Creando un Intent para invocar a Cliente Activity
        startActivity(pantallaAgregarProductosFactura); //Iniciando la pantalla Clientes

    }
}